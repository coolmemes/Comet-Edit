package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PurchaseItemMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        int pageId = msg.readInt();
        int itemId = msg.readInt();
        String data = msg.readString();
        int amount = msg.readInt();

        CatalogManager.getInstance().getPurchaseHandler().purchaseItem(client, pageId, itemId, data, amount, null);
    }
}
