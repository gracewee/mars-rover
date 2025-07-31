package com.marsrover;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Command Enum Tests")
class CommandTest {


    @ParameterizedTest
    @ValueSource(strings = {"L", "R", "M", "l", "r", "m"})
    void shouldAllowCommandFromValidStrings(String s) {
        Command command = Command.fromString(s);
        assertEquals(s.toUpperCase().charAt(0), command.getSymbol());
    }

    @Test
    void shouldThrowExceptionForInvalidCommand() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Command.fromString("X"));
        assertTrue(exception.getMessage().contains("Invalid command"));
    }

    @Test
    void shouldCreateCommandsFromValidSingleCharacterStrings() {
        assertEquals(Command.LEFT, Command.fromString("L"));
        assertEquals(Command.RIGHT, Command.fromString("R"));
        assertEquals(Command.MOVE, Command.fromString("M"));
        assertEquals(Command.LEFT, Command.fromString("l"));
        assertEquals(Command.RIGHT, Command.fromString("r"));
        assertEquals(Command.MOVE, Command.fromString("m"));
    }

    @Test
    void shouldCreateCommandsFromValidSingleCharacter() {
        assertEquals(Command.LEFT, Command.fromChar('L'));
        assertEquals(Command.RIGHT, Command.fromChar('R'));
        assertEquals(Command.MOVE, Command.fromChar('M'));
        assertEquals(Command.LEFT, Command.fromChar('l'));
        assertEquals(Command.RIGHT, Command.fromChar('r'));
        assertEquals(Command.MOVE, Command.fromChar('m'));
    }


}
