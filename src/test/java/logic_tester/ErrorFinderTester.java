package logic_tester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import basic.Field;
import basic.GameBoard;
import basic.Position;
import logic.ErrorFinder;

public class ErrorFinderTester {
	private GameBoard gameBoard;
	
	@BeforeEach
	void init() {
		int[][] gameboardWithErrors = new int[][] {
			{1, 4, 2, 9, 6, 8, 3, 7, 0}, // -
			{5, 9, 7, 0, 0, 2, 4, 8, 6}, // |
			{8, 3, 6, 4, 5, 0, 9, 1, 2}, // |
			{4, 1, 9, 5, 1, 0, 0, 1, 5}, // |
			{3, 2, 5, 6, 2, 6, 9, 1, 5}, // X
			{6, 7, 8, 0, 2, 7, 8, 5, 1}, // |
			{2, 5, 1, 0, 7, 0, 7, 0, 1}, // |
			{9, 6, 3, 3, 7, 1, 2, 8, 2}, // |
			{7, 8, 4, 1, 9, 3, 6, 2, 0}  // -
		};// |-----------Y-----------|
		
		Field[][] fields = new Field[9][9];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				fields[x][y] = new Field(new Position(x, y), gameboardWithErrors[x][y], true);
			}
		}
		
		gameBoard = new GameBoard(fields);
	}
	
	@Test
	@DisplayName("The first row doesn't contain any errors")
	void noErrorsInFirstRow() {
		ErrorFinder ef = new ErrorFinder(gameBoard);
		GameBoard markedBoard = ef.getGameBoard();
		
		LinkedList<Field> row = markedBoard.getArea(0, 0, 8, 0);
		
		for (int i = 0; i < 9; i++) {
			assertTrue(row.get(i).isCorrect());
		}
	}
	
	@Test
	@DisplayName("The first column doesn't contain any errors")
	void noErrorsInFirstColumn() {
		ErrorFinder ef = new ErrorFinder(gameBoard);
		GameBoard markedBoard = ef.getGameBoard();
		
		LinkedList<Field> col = markedBoard.getArea(0, 0, 0, 8);
		
		for (int i = 0; i < 9; i++) {
			assertTrue(col.get(i).isCorrect());
		}
	}
	
	@Test
	@DisplayName("The top left 3x3 square doesn't contain any errors")
	void noErrorsInTopLeftSquare() {
		ErrorFinder ef = new ErrorFinder(gameBoard);
		GameBoard markedBoard = ef.getGameBoard();
		
		LinkedList<Field> square = markedBoard.getArea(0, 0, 2, 2);
		
		for (int i = 0; i < 9; i++) {
			assertTrue(square.get(i).isCorrect());
		}
	}
	
	@Test
	@DisplayName("The field at position (3|3) contains an error") 
	void fieldContainsError() {
		ErrorFinder ef = new ErrorFinder(gameBoard);
		GameBoard markedBoard = ef.getGameBoard();
		
		Field field = markedBoard.getField(3, 3);
		
		assertFalse(field.isCorrect());
	}
	
	@Test
	@DisplayName("The field at position (3|3) can't contain an error, when marked as non-editable")
	void fieldContainsNoErrorWhenMarkedAsNotEditable() {
		// this field does contain an error, as seen in the test above
		Field notEditable = gameBoard.getField(3, 3);
		notEditable.setEditable(false);
		
		gameBoard.saveField(notEditable);
		
		ErrorFinder ef = new ErrorFinder(gameBoard);
		GameBoard markedBoard = ef.getGameBoard();
		
		Field field = markedBoard.getField(3, 3);
		
		assertTrue(field.isCorrect());
	}
	
	@Test
	@DisplayName("This GameBoard definitely contains errors") 
	void isNotCorrect() {
		ErrorFinder ef = new ErrorFinder(gameBoard);
		
		assertEquals(38, ef.getAmountOfErrors());
		assertFalse(ef.isCorrect());
	}
}
