package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class ItemStatsMessageComposer extends MessageComposer {
    private final int itemId;
    private final int spriteId;

    public ItemStatsMessageComposer(int itemId, int spriteId) {
        this.itemId = itemId;
        this.spriteId = spriteId;
    }

    public short getId() {
        return Composers.ItemStatsMessageComposer;
    }

    public void compose(IComposer msg) {
        // TODO: Marketplace Item Stats
        msg.writeInt(500);
        msg.writeInt(5);
        msg.writeInt(3);

        msg.writeInt(3);


            msg.writeInt(-1);
            msg.writeInt(6);
            msg.writeInt(6);

            msg.writeInt(-2);
            msg.writeInt(100);
            msg.writeInt(50);

            msg.writeInt(-3);
            msg.writeInt(50);
            msg.writeInt(10);

        msg.writeInt(itemId);
        msg.writeInt(spriteId);
    }
}