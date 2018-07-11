package com.cometproject.server.network.messages.outgoing.catalog.subscription;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;


public class SubscriptionRemainingAlertMessageComposer extends MessageComposer {

    private int type;

    public SubscriptionRemainingAlertMessageComposer(int type) {
        this.type = type;
    }

    @Override
    public short getId() {
        return Composers.SubscriptionRemainingAlertMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(type);
    }
}
