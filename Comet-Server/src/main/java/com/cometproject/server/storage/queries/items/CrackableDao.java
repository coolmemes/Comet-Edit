package com.cometproject.server.storage.queries.items;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crackable.CrackableItem;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrackableDao {
    public static List<CrackableItem> getCrackableRewards() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<CrackableItem> data = new ArrayList<CrackableItem>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM furniture_crackable_data", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String itemIds = resultSet.getString("itemId");
                int max = resultSet.getInt("maxTouch");
                int effect = resultSet.getInt("effectNeeded");
                String type = resultSet.getString("type");
                AchievementType tickAchievement = AchievementType.getTypeByName(resultSet.getString("achievement_tick"));
                AchievementType crackedAchievement = AchievementType.getTypeByName(resultSet.getString("achievement_cracked"));
                if(itemIds.contains(",")) {
                    for(String id : itemIds.split(",")) { data.add(new CrackableItem(Integer.valueOf(id), CrackableItem.CrackableType.valueOf(type), max, effect, tickAchievement, crackedAchievement, resultSet.getString("rewardsId"))); }
                } else {
                    data.add(new CrackableItem(resultSet.getInt("itemId"), CrackableItem.CrackableType.valueOf(type), max, effect, tickAchievement, crackedAchievement, resultSet.getString("rewardsId")));
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static void addCrackableReward(int crackableId, int rewardId, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO logs_crackable_items (crackable_id, reward_id, player_id) VALUES (?, ?, ?);", sqlConnection);

            preparedStatement.setInt(1, crackableId);
            preparedStatement.setInt(2, rewardId);
            preparedStatement.setInt(3, playerId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
