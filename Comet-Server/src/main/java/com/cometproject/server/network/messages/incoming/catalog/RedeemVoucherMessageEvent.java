package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.types.CatalogVoucher;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.EnforceRoomCategoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.catalog.VoucherDao;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RedeemVoucherMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String voucherCode = msg.readString();

        if (client.getPlayer().getVoucherRedeemAttempts() >= 20 &&
                (client.getPlayer().getLastVoucherRedeemAttempt() + 120) > (System.currentTimeMillis() / 1000)) {
            return;
        }

        client.getPlayer().setVoucherRedeemAttempts(client.getPlayer().getVoucherRedeemAttempts() + 1);
        client.getPlayer().setLastVoucherRedeemAttempt((int) System.currentTimeMillis() / 1000);

        final CatalogVoucher voucher = VoucherDao.findVoucherByCode(voucherCode);

        if (voucher == null) {
            client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("voucher.error.doesnt_exist", "The voucher you entered doesn't exist!")));
            return;
        }

        if (voucher.getStatus() == CatalogVoucher.VoucherStatus.CLAIMED) {
            client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("voucher.error.claimed", "The voucher you entered has already been claimed!")));
            return;
        }

        List<Integer> playersId = new ArrayList<Integer>();
        if(!voucher.getClaimedBy().isEmpty()) {
            if(!voucher.getClaimedBy().contains(",")) {
                playersId.add(new Integer(voucher.getClaimedBy()));
            } else {
                for (String playerId : voucher.getClaimedBy().split(",")) {
                    playersId.add(new Integer(playerId));
                }
            }

            if(playersId.contains(client.getPlayer().getId())) {
                client.send(new SimpleAlertMessageComposer(Locale.get("voucher.error.claimed_2")));
                return;
            }
        }

        playersId.add(client.getPlayer().getId());

        boolean failure = false;

        switch (voucher.getType()) {
            case CREDITS: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int creditsAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseCredits(creditsAmount);
                client.send(new SimpleAlertMessageComposer(Locale.get("voucher.credits.received").replace("%amount%", String.valueOf(creditsAmount))));
                break;
            }

            case DUCKETS: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int ducketsAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseActivityPoints(ducketsAmount);
                client.send(new SimpleAlertMessageComposer(Locale.get("voucher.duckets.received").replace("%amount%", String.valueOf(ducketsAmount))));
                break;
            }

            case DIAMONDS: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int diamondsAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increasePoints(diamondsAmount);
                client.send(new SimpleAlertMessageComposer( Locale.get("voucher.diamonds.received").replace("%amount%", String.valueOf(diamondsAmount))));
                break;
            }

            case SEASONAL: {
                if (!StringUtils.isNumeric(voucher.getData())) {
                    failure = true;
                    break;
                }

                final int seasonalAmount = Integer.parseInt(voucher.getData());

                client.getPlayer().getData().increaseSeasonalPoints(seasonalAmount);
                client.send(new SimpleAlertMessageComposer(Locale.get("voucher.seasonal.received").replace("%amount%", String.valueOf(seasonalAmount))));
                break;
            }

            case ROOM_BUNDLE: {
                RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(voucher.getData());

                try {
                    int roomId = RoomManager.getInstance().createRoom(roomBundle.getConfig().getRoomName().replace("%username%", client.getPlayer().getData().getUsername()), "", roomBundle.getRoomModelData(), 0, 20, 0, client, roomBundle.getConfig().getThicknessWall(), roomBundle.getConfig().getThicknessFloor(), roomBundle.getConfig().getDecorations(), roomBundle.getConfig().isHideWalls());

                    for (RoomBundleItem roomBundleItem : roomBundle.getRoomBundleData()) {
                        long newItemId = ItemDao.createItem(client.getPlayer().getId(), roomBundleItem.getItemId(), roomBundleItem.getExtraData());

                        if (roomBundleItem.getWallPosition() == null) {
                            RoomItemDao.placeFloorItem(roomId, roomBundleItem.getX(), roomBundleItem.getY(), roomBundleItem.getZ(), roomBundleItem.getRotation(), roomBundleItem.getExtraData(), newItemId);
                        } else {

                            RoomItemDao.placeWallItem(roomId, roomBundleItem.getWallPosition(), roomBundleItem.getExtraData(), newItemId);
                        }
                    }

                    client.send(new RoomForwardMessageComposer(roomId));
                    client.send(new EnforceRoomCategoryMessageComposer());
                    client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                    client.getPlayer().setLastRoomCreated((int) Comet.getTime());

                } catch (Exception e) {
                    client.send(new MotdNotificationMessageComposer("Invalid room bundle data, please contact an administrator."));
                    client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
                }
                break;
            }

            case BADGE: {
                client.getPlayer().getInventory().addBadge(voucher.getData(), true);
                client.send(new SimpleAlertMessageComposer(Locale.get("voucher.badge.received")));
                break;
            }

            case ITEM: {
                Long itemId = ItemDao.createItem(client.getPlayer().getId(), new Integer(voucher.getData()), "");
                PlayerItem playerReward = client.getPlayer().getInventory().add(itemId, new Integer(voucher.getData()), "", null, null);
                ItemDefinition itemDefinition = ItemManager.getInstance().getDefinition(new Integer(voucher.getData()));
                client.send(new SimpleAlertMessageComposer(Locale.get("voucher.item.received").replace("%item%", itemDefinition.getPublicName())));
                client.sendQueue(new UpdateInventoryMessageComposer());
                client.sendQueue(new UnseenItemsMessageComposer(Sets.newHashSet(playerReward)));
                break;
            }
        }

        if(failure) {
            client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("voucher.error", "The voucher was redeemed unsuccessfully")));
        } else {

            client.getPlayer().getData().save();
            client.getPlayer().sendBalance();
        }

        VoucherDao.claimVoucher(voucher.getId(), client.getPlayer().getId(), voucher.getLimitUse() - 1 == 0);
        client.getPlayer().setVoucherRedeemAttempts(0);
    }
}
