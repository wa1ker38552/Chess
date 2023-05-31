import java.util.HashMap;

public class NotationCalculator {
	private HashMap<String, String> notationKey = new HashMap<String, String>();
	private ChessEngine chessEngine;
	
	public NotationCalculator(ChessEngine chessEngine) {
		this.chessEngine = chessEngine;
		notationKey.put("BR", "r");
		notationKey.put("BB", "b");
		notationKey.put("BKn", "n");
		notationKey.put("BQ", "q");
		notationKey.put("BK", "k");
		notationKey.put("BP", "p");
		notationKey.put("WR", "R");
		notationKey.put("WB", "B");
		notationKey.put("WKn", "N");
		notationKey.put("WQ", "Q");
		notationKey.put("WK", "K");
		notationKey.put("WP", "P");
	}
	
	public String getFEN(ChessTile[][] board) {
		String notation = "";
		String turn;
		for (ChessTile[] row : board) {
			int i = 0;
			for (ChessTile tile : row) {
				if (tile.hasPiece()) {
					if (i != 0) {
						notation += i;
					}
					notation += this.notationKey.get(tile.getPiece().getPiece());
					i = 0;
				} else {
					i += 1;
				}
			}
			if (i != 0) {
				notation += i;
			}
			notation += "/";
		}
		
		// flip turn since turn hasn't been updated yet (it updates after the notation is calculated!)
		if (this.chessEngine.getTurn() == 0) {turn = "b";}
		else {turn = "w";}
		
		return notation.substring(0, notation.length()-1)+" "+turn;
	}
}
