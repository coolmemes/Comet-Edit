package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;

public class WiredActionBotTalk extends WiredActionItem {
    public static final int PARAM_MESSAGE_TYPE = 0;
    private BotEntity botEntity;
    private String message;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionBotTalk(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 23;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (!this.getWiredData().getText().contains("\t")) {
            return false;
        }

        final String[] talkData = this.getWiredData().getText().split("\t");

        if (talkData.length != 2) {
            return false;
        }

        final String botName = talkData[0];
        String message = talkData[1];

        if (botName.isEmpty() || message.isEmpty()) {
            return false;
        }

        if (entity instanceof PlayerEntity) {
            message = message.replace("%username%", entity.getUsername());
        }

        this.message = message.replace("<", "").replace(">", "");

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
            boolean isShout = (this.getWiredData().getParams().size() == 1 && (this.getWiredData().getParams().get(PARAM_MESSAGE_TYPE) == 1));

            if (isShout) {
                this.getRoom().getEntities().broadcastMessage(new ShoutMessageComposer(this.botEntity.getId(), message, ChatEmotion.NONE, 2));
            } else {
                this.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.botEntity.getId(), message, ChatEmotion.NONE, 2));
            }
        }
    }
}
