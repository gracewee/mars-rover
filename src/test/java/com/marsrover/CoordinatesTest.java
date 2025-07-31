package com.marsrover;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coordinates Tests")
class CoordinatesTest {

    private Coordinates coordinates;

    @BeforeEach
    void setUp() {
        coordinates = new Coordinates(0, 0, Direction.NORTH.getSymbolAsString());
    }

    @Test
    void shouldHaveEmptyBlockersListByDefault() {
        assertTrue(coordinates.getBlockers().isEmpty());
    }

    @Test
    void shouldSetAndGetBlockers() {
        Set<Position> blockers = new HashSet<>();
        blockers.add(new Position(1, 1));
        blockers.add(new Position(2, 2));
        coordinates.setBlockers(blockers);
        assertEquals(2, coordinates.getBlockers().size());
    }

    @Test
    void shouldMoveNorthWhenFacingNorth() {
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 1 N", coordinates.toString());
    }

    @Test
    void shouldMoveSouthWhenFacingSouth() {
        coordinates = new Coordinates(0, 0, Direction.SOUTH.getSymbolAsString());
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 -1 S", coordinates.toString());
    }

    @Test
    void shouldMoveEastWhenFacingEast() {
        coordinates = new Coordinates(0, 0, Direction.EAST.getSymbolAsString());
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("1 0 E", coordinates.toString());
    }

    @Test
    void shouldMoveWestWhenFacingWest() {
        coordinates = new Coordinates(0, 0, Direction.WEST.getSymbolAsString());
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("-1 0 W", coordinates.toString());
    }


    @Test
    void shouldHandleCaseInsensitiveMoveCommand() {
        coordinates = new Coordinates(0, 0, Direction.NORTH.getSymbolAsString());
        coordinates.calculateNextMove("m");
        assertEquals("0 1 N", coordinates.toString());
    }

