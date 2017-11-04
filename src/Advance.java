
public class Advance implements Comparable<Advance> {
	
	private Coord start, end;
	
	public Advance(int startX, int startY, int endX, int endY) {
		start = new Coord(startX, startY);
		end = new Coord(endX, endY);
	}
	public Advance(Coord start, Coord end) {
		this.start = start;
		this.end = end;
	}
	
	public Coord getStart() {
		return start;
	}
	public Coord getEnd() {
		return end;
	}
	@Override
	public int compareTo(Advance that) {
		int startVal = this.start.compareTo(that.start) * 100;
		int endVal = this.end.compareTo(that.end);
		return startVal + endVal;
	}
	public String toString() {
		return start.toString() + "\n" + end.toString();
	}
}
