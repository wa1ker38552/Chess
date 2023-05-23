
public class Pos {
	private int x;
	private int y;
	
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "("+this.x+", "+this.y+")";
	}
	
	public Pos calculateDistance(Pos target) {
		return new Pos(this.x-target.getX(), target.getY()-this.y);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
