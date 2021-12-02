import java.util.Arrays;

/** Generates, displays, and solves a maze. */
public class Maze {

	/** Index for east direction. */
	public static final int EAST = 1;

	/** Index for north direction. */
	public static final int NORTH = 0;

	/** Index for south direction. */
	public static final int SOUTH = 2;

	/** Index for west direction. */
	public static final int WEST = 3;

	/** Index for x direction. */
	public static final int X = 0;

	/** Index for y direction. */
	public static final int Y = 1;

	/**
	 * x and y offsets for per direction. For example, the point to the east of
	 * the point (x, y) is (x + OFFSETS[EAST][X], y + OFFSETS[EAST][Y]).
	 */
	public static final int[][] OFFSETS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };


	/**
	 * Modifies passage to contain a one-way passage from location a to location
	 * b. These locations are arrays of two numbers, x and y. Assumes these two
	 * locations are adjacent.
	 * 
	 * @param passages
	 *            passages[x][y][direction] is true if there is a passage from
	 *            location x, y to its neighbor in direction. Directions are
	 *            specified by the constants NORTH, EAST, SOUTH, and WEST.
	 */
	public static void addPassage(boolean[][][] passages, int[] a, int[] b) {
		// Getting the x and y co-ordinate from location a
		int aX = a[0];
		int aY = a[1];

		// Getting the x and y co-ordinate from location b
		int bX = b[0];
		int bY = b[1];

		// Determining in which direction location b is from location a, and adding a passage between them
		if (aX > bX) {
			// to the west
			int direction = 3;
			passages[aX][aY][direction] = true;
		}
		else if (bX > aX) {
			// to the east
			int direction = 1;
			passages[aX][aY][direction] = true;
		}
		else if (aY > bY) {
			// to the south
			int direction = 2;
			passages[aX][aY][direction] = true;
		}
		else if (bY > aY) {
			// to the north
			int direction = 0;
			passages[aX][aY][direction] = true;
		}
	}

	/**
	 * Returns a new array of pairs containing start followed by all of the
	 * elements in list.
	 */
	public static int[][] addToFront(int[] start, int[][] list) {
		// Getting the x and y values for start
		int xStart = start[0];
		int yStart = start[1];

		// Creating the new array with start pair as first element
		int[][] startArr = new int[list.length + 1][2];
		startArr[0][0] = xStart;
		startArr[0][1] = yStart;

		// Adding list elements to the new array
		for (int i = 0; i < list.length; i++) {
			startArr[i + 1][0] = list[i][0];
			startArr[i + 1][1] = list[i][1];
		}

		return startArr;
	}

	/**
	 * Returns a random one of the first n elements of list, or null, if n is 0.
	 */
	public static int[] chooseRandomlyFrom(int[][] list, int n) {
		if (n == 0) {
			return null;
		}
		else {
			int randomNum = StdRandom.uniform(n);
			return list[randomNum];
		}
	}

	/**
	 * Returns the first index of the location of pair in the first n elements
	 * of list, or -1 if it does not appear. pair is assumed to be an array of
	 * two numbers, and this method checks to see if pair has the same
	 * numbers as one of the first n elements of list.
	 *
	 * @param pair
	 * 		The pair to search for in the list; pair is an array of two
	 * 		elements.
	 * @param list
	 * 		A list of pairs; each element in the list is an array with two
	 * 		elements.
	 * @param n
	 * 		The number of elements in the list to search through.
	 */
	public static int contains(int[] pair, int[][] list, int n) {
		// Getting the x and y value for pair
		int xVal = pair[0];
		int yVal = pair[1];

		// Running through list to find the first index of the location of pair
		for (int i = 0; i < n; i++) {
			if ((list[i][0] == xVal) && (list[i][1] == yVal)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Graphically draws the maze.
	 * 
	 * @param passages
	 *            passages[x][y][direction] is true if there is a passage from
	 *            location x, y to its neighbor in direction. Directions are
	 *            specified by the constants NORTH, EAST, SOUTH, and WEST.
	 */
	public static void drawMaze(boolean[][][] passages) {
		StdDraw.clear(StdDraw.PINK);
		StdDraw.setPenColor(StdDraw.WHITE);
		int width = passages.length;
		StdDraw.setPenRadius(0.75 / width);
		// Draw passages
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				if (passages[x][y][NORTH] || (y + 1 < width && passages[x][y + 1][SOUTH])) {
					StdDraw.line(x, y, x, y + 1);
				}
				if (passages[x][y][EAST] || (x + 1 < width && passages[x + 1][y][WEST])) {
					StdDraw.line(x, y, x + 1, y);
				}
			}
		}
		// Draw entrance and exit
		StdDraw.line(0, 0, -1, 0);
		StdDraw.line(width - 1, width - 1, width, width - 1);
		StdDraw.show();
	}

	/**
	 * Graphically draws the solution.
	 */
	public static void drawSolution(int[][] path, int width) {
		StdDraw.setPenColor(); // Black by default
		StdDraw.setPenRadius();
		StdDraw.line(0, 0, -1, 0);
		StdDraw.line(width - 1, width - 1, width, width - 1);
		for (int i = 0; i < path.length - 1; i++) {
			StdDraw.line(path[i][0], path[i][1], path[i + 1][0], path[i + 1][1]);
		}
		StdDraw.show();
	}

	/**
	 * Checks if here's neighbor in direction (called there) is in unexplored.
	 * If so, adds a passage from here to there and returns there. If not,
	 * returns null.
	 * 
	 * @param passages
	 *            passages[x][y][direction] is true if there is a passage from
	 *            location x, y to its neighbor in direction. Directions are
	 *            specified by the constants NORTH, EAST, SOUTH, and WEST.
	 * @param unexplored
	 *            a list of locations that have not yet been reached.
	 * @param n
	 *            the number of valid elements in unexplored. Unexplored is
	 *            "resized" by changing this number.
	 * @param here
	 *            the location from which a neighbor is sought.
	 * @param direction
	 *            the direction to expand location, one of NORTH, SOUTH, EAST,
	 *            or WEST.
	 * @return the newly-explored location there if this succeeded, null
	 *         otherwise.
	 */
	public static int[] expandLocation(boolean[][][] passages, int[][] unexplored, int n, int[] here, int direction) {
		int[] there = new int[2];
		// Find the neighboring point
		there[X] = here[X] + OFFSETS[direction][X];
		there[Y] = here[Y] + OFFSETS[direction][Y];

		// Checking to see if there is in unexplored
		for (int i = 0; i < n; i++) {
			if ((unexplored[i][0] == there[0]) && (unexplored[i][1] == there[1])) {
				addPassage(passages, here, there);
				return there;
			}
		}
		return null;
	}

	/**
	 * Chooses "here" to be either lastExploredLocation (if it is not null) or a
	 * random location in frontier. If possible, adds a passage from "here" to a
	 * location "there" in unexplored, then moves "there" from unexplored to
	 * frontier. If not, moves "here" from frontier to done. Returns "there", or
	 * null if no new location was explored.
	 * 
	 * @param passages
	 *            passages[x][y][direction] is true if there is a passage from
	 *            location x, y to its neighbor in direction. Directions are
	 *            specified by the constants NORTH, EAST, SOUTH, and WEST.
	 * @param done
	 *            a list of locations from which no new edges can be drawn to
	 *            locations in unexplored.
	 * @param frontier
	 *            a list of locations that have been reached by some edge but
	 *            are not yet in done.
	 * @param unexplored
	 *            a list of locations that have not yet been reached.
	 * @param counts
	 *            the number of valid elements in each of the three previous
	 *            arrays. The arrays are "resized" by changing these elements.
	 * @param lastExploredLocation
	 *            the last location that was explored or null.
	 * @return the newly explored location (or null if no new location was
	 *         explored).
	 */
	public static int[] expandMaze(boolean[][][] passages, int[][] done, int[][] frontier, int[][] unexplored,
								   int[] counts, int[] lastExploredLocation) {
		int[] here;
		if (lastExploredLocation == null) {
			here = chooseRandomlyFrom(frontier, counts[1]);
		} else {
			here = lastExploredLocation;
		}
		// Choose a random direction
		int direction = StdRandom.uniform(4);
		for (int i = 0; i < 4; i++) {
			int[] there = expandLocation(passages, unexplored, counts[2], here, direction);
			if (there != null) {
				// Move there from unexplored to frontier
				frontier[counts[1]] = there;
				counts[1]++;
				remove(there, unexplored, counts[2]);
				counts[2]--;
				// We're done
				return there;
			}
			direction = (direction + 1) % 4;
		}
		// No valid neighbor was found. Move here from frontier to done.
		done[counts[0]] = here;
		counts[0]++;
		remove(here, frontier, counts[1]);
		counts[1]--;
		return null;
	}

	/** Draws and then solves a maze. */
	public static void main(String[] args) {
		StdDraw.enableDoubleBuffering();
		int width = 20;
		StdDraw.setXscale(-0.5, width - 0.5);
		StdDraw.setYscale(-0.5, width - 0.5);
		StdDraw.show();
		boolean[][][] passages = new boolean[width][width][4];
		// Initially, no locations are done
		int[][] done = new int[width * width][];
		// The frontier only contains {0, 0}
		int[][] frontier = new int[width * width][];
		frontier[0] = new int[] { 0, 0 };
		// All other locations are in unexplored
		int[][] unexplored = new int[width * width][];
		// Number of nodes done, on the frontier, and unexplored
		int[] counts = { 0, 1, width * width - 1 };
		int i = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				if (x != 0 || y != 0) {
					unexplored[i] = new int[] { x, y };
					i++;
				}
			}
		}
		// As long as there are unexplored locations, expand the maze
		int[] lastExploredLocation = null;
		while (counts[2] > 0) {
			lastExploredLocation = expandMaze(passages, done, frontier, unexplored, counts, lastExploredLocation);
			drawMaze(passages);
			StdDraw.show();
			StdDraw.pause(25);
		}
		// Solve the maze
		int[][] solution = solve(passages, new int[] { 0, 0 }, new int[] { width - 1, width - 1 });
		drawSolution(solution, width);
	}

	/**
	 * Modifies list so that pair is replaced with the (n - 1)th element of
	 * list. Assumes pair is an array of two numbers that appears somewhere in
	 * list. Thus, when this method is done, the first n - 1 element of list are
	 * the same as the first n elements of the old version, but with pair
	 * removed and with the order of elements potentially different.
	 */
	public static void remove(int[] pair, int[][] list, int n) {
		// Getting the x and y value for pair
		int xVal = pair[0];
		int yVal = pair[1];

		// Getting the x and y value for the n-1 element
		int xValN = list[n - 1][0];
		int yValN = list[n - 1][1];

		// Removing pair from list and replacing it with the (n-1)th element
		for (int i = 0; i < n; i++) {
			if ((list[i][0] == xVal) && (list[i][1] == yVal)) {
				list[i][0] = xValN;
				list[i][1] = yValN;
			}
		}
	}

	/**
	 * Returns a path (sequence of locations) leading from start to goal in
	 * passages or null if there is no such path.
	 * 
	 * @param passages
	 *            passages[x][y][direction] is true if there is a passage from
	 *            location x, y to its neighbor in direction. Directions are
	 *            specified by the constants NORTH, EAST, SOUTH, and WEST.
	 */
	public static int[][] solve(boolean[][][] passages, int[] start, int[] goal) {
		// Base case: we're already at the goal
		if ((start[X] == goal[X]) && (start[Y] == goal[Y])) {
			return new int[][] { goal };
		}
		// Can we reach the goal from any of our neighbors?
		for (int d = 0; d < 4; d++) {
			if (passages[start[0]][start[1]][d]) {
				int[] next = { start[X] + OFFSETS[d][X], start[Y] + OFFSETS[d][Y] };
				int[][] restOfPath = solve(passages, next, goal);
				if (restOfPath != null) {
					return addToFront(start, restOfPath);
				}
			}
		}
		// Nope -- we can't get there from here
		return null;
	}
}
