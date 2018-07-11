package com.cometproject.server.network.messages.outgoing.user.club;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.SubscriptionComponent;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class ClubStatusMessageComposer extends MessageComposer {
    private final SubscriptionComponent subscriptionComponent;

    public ClubStatusMessageComposer(final SubscriptionComponent subscriptionComponent) {
        this.subscriptionComponent = subscriptionComponent;
    }

    @Override
    public short getId() {
        return Composers.ScrSendUserInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        int timeLeft = 0;
        int days = 0;
        int months = 0;

        boolean needsForce = false;

        if(subscriptionComponent.getExpire() > 0) {
            needsForce = false;
        } else {
            needsForce = true;
        }

        if (subscriptionComponent.isValid()) {
            timeLeft = subscriptionComponent.getExpire() - (int) Comet.getTime();
            days = (int) Math.ceil(timeLeft / 86400);
            months = days / 31;

            if (months >= 1) {
                months--;
            }
        } else {
            if (subscriptionComponent.exists()) {
                subscriptionComponent.delete();
            }
        }

        msg.writeString("habbo_club");

        msg.writeInt(days - (months * 31));
        msg.writeInt(2);
        msg.writeInt(months);
        msg.writeInt(1); // tipo 1 HC 2 VIP
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(subscriptionComponent.getExpire()); // tiempo restante de la suscripción
        msg.writeInt(needsForce ? 1 : subscriptionComponent.getExpire());

    }
}
