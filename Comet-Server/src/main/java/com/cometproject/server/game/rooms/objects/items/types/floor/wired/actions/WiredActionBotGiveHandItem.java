package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;

public class WiredActionBotGiveHandItem extends WiredActionItem {
    private final static int PARAM_HANDITEM = 0;

    private BotEntity botEntity;
    private int handItemId;
    private String userUsername;

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
    public WiredActionBotGiveHandItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 24;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getParams().size() != 1) {
            return false;
        }

        if (this.getWiredData().getText().isEmpty()) {
            return false;
        }

        if (entity == null || !(entity instanceof PlayerEntity)) return false;

        this.handItemId = this.getWiredData().getParams().get(PARAM_HANDITEM);

        final String botName = this.getWiredData().getText();
        this.botEntity = this.getRoom().getBots().getBotByName(botName);
        this.userUsername = entity.getUsername();

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    public void onTickComplete() {
        if (this.botEntity != null) {
            this.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.botEntity.getId(), Locale.get("bots.chat.giveItemMessage").replace("%username%", userUsername), RoomManager.getInstance().getEmotions().getEmotion(":)"), 2));
            entity.carryItem(this.handItemId);
        }
    }
}
