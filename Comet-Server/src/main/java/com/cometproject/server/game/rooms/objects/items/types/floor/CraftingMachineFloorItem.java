package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crafting.CraftingMachine;
import com.cometproject.server.game.items.crafting.CraftingRecipe;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.crafting.CraftableProductsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.storage.queries.items.CrackableDao;

import java.text.DecimalFormat;

/**
 * Created by Salinas on 16/09/2017.
 */
public class CraftingMachineFloorItem extends RoomItemFloor {
    private double magicHeight = 0d;

    public CraftingMachineFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        PlayerEntity pEntity = (PlayerEntity) entity;
        CraftingMachine machine = ItemManager.getInstance().getCraftingMachine(this.getDefinition().getId());
        pEntity.getPlayer().setLastCraftingMachine(machine);
        pEntity.getPlayer().getSession().send(new CraftableProductsMessageComposer(machine));
        return true;
    }
}
