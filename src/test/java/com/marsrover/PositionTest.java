package com.marsrover;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Position Tests")
class PositionTest {

    @Test
    void shouldMoveNorthCorrectly() {
        Position original = new Position(0, 0);
        Position moved = original.move(Direction.NORTH); // ✅ Capture returned position

        // Original position unchanged (immutable)
        assertEquals(0, original.getX());
        assertEquals(0, original.getY());

        // New position has moved north
        assertEquals(0, moved.getX());
        assertEquals(1, moved.getY());
    }

    @Test
    void shouldMoveSouthCorrectly() {
        Position original = new Position(0, 0);
        Position moved = original.move(Direction.SOUTH);

        // Original unchanged
        assertEquals(0, original.getX());
        assertEquals(0, original.getY());

        // New position moved south
        assertEquals(0, moved.getX());
        assertEquals(-1, moved.getY());
    }

    @Test
    void shouldMoveEastCorrectly() {
        Position original = new Position(0, 0);
        Position moved = original.move(Direction.EAST);

        // Original unchanged
        assertEquals(0, original.getX());
        assertEquals(0, original.getY());

        // New position moved east
        assertEquals(1, moved.getX());
        assertEquals(0, moved.getY());
    }

    @Test
    void shouldMoveWestCorrectly() {
        Position original = new Position(4, 3);
        Position moved = original.move(Direction.WEST);

        // Original unchanged
        assertEquals(4, original.getX());
        assertEquals(3, original.getY());

        // New position moved west
        assertEquals(3, moved.getX());
        assertEquals(3, moved.getY());
    }

    @Test
    void shouldHandleMultipleMoves() {
        Position start = new Position(5, 5);

        Position north = start.move(Direction.NORTH);
        Position east = north.move(Direction.EAST);
        Position south = east.move(Direction.SOUTH);
        Position west = south.move(Direction.WEST);

        // Should be back to original position
        assertEquals(start.getX(), west.getX());
        assertEquals(start.getY(), west.getY());

        // But they should be different objects
        assertNotSame(start, west);
    }

    @Test
    void shouldBeEqualWhenCoordinatesAreSame() {
        Position pos1 = new Position(5, 10);
        Position pos2 = new Position(5, 10);

        assertEquals(pos1, pos2);
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenCoordinatesDiffer() {
        Position pos1 = new Position(5, 10);
        Position pos2 = new Position(5, 11);
        Position pos3 = new Position(6, 10);

        assertNotEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
        assertNotEquals(pos2, pos3);
    }

    @Test
    void shouldHaveConsistentHashCode() {
        Position position = new Position(5, 10);
        int hash1 = position.hashCode();
        int hash2 = position.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldCorrectlyCheckIfWithinBorders() {
        Position position = new Position(5, 5);

        assertTrue(position.isWithinBorders(0, 0, 10, 10));
        assertTrue(position.isWithinBorders(5, 5, 5, 5)); // Exact boundaries
        assertFalse(position.isWithinBorders(0, 0, 4, 10)); // X too high
        assertFalse(position.isWithinBorders(0, 0, 10, 4)); // Y too high
        assertFalse(position.isWithinBorders(6, 0, 10, 10)); // X too low
        assertFalse(position.isWithinBorders(0, 6, 10, 10)); // Y too low
    }

    @Test
    void shouldHandleNegativeBorders() {
        Position position = new Position(-3, -7);

        assertTrue(position.isWithinBorders(-10, -10, 0, 0));
        assertFalse(position.isWithinBorders(0, 0, 10, 10));
    }

    @Test
    void shouldBeImmutableOriginalPositionUnchangedAfterMove() {
        Position original = new Position(10, 20);
        int originalX = original.getX();
        int originalY = original.getY();
        int originalHash = original.hashCode();

        // Perform multiple moves
        original.move(Direction.NORTH);
        original.move(Direction.EAST);
        original.move(Direction.SOUTH);
        original.move(Direction.WEST);

        // Original should be completely unchanged
        assertEquals(originalX, original.getX());
        assertEquals(originalY, original.getY());
        assertEquals(originalHash, original.hashCode());
    }

    @Test
    void shouldCreateNewInstancesOnMove() {
        Position original = new Position(5, 5);
        Position moved = original.move(Direction.NORTH);

        assertNotSame(original, moved);
    }
}
