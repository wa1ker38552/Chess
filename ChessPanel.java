import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class ChessPanel extends JPanel {
	private ChessBoard ChessBoard;
	private JPanel MenuPanel;
	
	public ChessPanel(ChessEngine chess) {
		ChessBoard = new ChessBoard(chess);
		MenuPanel = new MenuPanel();
		
		this.setLayout(new GridLayout(1, 1));
		this.add(ChessBoard);
		// this.add(MenuPanel);
	}
	
	public void resetGame() {
		this.ChessBoard.removeAll();
		this.ChessBoard.revalidate();
		this.ChessBoard.repaint();
		this.ChessBoard.fillTiles();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
