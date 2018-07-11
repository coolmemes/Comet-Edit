package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.HCRequiredNotificationMessageComposer;


public class SubscriptionGateFloorItem extends RoomItemFloor {
    public boolean isOpen = false;

    public SubscriptionGateFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.isOpen = false;
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.isOpen = true;
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        this.setTicks(RoomItemFactory.getProcessTime(0.5));
    }

    @Override
    public void onTickComplete() {
        if(this.getTile().getEntities().size() != 0) {
            return;
        }

        this.isOpen = false;
        this.sendUpdate();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if(!(entity instanceof PlayerEntity)) {
            return true;
        }

        if (!((PlayerEntity) entity).getPlayer().getSubscription().isValid()) {
            ((PlayerEntity) entity).getPlayer().getSession().send(new HCRequiredNotificationMessageComposer(0));
            ((PlayerEntity) entity).getPlayer().getSession().send(new HCRequiredNotificationMessageComposer(2));
            return true;
        }

        return false;
    }
}
