package com.cometproject.server.game.rooms.competition;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.catalog.purchase.OldCatalogPurchaseHandler;
import com.cometproject.server.game.catalog.recycler.FurniMaticReward;
import com.cometproject.server.game.catalog.types.CatalogFrontPageEntry;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogOffer;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.storage.queries.catalog.CatalogDao;
import com.cometproject.server.storage.queries.catalog.RecycleDao;
import com.cometproject.server.storage.queries.rooms.CompetitionDao;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import com.cometproject.server.utilities.Initializable;
import com.cometproject.server.utilities.RandomInteger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.log4j.Logger;

import java.util.*;


public class CompetitionManager implements Initializable {
    private static CompetitionManager competitionManagerInstance;
    private Logger log = Logger.getLogger(CompetitionManager.class.getName());
    private int competitionId = CometSettings.competitionCampaignId;
    private List<RoomCompetition> roomsData;

    public CompetitionManager() {
    }

    @Override
    public void initialize(){
        this.roomsData = new ArrayList<RoomCompetition>();
        this.loadRoomsCompetitions();

        log.info("CompetitionManager initialized");
    }

    public static CompetitionManager getInstance() {
        if (competitionManagerInstance == null)
            competitionManagerInstance = new CompetitionManager();

        return competitionManagerInstance;
    }

    public void loadRoomsCompetitions() {
        if(!this.roomsData.isEmpty()) {
            this.roomsData.clear();
        }

        CompetitionDao.getRoomsCompetition(this.roomsData);
    }

    public int getVotesByRoomId(int roomId, int competitionId){
        int votes = 0;
        for(RoomCompetition data : this.roomsData){
            if(data.getRoomId() == roomId && data.getCompetition() == competitionId && data.getPlayerId() != RoomManager.getInstance().getRoomData(data.getRoomId()).getOwnerId()){
                votes++;
            }
        }
        return votes;
    }

    public boolean playerHasVote(int playerId, int roomId, int competitionId){
        for(RoomCompetition data : this.roomsData){
            if(data.getPlayerId() == playerId && data.getRoomId() == roomId && data.getCompetition() == competitionId){
                return true;
            }
        }
        return false;
    }

    public boolean competitionIsActive(int playerId, int roomId, int competitionId){
        for(RoomCompetition data : this.roomsData){
            if(data.getPlayerId() == RoomManager.getInstance().getRoomData(data.getRoomId()).getOwnerId() && data.getRoomId() == roomId && data.getCompetition() == competitionId){
                return true;
            }
        }
        return false;
    }

    public void addNewCompetitionData(int roomId, int playerId){
        CompetitionDao.addRoomCompetitionData(playerId, roomId);
        RoomCompetition roomCompetition = new RoomCompetition(playerId, roomId, competitionId);
        this.roomsData.add(roomCompetition);
    }
}
