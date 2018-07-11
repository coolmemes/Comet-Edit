package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.events.types.RollerFloorItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.AdvancedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.Direction;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class RollerFloorItem extends AdvancedFloorItem<RollerFloorItemEvent> {
    private boolean hasRollScheduled = false;
    private long lastTick = 0;

    private boolean cycleCancelled = false;

    private Set<Integer> entitiesOnRoller = new ConcurrentHashSet<>();
    private Set<Integer> movedEntities = new ConcurrentHashSet<>();

    private final RollerFloorItemEvent event;

    public RollerFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.event = new RollerFloorItemEvent(0);
    }

    @Override
    public void onLoad() {
        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    @Override
    public void onPlaced() {
        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.entitiesOnRoller.add(entity.getId());
        event.setTotalTicks(this.getTickCount());
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (!this.entitiesOnRoller.contains(entity.getId())) {
            return;
        }

        this.entitiesOnRoller.remove(entity.getId());
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        event.setTotalTicks(this.getTickCount());
    }

    @Override
    public void onEventComplete(RollerFloorItemEvent event) {
        if(this.cycleCancelled) {
            this.cycleCancelled = false;
        }

        this.movedEntities.clear();

        this.handleEntities();

        if(!cycleCancelled) {
            this.handleItems();
        }

        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    private void handleEntities() {
        Position sqInfront = this.getPosition().squareInFront(this.getRotation());
        List<RoomItemFloor> itemsSq = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        boolean retry = false;
        final Direction direction = Direction.get(this.getRotation());

        List<RoomEntity> entities = this.getRoom().getEntities().getEntitiesAt(this.getPosition());

        for (RoomEntity entity : entities) {
            if (entity.getPosition().getX() != this.getPosition().getX() && entity.getPosition().getY() != this.getPosition().getY()) {
                continue;
            }

            if (entity.getPositionToSet() != null) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getId(), entity.getPosition(), sqInfront, true, false, false, true, false) || this.getRoom().getEntities().positionHasEntity(sqInfront)) {
                retry = true;
                break;
            }

            if (entity.isWalking()) {
                continue;
            }

            if (sqInfront.getX() == this.getRoom().getModel().getDoorX() && sqInfront.getY() == this.getRoom().getModel().getDoorY()) {
                entity.leaveRoom(false, false, true);
                continue;
            }

            boolean hasRoller = false;
            boolean rollerIsFacing = false;
            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if (nextItem instanceof RollerFloorItem) {
                    hasRoller = true;
                }
            }

            if (hasRoller) {
                if (itemsSq.size() > 1) {
                    continue;
                }
            }

            WiredTriggerWalksOffFurni.executeTriggers(entity, this);


            int itemsAtTile = 0;

            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if (nextItem instanceof GroupGateFloorItem) break;

                itemsAtTile++;

                if (nextItem instanceof RollerFloorItem) {
                    hasRoller = true;

                    final Direction rollerDirection = Direction.get(nextItem.getRotation());
                    final Direction rollerInverted = rollerDirection.invert();

                    if (rollerInverted == direction) {
                        rollerIsFacing = true;
                    }
                }

                WiredTriggerWalksOnFurni.executeTriggers(entity, nextItem);

                nextItem.onEntityStepOn(entity);
            }

            if (hasRoller && rollerIsFacing) {
                if (itemsAtTile > 1) {
                    retry = true;
                    break;
                }
            }

            final double toHeight = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY()).getWalkHeight();

            final RoomTile oldTile = this.getRoom().getMapping().getTile(entity.getPosition().getX(), entity.getPosition().getY());
            final RoomTile newTile = this.getRoom().getMapping().getTile(sqInfront.getX(), sqInfront.getY());

            if (oldTile != null) {
                oldTile.getEntities().remove(entity);
            }

            if (newTile != null) {
                newTile.getEntities().add(entity);
            }

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(entity.getPosition(), new Position(sqInfront.getX(), sqInfront.getY(), toHeight), this.getVirtualId(), entity.getId(), 0));
            entity.setPosition(new Position(sqInfront.getX(), sqInfront.getY(), toHeight));

            this.onEntityStepOff(entity);
            movedEntities.add(entity.getId());
        }

        if (retry) {
            this.cycleCancelled = true;
        }
    }

    private void handleItems() {
        List<RoomItemFloor> floorItems = this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY());

        if (floorItems.size() < 2) {
            return;
        }

        int rollerCount = 0;
        for (RoomItemFloor f : floorItems) {
            if (f instanceof RollerFloorItem) {
                rollerCount++;
            }
        }

        if (rollerCount > 1) {
            return;
        }

        final Position sqInfront = this.getPosition().squareInFront(this.getRotation());
        final RoomItemFloor topItemfront = this.getRoom().getItems().getFloorItem(this.getRoom().getMapping().getTile(sqInfront).getTopItem());
        List<RoomItemFloor> itemsSq = this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY());

        List<RoomItemFloor> sortedList = floorItems.stream()
                .sorted(Comparator.comparing(RoomItemFloor::getHeight))
                .collect(Collectors.toList());

        sortedList.remove(this);

        boolean noItemsOnNext = false;
        double zOffset = this.getRoom().getMapping().getTile(sqInfront).getStackHeight();
        for (RoomItemFloor floor : sortedList) {
            if (floor.getPosition().getX() != this.getPosition().getX() && floor.getPosition().getY() != this.getPosition().getY()) {
                continue;
            }

            if (floor instanceof RollerFloorItem || floor.getPosition().getZ() <= this.getPosition().getZ()) {
                continue;
            }

            if (!floor.getDefinition().canStack() && !(floor instanceof RollableFloorItem)) {
                if (floor.getTile().getTopItem() != floor.getId())
                    continue;
            }

            double height = floor.getPosition().getZ();

            boolean hasRoller = false;

            for (RoomItemFloor iq : itemsSq) {
                if (iq instanceof RollerFloorItem) {
                    hasRoller = true;
                }
            }

            if (!hasRoller || noItemsOnNext) {
                noItemsOnNext = true;
            }

            if(topItemfront != null){
                if(!topItemfront.getDefinition().canStack()) {
                    return;
                }
            }

            if (!this.getRoom().getMapping().isValidStep(null, new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), sqInfront, true, false, false, true, true) || this.getRoom().getEntities().positionHasEntity(sqInfront, this.movedEntities)) {
                return;
            }

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(new Position(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ()), new Position(sqInfront.getX(), sqInfront.getY(), zOffset), this.getVirtualId(), 0, floor.getVirtualId()));

            floor.getPosition().setX(sqInfront.getX());
            floor.getPosition().setY(sqInfront.getY());
            floor.getPosition().setZ(zOffset);
            zOffset = zOffset + floor.getDefinition().getHeight();

            RoomItemDao.saveItemPosition(floor.getPosition().getX(), floor.getPosition().getY(), floor.getPosition().getZ(), floor.getRotation(), floor.getId());
        }

        this.getRoom().getMapping().updateTile(this.getPosition().getX(), this.getPosition().getY());
        this.getRoom().getMapping().updateTile(sqInfront.getX(), sqInfront.getY());

        for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
            for (RoomItemFloor floor : floorItems) {
                nextItem.onItemAddedToStack(floor);
            }
        }
    }

    private int getTickCount() {
        return RoomItemFactory.getProcessTime((this.getRoom().hasAttribute("customRollerSpeed") ? (int) this.getRoom().getAttribute("customRollerSpeed") : 2));
    }
}