/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	
    public void run() {
    	
        final JFrame frame = new JFrame("Checkers+");
        BoardGraphics game = new BoardGraphics();
        frame.setLocation(0, 0);
        
        GridBagConstraints c = new GridBagConstraints();
        
        final JPanel play_panel = new JPanel(new GridBagLayout());
        final JPanel choose_panel = new JPanel(new GridBagLayout());       
        final JPanel rules_panel = new JPanel(new GridBagLayout());

        
        final JButton play = new JButton("Play");
        play.setPreferredSize(new Dimension(200, 80));
        play.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton rules = new JButton("Rules");
        rules.setPreferredSize(new Dimension(200, 80));
        rules.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton quit = new JButton("Quit");
        quit.setPreferredSize(new Dimension(200, 80));
        quit.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton rulesBack = new JButton("<==");
        rulesBack.setPreferredSize(new Dimension(120, 40));
        rulesBack.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton chooseBack = new JButton("<==");
        chooseBack.setPreferredSize(new Dimension(120, 40));
        chooseBack.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton black = new JButton("Black");
        black.setPreferredSize(new Dimension(200, 80));
        black.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton red = new JButton("Red");
        red.setPreferredSize(new Dimension(200, 80));
        red.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        final JButton boardBack = new JButton("<==");
        chooseBack.setPreferredSize(new Dimension(120, 40));
        chooseBack.setFont(new Font("Arial" , Font.PLAIN, 40));
        
        play.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(play_panel);
        		frame.add(choose_panel);
        		frame.setVisible(true);
        		frame.repaint();
        	}
        });
        
        rules.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(play_panel);
        		frame.add(rules_panel);
        		frame.setVisible(true);
        		frame.repaint();
        	}
        });
        
        quit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        
        black.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(choose_panel);
        		frame.add(game);
        		game.setBlackBot(false);
        		frame.add(boardBack, BorderLayout.SOUTH);
        		frame.setVisible(true);
        		frame.repaint();
        	}
        });

        red.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(choose_panel);
        		frame.add(game);
        		game.setBlackBot(true);
        		frame.add(boardBack, BorderLayout.SOUTH);
        		frame.setVisible(true);
        		game.paintComponent(game.getGraphics());
        		game.makeBotMove();
        		frame.repaint();
        	}
        });
        
        chooseBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(choose_panel);
        		frame.add(play_panel);
        		frame.setVisible(true);
        		frame.repaint();
        	}
        });
        
        rulesBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(rules_panel);
        		frame.add(play_panel);
        		frame.setVisible(true);
        		frame.repaint();
        	}
        });
        
        boardBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.remove(game);
        		frame.remove(boardBack);
        		frame.add(play_panel);
        		game.reset();
        		frame.setVisible(true);
        		frame.repaint();
        	}
        });
        
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        play_panel.add(play, c);
        c.gridy = 1;
        play_panel.add(rules, c);
        c.gridy = 2;
        play_panel.add(quit, c);
        
        frame.add(play_panel);
        
        //Choose Panel
        choose_panel.add(black, c);
        c.gridx = 2;
        choose_panel.add(red, c);
        c.gridx = 0;
        c.gridy = 3;
        choose_panel.add(chooseBack, c);
        
        //Rules panel
        JTextArea rulesText = new JTextArea("Standard Checker's Rules:\n\n- Black goes first\n\n- "
        		                            + "Players alternate turns"
        									+ "\n\n- One can move one piece per turn\n\n- One can "
        									+ "advance or capture\n\n- A piece advances by moving "
        									+ "one space diagonally forward\n\n- A piece captures by "
        									+ "jumping over an opponent's piece one\ndiagonal space"
        									+ " forward and landing on the other side\n\n- One can"
        									+ " chain as many captures as possible in one turn\n\n-"
        									+ "If one can capture a piece during a turn, one must"
        									+ " do it\n\n- If a piece reaches the other side of the"
        									+ " board, it becomes\na king and can advance and "
        									+ "capture in both directions\n\n- One makes a move by"
        									+ " clicking a valid piece and then\nclicking a valid"
        									+ " destination\n\n- A player loses when they cannot"
        									+ " make a move\n\n- You will be playing against a bot"
        									+ "\n\n- Good luck!");
  
        rulesText.setSize(new Dimension(700, 700));
        rulesText.setFont(new Font("Arial" , Font.PLAIN, 20));
        rulesText.setLineWrap(true);
        rules_panel.setBackground(Color.WHITE);
        
        rules_panel.add(rulesText, c);
        c.gridy = 4;
        rules_panel.add(rulesBack, c);
        
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(820, 900);

    }
    

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}