package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerCollision;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.utilities.Direction;

import java.util.concurrent.atomic.AtomicInteger;

public class WiredActionMoveToDirection extends WiredActionItem {
    private static final int PARAM_START_DIR = 0;
    private static final int PARAM_ACTION_WHEN_BLOCKED = 1;

    private static final int ACTION_WAIT = 0;
    private static final int ACTION_TURN_RIGHT_45 = 1;
    private static final int ACTION_TURN_RIGHT_90 = 2;
    private static final int ACTION_TURN_LEFT_45 = 3;
    private static final int ACTION_TURN_LEFT_90 = 4;
    private static final int ACTION_TURN_BACK = 5;
    private static final int ACTION_TURN_RANDOM = 6;

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
    public WiredActionMoveToDirection(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 13;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getParams().size() != 2) {
            return false;
        }

        final int startDir = this.getWiredData().getParams().get(PARAM_START_DIR);
        boolean colisionPlayer = false;
        synchronized (this.getWiredData().getSelectedIds()) {
            for (long itemId : this.getWiredData().getSelectedIds()) {
                RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

                if (floorItem == null) continue;

                if (floorItem.getMoveDirection() == -1) {
                    floorItem.setMoveDirection(startDir);
                }

                final Position currentPosition = floorItem.getPosition().copy();
                final Position nextPosition = floorItem.getPosition().squareInFront(floorItem.getMoveDirection());
                final RoomTile roomTile = this.getRoom().getMapping().getTile(nextPosition);

                if (roomTile != null) {
                    if (roomTile.getEntity() != null) {

                        WiredTriggerCollision.executeTriggers(roomTile.getEntity(), floorItem);
                        floorItem.setPosition(currentPosition);
                        colisionPlayer = true;
                        return false;
                    }
                }

                this.moveItem(floorItem, new AtomicInteger(0), colisionPlayer);
            }
        }

        return false;
    }

    private void moveItem(RoomItemFloor floorItem, AtomicInteger tries, boolean colisionPlayer) {
        final Position currentPosition = floorItem.getPosition().copy();
        final Position nextPosition = floorItem.getPosition().squareInFront(floorItem.getMoveDirection());

        if (this.getRoom().getItems().moveFloorItem(floorItem.getId(), floorItem.getPosition().squareInFront(floorItem.getMoveDirection()), floorItem.getRotation(), true)) {
            nextPosition.setZ(floorItem.getPosition().getZ());


            if(!colisionPlayer) this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(currentPosition, nextPosition, this.getVirtualId(), 0, floorItem.getVirtualId()));

        } else if(!colisionPlayer){
            tries.incrementAndGet();

            if (tries.get() < 4)
                this.attemptBlockedAction(floorItem, tries);
        }
    }

    private void attemptBlockedAction(RoomItemFloor floorItem, AtomicInteger tries) {
        final int actionWhenBlocked = this.getWiredData().getParams().get(PARAM_ACTION_WHEN_BLOCKED);

        if (actionWhenBlocked == 0) {
            return;
        }

        int movementDirection = floorItem.getMoveDirection();
        final Position position = floorItem.getPosition().squareInFront(floorItem.getMoveDirection());

        switch (actionWhenBlocked) {
            case ACTION_TURN_BACK:
                movementDirection = Direction.get(movementDirection).invert().num;
                break;

            case ACTION_TURN_RANDOM:
                movementDirection = Direction.random().num;
                break;

            case ACTION_TURN_RIGHT_45:
                movementDirection = this.getNextDirection(movementDirection);
                break;

            case ACTION_TURN_RIGHT_90:
                movementDirection = this.clockwise(movementDirection, 2);
                break;

            case ACTION_TURN_LEFT_45:
                movementDirection = this.getPreviousDirection(movementDirection);
                break;

            case ACTION_TURN_LEFT_90:
                movementDirection = this.antiClockwise(movementDirection, 2);
                break;

        }

        floorItem.setMoveDirection(movementDirection);
        this.moveItem(floorItem, tries, false);
    }

    private int clockwise(int movementDirection, int times) {
        for (int i = 0; i < times; i++) {
            movementDirection = this.getNextDirection(movementDirection);
        }

        return movementDirection;
    }

    private int antiClockwise(int movementDirection, int times) {
        for (int i = 0; i < times; i++) {
            movementDirection = this.getPreviousDirection(movementDirection);
        }

        return movementDirection;
    }

    private int getNextDirection(int movementDirection) {
        if (movementDirection == 7) {
            return 0;
        }

        return movementDirection + 1;
    }

    private int getPreviousDirection(int movementDirection) {
        if (movementDirection == 0) {
            return 7;
        }

        return movementDirection - 1;
    }
}
