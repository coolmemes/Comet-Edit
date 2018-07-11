package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class NameChangeVerificationMessageComposer extends MessageComposer {
    private String name;
    private int error;
    private List<String> tags;

    public NameChangeVerificationMessageComposer(String name, int error, List<String> tags) {
        this.name = name;
        this.error = error;
        this.tags = tags;
    }

    public NameChangeVerificationMessageComposer(String name, int error) {
        this.name = name;
        this.error = error;
    }

    @Override
    public short getId() {
        return Composers.NameChangeVerificationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(error);
        msg.writeString(name);

        if (this.tags == null) {
            msg.writeInt(0);
        } else {
            msg.writeInt(tags.size());
            for (String tag : tags) {
                msg.writeString(name + tag);
            }
        }
    }
}
