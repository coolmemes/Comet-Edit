package com.cometproject.server.network.messages.outgoing.user.newyearresolution;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;

public class NewYearResolutionCompletedMessageComposer extends MessageComposer {
    private String badge;

    public NewYearResolutionCompletedMessageComposer(String badge) {
        this.badge = badge;
    }

    @Override
    public short getId() {
        return Composers.NewYearResolutionCompletedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(badge);
        msg.writeString(badge);
    }
}
