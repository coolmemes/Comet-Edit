package com.cometproject.server.network.messages.outgoing.catalog.recycler;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.recycler.FurniMaticReward;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salinas on 01/02/2018.
 */
public class RecyclerErrorMessageComposer extends MessageComposer {
    private List<FurniMaticReward> rewards;
    private List<FurniMaticReward> levelRewards;

    public RecyclerErrorMessageComposer() {
    }

    @Override
    public short getId() {
        return Composers.RecyclerErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeInt(0);

    }
}
