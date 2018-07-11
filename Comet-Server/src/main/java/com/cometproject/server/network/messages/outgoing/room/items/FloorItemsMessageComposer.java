package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;


public class FloorItemsMessageComposer extends MessageComposer {
    private final Room room;

    public FloorItemsMessageComposer(final Room room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.ObjectsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (room.getItems().getFloorItems().size() > 0) {
            msg.writeInt(room.getItems().getItemOwners().size());

            for (Map.Entry<Integer, Integer> itemOwner : room.getItems().getItemOwners().entrySet()) {
                msg.writeInt(itemOwner.getKey());
                msg.writeString(PlayerDao.getUsernameByPlayerId(itemOwner.getValue()));
            }

            if (room.getData().isWiredHidden()) {
                List<RoomItemFloor> items = Lists.newArrayList();

                for (RoomItemFloor item : room.getItems().getFloorItems().values()) {
                    if (!(item instanceof WiredFloorItem)) {
                        items.add(item);
                    }
                }

                msg.writeInt(items.size());

                for (RoomItemFloor item : items) {
                    item.serialize(msg);
                }
            } else {
                msg.writeInt(room.getItems().getFloorItems().size());

                for (RoomItemFloor item : room.getItems().getFloorItems().values()) {
                    item.serialize((msg));
                }
            }

        } else {
            msg.writeInt(0);
            msg.writeInt(0);
        }


    }
}
