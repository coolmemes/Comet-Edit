package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomPhotoType;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CameraDao {

    private static Logger log = Logger.getLogger(RoomItemDao.class.getName());

    public static int createPhoto(RoomPhotoType type, byte[] bytes, String hash, int roomId, String author) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO server_camera (type, image, room_id, hash, created_at, author) VALUES (?, ?, ?, ?, ?, ?)", sqlConnection, true);
            preparedStatement.setString(1, type.getType());
            preparedStatement.setBytes(2, bytes);
            preparedStatement.setInt(3, roomId);
            preparedStatement.setString(4, hash);
            preparedStatement.setInt(5, Comet.getTimeInt());
            preparedStatement.setString(6, author);

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }

    public static void updatePhotoType(RoomPhotoType type, int photoId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE server_camera SET type = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, type.getType());
            preparedStatement.setInt(2, photoId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void publishPhoto(int photoId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE server_camera SET published = '1' WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, photoId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void deleteThumbnail(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM server_camera WHERE room_id = ? AND type = ?", sqlConnection);

            preparedStatement.setInt(1, roomId);
            preparedStatement.setString(2, "thumb");

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
