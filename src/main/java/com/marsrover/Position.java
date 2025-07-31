package com.marsrover;

import java.util.Objects;

/**
 * Represents an immutable position on the Mars surface with x and y coordinates.
 * <p>
 * Provides functionality for movement calculations, boundary checking, and
 * optimized equality/hashing operations for use in collections.
 * </p>
 * 
 * @author gracewee
 * @version 1.0
 * @since 1.0
 */
public class Position {
    private final int x;
    private final int y;
    private final int hashCode; // Pre-computed hash for performance

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y); //pre-compute hash code
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return hashCode; // Return pre-computed hash
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }

    public Position move(Direction d) {
        return new Position(x + d.getDeltaX(), y + d.getDeltaY());
    }

    public boolean isWithinBorders(int minX, int minY, int maxX, int maxY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
