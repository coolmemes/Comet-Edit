package com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.actions;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;

/**
 * Created by Salinas on 04/02/2018.
 */
public class WiredCustomGiveFreeze extends WiredActionItem {
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
    public WiredCustomGiveFreeze(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {

        if (entity == null) return false;
        this.entity = entity;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    @Override
    public void onTickComplete() {
        if (this.entity == null) return;

        if (this.getWiredData() == null) {
            this.entity = null;
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) this.entity);
        String freezeType = this.getWiredData().getText();

        if (freezeType.equals("0") || freezeType.equals("congelar") || freezeType.equals("freeze") || freezeType.equals("true")){
            playerEntity.getPlayer().getEntity().setCanWalk(false);
        } else if(freezeType.equals("1") || freezeType.equals("descongelar") || freezeType.equals("unfreeze") || freezeType.equals("false")){
            playerEntity.getPlayer().getEntity().setCanWalk(true);
        }


        this.entity = null;
        return;
    }


    @Override
    public int getInterface() {
        return 7;
    }


    @Override
    public boolean requiresPlayer() {
        return true;
    }
}