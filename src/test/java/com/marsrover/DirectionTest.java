package com.marsrover;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Direction Enum Tests")
class DirectionTest {

    @ParameterizedTest
    @ValueSource(strings = {"N", "S", "E", "W", "n", "s", "e", "w"})
    void shouldHaveCorrectSymbol(String symbol) {
        Direction direction = Direction.fromString(symbol);
        assertEquals(symbol.toUpperCase(), direction.getSymbolAsString());
    }

    @Test
    void shouldTurnLeftCorrectly() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
    }

    @Test
    void shouldTurnRightCorrectly() {
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }

    @Test
    void shouldCompleteFullRotationWithLeftTurns() {
        Direction current = Direction.NORTH;
        current = current.turnLeft(); // WEST
        current = current.turnLeft(); // SOUTH
        current = current.turnLeft(); // EAST
        current = current.turnLeft(); // NORTH
        assertEquals(Direction.NORTH, current);
    }

    @Test
    void shouldCompleteFullRotationWithRightTurns() {
        Direction current = Direction.NORTH;
        current = current.turnRight(); // EAST
        current = current.turnRight(); // SOUTH
        current = current.turnRight(); // WEST
        current = current.turnRight(); // NORTH
        assertEquals(Direction.NORTH, current);
    }

    @Test
    void shouldThrowExceptionForInvalidDirection() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Direction.fromString("X"));

        assertTrue(exception.getMessage().contains("Invalid direction"));
    }

}
