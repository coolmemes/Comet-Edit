package com.cometproject.server.game.rooms.types;

/**
 * Created by Salinas on 12/02/2018.
 */
public enum RoomPhotoType {
    PHOTO("photo"),
    THUMB("thumb"),
    PREVIEW("preview");

    private String typeName;

    RoomPhotoType(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return typeName;
    }
}
