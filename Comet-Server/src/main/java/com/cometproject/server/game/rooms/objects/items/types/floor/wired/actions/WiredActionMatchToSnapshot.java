package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.DiceFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.BanzaiTimerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredItemSnapshot;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.engine.UpdateStackMapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;

import java.util.ArrayList;
import java.util.List;


public class WiredActionMatchToSnapshot extends WiredActionItem {
    private static final int PARAM_MATCH_STATE = 0;
    private static final int PARAM_MATCH_ROTATION = 1;
    private static final int PARAM_MATCH_POSITION = 2;

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
    public WiredActionMatchToSnapshot(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 3;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.hasTicks()) return false;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    @Override
    public void onTickComplete() {
        boolean stateChanged = false;
        boolean rotationChanged = false;

        if (this.getWiredData().getSnapshots().size() == 0) {
            return;
        }

        final boolean matchState = this.getWiredData().getParams().get(PARAM_MATCH_STATE) == 1;
        final boolean matchRotation = this.getWiredData().getParams().get(PARAM_MATCH_ROTATION) == 1;
        final boolean matchPosition = this.getWiredData().getParams().get(PARAM_MATCH_POSITION) == 1;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);
            if (floorItem == null || (floorItem instanceof BanzaiTimerFloorItem && matchState)) continue;

            WiredItemSnapshot itemSnapshot = this.getWiredData().getSnapshots().get(itemId);
            if (itemSnapshot == null) continue;

            if (matchState && !(floorItem instanceof DiceFloorItem || floorItem instanceof HighscoreClassicFloorItem)) {
                String currentExtradata = floorItem.getExtraData();
                String newExtradata = itemSnapshot.getExtraData();

                if(!currentExtradata.equals(newExtradata)) {
                    floorItem.setExtraData(newExtradata);
                    stateChanged = true;
                }
            }

            if(matchRotation) {
                int currentRotation = floorItem.getRotation();
                int newRotation = itemSnapshot.getRotation();

                if(currentRotation != newRotation) {
                    floorItem.setRotation(newRotation);
                    rotationChanged = true;
                }
            }

            if(stateChanged || rotationChanged) {
                floorItem.sendUpdate();
            }

            if(matchPosition) {
                Position currentPosition = floorItem.getPosition();
                Position newPosition = new Position(itemSnapshot.getX(), itemSnapshot.getY(), itemSnapshot.getZ());

                if(!currentPosition.equals(newPosition)){
                    this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(currentPosition.copy(), newPosition.copy(), -1, this.getVirtualId(), floorItem.getVirtualId()));
                    floorItem.setPosition(newPosition);

                    this.getRoom().getMapping().getTile(currentPosition).reload();
                    this.getRoom().getMapping().getTile(newPosition).reload();
                }
            }

            floorItem.save();
        }
    }
}
