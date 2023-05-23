import java.util.*;

public class ChessAI {
	private ChessEngine engine;
	private HashMap<String, Integer> pointValues = new HashMap<String, Integer>();
	
	public ChessAI(ChessEngine engine) {
		this.engine = engine;
		this.pointValues.put(null, 0);
		this.pointValues.put("WP", 10);
		this.pointValues.put("WKn", 30);
		this.pointValues.put("WB", 30);
		this.pointValues.put("WR", 50);
		this.pointValues.put("WQ", 90);
		this.pointValues.put("WK", 900);
		this.pointValues.put("BP", 10);
		this.pointValues.put("BKn", 30);
		this.pointValues.put("BB", 30);
		this.pointValues.put("BR", 50);
		this.pointValues.put("BQ", 90);
		this.pointValues.put("BK", 900);
	}
	
	public Move getHighestScore(HashMap<Move, Integer> values) {
		Move highestMove = null;
		int highestScore = Integer.MIN_VALUE;
		for (Move move : values.keySet()) {
			if (values.get(move) > highestScore) {
				highestScore = values.get(move);
				highestMove = move;
			}
		}
		return highestMove;
	}
	
	public Move getBestMove() {
		HashMap<Move, Integer> validMoves = new HashMap<Move, Integer>();
		ArrayList<ChessTile> selfPieces = new ArrayList<ChessTile>();
		ArrayList<ChessTile> possibleTargets = new ArrayList<ChessTile>();
		ChessTile[][] gameState = this.engine.getGameState();
		
		for (ChessTile[] row : gameState) {
			for (ChessTile tile : row) {
				if (tile.hasPiece() && tile.getPiece().getPiece().charAt(0) == 'B') {
					selfPieces.add(tile);
				} else {
					possibleTargets.add(tile);
				}
			}
		}
				
		// GET POSSIBLE MOVES
		for (ChessTile selfPiece : selfPieces) {
			for (ChessTile possibleTarget : possibleTargets) {
				Move move = new Move(selfPiece, possibleTarget);
				if (engine.checkValidMove(move.getPos(), move.getSelf(), move.getTarget())) {
					if (possibleTarget.hasPiece()) {
						validMoves.put(move, this.pointValues.get(possibleTarget.getPiece().getPiece()));
					} else {
						validMoves.put(move, this.pointValues.get(null));
					}
				}
			}
		}
		
		for (Move move : validMoves.keySet()) {
			for (ChessTile validTile : possibleTargets) {
				if (validTile.hasPiece()) {
					// Iterate over valid tiles with white pieces on them in the presepctive of white
					Pos position = move.getTarget().getPosition().calculateDistance(validTile.getPosition());
					// Fake tile
					ChessTile fakeTile = new ChessTile(move.getTarget().getPosition(), engine);
					fakeTile.setPiece(move.getSelf().getPiece());
					if (engine.checkValidMove(position, validTile, fakeTile)) {
						int pointValue = validMoves.get(move);
						pointValue -= this.pointValues.get(move.getSelf().getPiece().getPiece());
						validMoves.replace(move, pointValue);
					}
				}
			}
		}
		
		return getHighestScore(validMoves);
	}
}
