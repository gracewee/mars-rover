package com.marsrover;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RunRoverApplication Tests")
class RunRoverApplicationTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        // Capture System.out and System.err for testing
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void shouldRunDefaultDemoWhenNoArgumentsProvided() {
        String[] args = {};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Running default demo"));
        assertTrue(output.contains("Initial position:"));
        assertTrue(output.contains("Final position:"));
    }

    @Test
    void shouldProcessRoverFromArgumentsWhenArgumentsProvided() {
        String[] args = {"5", "5", "1", "2", "N", "LMLMLMLMM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 1 2 N"));
        assertTrue(output.contains("Final position:"));
    }

    @Test
    void shouldHandleMultipleRoversInSequence() {
        String[] args = {"5", "5", "1", "2", "N", "LMLMLMLMM", "3", "3", "E", "MMRMMRMRRM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 1 2 N"));
        assertTrue(output.contains("Initial position: 3 3 E"));

        // Should have two "Final position" outputs
        long finalPositionCount = output.lines()
                .filter(line -> line.contains("Final position:"))
                .count();
        assertEquals(2, finalPositionCount);
    }

    @Test
    void shouldProcessValidSingleRoverArgumentsCorrectly() {
        String[] args = {"5", "5", "1", "2", "N", "LMLMLMLMM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 1 2 N"));
        assertTrue(output.contains("Final position: 1 3 N"));
        assertFalse(errorStream.toString().contains("Error"));
    }

    @Test
    void shouldHandleRoverAtOrigin() {
        String[] args = {"5", "5", "0", "0", "N", "M"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 0 0 N"));
        assertTrue(output.contains("Final position: 0 1 N"));
    }

    @Test
    void shouldHandleRoverWithNoCommands() {
        String[] args = {"5", "5", "2", "2", "E", ""};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 2 2 E"));
        assertTrue(output.contains("Final position: 2 2 E"));
    }

    @Test
    void shouldHandleInvalidDirectionGracefully() {
        String[] args = {"5", "5", "1", "2", "X", "LMLMLMLMM"};

        RunRoverApplication.main(args);

        String error = errorStream.toString();
        assertTrue(error.contains("Invalid direction 'X'"));
    }

    @Test
    void shouldHandleInvalidCommandsGracefully() {
        String[] args = {"5", "5", "1", "2", "N", "LMXLMLMM"};

        RunRoverApplication.main(args);

        String error = errorStream.toString();
        assertTrue(error.contains("Invalid commands"));
    }

    @Test
    void shouldHandleInsufficientArgumentsGracefully() {
        String[] args = {"5", "5", "1"};

        RunRoverApplication.main(args);

        String error = errorStream.toString();
        assertTrue(error.contains("Error parsing arguments"));
        assertTrue(error.contains("Usage:"));
    }

    @Test
    void shouldHandleNonNumericCoordinatesGracefully() {
        String[] args = {"5", "5", "a", "b", "N", "LMLMLMLMM"};

        RunRoverApplication.main(args);

        String error = errorStream.toString();
        assertTrue(error.contains("Error parsing arguments"));
    }

    @Test
    void shouldHandleNonNumericBorderGracefully() {
        String[] args = {"a", "b", "1", "2", "N", "LMLMLMLMM"};

        RunRoverApplication.main(args);

        String error = errorStream.toString();
        assertTrue(error.contains("Error parsing arguments"));
    }

    @Test
    void shouldValidateCorrectDirections() throws Exception {
        Method isValidDirection = RunRoverApplication.class.getDeclaredMethod("isValidDirection", String.class);
        isValidDirection.setAccessible(true);

        assertTrue((Boolean) isValidDirection.invoke(null, "N"));
        assertTrue((Boolean) isValidDirection.invoke(null, "S"));
        assertTrue((Boolean) isValidDirection.invoke(null, "E"));
        assertTrue((Boolean) isValidDirection.invoke(null, "W"));
        assertTrue((Boolean) isValidDirection.invoke(null, "n"));
        assertTrue((Boolean) isValidDirection.invoke(null, "s"));
    }

    @Test
    void shouldRejectInvalidDirections() throws Exception {
        Method isValidDirection = RunRoverApplication.class.getDeclaredMethod("isValidDirection", String.class);
        isValidDirection.setAccessible(true);

        assertFalse((Boolean) isValidDirection.invoke(null, "X"));
        assertFalse((Boolean) isValidDirection.invoke(null, "NORTH"));
        assertFalse((Boolean) isValidDirection.invoke(null, ""));
        assertFalse((Boolean) isValidDirection.invoke(null, "123"));
    }

    @Test
    void shouldValidateCorrectCommands() throws Exception {
        Method isValidCommands = RunRoverApplication.class.getDeclaredMethod("isValidCommands", String.class);
        isValidCommands.setAccessible(true);

        assertTrue((Boolean) isValidCommands.invoke(null, "L"));
        assertTrue((Boolean) isValidCommands.invoke(null, "R"));
        assertTrue((Boolean) isValidCommands.invoke(null, "M"));
        assertTrue((Boolean) isValidCommands.invoke(null, "LRM"));
        assertTrue((Boolean) isValidCommands.invoke(null, "LMLMLMLMM"));
        assertTrue((Boolean) isValidCommands.invoke(null, "MMRMMRMRRM"));
        assertTrue((Boolean) isValidCommands.invoke(null, ""));
    }

    @Test
    void shouldRejectInvalidCommands() throws Exception {
        Method isValidCommands = RunRoverApplication.class.getDeclaredMethod("isValidCommands", String.class);
        isValidCommands.setAccessible(true);

        assertFalse((Boolean) isValidCommands.invoke(null, "X"));
        assertFalse((Boolean) isValidCommands.invoke(null, "LMX"));
        assertFalse((Boolean) isValidCommands.invoke(null, "123"));
        assertFalse((Boolean) isValidCommands.invoke(null, "LMRN"));
    }

    @Test
    void shouldExecuteCompleteRoverMissionSuccessfully() {
        String[] args = {"5", "5", "1", "2", "N", "LMLMLMLMM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 1 2 N"));
        assertTrue(output.contains("Final position: 1 3 N"));

        // Verify step-by-step output
        assertTrue(output.contains("After L:"));
        assertTrue(output.contains("After M:"));
    }

    @Test
    void shouldHandleRoverReachingBorderCorrectly() {
        String[] args = {"2", "2", "0", "0", "N", "MMM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 0 0 N"));
        // Should stop at border, not go beyond
        assertTrue(output.contains("Final position: 0 2 N"));
    }

    @Test
    void shouldHandleMultipleRoversWithBlockers() {
        String[] args = {"5", "5", "1", "2", "N", "LMLMLMLMM", "3", "3", "E", "MMRMMRMRRM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        // First rover
        assertTrue(output.contains("Initial position: 1 2 N"));
        // Second rover
        assertTrue(output.contains("Initial position: 3 3 E"));
        // Both should have final positions
        long finalPositionCount = output.lines()
            .filter(line -> line.contains("Final position:"))
            .count();
        assertEquals(2, finalPositionCount);
    }

    @Test
    void shouldHandleComplexCommandSequences() {
        String[] args = {"10", "10", "5", "5", "N", "LMLMLMLMMRMMRMRRMLMLMLMLMM"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 5 5 N"));
        assertTrue(output.contains("Final position:"));
        assertFalse(errorStream.toString().contains("Error"));
    }

    @Test
    void shouldRunDefaultDemoSuccessfully() {
        String[] args = {};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Running default demo"));
        assertTrue(output.contains("Initial position: 1 2 N"));
        assertTrue(output.contains("Initial position: 3 3 E"));
        assertTrue(output.contains("Final position:"));

        // Should have two rovers in demo
        long initialPositionCount = output.lines()
            .filter(line -> line.contains("Initial position:"))
            .count();
        assertEquals(2, initialPositionCount);
    }

    @Test
    void shouldHandleZeroBorderValues() {
        String[] args = {"0", "0", "0", "0", "N", "M"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 0 0 N"));
        // Should not move beyond zero border
        assertTrue(output.contains("Final position: 0 0 N"));
    }

    @Test
    void shouldHandleNegativeCoordinates() {
        String[] args = {"5", "5", "-1", "-1", "N", "M"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: -1 -1 N"));
        assertTrue(output.contains("Final position: -1 0 N"));
    }

    @Test
    void shouldHandleSingleCharacterCommands() {
        String[] args = {"5", "5", "2", "2", "N", "L"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 2 2 N"));
        assertTrue(output.contains("Final position: 2 2 W"));
    }

    @Test
    void shouldHandleLargeBorderValues() {
        String[] args = {"100", "100", "50", "50", "N", "M"};

        RunRoverApplication.main(args);

        String output = outputStream.toString();
        assertTrue(output.contains("Initial position: 50 50 N"));
        assertTrue(output.contains("Final position: 50 51 N"));
    }
}
