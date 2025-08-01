package com.marsrover;

/**
 * Represents the available commands for controlling a Mars rover.
 * <p>
 * Each command is represented by a single character symbol and provides
 * functionality to parse commands from characters or strings.
 * </p>
 * 
 * @author gracewee
 * @version 1.0
 * @since 1.0
 */
public enum Command {
    LEFT('L'),
    RIGHT('R'),
    MOVE('M');

    private final char symbol;

    private static final Command[] VALUES = values();

    Command(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getSymbolAsString() {
        return Character.toString(this.symbol);
    }

    public static Command fromChar(char c) {
        c = Character.toUpperCase(c);
        for (Command command : VALUES) {
            if (command.symbol == c) {
                return command;
            }
        }
        throw new IllegalArgumentException("Invalid command: " + c);
    }

    public static Command fromString(String s) {
        if (s == null || s.length() != 1) {
            throw new IllegalArgumentException("Invalid command string: " + s);
        }
        return fromChar(s.charAt(0));
    }
}
