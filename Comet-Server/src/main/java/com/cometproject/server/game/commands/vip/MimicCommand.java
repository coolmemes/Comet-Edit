package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;


public class MimicCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }
        String username = params[0];

        PlayerEntity entity = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(username, RoomEntityType.PLAYER);

        if (entity == null)
            return;

        if (entity.getUsername().equals(client.getPlayer().getData().getUsername())) {
            return;
        }

        PlayerEntity playerEntity = client.getPlayer().getEntity();

        playerEntity.getPlayer().getData().setFigure(entity.getFigure());
        playerEntity.getPlayer().getData().setGender(entity.getGender());
        playerEntity.getPlayer().getData().save();

        playerEntity.getPlayer().poof();
    }

    @Override
    public String getPermission() {
        return "mimic_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.mimic.description");
    }
}
