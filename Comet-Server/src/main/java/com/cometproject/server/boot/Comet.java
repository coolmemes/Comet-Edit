package com.cometproject.server.boot;

import com.cometproject.server.boot.utils.ConsoleCommands;
import com.cometproject.server.boot.utils.ShutdownHook;
import com.cometproject.server.boot.utils.gui.CometGui;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Comet {
    /**
     * Logging during start-up & console commands
     */
    private static Logger log = Logger.getLogger(Comet.class.getName());

    /**
     * The main server instance
     */
    private static CometServer server;

    /**
     * The time the server was started
     */
    public static long start;

    /**
     * Is a debugger attached?
     */
    public static volatile boolean isDebugging = false;

    /**
     * Is Comet running?
     */
    public static volatile boolean isRunning = true;

    /**
     * Whether or not we want to show the GUI
     */
    public static boolean showGui = false;

    public static boolean allowToUseDelayOnWireds = true;

    /**
     * Start the server!
     *
     * @param args The arguments passed from the run command
     */
    public static void run(String[] args) {
        start = System.currentTimeMillis();

        try {
            PropertyConfigurator.configure(new FileInputStream("./config/log4j.properties"));
        } catch (Exception e) {
            log.error("Error while loading log4j configuration", e);
            return;
        }

        log.info("Comet Server - " + getBuild());

        log.info("  /$$$$$$                                      /$$    ");
        log.info(" /$$__  $$                                    | $$    ");
        log.info("| $$  \\__/  /$$$$$$  /$$$$$$/$$$$   /$$$$$$  /$$$$$$  ");
        log.info("| $$       /$$__  $$| $$_  $$_  $$ /$$__  $$|_  $$_/  ");
        log.info("| $$      | $$  \\ $$| $$ \\ $$ \\ $$| $$$$$$$$  | $$    ");
        log.info("| $$    $$| $$  | $$| $$ | $$ | $$| $$_____/  | $$ /$$");
        log.info("|  $$$$$$/|  $$$$$$/| $$ | $$ | $$|  $$$$$$$  |  $$$/");
        log.info(" \\______/  \\______/ |__/ |__/ |__/ \\_______/   \\___/  ");

        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (arg.contains("dt_")) {
                isDebugging = true;
                break;
            }
        }

        Level logLevel = Level.INFO;

        if (args.length < 1) {
            log.debug("No config args found, falling back to default configuration!");
            server = new CometServer(null);
        } else {
            Map<String, String> cometConfiguration = new HashMap<>();

            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--debug-logging")) {
                    isDebugging = true;
                    logLevel = Level.DEBUG;
                }

                if(args[i].equals("--gui")) {
                    // start GUI!
                    showGui = true;
                }

                if (!args[i].contains("="))
                    continue;

                String[] splitArgs = args[i].split("=");

                cometConfiguration.put(splitArgs[0], splitArgs.length != 1 ? splitArgs[1] : "");
            }

            server = new CometServer(cometConfiguration);
        }

        Logger.getRootLogger().setLevel(logLevel);
        server.init();

        ConsoleCommands.init();
        ShutdownHook.init();
    }

    /**
     * Exit the comet server
     *
     * @param message The message to display to the console
     */
    public static void exit(String message) {
        log.error("Comet has shutdown. Reason: \"" + message + "\"");
        System.exit(0);
    }

    /**
     * Get the current time in seconds
     *
     * @return The time in seconds
     */
    public static long getTime() {
        return (System.currentTimeMillis() / 1000L);
    }

    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Get the current build of Comet
     *
     * @return The current build of Comet
     */
    public static String getBuild() {
        return Comet.class.getPackage().getImplementationVersion() == null ? "Comet-DEV" : Comet.class.getPackage().getImplementationVersion();
    }

    /**
     * Get the main server instance
     *
     * @return The main server instance
     */
    public static CometServer getServer() {
        return server;
    }

    public static boolean listsEquals(List<? extends Object> list1, List<? extends Object> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    public static int getTimeInt() { return (int) (new Date().getTime()/1000); }

    public static long getTimeFromString(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime()/1000;
        } catch (ParseException e) {
            return 0;
        }
    }
}