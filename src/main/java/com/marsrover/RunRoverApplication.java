package com.marsrover;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

/**
 * Main application class for the Mars Rover simulation.
 * <p>
 * Provides command-line interface for controlling rovers with support for
 * multiple rovers, boundary definitions, and collision detection. Can run
 * in interactive mode or process command-line arguments.
 * </p>
 * 
 * @author gracewee
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class RunRoverApplication {

	public static void main(String[] args) {

		if (args.length > 0) {
			// Parse command line arguments in format: "5 5 1 2 N LMLMLMLMM"
			processRoverFromArgs(args);
		} else {
			// Run interactive mode or default demo
			runDefaultDemo();
		}
	}

	private static void processRoverFromArgs(String[] args) {
		try {
			// Join all arguments into a single string (in case they're passed as separate args)
			String input = String.join(" ", args);

			// Parse using Scanner
			Scanner scanner = new Scanner(input);

			// Read coordinates and direction
			Position border = new Position(scanner.nextInt(), scanner.nextInt());

			Set<Position> blockers = new HashSet<>();
			while (scanner.hasNext()) {
				int posX = scanner.nextInt();
				int posY = scanner.nextInt();
				String direction = scanner.next();
				if (!isValidDirection(direction)) {
					System.err.println("Error: Invalid direction '" + direction + "'.");
					return;
				}
				Coordinates rover = new Coordinates(posX,  posY, direction);
				rover.setBorder(- border.getX(), - border.getY(), border.getX(), border.getY());
				rover.setBlockers(blockers);
				System.out.println("Initial position: " + rover.toString());

				if (scanner.hasNext()) {
					String commands = scanner.next();
					if (!isValidCommands(commands)) {
						System.err.println("Error: Invalid commands '" + commands + "'.");
						return;
					}
					for (String move : commands.split("")) {
						rover.calculateNextMove(move);
						System.out.println("After " + move + ": " + rover.toString());
					}
				}
				System.out.println("Final position: " + rover.toString());
				blockers.add(rover.getPosition());
			}

		} catch (Exception e) {
			System.err.println("Error parsing arguments: " + e.getMessage());
			System.err.println("Usage: java RunRoverApplication <border x> <border y> <x> <y> <direction> [commands]");
			System.err.println("Example: java RunRoverApplication 1 2 N LMLMLMLMM");
		}
	}

	private static boolean isValidDirection(String direction) {
		try {
			Direction.fromString(direction);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private static boolean isValidCommands(String commands) {
		try {
			int idx = 0;
			while (idx < commands.length()) {
				Command.fromChar(commands.charAt(idx));
				idx++;
			}
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private static void runDefaultDemo() {
		System.out.println("Running default demo...");

		String[] args = new String[]{"5", "5", "1", "2", "N", "LMLMLMLMM", "3", "3", "E", "MMRMMRMRRM"};
		processRoverFromArgs(args);

	}

}
