package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Maps;

import java.util.Map;


public class PlaceItemMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return;
        }

        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        String data = msg.readString();

        if(client.getPlayer() == null || client.getPlayer().getInventory() == null) return;

        if (data == null) return;

        String[] parts = data.split(" ");
        int id = Integer.parseInt(parts[0].replace("-", ""));

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            Map<String, String> notificationParams = Maps.newHashMap();

            notificationParams.put("message", "${room.error.cant_set_not_owner}");

            client.send(new NotificationMessageComposer("furni_placement_error", notificationParams));
            return;
        }

        try {
            if (parts.length > 1 && parts[1].startsWith(":")) {
                String position = Position.validateWallPosition(parts[1] + " " + parts[2] + " " + parts[3]);

                if (position == null) {
                    return;
                }

                Long itemId = ItemManager.getInstance().getItemIdByVirtualId(id);

                if(itemId == null) {
                    return;
                }

                PlayerItem item = client.getPlayer().getInventory().getWallItem(itemId);

                if (item == null) {
                    return;
                }

                client.getPlayer().getEntity().getRoom().getItems().placeWallItem(item, position, client.getPlayer());

            } else {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int rot = Integer.parseInt(parts[3]);

                Long itemId = ItemManager.getInstance().getItemIdByVirtualId(id);

                if(itemId == null) {
                    return;
                }

                PlayerItem item = client.getPlayer().getInventory().getFloorItem(itemId);

                if (item == null) {
                    return;
                }

                client.getPlayer().getEntity().getRoom().getItems().placeFloorItem(item, x, y, rot, client.getPlayer());

                if(client.getPlayer().getEntity().getRoom().getItems() == null) {
                    return;
                }

                RoomItemFloor floorItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(item.getId());

                //TODO: Item placement achievements.

                /*switch (floorItem.getDefinition().getItemName())
                {
                    case "snowb_slope":
                        client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_69, 1);
                        break;
                    case "easter11_grasspatch":
                        client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_69, 1);
                        break;
                    case "val11_floor":
                        client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_69, 1);
                        break;
                }*/


                if (floorItem != null) {
                    RoomTile tile = floorItem.getTile();

                    if (tile != null) {
                        if (tile.getItems().size() > 1) {
                            client.getPlayer().getQuests().progressQuest(QuestType.FURNI_STACK);
                        }
                    }
                }
            }
        } catch (Exception e) {
            client.getLogger().error("Error while placing item", e);
        }

        client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_45, 1);
        client.getPlayer().getQuests().progressQuest(QuestType.FURNI_PLACE);
    }
}
