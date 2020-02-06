package exceptions;

public class UnsolvableException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public UnsolvableException() {
		super("This Sudoku cannot be solved!");
	}
}
