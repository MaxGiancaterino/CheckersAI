
public class Capture implements Comparable <Capture> {
	
	private Coord start, end, cap;
	
	public Capture(int startX, int startY, int endX, int endY) {
		start = new Coord(startX, startY);
		end = new Coord(endX, endY);
		int capX = (startX + endX) / 2;
		int capY = (startY + endY) / 2;
		cap = new Coord(capX, capY);
	}
	public Capture(Coord start, Coord end) {
		this.start = start;
		this.end = end;
		int startX = start.getX();
		int startY = start.getY();
		int endX = end.getX();
		int endY = end.getY();
		int capX = (startX + endX) / 2;
		int capY = (startY + endY) / 2;
		cap = new Coord(capX, capY);
	}
	public Coord getStart() {
		return start;
	}
	public Coord getEnd() {
		return end;
	}
	public Coord getCap() {
		return cap;
	}
	public static boolean validCap(Coord start, Coord end) {
		int startX = start.getX();
		int startY = start.getY();
		int endX = end.getX();
		int endY = end.getY();
		int dx = Math.abs(startX - endX);
		int dy = Math.abs(startY - endY);
		return dx == 2 && dy == 2;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Capture)) {
			return false;
		}
		Capture cap = (Capture) o;
		
		return this.start.equals(cap.start) && this.end.equals(cap.end);
	}
	@Override
	public int compareTo(Capture that) {
		int startVal = this.start.compareTo(that.start) * 10000;
		int endVal = this.end.compareTo(that.end) * 100;
		int capVal = this.cap.compareTo(that.cap);
		return startVal + endVal + capVal;
	}
}
