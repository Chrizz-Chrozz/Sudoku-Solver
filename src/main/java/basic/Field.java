package basic;

public class Field implements Cloneable {
	private Position pos;
	private int number;
	private boolean isCorrect, isEditable;

	private PossibleNumbers possibleNumbers;
	
	public Field(Position pos) {
		this.pos = pos;
		
		number = 0;
		isCorrect = true;
		isEditable = true;

		possibleNumbers = new PossibleNumbers();
		updatePossibleNumbers();
	}
	
	public Field(Position pos, int number, boolean isEditable) {
		this.pos = pos;
		
		this.number = number;
		isCorrect = true;
		this.isEditable = isEditable;
		
		possibleNumbers = new PossibleNumbers();
		updatePossibleNumbers();
	}
	
	public void updatePossibleNumbers() {
		if (number == 0) {
			possibleNumbers.fill(true); // all numbers can be entered into this field
		} else {
			possibleNumbers.fill(false);
			possibleNumbers.setPossibleNumber(number, true); // the number of this field is set
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
			updatePossibleNumbers();
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
	
	public PossibleNumbers getPossibleNumbers() {
		return possibleNumbers;
	}
	
	public void savePossibleNumbers(PossibleNumbers possibleNumbers) {
		this.possibleNumbers = possibleNumbers;
	}
	
	public Field clone() throws CloneNotSupportedException {
		Field temp = (Field) super.clone();
		temp.isCorrect = isCorrect;
		temp.isEditable = isEditable;
		temp.number = number;
		temp.pos = pos;
		temp.possibleNumbers = possibleNumbers.clone();
		return temp;
	}
}
