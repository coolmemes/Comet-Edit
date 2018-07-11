package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.types.RoomPhotoType;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.camera.CameraRenderImageMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.camera.CameraThumbnailUpdatedMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.storage.queries.rooms.CameraDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.io.MD5Hash;
import sun.security.provider.MD5;

import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;


public class CameraPhotoPreviewMessageEvent implements Event {
    private boolean isThumbail;

    public CameraPhotoPreviewMessageEvent(boolean isThumbail) {
        this.isThumbail = isThumbail;
    }

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client == null || client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) { return; }

        /*if(client.getPlayer().getLastPhotoTime() + 1000 > Comet.getTimeInt() && client.getPlayer().getData().getRank() < 8) {
            client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("camera.purchase.failed", "You need wait also 10 seconds to render a photo.")));
            return;
        } */

        int length = msg.readInt();
        byte[] bytes = msg.content().readBytes(length).array();

        String hash = DigestUtils.md5Hex("PHOTO-" + Comet.getTimeInt());

        try {
            int photoId = CameraDao.createPhoto(this.isThumbail ? RoomPhotoType.THUMB : RoomPhotoType.PREVIEW, bytes, hash, client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getData().getUsername());

            client.getPlayer().setLastPhotoId(photoId);
            client.getPlayer().setLastPhotoTime(Comet.getTimeMillis());  // convert to miliseconds
            client.getPlayer().setLastPhotoHash(hash);
            client.getPlayer().setLastPhotoPreview(true);
        } finally {
            client.send(this.isThumbail ? new CameraThumbnailUpdatedMessageComposer() : new CameraRenderImageMessageComposer(hash));
        }
    }
}