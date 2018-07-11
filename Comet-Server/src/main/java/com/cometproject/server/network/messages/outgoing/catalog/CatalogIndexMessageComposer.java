package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;


public class CatalogIndexMessageComposer extends MessageComposer {

    private final int playerRank;
    private final boolean vip;

    public CatalogIndexMessageComposer(int playerRank, boolean vip) {
        this.playerRank = playerRank;
        this.vip = vip;
    }

    @Override
    public short getId() {
        return Composers.CatalogIndexMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        List<CatalogPage> pages = CatalogManager.getInstance().getPagesForRank(this.playerRank, this.vip);

        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(-1);
        msg.writeString("root");
        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(count(-1, pages));

        for (CatalogPage page : pages) {
            if (page.getParentId() != -1) {
                continue;
            }

            msg.writeBoolean(true);
            msg.writeInt(page.getIcon());
            msg.writeInt(page.getId());
            msg.writeString(page.getLinkName().equals("undefined") ? page.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : page.getLinkName());
            msg.writeString(page.getCaption());
            msg.writeInt(page.getOfferSize());
	/*
	foreach (int i in Page.ItemOffers.Keys)
	{
		msg.writeInt(i);
	}*/
            msg.writeInt(count(page.getId(), pages));


            for (CatalogPage child : pages)
            {
                if (child.getParentId() != page.getId()) {
                    continue;
                }

                msg.writeBoolean(true);
                msg.writeInt(child.getIcon());
                msg.writeInt(child.getId());
                msg.writeString(child.getLinkName().equals("undefined") ? child.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : child.getLinkName());
                msg.writeString(child.getCaption());
                msg.writeInt(child.getOfferSize());
                for (CatalogItem item : child.getItems().values()) {
                    if(item.getItemId().equals("-1")) continue;

                    ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                    if (itemDefinition != null && itemDefinition.getOfferId() != -1 && itemDefinition.getOfferId() != 0) {
                        msg.writeInt(itemDefinition.getOfferId());
                    }
                }
                msg.writeInt(count(child.getId(), pages));

                for (CatalogPage baby : pages)
                {
                    if (baby.getParentId() != child.getId()) {
                        continue;
                    }

                    msg.writeBoolean(true);
                    msg.writeInt(baby.getIcon());
                    msg.writeInt(baby.getId());
                    msg.writeString(baby.getLinkName().equals("undefined") ? baby.getCaption().toLowerCase().replaceAll("[^A-Za-z0-9]", "").replace(" ", "_") : baby.getLinkName());
                    msg.writeString(baby.getCaption());
                    msg.writeInt(baby.getOfferSize());
                    for (CatalogItem item : baby.getItems().values()) {
                        if(item.getItemId().equals("-1")) continue;

                        ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId());

                        if (itemDefinition != null && itemDefinition.getOfferId() != -1 && itemDefinition.getOfferId() != 0) {
                            msg.writeInt(itemDefinition.getOfferId());
                        }
                    }
                    msg.writeInt(0);
                }
            }
        }

        msg.writeBoolean(false);
        msg.writeString("NORMAL");
    }

    private int count(int index, List<CatalogPage> pages) {
        int i = 0;

        for (CatalogPage page : pages) {
            if (page.getParentId() == index) {
                i++;
            }
        }

        return i;
    }
}
