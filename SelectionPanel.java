import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class SelectionPanel extends JPanel {
	private final String[] WP = {"WP", "WR", "WKn", "WB", "WQ"};
	private final String[] BP = {"BP", "BR", "BKn", "BB", "BQ"};
	private ChessEngine engine;
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	
	public SelectionPanel(ChessEngine chessEngine, ChessTile targetTile, int type) {
		this.setLayout(new GridLayout(1, 5));
		this.engine = chessEngine;
		
		String[] pieces;
		if (type == 0) {pieces = WP;}
		else {pieces = BP;}
		
		for (String piece : pieces) {
			JButton pieceButton = new JButton();
			Image icon = new ImageIcon("C:\\Users\\walke\\eclipse-workspace\\Semester 2 Project\\assets\\"+piece+".png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
			
			pieceButton.setIcon(new ImageIcon(icon));
			pieceButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			       promotePiece(pieceButton, piece, targetTile);
			    }
			});
			this.add(pieceButton);
			buttons.add(pieceButton);
		}
	}
	
	public SelectionPanel(ChessEngine chessEngine) {
		this.setLayout(new GridLayout(1, 5));
		this.engine = chessEngine;
				
		for (String piece : WP) {
			JButton pieceButton = new JButton();
			Image icon = new ImageIcon("C:\\Users\\walke\\eclipse-workspace\\Semester 2 Project\\assets\\"+piece+".png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
			
			pieceButton.setIcon(new ImageIcon(icon));
			pieceButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			       autoPromote(pieceButton, piece);
			    }
			});
			this.add(pieceButton);
			buttons.add(pieceButton);
		}
	}
	
	public void autoPromote(JButton button, String piece) {
		for (JButton b : buttons) {
			b.setBackground(null);
		}
		engine.setAutoPromotePiece(piece);
		button.setBackground(Color.GREEN);
	}
	
	public void promotePiece(JButton button, String piece, ChessTile targetTile) {
		for (JButton b : buttons) {
			b.setBackground(null);
		}
		engine.promotePiece(piece, targetTile);
		button.setBackground(Color.GREEN);
	}
}
