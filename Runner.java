import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Runner {
	public static void main(String[] args) {
		JFrame window = new JFrame("Chess");
		ChessEngine chess = new ChessEngine(window);
		ChessPanel content = new ChessPanel(chess);
		
		chess.setChessPanel(content);
		window.setIconImage(new ImageIcon("assets\\chess icon.jpg").getImage());
		
		// create menubar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		JMenu debug = new JMenu("Playing AI: False; Difficulty: None");
		JMenuItem quitButton = new JMenuItem("Quit"); 
		JMenuItem restartButton = new JMenuItem("Restart"); 
		JCheckBoxMenuItem playAIButton = new JCheckBoxMenuItem("Play with AI");
		JCheckBoxMenuItem highlightValidMoves = new JCheckBoxMenuItem("Show valid moves");
		JCheckBoxMenuItem autoPromoteButton = new JCheckBoxMenuItem("Auto promote");
		highlightValidMoves.setState(true);
		
		quitButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); 
		    }
		});
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				content.resetGame();
			}
		});
		playAIButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playAIButton.getState()) {
					AIPanel aiOptions = new AIPanel();
					JOptionPane.showMessageDialog(null, aiOptions, "Game", JOptionPane.INFORMATION_MESSAGE);
					
					if (aiOptions.getEasyButton().getBackground() == Color.GREEN) {
						chess.setFromStockfish(false);
						debug.setText("Playing AI: True; Difficulty: Easy");
					} else {
						chess.setFromStockfish(true);
						debug.setText("Playing AI: True; Difficulty: Hard");
					}
				} else {
					debug.setText("Playing AI: False; Difficulty: None");
				}
				chess.setPlayingAI(playAIButton.getState());
			}
		});
		highlightValidMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chess.setShowValidMoves(highlightValidMoves.getState());
			}
		});
		autoPromoteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chess.setAutoPromote(autoPromoteButton.getState());
				if (autoPromoteButton.getState()) {
					SelectionPanel selectionPanel = new SelectionPanel(chess);
					JOptionPane.showMessageDialog(null, selectionPanel, "Auto Piece Promotion", JOptionPane.PLAIN_MESSAGE);
					if (chess.getAutoPromotePiece() == null) {
						chess.setAutoPromote(false);
						autoPromoteButton.setState(false);
					}
				} else {
					chess.setAutoPromotePiece(null);
				}
			}
		});
		
		debug.setForeground(Color.RED);
		
		menu.add(quitButton);
		menu.add(restartButton);
		menu.add(playAIButton);
		menu.add(highlightValidMoves);
		menu.add(autoPromoteButton);
		menuBar.add(menu);
		menuBar.add(debug);
		
		window.setJMenuBar(menuBar);
		window.setContentPane(content);
		window.setSize(500, 500);
        window.setLocation(100, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
        content.requestFocusInWindow();
	}
}
