import java.awt.Image;

import javax.swing.ImageIcon;

public class Piece {
	String piece;
	Boolean hasMoved = false;
	ImageIcon pieceIcon;
	
	public Piece(String piece) {
		Image icon = new ImageIcon("C:\\Users\\walke\\eclipse-workspace\\Semester 2 Project\\assets\\"+piece+".png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
		this.piece = piece;
		this.pieceIcon = new ImageIcon(icon);
	}
	
	public String toString() {
		return this.piece;
	}
	
	public String getPiece() {
		return this.piece;
	}
	
	public ImageIcon getIcon() {
		return this.pieceIcon;
	}
	
	public boolean hasMoved() {
		return this.hasMoved;
	}
	
	public void setMoved(boolean condition) {
		this.hasMoved = condition;
	}
}
