package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.types.RoomPhotoType;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.camera.CameraPublishedMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.rooms.CameraDao;

/**
 * Created by Edu on 25/12/2016.
 */
public class CameraPhotoPublishMessageEvent implements Event {

    public void handle(Session client, MessageEvent msg) throws InterruptedException {
        if(client == null || client.getPlayer() == null || client.getPlayer().getData() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) { return; }

        try {
            /*if(!CometSettings.cameraPhotoPublishEnabled) {
                client.send(new AlertMessageComposer(Locale.getOrDefault("camera.publish.disabled", "Photo publishing disabled :/")));
                return;
            }*/

            if (client.getPlayer().getLastPhotoPreview()) {
                CameraDao.updatePhotoType(RoomPhotoType.PHOTO, client.getPlayer().getLastPhotoId());
                client.getPlayer().setLastPhotoPreview(false);
            }

            if (!CometSettings.playerInfiniteBalance) {
                int ducketsCost = 150;

                if(client.getPlayer().getData().getActivityPoints() < ducketsCost) {
                    client.send(new AlertMessageComposer(Locale.get("catalog.error.notenough")));
                    return;
                }

                client.getPlayer().getData().decreaseActivityPoints(ducketsCost);

                client.getPlayer().sendBalance();
                client.getPlayer().getData().save();
            }

            CameraDao.publishPhoto(client.getPlayer().getLastPhotoId());

        } finally {
            client.send(new CameraPublishedMessageComposer(client.getPlayer().getLastPhotoHash()));
        }
    }
}