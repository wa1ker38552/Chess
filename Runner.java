import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Runner {
	public static void main(String[] args) {
		JFrame window = new JFrame("Chess");
		ChessEngine chess = new ChessEngine(window);
		ChessPanel content = new ChessPanel(chess);
		
		window.setIconImage(new ImageIcon("C:\\Users\\walke\\eclipse-workspace\\Semester 2 Project\\assets\\chess icon.jpg").getImage());
		
		// create menubar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
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
					JOptionPane.showMessageDialog(null, "You may need to play one more turn as black until the AI starts playing.", "Game", JOptionPane.INFORMATION_MESSAGE);
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
		
		menu.add(quitButton);
		menu.add(restartButton);
		menu.add(playAIButton);
		menu.add(highlightValidMoves);
		menu.add(autoPromoteButton);
		menuBar.add(menu);
		
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
