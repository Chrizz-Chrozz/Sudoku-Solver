package basic_tester;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import basic.BoardSolver;
import basic.Field;
import basic.GameBoard;
import basic.Position;

public class BoardSolverTester {
	private GameBoard gameBoard;
	
	@BeforeEach
	void init() {
		int[][] numbers = new int[][] {
			{2, 1, 4, 8, 5, 7, 3, 9, 6},
			{6, 8, 3, 4, 2, 9, 5, 1, 7},
			{9, 7, 5, 6, 1, 3, 8, 2, 4},
			{3, 6, 8, 1, 7, 4, 2, 5, 9},
			{5, 2, 9, 3, 8, 6, 7, 4, 1},
			{7, 4, 1, 2, 9, 5, 6, 3, 8},
			{8, 3, 7, 9, 4, 2, 1, 6, 5},
			{1, 9, 6, 5, 3, 8, 4, 7, 2},
			{4, 5, 2, 7, 6, 1, 9, 8, 3}
		};
		
		byte[][] editable = new byte[][] {
			{0, 0, 0, 0, 1, 0, 1, 1, 0},
			{1, 0, 0, 1, 0, 1, 0, 1, 0},
			{1, 1, 0, 0, 0, 1, 0, 0, 0},
			{0, 0, 1, 0, 1, 1, 0, 1, 0},
			{0, 1, 9, 1, 0, 1, 0, 0, 1},
			{0, 0, 1, 1, 0, 0, 1, 0, 1},
			{0, 1, 0, 1, 1, 0, 0, 0, 1},
			{1, 1, 1, 0, 0, 0, 1, 0, 1},
			{0, 1, 1, 1, 0, 0, 0, 1, 1}
		};
		
		Field[][] fields = new Field[9][9];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				boolean isFieldEditable = editable[y][x] == 0;
				Field field = new Field(new Position(x, y), numbers[y][x], isFieldEditable);
				fields[x][y] = field;
			}
		}
		
		gameBoard = new GameBoard(fields);
	}
	
	@Test
	@DisplayName("Is the board solved?")
	void isBoardSolved() {
		BoardSolver bs = new BoardSolver(gameBoard);
		
		assertTrue(bs.isTheBoardSolved());
	}
	
	@Test
	@DisplayName("If the board contains an error, it is not solved.")
	void boardIsNotSolved() {
		gameBoard.saveField(new Field(new Position(0, 0), 3, true));
		
		BoardSolver bs = new BoardSolver(gameBoard);
		
		assertFalse(bs.isTheBoardSolved());
	}
	
	@Test
	@DisplayName("If the board contains empty fields, it is not solved.")
	void boardContainsEmptyFieldsAndIsNotSolved() {
		gameBoard.saveField(new Field(new Position(0, 0), 0, true));
		
		BoardSolver bs = new BoardSolver(gameBoard);
		
		assertFalse(bs.isTheBoardSolved());
	}
	
	@Test
	@DisplayName("Are the 'possible numbers' calculated correctly?")
	void arePossibleNumbersCorrect() {
		int[][] newNumbers = new int[][] {
			{0, 0, 1, 0, 3, 0, 6, 9, 2}, //--> (0|0): ------78-    (1|0): ---4--78-
			{2, 0, 3, 7, 0, 0, 0, 8, 1}, //--> (1|1): ---4----9
			{5, 6, 0, 1, 2, 0, 3, 4, 0}, //--> (2|2): -------8-
			{0, 1, 2, 0, 0, 0, 0, 0, 0},
			{9, 0, 0, 0, 0, 0, 0, 0, 0},
			{4, 3, 7, 0, 0, 0, 0, 0, 0},
			{0, 2, 0, 0, 0, 0, 0, 0, 0},
			{0, 5, 9, 0, 0, 0, 0, 0, 0},
			{1, 0, 4, 0, 0, 0, 0, 0, 0}
		};
		
		LinkedList<Field> fields = new LinkedList<Field>();
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				boolean isFieldEditable = true;
				Field field = new Field(new Position(x, y), newNumbers[y][x], isFieldEditable);
				fields.add(field);
			}
		}
		
		gameBoard.setArea(fields);
		BoardSolver bs = new BoardSolver(gameBoard);
		
		gameBoard = bs.getGameBoard();
		LinkedList<Integer> expected_For_0_0 = new LinkedList<Integer>(Arrays.asList(new Integer(7), new Integer(8)));
		LinkedList<Integer> expected_For_1_0 = new LinkedList<Integer>(Arrays.asList(new Integer(4), new Integer(7), new Integer(8)));
		LinkedList<Integer> expected_For_1_1 = new LinkedList<Integer>(Arrays.asList(new Integer(4), new Integer(9)));
		LinkedList<Integer> expected_For_2_2 = new LinkedList<Integer>(Arrays.asList(new Integer(8)));
		
		assertTrue(checkPossibleNumbers(0, 0, expected_For_0_0));
		assertTrue(checkPossibleNumbers(1, 0, expected_For_1_0));
		assertTrue(checkPossibleNumbers(1, 1, expected_For_1_1));
		assertTrue(checkPossibleNumbers(2, 2, expected_For_2_2));
	}
	
	private boolean checkPossibleNumbers(int x, int y, LinkedList<Integer> expectedValues) {
		LinkedList<Integer> actualValues = (LinkedList<Integer>) gameBoard.getField(x, y).getRemainingPossibleNumbers();
		int numbers_of_result_count = actualValues.size();
		
		actualValues.removeAll(expectedValues);
		return (expectedValues.size() == numbers_of_result_count && actualValues.size() == 0);
	}
}
