package basic;

import java.util.LinkedList;

public class GameBoard {
	/**
	 *
	 * (0|0) ------ X ------ (8|0)
	 *  	┌───┬───┬───┬─  ┐
	 *   |	│ 1 │ 2 │ 3 │...│
	 *   |	├───┼───┼───┼─  ┤
	 *   |	│ 4 │ 5 │ 6 │...│
	 *   Y	├───┼───┼───┼─  ┤
	 *   |	│ 7 │ 8 │ 9 │...│
	 *   |	├───┼───┼───┼─  ┤
	 * 	 |	 ... ... ... ...│
	 *  	└───┴───┴───┴───┘
	 * (0|8)                 (8|8)
	 */ 
	
	private Field[][] gameBoard;
	
	public GameBoard() {
		gameBoard = new Field[9][9];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				gameBoard[x][y] = new Field(new Position(x, y));
			}
		}
	}
	
	public GameBoard(int[][] numbers) {
		gameBoard = new Field[9][9];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				gameBoard[x][y] = new Field(new Position(x, y), numbers[x][y], (numbers[x][y] == 0));
			}
		}
	}
	
	public GameBoard(Field[][] fields) {
		gameBoard = fields;
	}
	
	public Field getField(int x, int y) {
		return gameBoard[x][y];
	}
	
	public void saveField(Field field) {
		gameBoard[field.getPos().getX()][field.getPos().getY()] = field;
	}
	
	public LinkedList<Field> getArea(int x1, int y1, int x2, int y2) {
		LinkedList<Field> requestedArea = new LinkedList<Field>();
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				requestedArea.add(getField(x, y));
			}
		}
		
		return requestedArea;
	}
	
	public void setArea(LinkedList<Field> area) {
		for (Field field : area) {
			gameBoard[field.getPos().getX()][field.getPos().getY()] = field;
		}
	}
	
	public int getAmountOfEmptyFields() {
		int counter = 0;
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				if (getField(x, y).getNumber() == 0) {
					counter++;
				}
			}
		}
		return counter;
	}
	
	public void clearAllEditableFields() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				Field field = getField(x, y);
				if (field.isEditable()) {
					field.setNumber(0);
				}
				saveField(field);
			}
		}
	}
	
	public void reset() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				Field field = getField(x, y);
				field.updatePossibleNumbersArray();
				field.updateCorrect(true);
				saveField(field);
			}
		}
	}
}
