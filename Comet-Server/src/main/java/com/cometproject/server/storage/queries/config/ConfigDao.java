package com.cometproject.server.storage.queries.config;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConfigDao {
    public static void getAll() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet config = null;
        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM server_configuration LIMIT 1", sqlConnection);

            config = preparedStatement.executeQuery();

            while(config.next()) {

                CometSettings.motdEnabled = config.getBoolean("motd_enabled");
                CometSettings.motdMessage = config.getString("motd_message");
                CometSettings.hotelName = config.getString("hotel_name");
                CometSettings.hotelUrl = config.getString("hotel_url");
                CometSettings.groupCost = config.getInt("group_cost");
                CometSettings.onlineRewardEnabled = config.getBoolean("online_reward_enabled");
                CometSettings.onlineRewardCredits = config.getInt("online_reward_credits");
                CometSettings.onlineRewardDuckets = config.getInt("online_reward_duckets");
                CometSettings.onlineRewardDiamonds = config.getInt("online_reward_diamonds");
                CometSettings.onlineRewardInterval = config.getInt("online_reward_interval");
                CometSettings.aboutImg = config.getString("about_image");
                CometSettings.eventImg = config.getString("event_image");
                CometSettings.aboutShowPlayersOnline = config.getBoolean("about_show_players_online");
                CometSettings.aboutShowRoomsActive = config.getBoolean("about_show_rooms_active");
                CometSettings.aboutShowUptime = config.getBoolean("about_show_uptime");
                CometSettings.floorEditorMaxX = config.getInt("floor_editor_max_x");
                CometSettings.floorEditorMaxY = config.getInt("floor_editor_max_y");
                CometSettings.floorEditorMaxTotal = config.getInt("floor_editor_max_total");
                CometSettings.roomMaxPlayers = config.getInt("room_max_players");
                CometSettings.roomEncryptPasswords = config.getBoolean("room_encrypt_passwords");
                CometSettings.roomCanPlaceItemOnEntity = config.getBoolean("room_can_place_item_on_entity");
                CometSettings.roomMaxBots = config.getInt("room_max_bots");
                CometSettings.roomMaxPets = config.getInt("room_max_pets");
                CometSettings.roomWiredRewardMinimumRank = config.getInt("room_wired_reward_minimum_rank");
                CometSettings.roomIdleMinutes = config.getInt("room_idle_minutes");
                CometSettings.roomGameIdleSeconds = config.getInt("room_game_idle_seconds");
                CometSettings.wordFilterMode = FilterMode.valueOf(config.getString("word_filter_mode").toUpperCase());
                CometSettings.useDatabaseIp = config.getBoolean("use_database_ip");
                CometSettings.saveLogins = config.getBoolean("save_logins");
                CometSettings.playerInfiniteBalance = config.getBoolean("player_infinite_balance");
                CometSettings.playerGiftCooldown = config.getInt("player_gift_cooldown");
                CometSettings.playerChangeFigureCooldown = config.getInt("player_change_figure_cooldown");
                CometSettings.playerFigureValidation = config.getBoolean("player_figure_validation");
                CometSettings.messengerMaxFriends = config.getInt("messenger_max_friends");
                CometSettings.messengerLogMessages = config.getBoolean("messenger_log_messages");
                CometSettings.storageItemQueueEnabled = config.getBoolean("storage_item_queue_enabled");
                CometSettings.storagePlayerQueueEnabled = config.getBoolean("storage_player_queue_enabled");
                CometSettings.furniMaticBoxItemId = config.getInt("catalog_furnimatic_box_item_id");
                CometSettings.hallOfFameEnabled = config.getBoolean("hall_of_fame_enabled");
                CometSettings.hallOfFameCurrency = config.getString("hall_of_fame_currency");
                CometSettings.hallOfFameRefreshMinutes = config.getInt("hall_of_fame_refresh_minutes");
                CometSettings.hallOfFameTextsKey = config.getString("hall_of_fame_texts_key");
                CometSettings.cameraPhotoItemId = config.getInt("catalog_camera_photo_item_id");
                CometSettings.cameraPhotoUrl = config.getString("camera_photo_url");
                CometSettings.competitionCampaignId = config.getInt("competition_campaign_id");
                CometSettings.competitionCampaignName = config.getString("competition_campaign_name");
                CometSettings.competitionCampaignEnabled = config.getBoolean("competition_campaign_enabled");
                CometSettings.maxSeasonalRewardPoints = config.getInt("max_seasonal_reward");
                CometSettings.minRankPinCodeRequired = config.getInt("min_rank_pincode");
                CometSettings.clubPresentPageID = config.getInt("club_gift_pageid");
                CometSettings.eventWinnerNotification = config.getBoolean("event_winner_notification");
                final String characters = config.getString("word_filter_strict_chars");

                CometSettings.strictFilterCharacters.clear();

                for (String charSet : characters.split(",")) {
                    if (!charSet.contains(":")) continue;

                    final String[] chars = charSet.split(":");

                    if (chars.length == 2) {
                        CometSettings.strictFilterCharacters.put(chars[0], chars[1]);
                    } else {
                        CometSettings.strictFilterCharacters.put(chars[0], "");
                    }
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(config);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
