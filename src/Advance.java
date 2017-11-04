
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Advance other = (Advance) obj;
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} else if (!end.equals(other.end)) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} else if (!start.equals(other.start)) {
			return false;
		}
		return true;
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
