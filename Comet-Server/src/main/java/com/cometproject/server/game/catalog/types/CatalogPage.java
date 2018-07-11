package com.cometproject.server.game.catalog.types;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.utilities.JsonFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CatalogPage {
    private static final Type listType = new TypeToken<List<String>>() {}.getType();

    private int id;
    private CatalogPageType type;
    private String caption;
    private int icon;
    private int minRank;
    private boolean vipOnly;
    private String template;
    private int parentId;
    private String linkName;

    private boolean enabled;

    private List<String> images;
    private List<String> texts;

    private Map<Integer, CatalogItem> items;
    private String extraData;

    public CatalogPage(ResultSet data, Map<Integer, CatalogItem> items) throws SQLException {

        this.id = data.getInt("id");
        this.caption = data.getString("caption");
        this.icon = data.getInt("icon_image");
        this.minRank = data.getInt("min_rank");
        this.vipOnly = data.getBoolean("vip_only");
        this.template = data.getString("page_layout");
        this.parentId = data.getInt("parent_id");
        this.linkName = data.getString("link");
        this.type = CatalogPageType.valueOf(data.getString("type"));
        this.extraData = data.getString("extra_data");

        this.images = new ArrayList<>();
        this.images.add(data.getString("page_headline"));
        this.images.add(data.getString("page_teaser"));
        this.images.add(data.getString("page_special"));

        this.texts = new ArrayList<>();
        this.texts.add(data.getString("page_text_1"));
        this.texts.add(data.getString("page_text_2"));
        this.texts.add(data.getString("page_text_teaser"));

        this.enabled = data.getString("enabled").equals("1");

        if(this.type == CatalogPageType.BUNDLE) {
            String bundleAlias = this.extraData;

            RoomBundle roomBundle = RoomBundleManager.getInstance().getBundle(bundleAlias);

            if(roomBundle != null) {
                List<CatalogBundledItem> bundledItems = new ArrayList<>();
                Map<Integer, List<RoomBundleItem>> bundleItems = new HashMap<>();

                for (RoomBundleItem bundleItem : roomBundle.getRoomBundleData()) {
                    if (bundleItems.containsKey(bundleItem.getItemId())) {
                        bundleItems.get(bundleItem.getItemId()).add(bundleItem);
                    } else {
                        bundleItems.put(bundleItem.getItemId(), Lists.newArrayList(bundleItem));
                    }
                }

                for(Map.Entry<Integer, List<RoomBundleItem>> bundledItem : bundleItems.entrySet()) {
                    bundledItems.add(new CatalogBundledItem("0", bundledItem.getValue().size(), bundledItem.getKey()));
                }

                final CatalogItem catalogItem = new CatalogItem(roomBundle.getId(), "-1", bundledItems, "single_bundle",
                        roomBundle.getCostCredits(), roomBundle.getCostSeasonal(), roomBundle.getCostVip(), 0,1, false, 0, 0, false, "", "", this.id);

                this.items = new HashMap<>();
                this.items.put(catalogItem.getId(), catalogItem);
            } else {
                this.items = new HashMap<>();
            }
        } else {
            this.items = items;
        }
}

    public int getOfferSize() {
        int size = 0;

        for (CatalogItem item : this.items.values()) {
            if(item.getItemId().equals("-1")) continue;

            if (ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId()) != null) {
                if (ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId()).getOfferId() != -1 && ItemManager.getInstance().getDefinition(item.getItems().get(0).getItemId()).getOfferId() != 0) {
                    size++;
                }
            }
        }

        return size;
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public int getIcon() {
        return icon;
    }

    public int getMinRank() {
        return minRank;
    }

    public boolean isVipOnly() { return vipOnly; }

    public String getTemplate() {
        return template;
    }

    public int getParentId() {
        return parentId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Map<Integer, CatalogItem> getItems() {
        return items;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getTexts() {
        return texts;
    }

    public String getLinkName() {
        return linkName;
    }

    public String getExtraData() {
        return extraData;
    }

    public CatalogPageType getType() {
        return type;
    }
}
