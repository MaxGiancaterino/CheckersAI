
public class Coord implements Comparable<Coord> {

	private final int x;
	private final int y;
	
	public Coord(int x, int y) {
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
	public int compareTo(Coord that) {
		int thisValue = this.x * 10 + this.y;
		int thatValue = that.x * 10 + that.y;
		return thisValue - thatValue;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Coord)) {
			return false;
		}
		Coord that = (Coord) o;
		return this.compareTo(that) == 0;
	}
	
	public String toString() {
		return x + " " + y;
	}
}
