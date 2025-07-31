package com.marsrover;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages the position and orientation of a Mars rover on the surface.
 * <p>
 * Handles rover movement, rotation, boundary constraints, and collision
 * detection with blocked positions. Processes movement commands while
 * respecting defined borders and obstacles.
 * </p>
 * 
 * @author gracewee
 * @version 1.0
 * @since 1.0
 */
public class Coordinates {

    private Position position;
    private Direction direction;

    private Position borderMin = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private Position borderMax = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private Set<Position> blockers = new HashSet<>();

    public Coordinates(int x, int y, String dir) {
        this.position = new Position(x, y);
        this.direction = Direction.fromString(dir);
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setBorder(int minX, int minY, int maxX, int maxY) {
        this.borderMin = new Position(minX, minY);
        this.borderMax = new Position(maxX, maxY);
    }

    public Set<Position> getBlockers() {
        return blockers;
    }

    public void setBlockers(Set<Position> blockers) {
        this.blockers = blockers;
    }

    public void calculateNextMove(Command command) {
        calculateNextMove(command.getSymbolAsString());
    }

    public void calculateNextMove(String move) {
        char mv = move.toUpperCase().charAt(0);
        if (mv == Command.MOVE.getSymbol()) {
            Position moved = this.position.move(this.direction);
            if (moved.isWithinBorders(borderMin.getX(), borderMin.getY(), borderMax.getX(), borderMax.getY())) {
                if (!blockers.contains(moved)) {
                    this.position = moved;
                }
            }
        } else if (mv == Command.LEFT.getSymbol()) {
            this.direction = this.direction.turnLeft();
        } else if (mv == Command.RIGHT.getSymbol()) {
            this.direction = this.direction.turnRight();
        } else {
            throw new IllegalArgumentException("Error: Invalid command " + move + ". Use only L, R, and M.");
        }
    }

    @Override
    public String toString() {
        return this.position.getX() + " " + this.position.getY() + " " + this.direction.getSymbolAsString();
    }
}
