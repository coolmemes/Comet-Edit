package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;

public class WiredActionBotClothes extends WiredActionItem {
    private BotEntity botEntity;
    private String figure;

    public WiredActionBotClothes(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 26;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (!this.getWiredData().getText().contains("\t")) {
            return false;
        }

        if (this.getWiredData().getText().isEmpty()) {
            return false;
        }

        final String[] WiredData = this.getWiredData().getText().split("\t");

        if (WiredData.length != 2) {
            return false;
        }

        final String botName = WiredData[0];
        this.figure = WiredData[1];

        this.botEntity = this.getRoom().getBots().getBotByName(botName);

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }


    public void onTickComplete() {
        if (this.botEntity != null) {
            this.botEntity.getData().setFigure(this.figure);
            this.getRoom().getEntities().broadcastMessage(new UpdateInfoMessageComposer(this.botEntity));

            this.botEntity.getData().save();
        }
    }
}
