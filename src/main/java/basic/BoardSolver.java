package basic;

import java.util.LinkedList;

public class BoardSolver {
	private GameBoard gameBoard;
	
	public BoardSolver(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		calculatePossibleNumbers();
	}
	
	public GameBoard getGameBoard() {
		return gameBoard;
	}
	
	public boolean isTheBoardSolved() {
		if (gameBoard.getAmountOfEmptyFields() == 0) {
			ErrorFinder ef = new ErrorFinder(gameBoard);
			return ef.isCorrect();
		}
		return false;
	}
	
	private void calculatePossibleNumbers() {
		for (int i = 0; i < 9; i++) {
			LinkedList<Field> line = gameBoard.getArea(0, i, 8, i);
			calculatePossibleNumbersForArea(line);
			LinkedList<Field> column = gameBoard.getArea(i, 0, i, 8);
			calculatePossibleNumbersForArea(column);
		}
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				LinkedList<Field> square = gameBoard.getArea(x * 3, y * 3, x * 3 + 2, y * 3 + 2);
				calculatePossibleNumbersForArea(square);
			}
		}
	}

	private void calculatePossibleNumbersForArea(LinkedList<Field> area) {
		for (int i = 0; i < 9; i++) {
			Field field = area.get(i);
			int number = field.getNumber();
			if (number != 0) {
				for (int j = 0; j < 9; j++) {
					Field currentField = area.get(j);
					if (currentField.getNumber() == 0) {
						currentField.setPossibleNumber(number, false);
						gameBoard.saveField(currentField);
					}
				}
			}
		}
	}
	
	public void solve() {
		
	}
}
