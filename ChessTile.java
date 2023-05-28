import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


@SuppressWarnings("serial")
public class ChessTile extends JButton {
	private Pos position;
	private ChessEngine chess;
	private Color tileColor;
	private Piece piece;
	
	public ChessTile(Pos position, ChessEngine chess) {
		this.chess = chess;
		this.position = position;
		
		int row = position.getY();
		int col = position.getX();

		if (row%2 == 0) {
			if (col%2 == 0) {
				this.tileColor = Color.WHITE;
				this.setBackground(Color.WHITE);
			} else {
				this.tileColor = Color.GRAY;
				this.setBackground(Color.GRAY);
			}
		} else {
			if (col%2 == 0) {
				this.tileColor = Color.GRAY;
				this.setBackground(Color.GRAY);
			} else {
				this.tileColor = Color.WHITE;
				this.setBackground(Color.WHITE);
			}
		}
		
		if (row == 1) {
			this.setPiece(new Piece("BP"));
		} else if (row == 6) {
			this.setPiece(new Piece("WP"));
		} else if (row == 0) {
			if (col == 0 || col == 7) {
				this.setPiece(new Piece("BR"));
			} else if (col == 1 || col == 6) {
				this.setPiece(new Piece("BKn"));
			} else if (col == 2 || col == 5) {
				this.setPiece(new Piece("BB"));
			} else if (col == 3) {
				this.setPiece(new Piece("BQ"));
			} else {
				this.setPiece(new Piece("BK"));
			}
		} else if (row == 7) {
			if (col == 0 || col == 7) {
				this.setPiece(new Piece("WR"));
			} else if (col == 1 || col == 6) {
				this.setPiece(new Piece("WKn"));
			} else if (col == 2 || col == 5) {
				this.setPiece(new Piece("WB"));
			} else if (col == 3) {
				this.setPiece(new Piece("WQ"));
			} else {
				this.setPiece(new Piece("WK"));
			}
		}
		
		ChessTile reference = this;
		this.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		       chess.setCurrentSelected(reference); 
		    }
		});
	}
	
	public String toString() {
		if (this.hasPiece()) {
			if (!this.piece.getPiece().contains("Kn")) {
				return this.piece.getPiece()+" ";
			}
			return this.piece.getPiece();
		} else {
			return null;
		}
	}
	
	public void setPiece(Piece piece) {
		if (piece == null) {
			this.piece = null;
			this.setIcon(null);
		} else {
			this.piece = piece;
			this.setIcon(this.piece.getIcon());
		}
	}
	
	public Color getTileColor() {
		return this.tileColor;
	}
	
	public boolean hasPiece() {
		return (this.piece != null);
	}
	
	public Piece getPiece() {
		return this.piece;
	}
	
	public Pos getPosition() {
		return this.position;
	}
	
	public void setPosition(int x, int y) {
		this.position.setX(x);
		this.position.setY(y);
	}
}
