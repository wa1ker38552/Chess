import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Runner {
	public static void main(String[] args) {
		JFrame window = new JFrame("Chess");
		ChessEngine chess = new ChessEngine(window);
		ChessPanel content = new ChessPanel(chess);
		
		// create menubar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		JMenuItem quitButton = new JMenuItem("Quit"); 
		JMenuItem restartButton = new JMenuItem("Restart"); 
		JCheckBoxMenuItem playAIButton = new JCheckBoxMenuItem("Play with AI");
		JCheckBoxMenuItem highlightValidMoves = new JCheckBoxMenuItem("Show valid moves");
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
				chess.setPlayingAI(playAIButton.getState());
			}
		});
		highlightValidMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chess.setShowValidMoves(highlightValidMoves.getState());
			}
		});
		
		menu.add(quitButton);
		menu.add(restartButton);
		menu.add(playAIButton);
		menu.add(highlightValidMoves);
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
