package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class WiredAddonBlob extends RoomItemFloor {

    public WiredAddonBlob(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
               this.setExtraData("1");
           }

            /*public void onGameStarted() {
                if(this.getRoom().getGame().getBlobCounter().get() < 2) {
                        this.getRoom().getGame().getBlobCounter().incrementAndGet();

                               this.setExtraData("0");
                        this.sendUpdate();
                  }
           }

            public void hideBlob() {
                this.getRoom().getGame().getBlobCounter().decrementAndGet();

                        this.setExtraData("1");
                this.sendUpdate();
    }*/

    @Override
    public void onEntityStepOn(RoomEntity entity) {

                       if(!(entity instanceof PlayerEntity) || this.getExtraData().equals("1")) {
                        return;
                    }

                        final PlayerEntity playerEntity = (PlayerEntity) entity;

                        if(playerEntity.getGameTeam() == GameTeam.NONE) {
                       return;
                   }
                   /*this.getRoom().getGame().increaseScore(playerEntity.getGameTeam(), 1);
                this.hideBlob(); */
    }
}