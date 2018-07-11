package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.types.RoomPhotoType;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.camera.CameraPurchasedMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.rooms.CameraDao;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Edu on 23/12/2016.
 */
public class CameraPhotoPurchaseMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) throws InterruptedException {
        if(client == null || client.getPlayer() == null || client.getPlayer().getData() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) { return; }

        Set<PlayerItem> unseenItems = Sets.newHashSet();

        try {
            int playerId = client.getPlayer().getId();
            int photoBaseId = CometSettings.cameraPhotoItemId;
            String extradata = "{\"t\":" + client.getPlayer().getLastPhotoTime() + ",\"u\":\"" + client.getPlayer().getId() + "\",\"n\":\"" + client.getPlayer().getData().getUsername() + "\",\"m\":\"\",\"s\":" + client.getPlayer().getId() + ",\"w\":\"" + CometSettings.cameraPhotoUrl.replace("%type%", "photo").replace("%hash%", client.getPlayer().getLastPhotoHash()) + "\"}";

            Long itemId = ItemDao.createItem(playerId, photoBaseId, extradata);

            unseenItems.add(client.getPlayer().getInventory().add(itemId, photoBaseId, extradata, null, null));

            if (!CometSettings.playerInfiniteBalance) {
                int creditsCost = 0;
                int ducketsCost = 0;

                if(client.getPlayer().getData().getCredits() < creditsCost || client.getPlayer().getData().getActivityPoints() < ducketsCost) {
                    client.send(new AlertMessageComposer(Locale.get("catalog.error.notenough")));
                    return;
                }

                client.getPlayer().getData().decreaseCredits(creditsCost);
                client.getPlayer().getData().decreaseActivityPoints(ducketsCost);

                client.getPlayer().sendBalance();
                client.getPlayer().getData().save();
            }

            client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_12, 1);
            client.send(new UpdateInventoryMessageComposer());
            client.send(new UnseenItemsMessageComposer(unseenItems));

            if(client.getPlayer().getLastPhotoPreview()) {
                CameraDao.updatePhotoType(RoomPhotoType.PHOTO, client.getPlayer().getLastPhotoId());
                client.getPlayer().setLastPhotoPreview(false);
            }
        } finally {
            client.send(new CameraPurchasedMessageComposer());
        }
    }
}