package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;

public class WiredActionBotMove extends WiredActionItem {
    private BotEntity botEntity;
    private Position targetPosition;

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
    public WiredActionBotMove(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 22;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData() == null || this.getWiredData().getSelectedIds() == null || this.getWiredData().getSelectedIds().isEmpty()) {
            return false;
        }

        Long itemId = WiredUtil.getRandomElement(this.getWiredData().getSelectedIds());

        if (itemId == null) {
            return false;
        }

        RoomItemFloor item = this.getRoom().getItems().getFloorItem(itemId);

        if (item == null || item.isAtDoor() || item.getPosition() == null || item.getTile() == null) {
            return false;
        }

        Position position = new Position(item.getPosition().getX(), item.getPosition().getY());

        final String entityName = this.getWiredData().getText();

        this.botEntity = this.getRoom().getBots().getBotByName(entityName);
        this.targetPosition = position;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }


    public void onTickComplete() {
        if (this.botEntity == null) return;

        this.botEntity.moveTo(this.targetPosition);
    }
}
