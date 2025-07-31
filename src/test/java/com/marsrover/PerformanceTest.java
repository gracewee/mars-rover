package com.marsrover;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Performance Tests")
public class PerformanceTest {

    private static final int ITERATIONS = 100_000;
    private static final int BLOCKER_COUNT = 1000;
    private static final String LONG_COMMAND_SEQUENCE = "LMLMLMLMMRMMRMRRMLMLMLMLMMRMMRMRRMLMLMLMLMMRMMRMRRM";

    private Coordinates coordinates;

    private static final long LIMIT_IN_SECS = 1L;
    private static final long LIMIT_IN_NANO_SECS = (long) LIMIT_IN_SECS * 1_000_000_000L;


    @BeforeEach
    void setUp() {
        // Create test data
        coordinates = new Coordinates(50, 50, Direction.NORTH.getSymbolAsString());

        // Set borders
        coordinates.setBorder(-100,-100,100,100);
    }

    private Set<Position> generateRandomBlockers(int count) {
        Set<Position> blockers = new HashSet<>();
        Random random = new Random(42); // Fixed seed for reproducible results

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(200) - 100; // Range: -100 to 100
            int y = random.nextInt(200) - 100;
            blockers.add(new Position(x, y));
        }
        return blockers;
    }

    @Test
    void testSingleMovePerformance() {
        System.out.println("\n=== Single Move Performance Test ===");

        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            coordinates.calculateNextMove(Command.MOVE);
            coordinates.setPosition(50, 50); // Reset position
        }
        long diffTime = System.nanoTime() - startTime;

        System.out.printf("Original:  %,d ns (%,.2f ms)%n", diffTime, diffTime / 1_000_000.0);
        assertTrue(diffTime <= LIMIT_IN_NANO_SECS, "Execute within " + LIMIT_IN_SECS + " secs");
    }

    @Test
    void testRotationPerformance() {
        System.out.println("\n=== Rotation Performance Test ===");

        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            coordinates.calculateNextMove(Command.LEFT);
            coordinates.calculateNextMove(Command.RIGHT);
            coordinates.setPosition(50, 50); // Reset position
        }
        long diffTime = System.nanoTime() - startTime;

        System.out.printf("Original:  %,d ns (%,.2f ms)%n", diffTime, diffTime / 1_000_000.0);
        assertTrue(diffTime <= LIMIT_IN_NANO_SECS, "Execute within " + LIMIT_IN_SECS + " secs");
    }

    @Test
    void testBlockersPerformance() {
        System.out.println("\n=== Blockers Performance Test ===");

        // Create blockers
        Set<Position> blockers = generateRandomBlockers(BLOCKER_COUNT);
        coordinates.setBlockers(blockers);

        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            coordinates.calculateNextMove(Command.MOVE);
            coordinates.setPosition(0, 0); // Reset position
        }
        long diffTime = System.nanoTime() - startTime;

        System.out.printf("Original:  %,d ns (%,.2f ms)%n", diffTime, diffTime / 1_000_000.0);
        assertTrue(diffTime <= LIMIT_IN_NANO_SECS, "Execute within " + LIMIT_IN_SECS + " secs");
    }

    @Test
    void testCommandSequencePerformance() {
        System.out.println("\n=== Command Sequence Performance Test ===");

        // Create blockers
        Set<Position> blockers = generateRandomBlockers(BLOCKER_COUNT);
        coordinates.setBlockers(blockers);

        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            for (char c : LONG_COMMAND_SEQUENCE.toCharArray()) {
                coordinates.calculateNextMove(String.valueOf(c));
            }
            coordinates.setPosition(50, 50); // Reset
            coordinates.setDirection(Direction.NORTH);
        }
        long diffTime = System.nanoTime() - startTime;

        System.out.printf("Original:  %,d ns (%,.2f ms)%n", diffTime, diffTime / 1_000_000.0);
        assertTrue(diffTime <= LIMIT_IN_NANO_SECS, "Execute within " + LIMIT_IN_SECS + " secs");
    }

}
