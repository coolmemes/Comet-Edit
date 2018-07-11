package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.marketplace.MarketplaceManager;
import com.cometproject.server.game.marketplace.types.MarketplaceOfferItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class OffersMessageComposer extends MessageComposer {
    private final List<MarketplaceOfferItem> offers;

    public OffersMessageComposer(List<MarketplaceOfferItem> offers) {
        this.offers = offers;
    }

    public short getId() {
        return Composers.OffersMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.offers.size());
        this.offers.forEach(offer -> {
                    msg.writeInt(offer.getOfferId());
                    msg.writeInt(offer.getState());
                    msg.writeInt(offer.getType());
                    msg.writeInt(offer.getDefinition().getSpriteId());
                    switch (offer.getType()) {
                        case 1:
                        case 2: {
                            msg.writeInt(0);
                            msg.writeString("");
                            break;
                        }
                        case 3: {
                            msg.writeInt(offer.getLimitedStack());
                            msg.writeInt(offer.getLimitedNumber());
                        }
                    }
                    msg.writeInt(offer.getFinalPrice());
                    msg.writeInt(0);
                    msg.writeInt(MarketplaceManager.getInstance().getAvaragePrice(offer.getDefinition().getSpriteId()));
                    msg.writeInt(MarketplaceManager.getInstance().getOffersByItem(offer.getDefinition().getSpriteId()));
                }
        );
        msg.writeInt(this.offers.size());
    }
}