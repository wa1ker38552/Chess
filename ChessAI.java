import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ChessAI {
	private ChessEngine engine;
	private HashMap<String, Integer> pointValues = new HashMap<String, Integer>();
	private NotationCalculator notationCalculator;
	
	public ChessAI(ChessEngine engine) {
		this.notationCalculator = new NotationCalculator(engine);
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
		if (this.engine.getFromStockfish()) {
			Stockfish stockfish = new Stockfish("assets\\stockfish-windows-2022-x86-64-avx2.exe");
			
			ChessTile[][] board = this.engine.getGameState();
			String notation = notationCalculator.getFEN(board).replace(" ", "%20");
			String move = stockfish.getBestMove(notation, 10);
			int startCol = "abcdefgh".indexOf(move.substring(0, 1));
			int startRow = 7-(Integer.parseInt(move.substring(1, 2))-1);
			int endCol = "abcdefgh".indexOf(move.substring(2, 3));
			int endRow = 7-(Integer.parseInt(move.substring(3, 4))-1);
			// System.out.println(startCol+", "+startRow+" "+endCol+", "+endRow);
			// System.out.println(move);
			
			return new Move(board[startRow][startCol], board[endRow][endCol]);
		}
		return calculateSelfBestMove(false);
	}
	
	public Move calculateSelfBestMove(boolean debug) {
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

		int average = 0;
		for (Move move : validMoves.keySet()) {
			average += validMoves.get(move);
		}
		
		if (average == 0) {
			if (debug) {System.out.print("Recalculated: ->");}
			for (Move move : validMoves.keySet()) {
				for (ChessTile validTile : possibleTargets) {
					if (validTile.hasPiece()) {
						// Iterate over valid tiles with white pieces on them in the presepctive of white
						Pos position = move.getTarget().getPosition().calculateDistance(validTile.getPosition());
						// Fake tile
						ChessTile fakeTile = new ChessTile(move.getTarget().getPosition(), engine);
						fakeTile.setPiece(move.getSelf().getPiece());
						if (engine.checkValidMove(position, validTile, fakeTile)) {
							validMoves.replace(move, -this.pointValues.get(move.getSelf().getPiece().getPiece()));
						}
					}
				}
			}
		}
		
		if (debug) {
			System.out.println(validMoves);
		}
	
		return getHighestScore(validMoves);
	}
}
