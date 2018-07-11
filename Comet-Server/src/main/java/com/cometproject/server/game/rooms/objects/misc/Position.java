package com.cometproject.server.game.rooms.objects.misc;

import com.cometproject.api.game.rooms.util.IPosition;
import com.cometproject.server.game.rooms.objects.RoomFloorObject;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;

import java.util.ArrayList;
import java.util.List;


public class Position implements IPosition {
    public static final int NORTH = 0;
    public static final int NORTH_EAST = 1;
    public static final int EAST = 2;
    public static final int SOUTH_EAST = 3;
    public static final int SOUTH = 4;
    public static final int SOUTH_WEST = 5;
    public static final int WEST = 6;
    public static final int NORTH_WEST = 7;

    public static final int[] COLLIDE_TILES = new int[]{
            NORTH, EAST, SOUTH, WEST
    };

    public static final int[] DIAG_TILES = new int[]{
            NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST
    };


    private int x;
    private int y;
    private double z;

    private int flag = -1;
    private int y_dev;
    private int x_dev;

    public Position(int x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Position old) {
        this.x = old.getX();
        this.y = old.getY();
        this.z = old.getZ();
    }

    public Position() {
        this.x = 0;
        this.y = 0;
        this.z = 0d;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0d;
    }

    public Position add(Position other) {
        return new Position(other.getX() + getX(), other.getY() + getY(), other.getZ() + getZ());
    }

    public Position subtract(Position other) {
        return new Position(other.getX() - getX(), other.getY() - getY(), other.getZ() - getZ());
    }

    public int getDistanceSquared(Position point) {
        int dx = this.getX() - point.getX();
        int dy = this.getY() - point.getY();

        return (dx * dx) + (dy * dy);
    }

    public static String validateWallPosition(String position) {
        try {
            String[] data = position.split(" ");
            if (data[2].equals("l") || data[2].equals("r")) {
                String[] width = data[0].substring(3).split(",");
                int widthX = Integer.parseInt(width[0]);
                int widthY = Integer.parseInt(width[1]);

                String[] length = data[1].substring(2).split(",");
                int lengthX = Integer.parseInt(length[0]);
                int lengthY = Integer.parseInt(length[1]);

                return ":w=" + widthX + "," + widthY + " " + "l=" + lengthX + "," + lengthY + " " + data[2];
            }
        } catch (Exception ignored) {

        }

        return null;
    }

    public static double calculateHeight(RoomItemFloor item) {
        if (item.getDefinition().getInteraction().equals("gate")) {
            return 0;
        } else if (item.getDefinition().canSit()) {
            return 0;
        }

        return item.getDefinition().getHeight();
    }

    public static int calculateRotation(Position from, Position to) {
        return calculateRotation(from.x, from.y, to.x, to.y, false);
    }

    public static int getInvertedRotation(int currentRotation) {
        switch (currentRotation) {
            case NORTH_EAST:
                return NORTH_WEST;
            case NORTH_WEST:
                return SOUTH_WEST;
            case SOUTH_WEST:
                return NORTH_WEST;
            case SOUTH_EAST:
                return NORTH_EAST;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
        }

        return currentRotation;
    }

    public static int calculateRotation(int x, int y, int newX, int newY, boolean reversed) {
        int rotation = 0;

        if (x > newX && y > newY)
            rotation = 7;
        else if (x < newX && y < newY)
            rotation = 3;
        else if (x > newX && y < newY)
            rotation = 5;
        else if (x < newX && y > newY)
            rotation = 1;
        else if (x > newX)
            rotation = 6;
        else if (x < newX)
            rotation = 2;
        else if (y < newY)
            rotation = 4;
        else if (y > newY)
            rotation = 0;

        if (reversed) {
            if (rotation > 3) {
                rotation = rotation - 4;
            } else {
                rotation = rotation + 4;
            }
        }

        return rotation;
    }

