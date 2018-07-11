package com.cometproject.server.storage.queries.items;

import com.cometproject.server.game.catalog.purchase.OldCatalogPurchaseHandler;
import com.cometproject.server.game.items.ItemManager;
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


public class ItemDao {
    public static Map<Integer, ItemDefinition> getDefinitions() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, ItemDefinition> data = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM furniture", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    data.put(resultSet.getInt("id"), new ItemDefinition(resultSet));
                } catch (Exception e) {
                    ItemManager.getInstance().getLogger().warn("Error while loading item definition for ID: " + resultSet.getInt("id"), e);
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

    public static long createItem(int userId, int itemId, String data) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into items (`user_id`, `room_id`, `base_item`, `extra_data`, `x`, `y`, `z`, `rot`, `wall_pos`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, itemId);
            preparedStatement.setString(4, data);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setInt(8, 0);
            preparedStatement.setString(9, "");

            SqlHelper.executeStatementSilently(preparedStatement, false);

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return resultSet.getLong(1);
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

    public static List<Long> createItems(List<OldCatalogPurchaseHandler.CatalogPurchase> catalogPurchases) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Long> data = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into items (`user_id`, `room_id`, `base_item`, `extra_data`, `x`, `y`, `z`, `rot`, `wall_pos`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

            for (OldCatalogPurchaseHandler.CatalogPurchase purchase : catalogPurchases) {
                preparedStatement.setInt(1, purchase.getPlayerId());
                preparedStatement.setInt(2, 0);
                preparedStatement.setInt(3, purchase.getItemBaseId());
                preparedStatement.setString(4, purchase.getData());
                preparedStatement.setInt(5, 0);
                preparedStatement.setInt(6, 0);
                preparedStatement.setInt(7, 0);
                preparedStatement.setInt(8, 0);
                preparedStatement.setString(9, "");

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                data.add(resultSet.getLong(1));
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

    public enum DefinitionType{

        WIDTH("width"),
        LENGHT("lenght"),
        INTERACTION("interaction_type"),
        MULTIHEIGHT("variable_heights"),
        MODES("interaction_modes_count"),
        HEIGHT("stack_height"),
        TRADE("allow_trade"),
        STACK("can_stack"),
        STACK_ITEMS("allow_inventory_stack"),
        GIFT("allow_gift"),
        SIT("can_sit"),
        VENDING("vending_ids"),
        WALK("is_walkable"),
        EFFECT("effectid"),
        NEED_RIGHTS("requires_rights"),
        LAY("canlayon");

        private String type;

        DefinitionType(String type) { this.type = type; }

        public String getDefinitionType() {
            return type;
        }
    }

    public static void updateDefinition(DefinitionType type, String value, int baseId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE furniture SET " + type.getDefinitionType() + " = ? WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, baseId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static int getItemByName(String itemName) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT id FROM furniture WHERE item_name = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, itemName);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
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

}
