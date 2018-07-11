package com.cometproject.server.game.gamecenter;

import com.cometproject.server.storage.queries.gamecenter.GameCenterDao;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.*;


public class GameCenterManager implements Initializable {
    private static GameCenterManager gameCenterManagerInstance;
    private static int gameId;
    private Logger log = Logger.getLogger(GameCenterManager.class.getName());

    private List<GameCenterInfo> gamesList;

    public GameCenterManager() {
    }

    @Override
    public void initialize() {
        this.gamesList = new ArrayList<GameCenterInfo>();
        this.loadGameCenterList();

        log.info("RecycleManager initialized");
    }

    public static GameCenterManager getInstance() {
        if (gameCenterManagerInstance == null)
            gameCenterManagerInstance = new GameCenterManager();

        return gameCenterManagerInstance;
    }

    public void loadGameCenterList() {
        if(!this.gamesList.isEmpty()) {
            this.gamesList.clear();
        }

        this.gamesList = GameCenterDao.getGames();
    }

    public GameCenterInfo getGameById(int gameId){

        GameCenterInfo gameInfo = null;
        for(GameCenterInfo infoGame : this.gamesList){
            if(infoGame.getGameId() == gameId){
                gameInfo = infoGame;
                break;
            }
        }
        return gameInfo;
    }

    public List<GameCenterInfo> getGamesList() {
        return this.gamesList;
    }
}
