package basic_tester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import basic.Field;
import basic.GameBoard;
import basic.Position;

public class GameBoardTester {
	private static GameBoard gameBoard;
	private static int[][] numbers;
	
	@BeforeAll
	static void init() {
		numbers = new int[][] {
			{4, 2, 0, 6, 9, 0, 0, 0, 0}, // -
			{2, 0, 0, 7, 1, 0, 0, 5, 0}, // |
			{0, 0, 1, 4, 5, 2, 5, 7, 8}, // |
			{6, 1, 3, 5, 0, 0, 4, 0, 0}, // |
			{9, 0, 4, 0, 1, 2, 0, 0, 8}, // X
			{6, 1, 2, 5, 6, 3, 5, 0, 2}, // |
			{0, 0, 3, 0, 5, 0, 3, 0, 1}, // |
			{2, 2, 0, 4, 8, 1, 3, 3, 0}, // |
			{4, 9, 3, 0, 1, 7, 2, 4, 6}  // -
		}; //|-----------Y-----------|
		
		gameBoard = new GameBoard(numbers);
	}
	
	@Test
	@DisplayName("Constructor Test")
	void doesTheConstructorWork() {
		assertEquals(6, gameBoard.getField(8, 8).getNumber());
		assertEquals(2, gameBoard.getField(7, 1).getNumber());
		assertEquals(9, gameBoard.getField(8, 1).getNumber());
	}
	
	@Test
	@DisplayName("Get the first column")
	void getTheFirstColumn() {
		LinkedList<Field> area = gameBoard.getArea(0, 0, 0, 8);
		
		for (int y = 0; y < 9; y++) {
			assertEquals(numbers[0][y], area.get(y).getNumber());
		}
	}
	
	@Test
	@DisplayName("Get the middle 3x3 square")
	void getMiddleSquare() {
		int[][] expected = new int[][] {
			{5, 0, 0},
			{0, 1, 2},
			{5, 6, 3}
		};
		
		LinkedList<Field> middleSquare = gameBoard.getArea(3, 3, 5, 5);
		
		for (int i = 0; i < middleSquare.size(); i++) {
			Field field = middleSquare.get(i);
			assertEquals(expected[field.getPos().getX() % 3][field.getPos().getY() % 3], field.getNumber());
		}
	}
	
	@Test
	@DisplayName("Replace the middle 3x3 square")
	void replaceMiddleSquare() {
		Field[] newFields = new Field[] {
			new Field(new Position(3, 3), 5, false), new Field(new Position(3, 4), 0, true), new Field(new Position(3, 5), 0, true),
			new Field(new Position(4, 3), 0, true), new Field(new Position(4, 4), 1, false), new Field(new Position(4, 5), 2, true),
			new Field(new Position(5, 3), 5, true), new Field(new Position(5, 4), 6, true), new Field(new Position(5, 5), 3, false)
		};

		LinkedList<Field> newMiddleSquare = new LinkedList<Field>(Arrays.asList(newFields));
		gameBoard.setArea(newMiddleSquare);
		
		LinkedList<Field> middleSquare = gameBoard.getArea(3, 3, 5, 5);
		
		for (int i = 0; i < 9; i++) {
			assertTrue(middleSquare.contains(newFields[i]));
		}
	}
	
	
}
