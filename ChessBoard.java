import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	private ChessEngine chess;
	
	public ChessBoard(ChessEngine chess) {
		this.chess = chess;
		this.setBackground(Color.GRAY);
		this.setLayout(new GridLayout(8, 8));
		fillTiles();
	}
	
	public void fillTiles() {
		for (int row=0; row<8; row++) {
			for (int col=0; col<8; col++) {
				ChessTile tile = new ChessTile(new Pos(col, row), this.chess);
				chess.setTile(row, col, tile);
				this.add(tile);
			}
		}
	}
}
