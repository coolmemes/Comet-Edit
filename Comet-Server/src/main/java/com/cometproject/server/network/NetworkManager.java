package com.cometproject.server.network;

import com.cometproject.api.messaging.console.ConsoleCommandRequest;
import com.cometproject.api.messaging.exec.ExecCommandRequest;
import com.cometproject.api.messaging.exec.ExecCommandResponse;
import com.cometproject.api.messaging.performance.QueryRequest;
import com.cometproject.api.messaging.status.StatusRequest;
import com.cometproject.api.messaging.status.StatusResponse;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.boot.CometServer;
import com.cometproject.server.boot.utils.ConsoleCommands;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.monitor.MonitorClient;
import com.cometproject.server.network.sessions.SessionManager;
import com.cometproject.server.protocol.security.exchange.RSA;
import com.cometproject.server.storage.SqlHelper;
import com.fasterxml.jackson.databind.ser.std.InetAddressSerializer;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.services.messaging.client.MessagingClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.log4j.Logger;
import org.xbill.DNS.Address;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;


public class NetworkManager {
    private static NetworkManager networkManagerInstance;

    public static boolean IDLE_TIMER_ENABLED = Boolean.parseBoolean(Configuration.currentConfig().get("comet.network.idleTimer.enabled", "false"));
    public static int IDLE_TIMER_READER_TIME = Integer.parseInt(Configuration.currentConfig().get("comet.network.idleTimer.readerIdleTime", "30"));
    public static int IDLE_TIMER_WRITER_TIME = Integer.parseInt(Configuration.currentConfig().get("comet.network.idleTimer.writerIdleTime", "30"));
    public static int IDLE_TIMER_ALL_TIME = Integer.parseInt(Configuration.currentConfig().get("comet.network.idleTimer.allIdleTime", "30"));

    private int serverPort;

    private SessionManager sessions;
    private MessageHandler messageHandler;

    private RSA rsa;

    private MonitorClient monitorClient;
    private MessagingClient messagingClient;

    private static Logger log = Logger.getLogger(NetworkManager.class.getName());

    public NetworkManager() {

    }

    public static NetworkManager getInstance() {
        if (networkManagerInstance == null)
            networkManagerInstance = new NetworkManager();

        return networkManagerInstance;
    }

    public void initialize(String ip, String ports) {
        this.rsa = new RSA();
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

        this.serverPort = Integer.parseInt(ports.split(",")[0]);

        try {
            this.messagingClient = MessagingClient.create("com.cometproject:instance/" + Comet.instanceId + "/" +
                    "" + CometSettings.hotelName.replace(" ", "-").toLowerCase(),
                    new CoerceConfiguration("configuration/Coerce.json"));

            this.messagingClient.observe(ConsoleCommandRequest.class, (consoleCommandRequest -> {
                ConsoleCommands.handleCommand(consoleCommandRequest.getCommand());
            }));

            this.messagingClient.observe(ExecCommandRequest.class, (execRequest -> {
                final String command = execRequest.getCommand();

                try {
                    Process process = Runtime.getRuntime().exec(command);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            process.getInputStream()));

                    StringBuilder commandOutput = new StringBuilder();
                    String buffer;

                    while ((buffer = reader.readLine()) != null) {
                        commandOutput.append(buffer).append("\n");
                    }

                    this.messagingClient.sendResponse(execRequest.getMessageId(), execRequest.getSender(),
                            new ExecCommandResponse(commandOutput.toString()));
                } catch (IOException e) {
                    this.messagingClient.sendResponse(execRequest.getMessageId(), execRequest.getSender(),
                            new ExecCommandResponse("Exception: " + e));
                }
            }));

            this.messagingClient.observe(StatusRequest.class, (statusRequest -> {
                messagingClient.sendResponse(statusRequest.getMessageId(), statusRequest.getSender(),
                        new StatusResponse(Comet.getStats(), Comet.getBuild()));
            }));

            final InetAddress address = InetAddress.getLocalHost();
//            final InetAddress address = Address.getByName("master.cometproject.com");

            this.messagingClient.connect(address.getHostAddress(), 6500, (client) -> {
                // Create logging appender!
                // Initialise with the master service.

            });
        } catch (Exception e) {
            System.out.println("Failed to initialise NetworkManager");
            System.exit(0);
            return;
        }

        this.rsa.init();

        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

        System.setProperty("io.netty.leakDetectionLevel", "disabled");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        EventLoopGroup acceptGroup;
        EventLoopGroup ioGroup;
        EventLoopGroup channelGroup;

        final boolean isEpollEnabled = Boolean.parseBoolean(Configuration.currentConfig().get("comet.network.epoll", "false"));
        final boolean isEpollAvailable = Epoll.isAvailable();
        final int defaultThreadCount = 16; // TODO: Find the best count.

        if (isEpollAvailable && isEpollEnabled) {
            log.info("Epoll is enabled");
            acceptGroup = new EpollEventLoopGroup(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.acceptGroupThreads", defaultThreadCount)));
            ioGroup = new EpollEventLoopGroup(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.ioGroupThreads", defaultThreadCount)));
            channelGroup = new EpollEventLoopGroup(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.channelGroupThreads", defaultThreadCount)));
        } else {
            if (isEpollAvailable) {
                log.info("Epoll is available but not enabled");
            } else {
                log.info("Epoll is not available");
            }

            acceptGroup = new NioEventLoopGroup(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.acceptGroupThreads", defaultThreadCount)));
            ioGroup = new NioEventLoopGroup(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.ioGroupThreads", defaultThreadCount)));
            channelGroup = new NioEventLoopGroup(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.channelGroupThreads", defaultThreadCount)));
        }

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptGroup, ioGroup)
                .channel(isEpollAvailable && isEpollEnabled ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new NetworkChannelInitializer(channelGroup))
                .option(ChannelOption.SO_BACKLOG, Integer.parseInt(Configuration.currentConfig().get("comet.network.backlog", "500")))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024);

        if (ports.contains(",")) {
            for (String s : ports.split(",")) {
                this.bind(bootstrap, ip, Integer.parseInt(s));
            }
        } else {
            this.bind(bootstrap, ip, Integer.parseInt(ports));
        }
    }

    private void bind(ServerBootstrap bootstrap, String ip, int port) {
        try {
            bootstrap.bind(new InetSocketAddress(ip, port)).addListener(objectFuture -> {
                if (!objectFuture.isSuccess()) {
                    Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
                }
            });

            log.info("CometServer listening on port: " + port);
        } catch (Exception e) {
            e.printStackTrace();
            Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
        }
    }

    public SessionManager getSessions() {
        return this.sessions;
    }

    public MessageHandler getMessages() {
        return this.messageHandler;
    }

    public MonitorClient getMonitorClient() {
        return monitorClient;
    }

    public RSA getRSA() {
        return rsa;
    }

    public int getServerPort() {
        return serverPort;
    }

    public MessagingClient getMessagingClient() {
        return this.messagingClient;
    }
}
