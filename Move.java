
public class Move {
	private ChessTile self;
	private ChessTile target;
	private Pos difference;
	
	public Move(ChessTile self, ChessTile target) {
		this.self  = self;
		this.target = target;
		this.difference = target.getPosition().calculateDistance(self.getPosition());
	}
	
	public String toString() {
		if (this.target.hasPiece()) {
			return this.self+" -> "+this.target+" "+this.difference;
		} else {
			return this.self+" -> "+this.target.getPosition()+" "+this.difference;
		}
	}
	
	public ChessTile getSelf() {
		return this.self;
	}
	
	public ChessTile getTarget() {
		return this.target;
	}
	
	public Pos getPos() {
		return this.difference;
	}
}
