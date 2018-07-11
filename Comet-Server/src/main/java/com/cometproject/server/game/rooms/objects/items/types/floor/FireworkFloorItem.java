package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.google.common.collect.Lists;

import java.util.List;

public class FireworkFloorItem extends RoomItemFloor {

    private List<PlayerEntity> entitiesToKick = Lists.newArrayList();

    public FireworkFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (isWiredTrigger)
        return false;

        double distance = entity.getPosition().distanceTo(this.getPosition());

        if (distance > 2) {
            entity.moveTo(this.getPosition().squareInFront(this.getRotation()));
            return false;
        }

        PlayerEntity pEntity = (PlayerEntity) entity;

        int extradata = Integer.parseInt(this.getExtraData());
        extradata++;

        if(extradata == 4)
        {
            pEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_75, 1);
        }
        else if (extradata > 4)
        {
            extradata = 0;
        }

        this.setExtraData("" + extradata);
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(1.5));
        return true;
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
        this.sendUpdate();
    }
}
