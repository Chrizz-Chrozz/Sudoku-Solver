package basic;

import java.util.LinkedList;

public class GameBoard implements Cloneable {
	
	private Field[][] gameBoard;
	private boolean isSolved;
	
	/**
	 * Default Constructor
	 * 
	 * An empty GameBoard is initialized.
	 */
	public GameBoard() {
		gameBoard = new Field[9][9];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				gameBoard[x][y] = new Field(new Position(x, y));
			}
		}
		
		isSolved = false;
	}
	
	/**
	 * Creates a GameBoard containing the specified numbers.
	 * @param numbers A 2D-Array containing the numbers
	 */
	public GameBoard(int[][] numbers) {
		gameBoard = new Field[9][9];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				gameBoard[x][y] = new Field(new Position(x, y), numbers[x][y], (numbers[x][y] == 0));
			}
		}
		
		isSolved = false;
	}
	
	public GameBoard(Field[][] fields) {
		gameBoard = fields;
		
		isSolved = false;
	}

	/**
	 * Returns the requested Field
	 * @param x The x-Position of the requested Field
	 * @param y The y-Position of the requested Field
	 * @return The requested Field
	 */
	public Field getField(int x, int y) {
		return gameBoard[x][y];
	}
	
	/**
	 * Saves the Field at the correct position
	 * @param field The Field to be saved.
	 */
	public void saveField(Field field) {
		gameBoard[field.getPos().getX()][field.getPos().getY()] = field;
	}
	
	/**
	 * Returns a LinkedList containing the Fields of the requested Area
	 * @param x1 The x-Position of the upper-left corner 
	 * @param y1 The y-Position of the upper-left corner
	 * @param x2 The x-Position of the lower-right corner
	 * @param y2 The y-Position of the lower-right corner
	 * @return The LinkedList containing all of the Fields inside the area
	 */
	public LinkedList<Field> getArea(int x1, int y1, int x2, int y2) {
		LinkedList<Field> requestedArea = new LinkedList<Field>();
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				requestedArea.add(getField(x, y));
			}
		}
		
		return requestedArea;
	}
	
	/**
	 * Saves a LinkedList containing Fields
	 * @param area The LinkedList containing the Fields
	 */
	public void setArea(LinkedList<Field> area) {
		for (Field field : area) {
			gameBoard[field.getPos().getX()][field.getPos().getY()] = field;
		}
	}
	
	/**
	 * Counts the number of empty Fields
	 * @return The number of empty Fields
	 */
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
	
	public int getAmountOfSolvableFields() {
		int counter = 0;
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				Field field = getField(x, y);
				if (field.isEditable() && field.getPossibleNumbers().getAmount() == 1) {
					counter++;
				}
			}
		}
		return counter;
	}
	
	/**
	 * Resets the numbers of all Fields which are editable
	 */
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
	
	/**
	 * Resets the PossibleNumbers for each Field. Each Field is marked as correct.
	 */
	public void resetPossibleNumbers() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				Field field = getField(x, y);
				field.updatePossibleNumbers();
				field.updateCorrect(true);
				saveField(field);
			}
		}
	}
	
	public GameBoard clone() throws CloneNotSupportedException {
		GameBoard temp = (GameBoard) super.clone();
		temp.gameBoard = new Field[9][9];
		temp.isSolved = isSolved;
		
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				Field field = gameBoard[x][y];
				temp.gameBoard[x][y] = field.clone();
			}
		}
		
		return temp;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
}
