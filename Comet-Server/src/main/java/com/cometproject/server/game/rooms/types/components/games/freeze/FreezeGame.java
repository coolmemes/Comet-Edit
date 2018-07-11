package com.cometproject.server.game.rooms.types.components.games.freeze;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.*;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezeBall;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezePlayer;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezePowerUp;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.freeze.UpdateFreezeLivesMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.Direction;
import com.cometproject.server.utilities.RandomUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

public class FreezeGame extends RoomGame {

    private final Map<Integer, FreezePlayer> players = Maps.newConcurrentMap();

    private final Set<FreezeBall> activeBalls = Sets.newConcurrentHashSet();

    private static final Direction[] EXPLODE_NORMAL = new Direction[]{null, Direction.North, Direction.East, Direction.South, Direction.West};
    private static final Direction[] EXPLODE_DIAGONAL = new Direction[]{null, Direction.NorthEast, Direction.SouthEast, Direction.SouthWest, Direction.NorthWest};
    private static final Direction[] EXPLODE_MEGA = new Direction[]{null, Direction.North, Direction.NorthEast, Direction.East, Direction.SouthEast, Direction.South, Direction.SouthWest, Direction.West, Direction.NorthWest};

    public FreezeGame(Room room) {
        super(room, GameType.FREEZE);
    }

    @Override
    public void tick() {
        for (RoomItemFloor item : room.getItems().getByClass(FreezeTimerFloorItem.class)) {
            item.setExtraData((gameLength - timer) + "");
            item.sendUpdate();
        }

        for (FreezePlayer freezePlayer : this.players.values()) {
            if (freezePlayer.getFreezeTimer() > 0) {
                freezePlayer.decrementFreezeTimer();

                if (freezePlayer.getFreezeTimer() <= 0 && !freezePlayer.getEntity().canWalk()) {
                    freezePlayer.getEntity().setCanWalk(true);
                    freezePlayer.shieldTimer = 10;
                }
            }

            if (freezePlayer.getShieldTimer() > 0) {
                freezePlayer.decrementShieldTimer();
                freezePlayer.getEntity().applyEffect(new PlayerEffect( freezePlayer.getEntity().getGameTeam().getTeamId() + 48, 20));
            }

            if(freezePlayer.getEntity().getCurrentEffect().getEffectId() != freezePlayer.getEntity().getGameTeam().getTeamId() + 39 && freezePlayer.getShieldTimer() <= 0 && freezePlayer.getFreezeTimer() <= 0){
                freezePlayer.getEntity().applyEffect(new PlayerEffect( freezePlayer.getEntity().getGameTeam().getTeamId() + 39, 0));
            }
        }

        for (FreezeBall freezeBall : this.activeBalls) {
            if (freezeBall.getTicksUntilExplode() == FreezeBall.START_TICKS) {
                freezeBall.getSource().tempState(freezeBall.isMega() ? 6000 : (freezeBall.getRange() <= 4 ? freezeBall.getRange() : 4) * 1000);
            }

            if (freezeBall.getTicksUntilExplode() > 0) {
                freezeBall.decrementTicksUntilExplode();
                continue;
            }

            // kaboom
            dir:
            for (Direction direction : freezeBall.isMega() ? EXPLODE_MEGA : (freezeBall.isDiagonal() ? EXPLODE_DIAGONAL : EXPLODE_NORMAL)) {
                for (int i = 1; i <= (direction == null ? 1 : freezeBall.getRange()); i++) {
                    final RoomTile tile = direction == null ? freezeBall.getSource().getTile() : freezeBall.getSource().getRoom().getMapping().getTile(freezeBall.getSource().getPosition().getX() + (i * direction.modX), freezeBall.getSource().getPosition().getY() + (i * direction.modY));

                    if (tile != null) {
                        boolean hasFreezeTile = false;

                        for (RoomItemFloor floorItem : tile.getItems()) {
                            if (floorItem instanceof FreezeTileFloorItem) {
                                hasFreezeTile = true;

                                floorItem.tempState(11000 + ((i > 10 ? 9 : i - 1) * 100));
                            }
                        }

                        if (!hasFreezeTile) {
                            continue dir;
                        }

                        final Set<FreezeBlockFloorItem> blocks = new HashSet<>();

                        for (RoomItemFloor floorItem : tile.getItems()) {
                            if (floorItem instanceof FreezeBlockFloorItem) {
                                blocks.add(((FreezeBlockFloorItem) floorItem));
                            }
                        }

                        for (RoomEntity entity : tile.getEntities()) {
                            if (entity instanceof PlayerEntity) {
                                final PlayerEntity playerEntity = ((PlayerEntity) entity);

                                if (this.players.containsKey(playerEntity.getPlayerId())) {
                                    // we lost 10 points!
                                    this.decreaseScore(playerEntity.getGameTeam(), 10);

                                    final FreezePlayer freezePlayer = this.freezePlayer(playerEntity.getPlayerId());

                                    if (freezePlayer.getFreezeTimer() != 0 || freezePlayer.getShieldTimer() != 0) {
                                        continue;
                                    }

                                    freezePlayer.decrementLives();

                                    Session killer = NetworkManager.getInstance().getSessions().getByPlayerId(freezeBall.getPlayerId());

                                    if(killer != null && killer.getPlayer().getId() != freezePlayer.getEntity().getId()) {
                                        this.increaseScore(killer.getPlayer().getEntity().getGameTeam(), 10);
                                        killer.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_93, 1);

                                        if(freezeBall.isMega()) {
                                            killer.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_94, 1);
                                        }
                                    }

                                    if (freezePlayer.getLives() <= 0) {
                                        this.playerLost(freezePlayer);

                                        return;
                                    }

                                    freezePlayer.setFreezeTimer(5);

                                    entity.applyEffect(new PlayerEffect(12, 6));// quita una vida
                                    playerEntity.getPlayer().getSession().send(new UpdateFreezeLivesMessageComposer(playerEntity.getId(), freezePlayer.getLives()));


                                    entity.cancelWalk();
                                    entity.setCanWalk(false);
                                }
                            }
                        }

                        for (FreezeBlockFloorItem block : blocks) {
                            block.explode();
                        }

                        blocks.clear();
                    }
                }
            }

