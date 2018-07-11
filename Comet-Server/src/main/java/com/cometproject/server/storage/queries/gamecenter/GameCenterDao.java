package com.cometproject.server.storage.queries.gamecenter;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.recycler.FurniMaticLevel;
import com.cometproject.server.game.catalog.recycler.FurniMaticReward;
import com.cometproject.server.game.gamecenter.GameCenterInfo;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameCenterDao {
    public static List<GameCenterInfo> getGames() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<GameCenterInfo> gameList = new ArrayList<GameCenterInfo>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM gamecenter_list WHERE visible = '1'", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                GameCenterInfo game = new GameCenterInfo(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("path"), resultSet.getInt("roomId"));
                gameList.add(game);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
        return gameList;
    }
}
