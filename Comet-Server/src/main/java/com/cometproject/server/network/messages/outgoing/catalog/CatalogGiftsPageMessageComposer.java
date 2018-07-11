package com.cometproject.server.network.messages.outgoing.catalog;

        import com.cometproject.api.networking.messages.IComposer;
        import com.cometproject.server.boot.Comet;
        import com.cometproject.server.game.catalog.types.CatalogItem;
        import com.cometproject.server.game.catalog.types.CatalogPage;
        import com.cometproject.server.game.players.components.SubscriptionComponent;
        import com.cometproject.server.network.messages.composers.MessageComposer;
        import com.cometproject.server.protocol.headers.Composers;


public class CatalogGiftsPageMessageComposer extends MessageComposer {

    private final CatalogPage catalogPage;
    private final SubscriptionComponent subscriptionComponent;

    public CatalogGiftsPageMessageComposer(final CatalogPage catalogPage, final SubscriptionComponent subscriptionComponent) {
        this.catalogPage = catalogPage;
        this.subscriptionComponent = subscriptionComponent;
    }

    @Override
    public short getId() {
        return Composers.CatalogGiftsPageMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        int timeLeft = (subscriptionComponent.getExpire() - (int) Comet.getTime()) / 86400;
        int size = this.catalogPage.getItems().size();

        msg.writeInt(timeLeft);
        msg.writeInt(subscriptionComponent.getPresents());
        msg.writeInt(size);

        for (CatalogItem item : this.catalogPage.getItems().values()) {
            item.composeClubPresents(msg);
        }

        msg.writeInt(size);

        for (CatalogItem item : this.catalogPage.getItems().values()) {
            item.serializeAvailability(msg);
        }
    }
}
