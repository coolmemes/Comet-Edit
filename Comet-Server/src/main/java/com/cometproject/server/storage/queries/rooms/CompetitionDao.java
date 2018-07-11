package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.game.rooms.competition.RoomCompetition;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompetitionDao {

        public static void getRoomsCompetition(List<RoomCompetition> roomData) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = SqlHelper.getConnection();

                preparedStatement = SqlHelper.prepare("SELECT * FROM competition_room_votes", sqlConnection);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    RoomCompetition roomCompetition = new RoomCompetition(resultSet.getInt("player_id"),resultSet.getInt("room_id"), resultSet.getInt("competition"));
                    roomData.add(roomCompetition);
                }
            } catch (SQLException e) {
                SqlHelper.handleSqlException(e);
            } finally {
                SqlHelper.closeSilently(resultSet);
                SqlHelper.closeSilently(preparedStatement);
                SqlHelper.closeSilently(sqlConnection);
            }
        }

        public static void addRoomCompetitionData(int playerId, int roomId) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;

            try {
                sqlConnection = SqlHelper.getConnection();

                preparedStatement = SqlHelper.prepare("INSERT INTO competition_room_votes (`player_id`, `room_id`, `competition`) VALUES(?, ?, 1);", sqlConnection);
                //preparedStatement = SqlHelper.prepare("INSERT INTO competition_room_votes (`player_id`, `room_id`, `competition`) VALUES(?, ?);", sqlConnection);
                preparedStatement.setInt(1, playerId);
                preparedStatement.setInt(2, roomId);

                SqlHelper.executeStatementSilently(preparedStatement, false);
            } catch (SQLException e) {
                SqlHelper.handleSqlException(e);
            } finally {
                SqlHelper.closeSilently(preparedStatement);
                SqlHelper.closeSilently(sqlConnection);
            }
        }
}
