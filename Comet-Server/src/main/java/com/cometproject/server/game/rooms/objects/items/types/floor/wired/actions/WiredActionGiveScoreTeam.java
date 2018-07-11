package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

        import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
        import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
        import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
        import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
        import com.cometproject.server.game.rooms.types.Room;
        import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class WiredActionGiveScoreTeam extends WiredActionItem {
    private static final int PARAM_SCORE = 0;
    private static final int PARAM_PER_GAME = 1;
    private static final int PARAM_TEAM = 2;

    public WiredActionGiveScoreTeam(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().size() < 3) {
            this.getWiredData().getParams().clear();
            this.getWiredData().getParams().put(PARAM_SCORE, 1);
            this.getWiredData().getParams().put(PARAM_PER_GAME, 1);
            this.getWiredData().getParams().put(PARAM_TEAM, 1);
        }
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 14;
    }

    @Override
    public void onTickComplete() {
        if (this.getTeamById() == null || this.getRoom().getGame() == null) {
            return;
        }
        //TODO: Highscore
        //this.getRoom().getGame().increaseScoreToTeam(this.getItemId(), this.getTeamById(), this.getPerGame(), this.getScore());
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (!(entity instanceof PlayerEntity)) {
            return false;
        }

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        if (playerEntity.getGameTeam() == null) {
            return false;
        }

        this.entity = playerEntity;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    public int getScore() {
        return this.getWiredData().getParams().get(PARAM_SCORE);
    }

    public int getPerGame() {
        return this.getWiredData().getParams().get(PARAM_PER_GAME);
    }

    public int getTeam() {
        return this.getWiredData().getParams().get(PARAM_TEAM);
    }

    public GameTeam getTeamById() {
        switch (this.getTeam()) {
            case 1: {
                return GameTeam.RED;
            }
            case 2: {
                return GameTeam.GREEN;
            }
            case 3: {
                return GameTeam.BLUE;
            }
            case 4: {
                return GameTeam.YELLOW;
            }
            default: {
                return null;
            }
        }
    }
}
