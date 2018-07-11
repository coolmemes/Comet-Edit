package com.cometproject.server.storage.queries.landing;

import com.cometproject.server.game.landing.types.PromoArticle;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.game.players.data.PlayerAvatarData;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class LandingDao {
    public static Map<Integer, PromoArticle> getArticles() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, PromoArticle> data = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM landing_articles WHERE visible = '1'", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getInt("id"), new PromoArticle(resultSet));
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

    public static Map<PlayerAvatar, Integer> getHallOfFame(String currency, int limit) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<PlayerAvatar, Integer> data = new LinkedHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT id, username, figure, " + currency + " FROM players WHERE " + currency + " > 0 ORDER BY " + currency + " DESC LIMIT " + limit, sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int playerId = resultSet.getInt("id");
                PlayerData playerData = PlayerDao.getDataById(playerId);

                if(BanManager.getInstance().hasBan("" + playerId, BanType.USER) || playerData.getRank() > 2)
                    continue;

                data.put(new PlayerAvatarData(playerId, resultSet.getString("username"), resultSet.getString("figure")), resultSet.getInt(currency));
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
}
