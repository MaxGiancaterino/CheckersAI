import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoardGraphics extends JPanel {
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = 800;
	public static final String BLANK_BOARD_FILE = "Board.png";
	public static final String PIECE1_FILE = "Piece1.png";
	public static final String PIECE2_FILE = "Piece2.png";
	public static final String PIECE3_FILE = "Piece3.png";
	public static final String PIECE4_FILE = "Piece4.png";
	public static final String SELECTION_FILE = "Selection.png";
	public static final int BOARD_SIZE = 800;
	public static final int PIECE_SIZE = 100;
	public static final int BOT_DEPTH = 7;
	public static final int NUM_THREADS = 8;

	private static BufferedImage piece1, piece2, piece3, piece4, blankBoard, selection;
	private Board board;

	private Coord first;
	private boolean firstMode;
	private boolean blacksTurn;
	private boolean blackBot;
	private boolean midTurn;
	private Set<Coord> legalMoves;
	private JLabel status;
	private ExecutorService executor;
	
	public BoardGraphics() {
		
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
		this.executor = executor;
		
		Board board = new Board();
		this.board = board;
		firstMode = true;
		blacksTurn = true;
		midTurn = false;
		legalMoves = new TreeSet<>();
		try {
			piece1 = ImageIO.read(new File(PIECE1_FILE));
			piece2 = ImageIO.read(new File(PIECE2_FILE));
			piece3 = ImageIO.read(new File(PIECE3_FILE));
			piece4 = ImageIO.read(new File(PIECE4_FILE));
			blankBoard = ImageIO.read(new File(BLANK_BOARD_FILE));
			selection = ImageIO.read(new File(SELECTION_FILE));

		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		setFocusable(true);

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = (e.getY() - 20) / 100;
				int y = e.getX() / 100;
				if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
				handleClick(x, y);
				}
			}
		});
	}
	
	public void reset() {
		board = new Board();
		firstMode = true;
		blacksTurn = true;
		midTurn = false;
		legalMoves = new TreeSet<>();
		first = null;
		this.remove(status);
	}
	
	public void handleClick(int x, int y) {
		if (firstMode) {
			if (checkValidFirst(new Coord(x, y))) {
				first = new Coord(x, y);
				firstMode = false;
			}
		} else {
			if (!midTurn) {
			legalMoves.clear();
			}
			Coord c = new Coord(x, y);
			boolean possibleCap = board.possibleCap(board.getPieceType(first));
			if (c.equals(first) && !midTurn) {
				firstMode = true;
				first = null;
			} else if (!midTurn && checkValidFirst(c)) {
				first = new Coord(x, y);
			} else if (board.attemptMove(first, c, midTurn)) {

				if (possibleCap) {
					if (board.getLegalCaptures(c.getX(), c.getY()).isEmpty()) {
						endTurn();
					} else {
						checkValidFirst(c);
						first = c;
						midTurn = true;
					}
				} else {
					endTurn();
				}
			} else if (!midTurn){
				firstMode = true;
				first = null;
			}
		}
		repaint();
	}

	public void endTurn() {
		blacksTurn = !blacksTurn;
		midTurn = false;
		firstMode = true;
		first = null;
		legalMoves.clear();
		board.crown();
		paintComponent(getGraphics());
		if (checkLost()) {
			if ((blacksTurn && blackBot) || (!blacksTurn && !blackBot)) {
				status.setText("You Won!");
			} else {
				status.setText("You Lost!");
			}
		} else if ((blacksTurn && blackBot) || (!blacksTurn && !blackBot)) {
			status.setText("Bot's Turn");
			status.setSize(new Dimension(300, 300));
			status.paintImmediately(0, 0, 100, 20);
			makeBotMove();
		} else {
			status.setText("Your Turn");
		}
	}

	public boolean checkLost() {
		int x;
		int y;
		if (blacksTurn) {
			x = 1;
			y = 2;
		} else {
			x = 3;
			y = 4;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Coord c = new Coord(i, j);
				if (board.getPieceType(c) == x || board.getPieceType(c) == y) {
					if (!board.getLegalCaptures(i, j).isEmpty() || !board.getLegalAdvances(i, j).isEmpty()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public void makeBotMove() {
		List<Coord> turn = TurnTree.getBotMove(board, blackBot, blacksTurn, BOT_DEPTH, executor);
		Coord c1 = null;
		for (Coord c : turn) {
			if (c1 == null) {
				c1 = c;
			} else {
				board.attemptMove(c1, c, midTurn);
				midTurn = true;
				c1 = c;
			}
		}
		endTurn();
	}

	public boolean checkValidFirst(Coord c) {
		int pieceType = board.getPieceType(c);
		if (((pieceType == 1 || pieceType == 2) && !blackBot && blacksTurn)
				|| ((pieceType == 3 || pieceType == 4) && blackBot && !blacksTurn)) {
			Set<Capture> captures = board.getLegalCaptures(c.getX(), c.getY());
			Set<Advance> advances = board.getLegalAdvances(c.getX(), c.getY());
			boolean possibleCap = board.possibleCap(board.getPieceType(c));
			if (!captures.isEmpty()) {
				legalMoves.add(c);
				for (Capture cap : captures) {
					legalMoves.add(cap.getEnd());
				}
				return true;
			}
			if (!possibleCap && !advances.isEmpty()) {
				legalMoves.add(c);
				for (Advance adv : advances) {
					legalMoves.add(adv.getEnd());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(blankBoard, 0, 20, null);
		for (Coord c : legalMoves) {
			g.drawImage(selection, c.getY() * 100, c.getX() * 100 + 20, PIECE_SIZE, PIECE_SIZE, null);
		}
		int[][] boardToDraw = board.getBoard();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int xCoord = j * 100;
				int yCoord = i * 100 + 20;
				switch (boardToDraw[i][j]) {
				case 1:
					g.drawImage(piece1, xCoord, yCoord, PIECE_SIZE, PIECE_SIZE, null);
					break;
				case 2:
					g.drawImage(piece2, xCoord, yCoord, PIECE_SIZE, PIECE_SIZE, null);
					break;
				case 3:
					g.drawImage(piece3, xCoord, yCoord, PIECE_SIZE, PIECE_SIZE, null);
					break;
				case 4:
					g.drawImage(piece4, xCoord, yCoord, PIECE_SIZE, PIECE_SIZE, null);
					break;
				}
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(BOARD_SIZE, BOARD_SIZE);
	}
	public void setBlackBot(boolean blackBot) {
		this.blackBot = blackBot;
		if (blackBot) {
			status = new JLabel("Bot's Turn");
			this.add(status);
			repaint();
		} else {
			status = new JLabel("Your Turn");
			this.add(status);
			repaint();
		}
	}
}
