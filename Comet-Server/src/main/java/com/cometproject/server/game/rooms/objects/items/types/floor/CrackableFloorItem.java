package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.crackable.CrackableItem;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.FloorItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.CrackableDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.storage.queue.types.ItemStorageQueue;
import com.google.common.collect.Sets;

/**
 * Created by Salinass on 15/10/2017.
 */

public class CrackableFloorItem extends DefaultFloorItem {
    private CrackableItem crackable;
    private boolean allowInteract = false;
    private PlayerEntity campaignEntity;

    public CrackableFloorItem(long id, int itemId, Room room, int owner, int x, int y, int z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.crackable = ItemManager.getInstance().getCrackableRewards(this.getDefinition().getId());
        if(Integer.valueOf(this.getExtraData()) >= this.crackable.getMaxTouch()) {
            this.updateExtraData((this.crackable.getMaxTouch() - 1) + "");
        }
    }

    public void updateExtraData(String newExtraData) {
        this.setExtraData(newExtraData);
        this.saveData();
        this.sendUpdate();
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {

        PlayerEntity pEntity = (PlayerEntity) entity;
        PlayerEffect actualEffect = pEntity.getCurrentEffect() == null ? new PlayerEffect(0, 0) : pEntity.getCurrentEffect();

        if(this.crackable.getCrackableType().equals(CrackableItem.CrackableType.PINATA) && !this.allowInteract ||
                this.crackable.getCrackableType().equals(CrackableItem.CrackableType.CAMPAIGN) && !this.getDefinition().canWalk() ||
                this.hasTicks() ||
                this.crackable.getCrackableType().equals(CrackableItem.CrackableType.INTERACTION) && this.getOwner() != entity.getId() ||
                this.crackable.getEffectNeeded() != actualEffect.getEffectId()) { return false; }

        int extradata = Integer.valueOf(this.getExtraData()) + 1;

        this.updateExtraData(extradata + "");

        if(extradata >= this.crackable.getMaxTouch()) {
            if(this.crackable.getCrackableType().equals(CrackableItem.CrackableType.CAMPAIGN)) { this.campaignEntity = (PlayerEntity) entity; }
            this.updateExtraData(extradata + "");
            this.setTicks(RoomItemFactory.getProcessTime(1.5));
            return true;
        }

        if(crackable.getTickAchievement() != null) { pEntity.getPlayer().getAchievements().progressAchievement(crackable.getTickAchievement(), 1); }
        this.allowInteract = false;

        return true;
    }

    @Override
    public void onTickComplete() {

        Session itemOwner = NetworkManager.getInstance().getSessions().getByPlayerId(this.getOwner());

        Session client = this.crackable.getCrackableType().equals(CrackableItem.CrackableType.CAMPAIGN) && this.campaignEntity != null && itemOwner.getPlayer().getData().getRank() > 7 ? this.campaignEntity.getPlayer().getSession() : itemOwner;
        Room room = this.getRoom();

        if(this.crackable != null && client != null && room != null && new Integer(this.getExtraData()) >= this.crackable.getMaxTouch()) {
            int rewardBaseId = this.crackable.getRandomReward();

            room.getItems().removeItem(this, client, false, false);
            final ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(rewardBaseId);
            this.getRoom().getItems().placeFloorItem(new InventoryItem(this.getId(), rewardBaseId, ""), this.getPosition().getX(), this.getPosition().getY(), this.getRotation(), client.getPlayer());
            RoomItemDao.setBaseItem(this.getId(), itemDefinition.getId());
            CrackableDao.addCrackableReward((int) this.getId(), rewardBaseId, client.getPlayer().getData().getId());

            if(crackable.getCrackedAchievement() != null) { client.getPlayer().getAchievements().progressAchievement(crackable.getCrackedAchievement(), 1); }
        } else {
            int extradata = Integer.valueOf(this.getExtraData()) - 1;
            this.updateExtraData(extradata + "");

            return;
        }
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(entity == null || !(entity instanceof PlayerEntity)) { return; }

        PlayerEntity playerEntity = (PlayerEntity) entity;

        if(this.crackable.getCrackableType().equals(CrackableItem.CrackableType.CAMPAIGN) && !this.getDefinition().canWalk() ||
                this.crackable.getCrackableType().equals(CrackableItem.CrackableType.INTERACTION) ) { return; }

        this.allowInteract = true;
        this.onInteract(entity, 0, false);
    }
}
