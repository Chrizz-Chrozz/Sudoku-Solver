package logic;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

import basic.*;
import exceptions.UnsolvableException;

public class BoardSolver {
	private GameBoard gameBoard;
	public final static int STANDARD_PROCEDURE = 0;
	public final static int ONLY_CALCULATE_POSSIBLE_NUMBERS = 1;

	public BoardSolver(GameBoard gameBoard, int procedure) {
		this.gameBoard = gameBoard;
		calculatePossibleNumbers();

		if (procedure == 0) {
			// AOSF stands for Amount Of Solvable Fields
			int AOSF_BEFORE;
			int AOSF_AFTER;

			do {
				AOSF_BEFORE = gameBoard.getAmountOfSolvableFields();
				eliminateDoubles();
				findFieldsWhereOnlyOneNumberIsPossible();
				AOSF_AFTER = gameBoard.getAmountOfSolvableFields();
			} while(AOSF_AFTER > AOSF_BEFORE);
		}
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
						PossibleNumbers pn = currentField.getPossibleNumbers();
						pn.setPossibleNumber(number, false);
						currentField.savePossibleNumbers(pn);
						gameBoard.saveField(currentField);
					}
				}
			}
		}
	}

	private void eliminateDoubles() {
		for (int i = 0; i < 9; i++) {
			LinkedList<Field> line = gameBoard.getArea(0, i, 8, i);
			eliminateDoublesForArea(line);
			LinkedList<Field> column = gameBoard.getArea(i, 0, i, 8);
			eliminateDoublesForArea(column);
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				LinkedList<Field> square = gameBoard.getArea(x * 3, y * 3, x * 3 + 2, y * 3 + 2);
				eliminateDoublesForArea(square);
			}
		}
	}

	private void eliminateDoublesForArea(LinkedList<Field> area) {
		for (Field f : area) {
			int counter = 1;
			PossibleNumbers numbersOfField = f.getPossibleNumbers();
			for (int i = 0; i < area.size(); i++) {
				Field currentField = area.get(i);
				if (!f.getPos().equals(currentField.getPos())) {
					if (currentField.getPossibleNumbers().equals(numbersOfField)) {
						counter++;
					}
				}
			}
			if (numbersOfField.getAmount() == counter && counter > 1) {
				for (int j = 0; j < area.size(); j++) {
					PossibleNumbers pn = area.get(j).getPossibleNumbers();
					if (!pn.equals(numbersOfField)) {
						LinkedList<Integer> numbersAsList = numbersOfField.asList();
						for (Integer i : numbersAsList) {
							pn.setPossibleNumber(i.intValue(), false);
						} 
					}
					area.get(j).savePossibleNumbers(pn);
				}
			}
		}
	}

	private void findFieldsWhereOnlyOneNumberIsPossible() {
		for (int i = 0; i < 9; i++) {
			LinkedList<Field> line = gameBoard.getArea(0, i, 8, i);
			findFieldInAreaWhereOnlyOneNumberIsPossible(line);
			LinkedList<Field> column = gameBoard.getArea(i, 0, i, 8);
			findFieldInAreaWhereOnlyOneNumberIsPossible(column);
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				LinkedList<Field> square = gameBoard.getArea(x * 3, y * 3, x * 3 + 2, y * 3 + 2);
				findFieldInAreaWhereOnlyOneNumberIsPossible(square);
			}
		}
	}

	private void findFieldInAreaWhereOnlyOneNumberIsPossible(LinkedList<Field> area) {
		for (int i = 1; i <= 9; i++) {
			int counter = 0;
			for (Field f : area) {
				if (f.getNumber() == 0 && f.getPossibleNumbers().getPossibleNumber(i)) {
					counter++;
				}
			}
			if (counter == 1) {
				// the number i can only be set for one field
				for (Field f : area) {
					if (f.getNumber() == 0 && f.getPossibleNumbers().getPossibleNumber(i)) {
						PossibleNumbers pn = f.getPossibleNumbers();
						pn.fill(false);
						pn.setPossibleNumber(i, true);
						f.savePossibleNumbers(pn);
					}
				}
			}
		}
	}

	public void solve() throws UnsolvableException {
		int AOEF_BEFORE, AOEF_AFTER;
		do {
			AOEF_BEFORE = gameBoard.getAmountOfEmptyFields();

			calculatePossibleNumbers();
			eliminateDoubles();
			findFieldsWhereOnlyOneNumberIsPossible();

			// get the whole GameBoard
			for (Field f : gameBoard.getArea(0, 0, 8, 8)) {
				if (f.isEditable() && f.getNumber() == 0) {
					PossibleNumbers pn = f.getPossibleNumbers();
					// if this field is editable and there is only one number possible, 
					// set the number of the field to the only one possible.
					if (pn.getAmount() == 1) {
						int onlyPossibleNumber = pn.asList().peek().intValue();
						f.setNumber(onlyPossibleNumber);
					}
				}
			}

			if (!isThisBoardSolvable()) {
				throw new UnsolvableException();
			}

			AOEF_AFTER = gameBoard.getAmountOfEmptyFields();
		} while (AOEF_AFTER < AOEF_BEFORE);
	}
	
	public void solveWithRecursion() throws UnsolvableException {
		GameBoard temp = null;
		try {
			temp = solveRecursive((GameBoard) gameBoard.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (temp != null) {
			gameBoard = temp;
		}
	}

	/**
	 * Solves the GameBoard using the algorithms implemented above
	 * @throws CloneNotSupportedException 
	 * @throws UnsolvableException 
	 */
	public GameBoard solveRecursive(GameBoard gb) 
			throws CloneNotSupportedException, UnsolvableException {
		
		Random random = new Random();
		long newSeed = random.nextLong();
		random.setSeed(newSeed);
		System.err.println("Solving recursive. Random: " + newSeed);
		
		BoardSolver bs = new BoardSolver(gb, BoardSolver.STANDARD_PROCEDURE);
		if (bs.isTheBoardSolved()) {
			gb.setSolved(true);
			return gb;
		} else {
			bs.solve();
			if (bs.isTheBoardSolved()) {
				GameBoard ret =  bs.getGameBoard();
				ret.setSolved(true);
				return ret;
			} else if (!bs.isThisBoardSolvable())  {
				throw new UnsolvableException();
			} else {
				LinkedList<Field> emptyFields = gb.getArea(0, 0, 8, 8).stream().filter(f -> f.isEmpty()).collect(Collectors.toCollection(LinkedList<Field>::new));
				for (Field f : emptyFields) {
					LinkedList<Integer> possibleNumbers = f.getPossibleNumbers().asList();
					for (int i = possibleNumbers.size(); i > 0; i--) {
						Integer randomInteger = possibleNumbers.remove(random.nextInt(i));
						
						f.setNumber(randomInteger.intValue());
						gb.saveField(f);
						GameBoard temp = null;
						try {
							temp = solveRecursive((GameBoard) gb.clone());
						} catch (UnsolvableException e) {
							
						}
						if (temp != null) {
							if (temp.isSolved()) return temp;
						}
					}
					f.setNumber(0);
					gb.saveField(f);
				}
			}
		}
		// since the board couldn't be solved, throw a new Unsolvable Exception
		throw new UnsolvableException();
	}


	public boolean isThisBoardSolvable() {
		LinkedList<Field> allFields = gameBoard.getArea(0, 0, 8, 8);

		long amountOfUnsolvableFields = allFields.stream()
				.filter(f -> f.isEmpty())
				.filter(f -> f.getPossibleNumbers().getAmount() == 0)
				.count();

		return (amountOfUnsolvableFields == 0);
	}
}
