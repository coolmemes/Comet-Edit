package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;


public class WiredActionLeaveTeam extends WiredActionItem {
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
    public WiredActionLeaveTeam(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 10;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (event.entity == null || !(event.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);

        if (playerEntity.getGameTeam() == null) {
            return; // entity already in a team!
        }

        this.getRoom().getGame().removeFromTeam(playerEntity.getGameTeam(), playerEntity.getPlayerId());
        playerEntity.setGameTeam(null);

        if (playerEntity.getCurrentEffect() != null && playerEntity.getCurrentEffect().getEffectId() == playerEntity.getGameTeam().getFreezeEffect()) {
            playerEntity.applyEffect(null);
        }
    }
}
