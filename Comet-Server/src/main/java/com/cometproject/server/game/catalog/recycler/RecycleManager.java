package com.cometproject.server.game.catalog.recycler;

import com.cometproject.server.game.catalog.purchase.OldCatalogPurchaseHandler;
import com.cometproject.server.game.catalog.recycler.FurniMaticReward;
import com.cometproject.server.game.catalog.types.CatalogFrontPageEntry;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogOffer;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import com.cometproject.server.storage.queries.catalog.RecycleDao;
import com.cometproject.server.utilities.Initializable;
import com.cometproject.server.utilities.RandomInteger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.log4j.Logger;

import java.util.*;


public class RecycleManager implements Initializable {
    private static RecycleManager recycleManagerInstance;
    private static int rewardId;
    private Logger log = Logger.getLogger(RecycleManager.class.getName());

    private List<FurniMaticReward> rewards;
    private List<FurniMaticLevel> levels;

    public RecycleManager() {
    }

    @Override
    public void initialize() {
        this.rewards = new ArrayList<FurniMaticReward>();
        this.levels = new ArrayList<FurniMaticLevel>();
        this.loadRecyclerMachine();

        log.info("RecycleManager initialized");
    }

    public static RecycleManager getInstance() {
        if (recycleManagerInstance == null)
            recycleManagerInstance = new RecycleManager();

        return recycleManagerInstance;
    }

    public void loadRecyclerMachine() {
        if(!this.rewards.isEmpty()) {
            this.rewards.clear();
        }

        if(!this.levels.isEmpty()) {
            this.levels.clear();
        }

        RecycleDao.getRewards(this.rewards);
        RecycleDao.getLevels(this.levels);
    }

    public int getRandomReward(){
        List<Integer> levelRewards = new ArrayList<>();
        int random = 0;
        Boolean found = false;

        for(FurniMaticLevel level : this.levels){

            if(found || level.getLevelId() == 1) { break; }

            random = RandomInteger.getRandom(1, level.getChance());

            if(random == level.getChance()){

                for(FurniMaticReward reward : this.rewards) {
                    if(reward.getLevel() == level.getLevelId())
                    levelRewards.add(reward.getBaseId());
                    else{ continue; }
                }

                rewardId = levelRewards.get((int)Math.floor(Math.random() * levelRewards.size()));
                levelRewards.clear();
                found = true;
            }
            else { continue; }
        }

        if(!found){

            for(FurniMaticReward reward : this.rewards) {
                if(reward.getLevel() != 1) { continue; }
                levelRewards.add(reward.getBaseId());
            }

            rewardId = levelRewards.get((int)Math.floor(Math.random() * levelRewards.size()));
            levelRewards.clear();
        }
        return rewardId;
    }

    public List<FurniMaticReward> getRewards() {
        return this.rewards;
    }

    public List<FurniMaticLevel> getLevels() {
        return this.levels;
    }

}
