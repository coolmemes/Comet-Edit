package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.NuxAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.UpdateStackMapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.ItemDao;

import java.util.List;

public class SaveRoomBrandingCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) return;

        String action = params[0];

        Room room = client.getPlayer().getEntity().getRoom();
        RoomTile tileToFindItem = room.getMapping().getTile(client.getPlayer().getEntity().getPosition().devX(), client.getPlayer().getEntity().getPosition().devY());
        RoomItemFloor itemFloor2 = tileToFindItem.getTopItemInstance();

        switch (action) {
            case "on":
                if (!client.getPlayer().getEntity().hasStatus(RoomEntityStatus.DEVELOPER)) {
                    client.getPlayer().getEntity().addStatus(RoomEntityStatus.DEVELOPER, "dev");
                } else {
                    client.getPlayer().getEntity().removeStatus(RoomEntityStatus.DEVELOPER);
                }
                break;
            case "off":
                if (client.getPlayer().getEntity().hasStatus(RoomEntityStatus.DEVELOPER)) {
                    client.getPlayer().getEntity().removeStatus(RoomEntityStatus.DEVELOPER);
                } else {
                    client.getPlayer().getEntity().addStatus(RoomEntityStatus.DEVELOPER, "dev");
                }
                break;

            case "info":
                StringBuilder itemInfo = new StringBuilder();

                List<RoomItemFloor> itemsOnTile = tileToFindItem.getItems();
                for (RoomItemFloor itemFloor : itemsOnTile) {

                    ItemDefinition itemDefinition = itemFloor.getDefinition();

                    itemInfo.append(Locale.get("developer.command.itemname_text").replace("%itemname%", itemDefinition.getItemName().toUpperCase()) + "\r");
                    itemInfo.append(Locale.get("developer.command.extradata_modes_text").replace("%extradata%", itemFloor.getExtraData()).replace("%modes%", itemDefinition.getInteractionCycleCount() + "") + "\r");
                    itemInfo.append(Locale.get("developer.command.publicname_interaction_text").replace("%publicname%", itemDefinition.getPublicName()).replace("%interaction%", itemDefinition.getInteraction()) + "\r");
                    itemInfo.append(Locale.get("developer.command.width_lenght_heigh_text").replace("%widht%", itemDefinition.getWidth() + "").replace("%lenght%", itemDefinition.getLength() + "").replace("%height%", itemDefinition.getHeight() + "") + "\r");
                    itemInfo.append(Locale.get("developer.command.coords_text").replace("%x%", itemFloor.getPosition().getX() + "").replace("%y%", itemFloor.getPosition().getY() + "").replace("%z%", itemFloor.getPosition().getZ() + "") + "\r\r");
                }
                client.getPlayer().sendMotd(itemInfo.toString());
                break;

            case "start":
                itemFloor2.setExtraData("state\t0\timageUrl\timage\toffsetX\t0\toffsetY\t0\toffsetZ\t0");
                itemFloor2.saveData();
                break;

            case "set":
                String roomAd = itemFloor2.getExtraData();
                String[] parts = roomAd.split("\t");
                String imageUrl = parts[3];
                String offsetX = parts[5];
                String offsetY = parts[7];
                String offsetZ = parts[9];

                String type = params[1];

                if (itemFloor2 == null) {
                    client.send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.get("command.developer.itemnotfound"), 34));
                    return;
                }

                switch (type) {
                    case "url":
                    case "change":
                        imageUrl = params[2];
                        itemFloor2.setExtraData("state\t0\timageUrl\t" + imageUrl + "\toffsetX\t" + offsetX + "\toffsetY\t" + offsetY + "\toffsetZ\t" + offsetZ);
                        itemFloor2.saveData();
                        break;
                    case "offsetX":
                    case "X":
                    case "x":
                        offsetX = params[2];
                        itemFloor2.setExtraData("state\t0\timageUrl\t" + imageUrl + "\toffsetX\t" + offsetX + "\toffsetY\t" + offsetY + "\toffsetZ\t" + offsetZ);
                        itemFloor2.saveData();
                        break;
                    case "offsetY":
                    case "Y":
                    case "y":
                        offsetY = params[2];
                        itemFloor2.setExtraData("state\t0\timageUrl\t" + imageUrl + "\toffsetX\t" + offsetX + "\toffsetY\t" + offsetY + "\toffsetZ\t" + offsetZ);
                        itemFloor2.saveData();
                        break;
                    case "offsetZ":
                    case "Z":
                    case "z":
                        offsetZ = params[2];
                        itemFloor2.setExtraData("state\t0\timageUrl\t" + imageUrl + "\toffsetX\t" + offsetX + "\toffsetY\t" + offsetY + "\toffsetZ\t" + offsetZ);
                        itemFloor2.saveData();
                        break;
                }

                room.getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(itemFloor2));
                client.getPlayer().sendMotd("state\t0\timageUrl\t" + imageUrl + "\toffsetX\t" + offsetX + "\toffsetY\t" + offsetY + "\toffsetZ\t" + offsetZ);
                break;

        }

    }

    @Override
    public String getPermission() {
        return "roomad_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomad.description");
    }
}
