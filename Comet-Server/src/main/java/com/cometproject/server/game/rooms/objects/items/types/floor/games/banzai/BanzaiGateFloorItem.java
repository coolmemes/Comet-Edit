package com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameGateFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;


public class BanzaiGateFloorItem extends AbstractGameGateFloorItem {
    private GameTeam gameTeam;

    public BanzaiGateFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        switch (this.getDefinition().getInteraction()) {
            case "bb_red_gate":
                gameTeam = GameTeam.RED;
                break;
            case "bb_blue_gate":
                gameTeam = GameTeam.BLUE;
                break;
            case "bb_green_gate":
                gameTeam = GameTeam.GREEN;
                break;
            case "bb_yellow_gate":
                gameTeam = GameTeam.YELLOW;
                break;
        }
    }

    @Override
    public GameType gameType() {
        return GameType.BANZAI;
    }

    @Override
    public GameTeam getTeam() {
        return gameTeam;
    }
}
