package com.marsrover;

public enum Direction {
    NORTH(0, 1, 'N'),
    EAST(1, 0, 'E'),
    SOUTH(0, -1, 'S'),
    WEST(-1, 0, 'W');

    private final int deltaX;
    private final int deltaY;
    private final char symbol;

    private static final Direction[] VALUES = values(); // Cache for rotation
    private static final int SIZE = VALUES.length;

    Direction(int deltaX, int deltaY, char symbol) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.symbol = symbol;
    }

    // Getters
    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getSymbolAsString() {
        return String.valueOf(symbol);
    }

    // Optimized rotation methods using ordinal arithmetic
    public Direction turnLeft() {
        return VALUES[(ordinal() + SIZE - 1) % SIZE];
    }

    public Direction turnRight() {
        return VALUES[(ordinal() + 1) % SIZE];
    }

    // Static factory methods
    public static Direction fromChar(char c) {
        char upper = Character.toUpperCase(c);
        for (Direction dir : VALUES) {
            if (dir.symbol == upper) {
                return dir;
            }
        }
        throw new IllegalArgumentException("Invalid direction: " + c);
    }

    public static Direction fromString(String s) {
        if (s == null || s.length() != 1) {
            throw new IllegalArgumentException("Invalid direction string: " + s);
        }
        return fromChar(s.charAt(0));
    }
}