            final int launcherId = freezeBall.getPlayerId();

            if (this.players.containsKey(launcherId)) {
                this.players.get(launcherId).incrementBalls();
            }
            this.activeBalls.remove(freezeBall);
        }
    }

    private void playerLost(FreezePlayer freezePlayer) {
        final FreezeExitFloorItem exitItem = this.getExitTile();
        freezePlayer.getEntity().teleportToItem(exitItem);

        this.players.remove(freezePlayer.getEntity().getPlayerId());

        this.getGameComponent().getPlayers().remove(freezePlayer.getEntity());
        this.getGameComponent().removeFromTeam(freezePlayer.getEntity());

        int teams = 0;

        for (GameTeam team : this.getGameComponent().getTeams().keySet()) {
            if (this.getGameComponent().getTeams().get(team).size() > 0) {
                teams++;
            }
        }

        if (teams == 1) {
            this.gameComplete();
        }

        freezePlayer.getEntity().applyEffect(new PlayerEffect(0, 0));
    }

    private void gameComplete() {

        final GameTeam winningTeam = this.getBestTeam();

        final FreezeExitFloorItem exitItem = this.getExitTile();

        if(winningTeam != null) {
            for(FreezePlayer freezePlayer : this.players.values()) {
                if(freezePlayer.getEntity().getGameTeam() == winningTeam) {
                    freezePlayer.getEntity().getPlayer().getAchievements().progressAchievement(AchievementType.ACH_77, 1);
                    this.room.getEntities().broadcastMessage(new ActionMessageComposer(freezePlayer.getEntity().getId(), 1));
                }
            }
        }

        for(FreezePlayer freezePlayer : this.players.values()) {
            freezePlayer.getEntity().getPlayer().getAchievements().progressAchievement(AchievementType.ACH_79, 1);
            if(exitItem != null) {
                freezePlayer.getEntity().teleportToItem(exitItem);
            }
            freezePlayer.getEntity().setGameTeam(GameTeam.NONE);
            freezePlayer.getEntity().applyEffect(new PlayerEffect(0,0));
        }

        this.onGameEnds();

    }

    private GameTeam getBestTeam() {
        Map.Entry<GameTeam, Integer> winningTeam = null;

        for (Map.Entry<GameTeam, Integer> score : this.getGameComponent().getScores().entrySet()) {
            if (winningTeam == null || winningTeam.getValue() < score.getValue()) {
                winningTeam = score;
            }
        }

        return winningTeam != null ? winningTeam.getKey() : GameTeam.NONE;
    }

    public void launchBall(FreezeTileFloorItem freezeTile, FreezePlayer freezePlayer) {
        int range = freezePlayer != null ? 2 : (RandomUtil.getRandomBool(0.10) ? 999 : RandomUtil.getRandomInt(1, 3));
        boolean diagonal = freezePlayer == null && (RandomUtil.getRandomBool(0.5));
        int playerId = freezePlayer == null ? -1 : freezePlayer.getEntity().getPlayerId();

        if (freezePlayer != null) {
            switch (freezePlayer.getPowerUp()) {
                case ExtraRange:
                    range += 2;
                    break;

                case DiagonalExplosion:
                    diagonal = true;
                    break;

                case MegaExplosion:
                    range = 999;
            }

            freezePlayer.powerUp(FreezePowerUp.None);
        }

        final FreezeBall freezeBall = new FreezeBall(playerId, freezeTile, range, diagonal);
        this.activeBalls.add(freezeBall);
    }

    @Override
    public void onGameStarts() {
        this.activeBalls.clear();

        // Everyone starts with 40 points & 3 lives.
        for (PlayerEntity playerEntity : this.room.getGame().getPlayers()) {
            this.players.put(playerEntity.getPlayerId(), new FreezePlayer(playerEntity));
            ((FreezeGame) this.room.getGame().getInstance()).increaseScore(playerEntity.getGameTeam(), 40);
            ((FreezeGame) this.room.getGame().getInstance()).updateScoreboard(playerEntity.getGameTeam());

            playerEntity.getPlayer().getSession().send(new UpdateFreezeLivesMessageComposer(playerEntity.getId(), 3));

            updateScoreboard(playerEntity.getGameTeam());
        }

        for (FreezeBlockFloorItem blockItem : this.room.getItems().getByClassTwo(FreezeBlockFloorItem.class)) {
            blockItem.reset();
        }

        for (FreezeExitFloorItem exitItem : this.room.getItems().getByClassTwo(FreezeExitFloorItem.class)) {
            exitItem.setExtraData("1");
            exitItem.sendUpdate();
        }
    }

    public FreezePlayer freezePlayer(final int playerId) {
        return this.players.get(playerId);
    }

    @Override
    public void onGameEnds() {
        for (FreezeBlockFloorItem blockItem : this.room.getItems().getByClassTwo(FreezeBlockFloorItem.class)) {
            blockItem.setExtraData("0");
            blockItem.sendUpdate();
        }

        for (FreezeExitFloorItem exitItem : this.room.getItems().getByClassTwo(FreezeExitFloorItem.class)) {
            exitItem.setExtraData("0");
            exitItem.sendUpdate();
        }

        for (FreezeTimerFloorItem timer : this.room.getItems().getByClassTwo(FreezeTimerFloorItem.class)) {
            timer.setExtraData("0");
            timer.sendUpdate();
            timer.save();
        }

        for (FreezeGateFloorItem gate : this.room.getItems().getByClassTwo(FreezeGateFloorItem.class)) {
            gate.setExtraData("0");
            gate.sendUpdate();
            gate.save();
        }

        for (PlayerEntity playerEntity : this.getGameComponent().getPlayers()) {
            playerEntity.setCanWalk(true);
        }

        this.room.getGame().stop();
        this.activeBalls.clear();
        this.getGameComponent().resetScores(true);
    }

    public void increaseScore(GameTeam team, int amount) {
        this.getGameComponent().increaseScore(team, amount);
        this.updateScoreboard(team);
    }

    public void decreaseScore(GameTeam team, int amount) {
        this.getGameComponent().decreaseScore(team, amount);
        this.updateScoreboard(team);
    }

    public void updateScoreboard(GameTeam team) {
        for (RoomItemFloor scoreboard : this.room.getItems().getByClass(FreezeCounterFloorItem.class)) {
            if (scoreboard instanceof FreezeCounterFloorItem) {
                if (team == null || scoreboard.getDefinition().getItemName().toLowerCase().endsWith(String.valueOf(team.getTeamLetter()))) {
                    ((FreezeCounterFloorItem) scoreboard).sendScoreUpdate(team);
                }
            }
        }
    }

    public int getScore(GameTeam team) {
        return this.getGameComponent().getScore(team);
    }

    private FreezeExitFloorItem getExitTile() {
        for (FreezeExitFloorItem exitItem : this.room.getItems().getByClassTwo(FreezeExitFloorItem.class)) {
            return exitItem;
        }

        return null;
    }
}
