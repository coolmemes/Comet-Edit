package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PurchaseGiftMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        int pageId = msg.readInt();
        int itemId = msg.readInt();

        String extraData = msg.readString();

        String sendingUser = msg.readString();
        String message = msg.readString();
        int spriteId = msg.readInt();
        int wrappingPaper = msg.readInt();
        int decorationType = msg.readInt();
        boolean showUsername = msg.readBoolean();

        if(!CatalogManager.getInstance().getGiftBoxesNew().contains(spriteId) && !CatalogManager.getInstance().getGiftBoxesOld().contains(spriteId)) {
            client.disconnect();
            return;
        }

        GiftData data = new GiftData(pageId, itemId, client.getPlayer().getId(), sendingUser, message, spriteId, wrappingPaper, decorationType, showUsername, extraData);

        CatalogManager.getInstance().getPurchaseHandler().purchaseItem(client, pageId, itemId, extraData, 1, data);
    }
}
