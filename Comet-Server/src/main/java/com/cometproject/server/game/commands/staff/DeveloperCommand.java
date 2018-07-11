package com.cometproject.server.game.commands.staff;

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

/**
 * Created by Salinas on 02/02/2018.
 */
public class DeveloperCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) return;

        String action = params[0];

        Room room = client.getPlayer().getEntity().getRoom();
        RoomTile tileToFindItem = room.getMapping().getTile(client.getPlayer().getEntity().getPosition().devX(), client.getPlayer().getEntity().getPosition().devY());
        RoomItemFloor itemFloor2 = tileToFindItem.getTopItemInstance();

        switch (action){
            case "about":
                client.send(new NuxAlertMessageComposer("CUSTOM", "habbopages/developer.txt?" + params[1]));
                break;

            case "on":
                if(!client.getPlayer().getEntity().hasStatus(RoomEntityStatus.DEVELOPER)) { client.getPlayer().getEntity().addStatus(RoomEntityStatus.DEVELOPER, "dev"); }
                else { client.getPlayer().getEntity().removeStatus(RoomEntityStatus.DEVELOPER); }
                break;
            case "off":
                if(client.getPlayer().getEntity().hasStatus(RoomEntityStatus.DEVELOPER)) { client.getPlayer().getEntity().removeStatus(RoomEntityStatus.DEVELOPER); }
                else { client.getPlayer().getEntity().addStatus(RoomEntityStatus.DEVELOPER, "dev"); }
                break;

            case "info":
                StringBuilder itemInfo = new StringBuilder();

                List<RoomItemFloor> itemsOnTile = tileToFindItem.getItems();
                for(RoomItemFloor itemFloor : itemsOnTile) {

                    ItemDefinition itemDefinition = itemFloor.getDefinition();

                    itemInfo.append(Locale.get("developer.command.itemname_text").replace("%itemname%", itemDefinition.getItemName().toUpperCase()) +  "\r");
                    itemInfo.append(Locale.get("developer.command.id_spriteid_text").replace("%id%", itemFloor.getId() + "").replace("%base_id%", itemDefinition.getId() + "")+  "\r");
                    if (!itemDefinition.equals("wf_")) {
                        itemInfo.append(Locale.get("developer.command.extradata_modes_text").replace("%extradata%", itemFloor.getExtraData()).replace("%modes%", itemDefinition.getInteractionCycleCount() + "")+  "\r");
                    }
                    itemInfo.append(Locale.get("developer.command.publicname_interaction_text").replace("%publicname%", itemDefinition.getPublicName()).replace("%interaction%", itemDefinition.getInteraction())+  "\r");
                    itemInfo.append(Locale.get("developer.command.width_lenght_heigh_text").replace("%widht%", itemDefinition.getWidth() + "").replace("%lenght%", itemDefinition.getLength() + "").replace("%height%", itemDefinition.getHeight() + "")+  "\r");
                    itemInfo.append(Locale.get("developer.command.coords_text").replace("%x%", itemFloor.getPosition().getX() + "").replace("%y%", itemFloor.getPosition().getY() + "").replace("%z%", itemFloor.getPosition().getZ() + "")+  "\r");
                    itemInfo.append(Locale.get("developer.command.trade_stack_recycle_text").replace("%trade%", itemDefinition.canTrade() + "").replace("%stack%", itemDefinition.canTrade() + "").replace("%recycle%", itemDefinition.canRecycle() + "")+  "\r");
                    itemInfo.append(Locale.get("developer.command.gift_inventorystack_walkable").replace("%gift%", itemDefinition.canGift() + "").replace("%stack%", itemDefinition.canInventoryStack() + "").replace("%walk%", itemDefinition.canWalk() + "")+  "\r");
                    itemInfo.append(Locale.get("developer.command.sit_vendingid_effectid_text").replace("%sit%", itemDefinition.canSit() + "").replace("%vending%", itemDefinition.getVendingIds().toString() + "").replace("%effect%", itemDefinition.getEffectId() + "")+  "\r");
                    itemInfo.append("\r\r____________________________________________________\r\r");
                }
                client.getPlayer().sendMotd(itemInfo.toString());
                break;

            case "set":
                String type = params[1];
                Integer itemId = new Integer(params[2]);
                String column = params[3];
                String value = params[4];

                ItemDao.DefinitionType paramToUpdate = null;

                ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(itemId);
                if(itemDefinition == null) return;

                switch (type){
                    case "baseitem":
                        switch (column){
                            case "interactiontype":
                                paramToUpdate = ItemDao.DefinitionType.INTERACTION;
                                break;
                            case "multiheight":
                                paramToUpdate = ItemDao.DefinitionType.MULTIHEIGHT;
                                break;
                            case "modes":
                                paramToUpdate = ItemDao.DefinitionType.MODES;
                                break;
                            case "height":
                                paramToUpdate = ItemDao.DefinitionType.HEIGHT;
                                break;
                            case "length":
                                paramToUpdate = ItemDao.DefinitionType.LENGHT;
                                break;
                            case "width":
                                paramToUpdate = ItemDao.DefinitionType.WIDTH;
                                break;
                            case "tradable":
                                paramToUpdate = ItemDao.DefinitionType.TRADE;
                                break;
                            case "stackable":
                                paramToUpdate = ItemDao.DefinitionType.STACK;
                                break;
                            case "inventorystack":
                                paramToUpdate = ItemDao.DefinitionType.STACK_ITEMS;
                                break;
                            case "giftable":
                                paramToUpdate = ItemDao.DefinitionType.GIFT;
                                break;
                            case "chair":
                                paramToUpdate = ItemDao.DefinitionType.SIT;
                                break;
                            case "vending":
                                paramToUpdate = ItemDao.DefinitionType.VENDING;
                                break;
                            case "walk":
                                paramToUpdate = ItemDao.DefinitionType.WALK;
                                break;
                            case "effect":
                                paramToUpdate = ItemDao.DefinitionType.EFFECT;
                                break;
                            case "rights":
                                paramToUpdate = ItemDao.DefinitionType.NEED_RIGHTS;
                                break;
                            case "lay":
                                paramToUpdate = ItemDao.DefinitionType.LAY;
                                break;
                        }

                        if(paramToUpdate != null) {

                            client.send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.get("command.developer.success").replace("%item%", itemDefinition.getPublicName()).replace("%var%", paramToUpdate.getDefinitionType()).replace("%var_to%", value), 34));
                            ItemDao.updateDefinition(paramToUpdate, value, itemId);
                            RoomManager.getInstance().getRoomInstances().forEach((id, roomUpdate) -> {
                                roomUpdate.reloadItems();
                                roomUpdate.getItems().getFloorItems().forEach((itemIds, item) -> {
                                    item.getTile().reload();
                                    roomUpdate.getEntities().broadcastMessage(new UpdateStackMapMessageComposer(item.getTile()));
                                    item.sendUpdate();
                                });
                            });
                            ItemManager.getInstance().loadItemDefinitions();
                        }
                        else{
                            client.send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.get("command.developer.unsuccess").replace("%item%", itemDefinition.getPublicName()).replace("%var%", paramToUpdate.getDefinitionType()).replace("%var_to%", value), 34));
                        }
                        break;

                    case "item":

                        if(itemFloor2 == null) {
                            client.send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.get("command.developer.itemnotfound"), 34));
                            return;
                        }

                        switch (column){
                            case "extradata":
                                itemFloor2.setExtraData(value);
                                room.getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(itemFloor2));
                                itemFloor2.saveData();
                                break;
                            case "rot":
                                itemFloor2.setRotation(new Integer(value));
                                room.getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(itemFloor2));
                                break;
                            case "z":
                                Position newPos = new Position(itemFloor2.getPosition().getX(), itemFloor2.getPosition().getY(), new Integer(value));
                                room.getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(itemFloor2.getPosition(), newPos, 0, 0, itemFloor2.getVirtualId()));
                                itemFloor2.setPosition(newPos);
                                break;
                        }
                        break;

                }


                break;
        }




    }

    @Override
    public String getPermission() {
        return "developer_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.developer.description");
    }
}
