package com.cometproject.server.network.messages.incoming.room.item.wired;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.WiredActionGiveReward;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.WiredActionMatchToSnapshot;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionMatchSnapshot;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.actions.WiredCustomSendEventItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.custom.triggers.WiredTriggerUserIdle;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.SaveWiredMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class SaveWiredDataMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if ((!isOwner && !hasRights) && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            return;
        }

        WiredFloorItem wiredItem = ((WiredFloorItem) room.getItems().getFloorItem(itemId));

        if( ((wiredItem instanceof WiredActionGiveReward) && CometSettings.roomWiredRewardMinimumRank > client.getPlayer().getData().getRank()) ||
                ((wiredItem instanceof WiredCustomSendEventItem) && CometSettings.roomWiredRewardMinimumRank > client.getPlayer().getData().getRank())) {
            return;
        }

        if (wiredItem == null) return;

        int paramCount = msg.readInt();

        for (int param = 0; param < paramCount; param++) {
            int paramData = msg.readInt();

            wiredItem.getWiredData().getParams().put(param, paramData);
        }

        String filteredMessage = msg.readString();

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(filteredMessage);

            if (filterResult.isBlocked()) {
                client.send(new AdvancedAlertMessageComposer(Locale.get("game.message.blocked").replace("%s", filterResult.getMessage())));
                return;
            } else if (filterResult.wasModified()) {
                filteredMessage = filterResult.getMessage();
            }
        }


        wiredItem.getWiredData().setText(filteredMessage);

        wiredItem.getWiredData().getSelectedIds().clear();

        int selectedItemCount = msg.readInt();

        for (int i = 0; i < selectedItemCount; i++) {
            wiredItem.getWiredData().selectItem(ItemManager.getInstance().getItemIdByVirtualId(msg.readInt()));
        }

        if (wiredItem instanceof WiredActionItem) {
            ((WiredActionItem) wiredItem).getWiredData().setDelay(msg.readInt());
        }

        wiredItem.getWiredData().setSelectionType(msg.readInt());
        wiredItem.save();

        if (wiredItem instanceof WiredActionMatchToSnapshot ||
                wiredItem instanceof WiredConditionMatchSnapshot) {
            wiredItem.refreshSnapshots();
        }

        client.send(new SaveWiredMessageComposer());
        wiredItem.onDataRefresh();
        wiredItem.onDataChange();
    }
}
