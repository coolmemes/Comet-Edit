package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class UnloadCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(client == null || client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) { return; }

        Room room = client.getPlayer().getEntity().getRoom();

        if(room.getData().getOwnerId() != client.getPlayer().getId() && !room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().modTool()) {  return; }



        client.getPlayer().getEntity().getRoom().getItems().commit();
        client.getPlayer().getEntity().getRoom().setIdleNow();

        room.getEntities().broadcastMessage(new AdvancedAlertMessageComposer(Locale.get("command.unload.success.title"), Locale.get("command.unload.success.text"), Locale.get("command.unload.success.button"), "event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId(), ""));



    }

    @Override
    public String getPermission() {
        return "unload_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unload.description");
    }
}
