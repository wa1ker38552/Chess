import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class SelectionPanel extends JPanel {
	private final String[] pieces = {"WP", "WR", "WKn", "WB", "WQ"};
	
	public SelectionPanel(ChessEngine chessEngine, ChessTile targetTile) {
		this.setLayout(new GridLayout(1, 5));
		
		for (String piece : this.pieces) {
			JButton pieceButton = new JButton();
			Image icon = new ImageIcon("C:\\Users\\walke\\eclipse-workspace\\Semester 2 Project\\assets\\"+piece+".png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
			
			pieceButton.setIcon(new ImageIcon(icon));
			pieceButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			       chessEngine.promotePiece(piece, targetTile);
			    }
			});
			this.add(pieceButton);
		}
	}
}
