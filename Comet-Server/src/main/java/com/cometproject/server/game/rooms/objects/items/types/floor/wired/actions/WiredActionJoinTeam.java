package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GamePlayer;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.network.messages.outgoing.user.details.HideAvatarOptionsMessageComposer;


public class WiredActionJoinTeam extends WiredActionItem {
    private static final int PARAM_TEAM_ID = 0;

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
    public WiredActionJoinTeam(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().size() != 1) {
            this.getWiredData().getParams().put(PARAM_TEAM_ID, 1); // team red
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 9;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null || !(entity instanceof PlayerEntity)) {
            return false;
        }

        this.entity = entity;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }


    @Override
    public void onTickComplete(){
        if (this.entity == null || !(this.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) this.entity);

        if (playerEntity.getGameTeam() != GameTeam.NONE) {
            return; // entity already in a team!
        }

        final GameTeam gameTeam = this.getTeam();

        if (this.getTeam() == GameTeam.NONE)
            return;

        playerEntity.setGameTeam(gameTeam);
        this.getRoom().getGame().joinTeam(gameTeam, playerEntity);

        playerEntity.applyEffect(new PlayerEffect(gameTeam.getFreezeEffect(), false));
        return;

    }

    private GameTeam getTeam() {
        switch (this.getWiredData().getParams().get(PARAM_TEAM_ID)) {

            case 1:
                return GameTeam.RED;
            case 2:
                return GameTeam.GREEN;
            case 3:
                return GameTeam.BLUE;
            case 4:
                return GameTeam.YELLOW;
        }

        return GameTeam.NONE;
    }
}
