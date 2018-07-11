package com.cometproject.server.storage.queries.catalog;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.recycler.FurniMaticLevel;
import com.cometproject.server.game.catalog.recycler.FurniMaticReward;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RecycleDao {
    public static void getRewards(List<FurniMaticReward> rewards) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_recycler_rewards", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FurniMaticReward reward = new FurniMaticReward(resultSet.getInt("display_id"), resultSet.getInt("item_id"), resultSet.getInt("level"));
                if(reward.getDefinition() == null) { continue; }
                rewards.add(reward);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void getLevels(List<FurniMaticLevel> levels) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_recycler_levels", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FurniMaticLevel level = new FurniMaticLevel(resultSet.getInt("level_id"), resultSet.getInt("chance"));
                levels.add(level);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
