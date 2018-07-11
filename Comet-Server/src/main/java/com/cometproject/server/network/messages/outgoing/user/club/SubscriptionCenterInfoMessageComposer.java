package com.cometproject.server.network.messages.outgoing.user.club;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.SubscriptionComponent;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.sql.Date;
import java.sql.Timestamp;


public class SubscriptionCenterInfoMessageComposer extends MessageComposer {
    private final SubscriptionComponent subscriptionComponent;

    public SubscriptionCenterInfoMessageComposer(final SubscriptionComponent subscriptionComponent) {
        this.subscriptionComponent = subscriptionComponent;
    }

    @Override
    public short getId() {
        return Composers.SubscriptionCenterInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        Date start = new Date(subscriptionComponent.getStart());
        msg.writeInt(1); // días seguidos suscrito
        msg.writeString(start.toString()); // fecha de suscripción
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        msg.writeInt(0); // créditos gastados
        msg.writeInt(0); // premio a recibir
        msg.writeInt(0); // créditos gastados?
        msg.writeInt(0); // siguiente paga en minutos
    }
}
