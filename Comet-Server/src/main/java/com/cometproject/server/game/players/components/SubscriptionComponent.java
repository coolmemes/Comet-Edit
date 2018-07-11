package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.player.club.SubscriptionDao;
public class SubscriptionComponent implements PlayerComponent {
    private Player player;

    private boolean hasSub;
    private int expire;
    private int start;
    private int presents;

    public SubscriptionComponent(Player player) {
        this.player = player;
        this.load();
    }

    public void load() {
        this.expire = getExpireFromDao();
        this.start = getStartFromDao();
        this.presents = getPresentsFromDao();

        if(isValid()){
            this.hasSub = true;
        }
        else this.hasSub = false;
    }

    public void add(int days) {
        if(this.hasSub) {
            SubscriptionDao.renewSubscription(this.player.getId(), this.getExpire() + 86400 * days);
            load();
            return;
        }
        else {
            SubscriptionDao.addSubscription(this.player.getId(), Comet.getTimeInt() + 86400 * days);
            load();
        }
    }

    public void delete() {
        this.hasSub = false;
    }

    @Override
    public void dispose() {

    }

    public boolean isValid() {
        if (this.getExpire() <= Comet.getTime()) {
            return false;
        }

        return true;
    }

    public boolean exists() {
        return this.hasSub;
    }

    public int getExpire() {
        return this.expire;
    }

    public int getStart() {
        return this.start;
    }

    public int getPresents() {
        return this.presents;
    }

    public void decrementPresents(int playerId){
        this.presents--;
        SubscriptionDao.decrementPresents(playerId, this.presents);
    }

    public int getExpireFromDao() {
        return SubscriptionDao.getExpireTime(this.player.getId());
    }

    public int getStartFromDao() {
        return SubscriptionDao.getStartTime(this.player.getId());
    }

    public int getPresentsFromDao() {
        return SubscriptionDao.getPresents(this.player.getId());
    }

    public Player getPlayer() {
        return this.player;
    }
}