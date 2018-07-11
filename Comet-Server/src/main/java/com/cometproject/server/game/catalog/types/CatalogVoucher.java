package com.cometproject.server.game.catalog.types;

public class CatalogVoucher {

    public enum VoucherType {
        CREDITS,
        DUCKETS,
        DIAMONDS,
        SEASONAL,
        ROOM_BUNDLE,
        BADGE,
        ITEM
    }
    public enum VoucherStatus {
        UNCLAIMED,
        CLAIMED
    }

    private final int id;
    private final VoucherType type;
    private final String data;
    private final int createdBy;
    private final int createdAt;
    private final String claimedBy;
    private final int claimedAt;
    private final int limitUse;
    private final VoucherStatus status;
    private final String code;

    public CatalogVoucher(int id, VoucherType type, String data, int createdBy, int createdAt, String claimedBy, int claimedAt, int limitUse, VoucherStatus status, String code) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.claimedBy = claimedBy;
        this.claimedAt = claimedAt;
        this.limitUse = limitUse;
        this.status = status;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public VoucherType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public String getClaimedBy() {
        return claimedBy;
    }

    public int getClaimedAt() {
        return claimedAt;
    }

    public int getLimitUse() { return limitUse; }

    public VoucherStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}