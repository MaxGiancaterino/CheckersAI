import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Node implements Comparable<Node> {

	private List<Coord> turn;
	private int value;
	private Node parent = null;
	private Set<Node> children = null;
	private int[][] arrBoard;
	private boolean blacksTurn;
	private boolean lost;

	public Node() {
		this.children = new TreeSet<>();
	}

	public void makeRoot(Board board, boolean blacksTurn) {
		this.arrBoard = board.getBoard();
		this.blacksTurn = blacksTurn;
		this.turn = null;
	}

	public void addChild(List<Coord> turn, int[][] board, boolean lost) {
		Node child = new Node();
		child.turn = turn;
		child.parent = this;
		child.arrBoard = board;
		child.blacksTurn = !child.parent.blacksTurn;
		child.lost = lost;
		if (lost) {
			child.value = child.parent.value;
		}
		this.children.add(child);
	}

	public void getNextCaps(List<Coord> turn, Board board, int i, int j) {
		Set<Capture> captures = board.getLegalCaptures(i, j);
		for (Capture cap : captures) {
			Board copyBoard = new Board(board.getBoard());
			List<Coord> copyTurn = new LinkedList<>();
			for (Coord c : turn) {
				copyTurn.add(c);
			}
			copyTurn.add(cap.getEnd());
			copyBoard.attemptMove(cap.getStart(), cap.getEnd(), true);
			getNextCaps(copyTurn, copyBoard, cap.getEnd().getX(), cap.getEnd().getY());
		}
		if (captures.size() == 0) {
			board.crown();
			this.addChild(turn, board.getBoard(), false);
		}
	}

	public void getChildren() {
		if (lost) {
			this.addChild(new LinkedList<>(), arrBoard, true);
		} else {
			// System.out.println("MOVE ATTEMPT");
			lost = true;
			Board board = new Board(arrBoard);
			// board.printBoard();
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					int pieceType = arrBoard[i][j];
					if (blacksTurn && (pieceType == 3 || pieceType == 4 || pieceType == 0)) {
						continue;
					}
					if (!blacksTurn && (pieceType == 1 || pieceType == 2 || pieceType == 0)) {
						continue;
					}
					Set<Advance> advances = board.getLegalAdvances(i, j);
					if (!(board.possibleCap(board.getPieceType(new Coord(i, j))))) {
						for (Advance adv : advances) {
							Board tempBoard = new Board(board.getBoard());
							List<Coord> turn = new LinkedList<>();
							turn.add(adv.getStart());
							turn.add(adv.getEnd());
							tempBoard.attemptMove(adv.getStart(), adv.getEnd(), false);
							tempBoard.crown();
							this.addChild(turn, tempBoard.getBoard(), false);
						}
					}
					Set<Capture> captures = board.getLegalCaptures(i, j);
					for (Capture cap : captures) {
						Board tempBoard = new Board(board.getBoard());
						List<Coord> turn = new LinkedList<>();
						Coord start = cap.getStart();
						Coord end = cap.getEnd();
						turn.add(start);
						turn.add(end);
						tempBoard.attemptMove(start, end, false);
						getNextCaps(turn, tempBoard, end.getX(), end.getY());
					}
					if (!advances.isEmpty() || !captures.isEmpty()) {
						lost = false;
					}
				}
			}
			if (lost) {
				if (blacksTurn) {
					this.value = -12;
				} else {
					this.value = 12;
				}
				this.addChild(new LinkedList<>(), arrBoard, true);
			}
		}
	}

	@Override
	public int compareTo(Node that) {
		if (this.turn == null || this.turn.isEmpty()) {
			return 1;
		}
		String thisString = "";
		for (Coord c : this.turn) {
			thisString = thisString + c.getX() + c.getY();
		}
		String thatString = "";
		for (Coord c : that.turn) {
			thatString = thatString + c.getX() + c.getY();
		}
		try {
			int value = Integer.parseInt(thisString) - Integer.parseInt(thatString);
			return value;
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	public void evaluate() {
		if (!lost) {
			int blackValue = 0;
			int redValue = 0;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (arrBoard[i][j] == 1 || arrBoard[i][j] == 2) {
						blackValue++;
					}
					if (arrBoard[i][j] == 3 || arrBoard[i][j] == 4) {
						redValue++;
					}
				}
			}
			// System.out.println("VALUE: " + (blackValue - redValue));
			this.value = blackValue - redValue;
		}
	}

	public static int evaluateNode(Node t, boolean blackBot) {
		Set<Node> children = t.children;
		int[] values = new int[children.size()];
		int index = 0;
		for (Node child : children) {
			if (child.lost || child.children.isEmpty()) {
				values[index] = child.getValue();
			} else {
				values[index] = evaluateNode(child, blackBot);
				child.value = values[index];
			}
			index++;
		}
		if (blackBot) {
			if (t.blacksTurn) {
				return max(values);
			} else {
				return min(values);
			}
		} else {
			if (!t.blacksTurn) {
				return min(values);
			} else {
				return max(values);
			}
		}
	}

	public static int max(int[] values) {
		int max = values[0];
		for (int value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	public static int min(int[] values) {
		int min = values[0];
		for (int value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	public void printBranch() {

	}

	public Set<Node> returnChildren() {
		return children;
	}

	public int getValue() {
		return value;
	}

	public List<Coord> getTurn() {
		return turn;
	}

}