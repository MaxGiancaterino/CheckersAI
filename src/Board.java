
import java.util.HashSet;
import java.util.Set;

public class Board {

	/**
	 * 2D array to model board state: 0 - empty space 1 - black uncrowned piece
	 * 2 - black crowned piece 3 - red uncrowned piece 4 - red crowned piece
	 */
	private int[][] board = {{0, 3, 0, 3, 0, 3, 0 ,3}, 
			 				 {3, 0, 3, 0, 3, 0, 3, 0}, 
			 				 {0, 3, 0, 3, 0, 3, 0, 3}, 
			 				 {0, 0, 0, 0, 0, 0, 0, 0}, 
			 				 {0, 0, 0, 0, 0, 0, 0, 0}, 
			 				 {1, 0, 1, 0, 1, 0, 1, 0}, 
			 				 {0, 1, 0, 1, 0, 1, 0, 1}, 
			 				 {1, 0, 1, 0, 1, 0, 1, 0}};
	
	public Board(int[][] board) {
		this.board = board;
	}

	public Board() {
	}

	public Set<Advance> getLegalAdvances(int x, int y) {
		Set<Advance> legalAdvances = new HashSet<>();
		int pieceType = board[x][y];
		if (pieceType == 1 || pieceType == 2 || pieceType == 4) {
			if (x != 0 && y != 0) {
				int topLeft = board[x - 1][y - 1];
				if (topLeft == 0) {
					legalAdvances.add(new Advance(x, y, x - 1, y - 1));
				}
			}
			if (x != 0 && y != 7) {
				int topRight = board[x - 1][y + 1];
				if (topRight == 0) {
					legalAdvances.add(new Advance(x, y, x - 1, y + 1));
				}
			}
		}
		if (pieceType == 3 || pieceType == 4 || pieceType == 2) {
			if (x != 7 && y != 0) {
				int botLeft = board[x + 1][y - 1];
				if (botLeft == 0) {
					legalAdvances.add(new Advance(x, y, x + 1, y - 1));
				}

			}
			if (x != 7 && y != 7) {
				int botRight = board[x + 1][y + 1];
				if (botRight == 0) {
					legalAdvances.add(new Advance(x, y, x + 1, y + 1));
				}
			}
		}
		return legalAdvances;
	}

	public Set<Capture> getLegalCaptures(int x, int y) {
		Set<Capture> legalCaptures = new HashSet<>();
		int pieceType = board[x][y];
		if (pieceType == 1 || pieceType == 2 || pieceType == 4) {
			if (x >= 2 && y >= 2) {
				int topLeft = board[x - 1][y - 1];
				int topLeftCap = board[x - 2][y - 2];
				if (pieceType != 4 && topLeftCap == 0 && (topLeft == 3 || topLeft == 4)) {
					legalCaptures.add(new Capture(x, y, x - 2, y - 2));
				}
				if (pieceType == 4 && topLeftCap == 0 && (topLeft == 1 || topLeft == 2)) {
					legalCaptures.add(new Capture(x, y, x - 2, y - 2));
				}
			}
			if (x >= 2 && y <= 5) {
				int topRight = board[x - 1][y + 1];
				int topRightCap = board[x - 2][y + 2];
				if (pieceType != 4 && topRightCap == 0 && (topRight == 3 || topRight == 4)) {
					legalCaptures.add(new Capture(x, y, x - 2, y + 2));
				}
				if (pieceType == 4 && topRightCap == 0 && (topRight == 1 || topRight == 2)) {
					legalCaptures.add(new Capture(x, y, x - 2, y + 2));
				}
			}
		}
		if (pieceType == 3 || pieceType == 4 || pieceType == 2) {
			if (x <= 5 && y >= 2) {
				int botLeft = board[x + 1][y - 1];
				int botLeftCap = board[x + 2][y - 2];
				if (pieceType != 2 && botLeftCap == 0 && (botLeft == 1 || botLeft == 2)) {
					legalCaptures.add(new Capture(x, y, x + 2, y - 2));
				}
				if (pieceType == 2 && botLeftCap == 0 && (botLeft == 3 || botLeft == 4)) {
					legalCaptures.add(new Capture(x, y, x + 2, y - 2));
				}
			}
			if (x <= 5 && y <= 5) {
				int botRight = board[x + 1][y + 1];
				int botRightCap = board[x + 2][y + 2];
				if (pieceType != 2 && botRightCap == 0 && (botRight == 1 || botRight == 2)) {
					legalCaptures.add(new Capture(x, y, x + 2, y + 2));
				}
				if (pieceType == 2 && botRightCap == 0 && (botRight == 3 || botRight == 4)) {
					legalCaptures.add(new Capture(x, y, x + 2, y + 2));
				}
			}
		}
		return legalCaptures;
	}

	public boolean attemptMove(Coord start, Coord end, boolean midTurn) {
		Set<Capture> legalCaptures = getLegalCaptures(start.getX(), start.getY());
		if (Capture.validCap(start, end) && legalCaptures.contains(new Capture(start, end))) {
			capture(start, end);
			return true;
		}
		Set<Advance> legalAdvances = getLegalAdvances(start.getX(), start.getY());
		if (!possibleCap(getPieceType(start)) && !midTurn && legalAdvances.contains(new Advance(start, end))) {
			advance(start, end);
			return true;
		}
		return false;
	}

	public boolean possibleCap(int pieceType) {
		int x;
		int y;
		if (pieceType == 1 || pieceType == 2) {
			x = 1;
			y = 2;
		} else if (pieceType == 3 || pieceType == 4) {
			x = 3;
			y = 4;
		} else {
			return false;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == x || board[i][j] == y) {
					if (getLegalCaptures(i, j).size() != 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void capture(Coord start, Coord end) {
		Capture cap = new Capture(start, end);
		putAtCoord(getPieceType(start), end);
		putAtCoord(0, start);
		putAtCoord(0, cap.getCap());
	}

	public void advance(Coord start, Coord end) {
		putAtCoord(getPieceType(start), end);
		putAtCoord(0, start);
	}

	public int getPieceType(Coord c) {
		int x = c.getX();
		int y = c.getY();
		return board[x][y];
	}

	private void putAtCoord(int i, Coord c) {
		int x = c.getX();
		int y = c.getY();
		board[x][y] = i;	
	}

	public void printBoard() {
		for (int[] rows : board) {
			for (int i : rows) {
				System.out.print(i + " ");

			}
			System.out.println("");
		}
	}

	public int[][] getBoard() {
		int[][] boardCopy = new int[8][8];
		for (int i = 0; i < 8; i++) {
			System.arraycopy(board[i], 0, boardCopy[i], 0, 8);
		}
		return boardCopy;
	}
	public void crown() {
		for (int i = 0; i < 8; i++) {
			if (board[0][i] == 1) {
				board[0][i] = 2;
			}
			if (board[7][i] == 3) {
				board[7][i] = 4;
			}
		}
	}
}
