package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;


public class WiredAddonPuzzleBox extends RoomItemFloor {
    public WiredAddonPuzzleBox(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (entity != null) {
            if (!this.getPosition().touching(entity)) {
                entity.moveTo(this.getPosition().squareBehind(this.getRotation()).getX(), this.getPosition().squareBehind(this.getRotation()).getY());
                return false;
            }
        }

        Position tileGoal = this.getPosition().squareInFront(this.rotation);

        if (tileGoal != null) {
            switch (entity.getBodyRotation()) {
                case 4:
                    tileGoal = new Position(this.getPosition().getX(), this.getPosition().getY() + 1, tileGoal.getZ());
                    break;

                case 0:
                    tileGoal = new Position(this.getPosition().getX(), this.getPosition().getY() - 1, tileGoal.getZ());
                    break;

                case 6:
                    tileGoal = new Position(this.getPosition().getX() - 1, this.getPosition().getY(), tileGoal.getZ());
                    break;

                case 2:
                    tileGoal = new Position(this.getPosition().getX() + 1, this.getPosition().getY(), tileGoal.getZ());
                    break;
            }

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(this.getPosition(), tileGoal, 0, 0, this.getVirtualId()));
            this.getPosition().setX(tileGoal.getX());
            this.getPosition().setY(tileGoal.getY());
            this.getPosition().setZ(tileGoal.getZ());

            RoomItemDao.saveItemPosition(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getRotation(), this.getId());


        //this.getRoom().getMapping().updateTile(entity.getPosition().getX(), entity.getPosition().getY());
        //this.getRoom().getMapping().updateTile(tileGoal.getX(), tileGoal.getY());
            this.getTile().reload();
            entity.moveTo(this.getPosition().getX(), this.getPosition().getY());
        }
        return true;
    }
}
