package basic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Field {
	private Position pos;
	private int number;
	private boolean isCorrect, isEditable;
	private boolean[] possibleNumbers;
	
	public Field(Position pos) {
		this.pos = pos;
		
		number = 0;
		isCorrect = true;
		isEditable = true;

		possibleNumbers = new boolean[9];
		updatePossibleNumbersArray();
	}
	
	public Field(Position pos, int number, boolean isEditable) {
		this.pos = pos;
		
		this.number = number;
		isCorrect = true;
		this.isEditable = isEditable;
		
		possibleNumbers = new boolean[9];
		updatePossibleNumbersArray();
	}
	
	public void updatePossibleNumbersArray() {
		if (number == 0) {
			Arrays.fill(possibleNumbers, true); // all numbers can be entered into this field
		} else {
			Arrays.fill(possibleNumbers, false);
			possibleNumbers[number - 1] = true; // the number of this field is set
		}
	}
	
	public Position getPos() {
		return pos;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int nNumber) {
		if (isEditable) {
			number = nNumber;
			updatePossibleNumbersArray();
		}
	}
	
	public boolean isCorrect() {
		return isCorrect;
	}
	
	public void updateCorrect(boolean nCorrect) {
		if (isEditable) {
			isCorrect = nCorrect;
		}
	}
	
	public boolean isEditable() {
		return isEditable;
	}
	
	public void setEditable(boolean nEditable) {
		if (number != 0) {
			isEditable = nEditable;
		}
	}
	
	public boolean isEmpty() {
		return (number == 0 && isEditable);
	}
	
	public void setPossibleNumber(int number, boolean b) { // changes state of specified number
		possibleNumbers[number - 1] = b;
	}
	
	public boolean getPossibleNumber(int number) {
		return possibleNumbers[number - 1];
	}
	
	public List<Integer> getRemainingPossibleNumbers() {
		List<Integer> remainingPossibleNumbers = new LinkedList<Integer>();
		for (int i = 0; i < possibleNumbers.length; i++) {
			if (possibleNumbers[i] == true) {
				remainingPossibleNumbers.add(Integer.valueOf(i + 1));
			}
		}
		return remainingPossibleNumbers;
	}
}