    public Position squareInFront(int angle) {
        return calculatePosition(this.x, this.y, angle, false);
    }

    public Position squareInFront(int angle, int distance) {
        return calculatePosition(this.x, this.y, angle, false, distance);
    }

    public Position developerPosition(Position position){
        return position;
    }

    public Position squareBehind(int angle) {
        return calculatePosition(this.x, this.y, angle, true);
    }

    public List<Position> tilesAround() {
        List<Position> tiles = new ArrayList<Position>();

        for(int i=0; i<8; i++) { tiles.add(squareInFront(i)); }

        return tiles;
    }

    public static Position calculatePosition(int x, int y, int angle, boolean isReversed, int distance) {
        switch (angle) {
            case 0:
                if (!isReversed)
                    y -= distance;
                else
                    y += distance;
                break;

            case 1:
                if (!isReversed) {
                    x += distance;
                    y -= distance;
                } else {
                    x -= distance;
                    y += distance;
                }
                break;

            case 2:
                if (!isReversed)
                    x += distance;
                else
                    x -= distance;
                break;

            case 3:
                if (!isReversed) {
                    x += distance;
                    y += distance;
                } else {
                    x -= distance;
                    y -= distance;
                }
                break;

            case 4:
                if (!isReversed)
                    y += distance;
                else
                    y -= distance;
                break;

            case 5:
                if (!isReversed) {
                    x -= distance;
                    y += distance;
                } else {
                    x++;
                    y--;
                }
                break;

            case 6:
                if (!isReversed)
                    x -= distance;
                else
                    x += distance;
                break;

            case 7:
                if (!isReversed) {
                    x -= distance;
                    y -= distance;
                } else {
                    x += distance;
                    y += distance;
                }
                break;
        }

        return new Position(x, y);
    }

    public static Position calculatePosition(int x, int y, int angle, boolean isReversed) {
        switch (angle) {
            case 0:
                if (!isReversed)
                    y--;
                else
                    y++;
                break;

            case 1:
                if (!isReversed) {
                    x++;
                    y--;
                } else {
                    x--;
                    y++;
                }
                break;

            case 2:
                if (!isReversed)
                    x++;
                else
                    x--;
                break;

            case 3:
                if (!isReversed) {
                    x++;
                    y++;
                } else {
                    x--;
                    y--;
                }
                break;

            case 4:
                if (!isReversed)
                    y++;
                else
                    y--;
                break;

            case 5:
                if (!isReversed) {
                    x--;
                    y++;
                } else {
                    x++;
                    y--;
                }
                break;

            case 6:
                if (!isReversed)
                    x--;
                else
                    x++;
                break;

            case 7:
                if (!isReversed) {
                    x--;
                    y--;
                } else {
                    x++;
                    y++;
                }
                break;
        }

        return new Position(x, y);
    }

    public double distanceTo(IPosition pos) {
        return Math.abs(this.getX() - pos.getX()) + Math.abs(this.getY() - pos.getY());
    }

    public double distanceTo(RoomFloorObject roomFloorObject) {
        return distanceTo(roomFloorObject.getPosition());
    }

    public boolean touching(Position pos) {
        if (!(Math.abs(this.getX() - pos.getX()) > 1 || Math.abs(this.getY() - pos.getY()) > 1)) {
            return true;
        }

        if (this.getX() == pos.getX() && this.getY() == pos.getY()) {
            return true;
        }

        return false;
    }

    public boolean touching(RoomFloorObject roomFloorObject) {
        return this.touching(roomFloorObject.getPosition());
    }

    public Position copy() {
        return new Position(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int devX() {
        return this.x_dev;
    }

    public int devY() {
        return this.y_dev;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXDev(int x) {
        this.x_dev = x;
    }

    public void setYDev(int y) {
        this.y_dev = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            return ((Position) o).getX() == this.getX() && ((Position) o).getY() == this.getY();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
