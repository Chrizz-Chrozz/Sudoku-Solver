package basic;

public class Position {
	private final int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Position) {
			if (((Position) o).getX() == this.x && ((Position) o).getY() == this.y) {
				return true;
			}
		}
		return false;
	}
}
