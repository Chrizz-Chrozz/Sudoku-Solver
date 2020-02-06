package logic;

import java.util.LinkedList;

import basic.Field;
import basic.GameBoard;

public class ErrorFinder {
	private GameBoard gameBoard; // local GameBoard

	public ErrorFinder(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		findErrors(gameBoard);
	}

	private void findErrors(GameBoard gameBoard) {
		for (int i = 0; i < 9; i++) {
			findErrorsInArea(gameBoard.getArea(0, i, 8, i)); // find errors in rows
			findErrorsInArea(gameBoard.getArea(i, 0, i, 8)); // find errors in columns
		}

		// find errors in 3x3 squares
		for (int j = 0; j < 3; j++) {
			for (int k = 0; k < 3; k++) {
				findErrorsInArea(gameBoard.getArea(j * 3, k * 3, (j + 1) * 3 - 1, (k + 1) * 3 - 1));
			}
		}
	}

	private void findErrorsInArea(LinkedList<Field> area) {
		int[] countOccurenceOfNumbersInArea = new int[9];

		for (int i = 0; i < area.size(); i++) {
			int numberOfCurrentField = area.get(i).getNumber();
			if (numberOfCurrentField != 0) {
				countOccurenceOfNumbersInArea[numberOfCurrentField - 1]++;
			}
		}

		markAllErrors(countOccurenceOfNumbersInArea, area);
	}

	private void markAllErrors(int[] countedOccurences, LinkedList<Field> area) {
		for (int i = 0; i < 9; i++) {
			if (countedOccurences[i] > 1) {
				// find and mark all fields containing this number
				for (int j = 0; j < area.size(); j++) {
					Field currentField = area.get(j);
					if (currentField.getNumber() == (i + 1) && currentField.isEditable()) { //non-editable fields are always correct
						currentField.updateCorrect(false);

						// save the marked field
						gameBoard.saveField(currentField);
					}
				}
			}
		}
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public int getAmountOfErrors() {
		int counter = 0;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (!gameBoard.getField(x, y).isCorrect()) {
					counter++;
				}
			}
		}
		return counter;
	}

	public boolean isCorrect() {
		return getAmountOfErrors() == 0;
	}
}
