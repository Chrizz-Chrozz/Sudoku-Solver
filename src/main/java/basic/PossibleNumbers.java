package basic;

import java.util.Arrays;
import java.util.LinkedList;

public class PossibleNumbers implements Cloneable {
	private boolean[] possibleNumbers;

	public PossibleNumbers() {
		possibleNumbers = new boolean[9];
	}
	
	public LinkedList<Integer> asList() {
		LinkedList<Integer> possibleNumbersList = new LinkedList<Integer>();
		for (int i = 0; i < possibleNumbers.length; i++) {
			if (possibleNumbers[i]) {
				possibleNumbersList.add(new Integer(i + 1));
			}
		}
		return possibleNumbersList;
	}
	
	public boolean getPossibleNumber(int n) {
		return possibleNumbers[n - 1];
	}
	
	public void setPossibleNumber(int n, boolean b) {
		possibleNumbers[n - 1] = b;
	}
	
	public void fill(boolean b) {
		Arrays.fill(possibleNumbers, b);
	}
	
	public int getAmount() {
		int counter = 0; 
		for (int i = 0; i < possibleNumbers.length; i++) {
			if (possibleNumbers[i]) counter++;
		}
		return counter;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PossibleNumbers) {
			for (int i = 1; i <= 9; i++) {
				if (((PossibleNumbers) o).getPossibleNumber(i) != possibleNumbers[i - 1]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public PossibleNumbers clone() throws CloneNotSupportedException {
		PossibleNumbers temp = (PossibleNumbers) super.clone();
		temp.possibleNumbers = possibleNumbers.clone();
		return temp;
	}
}
