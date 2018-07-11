package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.google.common.collect.Lists;

import java.util.List;

public class WiredActionExecuteStacks extends WiredActionItem {
    /**
     * The default constructor
     *
     * @param id        The ID of the item
     * @param itemId    The ID of the item definition
     * @param room      The instance of the room
     * @param owner     The ID of the owner
     * @param ownerName The username of the owner
     * @param x         The position of the item on the X axis
     * @param y         The position of the item on the Y axis
     * @param z         The position of the item on the z axis
     * @param rotation  The orientation of the item
     * @param data      The JSON object associated with this item
     */
    public WiredActionExecuteStacks(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        List<Position> tilesToExecute = Lists.newArrayList();

        for (long itemId : this.getWiredData().getSelectedIds()) {
            final RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null || (floorItem.getPosition().getX() == this.getPosition().getX() && floorItem.getPosition().getY() == this.getPosition().getY())) continue;

            tilesToExecute.add(new Position(floorItem.getPosition().getX(), floorItem.getPosition().getY()));
        }

        for (Position tileToUpdate : tilesToExecute) {
            for(RoomItemFloor roomItemFloor : this.getRoom().getMapping().getTile(tileToUpdate).getItems()) {
                if(roomItemFloor instanceof WiredActionItem) {
                    ((WiredActionItem) roomItemFloor).evaluate(event.entity, event.data);
                }
            }
        }

        tilesToExecute.clear();
    }


    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 18;
    }
}
