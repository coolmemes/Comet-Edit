package com.cometproject.server.storage.queries.catalog;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogFrontPageEntry;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.nuxs.NuxGift;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CatalogDao {
    public static void getPages(Map<Integer, CatalogPage> pages) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_pages WHERE visible = '1' ORDER BY order_num", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    int pageId = resultSet.getInt("id");
                    pages.put(pageId, new CatalogPage(resultSet, CatalogManager.getInstance().getItemsForPage(pageId)));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Comet.getServer().getLogger().warn("Failed to load catalog page: " + resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void getItems(Map<Integer, CatalogItem> items) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_items", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    final CatalogItem catalogItem = new CatalogItem(resultSet);

                    if (!catalogItem.getItemId().equals("-1") && catalogItem.getItems().size() == 0) {
                        Comet.getServer().getLogger().warn(String.format("Catalog Item with ID: %s and name: %s has invalid item data! (Data: %s)", catalogItem.getId(), catalogItem.getDisplayName(), catalogItem.getItemId()));
                        continue;
                    }

                    items.put(resultSet.getInt("id"), catalogItem);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Comet.getServer().getLogger().warn("Failed to load catalog item: " + resultSet.getString("id"));
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static List<NuxGift> getNuxGiftsSelectionView() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<NuxGift> nuxGiftsData = new ArrayList<NuxGift>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT id, page_type, type, reward_icon, reward_name, reward_productdata, reward_data FROM catalog_gift_nuxuser ORDER BY id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            int id = 0;

            while (resultSet.next()) {
                nuxGiftsData.add(new NuxGift(
                        id,
                        resultSet.getString("type"),
                        resultSet.getInt("page_type"),
                        resultSet.getString("reward_icon"),
                        resultSet.getString("reward_name"),
                        resultSet.getString("reward_productdata"),
                        resultSet.getString("reward_data")));
                id++;
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return nuxGiftsData;
    }

    private static Map<Integer, CatalogItem> getItemsByPage(int pageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, CatalogItem> data = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_items WHERE page_id = ?", sqlConnection);
            preparedStatement.setInt(1, pageId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
//                try {
//                    int itemId = Integer.parseInt(resultSet.getString("item_ids"));
//
//                    if (itemId != -1 && !ItemManager.getInstance().getItemDefinitions().containsKey(itemId)) {
//                        continue;
//                    }
//                } catch (Exception e) {
//                    continue;
//                }

                try {
                    final CatalogItem catalogItem = new CatalogItem(resultSet);

                    if (!catalogItem.getItemId().equals("-1") && catalogItem.getItems().size() == 0) {
                        Comet.getServer().getLogger().warn(String.format("Catalog Item with ID: %s and name: %s has invalid item data! (Data: %s)", catalogItem.getId(), catalogItem.getDisplayName(), catalogItem.getItemId()));
                        continue;
                    }

                    data.put(resultSet.getInt("id"), catalogItem);
                } catch (Exception e) {
                    Comet.getServer().getLogger().warn("Error while loading catalog item: " + resultSet.getInt("id"));
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

    public static void updateLimitSellsForItem(int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE catalog_items SET limited_sells = limited_sells + 1 WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void loadGiftBoxes(List<Integer> giftBoxesOld, List<Integer> giftBoxesNew) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_gift_wrapping", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString("type").equals("old")) {
                    giftBoxesOld.add(resultSet.getInt("sprite_id"));
                } else {
                    giftBoxesNew.add(resultSet.getInt("sprite_id"));
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void getFeaturedPages(List<CatalogFrontPageEntry> frontPageEntries) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_featured_pages", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                frontPageEntries.add(new CatalogFrontPageEntry(resultSet.getInt("id"), resultSet.getString("caption"),
                        resultSet.getString("image"), resultSet.getString("page_link"), resultSet.getInt("page_id")));
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
