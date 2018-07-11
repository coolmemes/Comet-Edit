package com.cometproject.server.game.nuxs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Salinas on 15/09/2017.
 */
public class NuxGift {
    public enum RewardType {
        ITEM,
        DIAMONDS,
        SEASONAL,
        BADGE,
        REWARD1,
        REWARD2,
        REWARD3
    }

    private int id;
    private int pageType;
    private RewardType type;
    private String name;
    private String icon;
    private String productdata;
    private List<String> data = new ArrayList<String>();

    public NuxGift(int id, String type, int pageType, String icon, String name, String productdata, String data){
        this.id = id;
        if(type.equals("item")) {
            this.type = RewardType.ITEM;
        } else if(type.equals("diamonds")) {
            this.type = RewardType.DIAMONDS;
        } else if(type.equals("seasonal")) {
            this.type = RewardType.SEASONAL;
        } else if(type.equals("reward1")) {
            this.type = RewardType.REWARD1;
        } else if(type.equals("reward2")) {
            this.type = RewardType.REWARD2;
        } else if(type.equals("reward3")) {
            this.type = RewardType.REWARD3;
        } else {
            this.type = RewardType.BADGE;
        }
        this.pageType = pageType;
        this.icon = icon;
        this.name = name;
        this.productdata = productdata;
        Collections.addAll(this.data, data.split(","));
    }

    public int getId() { return this.id; }
    public int getPageType() { return this.pageType; }
    public RewardType getType() { return this.type; }
    public String getName() { return this.name; }
    public String getIcon() { return this.icon; }
    public String getProductdata() { return this.productdata; }
    public String getRandomData() {
        int max = this.data.size() - 1;
        int min = 0;

        return this.data.get( (int)Math.floor(Math.random() * (max - min)) + min );
    }
}