    @Test
    void shouldTurnLeftFromNorthToWest() {
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.LEFT.getSymbolAsString());
        assertEquals(Direction.WEST, coordinates.getDirection());
    }

    @Test
    void shouldTurnLeftFromWestToSouth() {
        coordinates.setDirection(Direction.WEST);
        coordinates.calculateNextMove(Command.LEFT.getSymbolAsString());
        assertEquals(Direction.SOUTH, coordinates.getDirection());
    }

    @Test
    void shouldTurnLeftFromSouthToEast() {
        coordinates.setDirection(Direction.SOUTH);
        coordinates.calculateNextMove(Command.LEFT.getSymbolAsString());
        assertEquals(Direction.EAST, coordinates.getDirection());
    }

    @Test
    void shouldTurnLeftFromEastToNorth() {
        coordinates.setDirection(Direction.EAST);
        coordinates.calculateNextMove(Command.LEFT.getSymbolAsString());
        assertEquals(Direction.NORTH, coordinates.getDirection());
    }

    @Test
    void shouldTurnRightFromNorthToEast() {
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.RIGHT.getSymbolAsString());
        assertEquals(Direction.EAST, coordinates.getDirection());
    }

    @Test
    void shouldTurnRightFromEastToSouth() {
        coordinates.setDirection(Direction.EAST);
        coordinates.calculateNextMove(Command.RIGHT.getSymbolAsString());
        assertEquals(Direction.SOUTH, coordinates.getDirection());
    }

    @Test
    void shouldTurnRightFromSouthToWest() {
        coordinates.setDirection(Direction.SOUTH);
        coordinates.calculateNextMove(Command.RIGHT.getSymbolAsString());
        assertEquals(Direction.WEST, coordinates.getDirection());
    }

    @Test
    void shouldTurnRightFromWestToNorth() {
        coordinates.setDirection(Direction.WEST);
        coordinates.calculateNextMove(Command.RIGHT.getSymbolAsString());
        assertEquals(Direction.NORTH, coordinates.getDirection());
    }

    @Test
    void shouldHandleCaseInsensitiveRotationCommand() {
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove("l");
        assertEquals(Direction.WEST, coordinates.getDirection());

        coordinates.calculateNextMove("r");
        assertEquals(Direction.NORTH, coordinates.getDirection());
    }

    @Test
    void shouldThrowExceptionForInvalidCommands() {
        coordinates.setDirection(Direction.NORTH);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                coordinates.calculateNextMove("A");
            });

        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void shouldNotMoveBeyondPositiveXBorder() {
        coordinates.setBorder(-2, -2, 2, 2);
        coordinates.setPosition(2, 0);
        coordinates.setDirection(Direction.EAST);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("2 0 E", coordinates.toString());
    }

    @Test
    void shouldNotMoveBeyondPositiveYBorder() {
        coordinates.setBorder(-2, -2, 2, 2);
        coordinates.setPosition(0, 2);
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 2 N", coordinates.toString());
    }

    @Test
    void shouldNotMoveBeyondNegativeXBorder() {
        coordinates.setBorder(-2, -2, 2, 2);
        coordinates.setPosition(-2, 0);
        coordinates.setDirection(Direction.WEST);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("-2 0 W", coordinates.toString());
    }

    @Test
    void shouldNotMoveBeyondNegativeYBorder() {
        coordinates.setBorder(-2, -2, 2, 2);
        coordinates.setPosition(0, -2);
        coordinates.setDirection(Direction.SOUTH);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 -2 S", coordinates.toString());
    }

    @Test
    void shouldAllowMoveWithinBorders() {
        coordinates.setBorder(-5, -5, 5, 5);
        coordinates.setPosition(1, 1);
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("1 2 N", coordinates.toString());
    }

    @Test
    void shouldNotMoveIntoBlockerPosition() {
        Set<Position> blockers = new HashSet<>();
        blockers.add(new Position(0, 1));
        coordinates.setBlockers(blockers);
        coordinates.setPosition(0,0);
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 0 N", coordinates.toString());
    }

    @Test
    void shouldMoveToNonBlockedPositions() {
        Set<Position> blockers = new HashSet<>();
        blockers.add(new Position(1, 1));
        coordinates.setBlockers(blockers);
        coordinates.setPosition(new Position(0, 0));
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 1 N", coordinates.toString());
    }

    @Test
    void shouldHandleMultipleBlockers() {
        Set<Position> blockers = new HashSet<>();
        blockers.add(new Position(0, 1));
        blockers.add(new Position(1, 0));
        coordinates.setBlockers(blockers);
        coordinates.setPosition(new Position(0, 0));
        coordinates.setDirection(Direction.NORTH);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 0 N", coordinates.toString());

        coordinates.setDirection(Direction.EAST);
        coordinates.calculateNextMove(Command.MOVE.getSymbolAsString());
        assertEquals("0 0 E", coordinates.toString());
    }

    @Test
    void shouldHandleSequenceOfMovesAndRotations() {
        coordinates.setPosition(1, 2);
        coordinates.setDirection(Direction.NORTH);
        coordinates.setBorder(-5, -5, 5, 5);

        // LMLMLMLMM
        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("1 2 W", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 2 W", coordinates.toString());

        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("0 2 S", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 1 S", coordinates.toString());

        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("0 1 E", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("1 1 E", coordinates.toString());

        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("1 1 N", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("1 2 N", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("1 3 N", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("1 4 N", coordinates.toString());

    }

    @Test
    void shouldHandleSequenceOfMovesAndRotationsWithBlockers() {
        coordinates.setPosition(1, 2);
        coordinates.setDirection(Direction.NORTH);
        coordinates.setBorder(-5, -5, 5, 5);

        Set<Position> blockers = new HashSet<>();
        blockers.add(new Position(1, 1));
        coordinates.setBlockers(blockers);

        // LMLMLMLMM
        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("1 2 W", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 2 W", coordinates.toString());

        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("0 2 S", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 1 S", coordinates.toString());

        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("0 1 E", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 1 E", coordinates.toString());

        coordinates.calculateNextMove(Command.LEFT);
        assertEquals("0 1 N", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 2 N", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 3 N", coordinates.toString());

        coordinates.calculateNextMove(Command.MOVE);
        assertEquals("0 4 N", coordinates.toString());

    }

}
