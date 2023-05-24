import java.util.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class ChessEngine {
	ChessTile[][] board = new ChessTile[8][8];
	private ChessTile currentlySelected;
	private int turn;
	private JFrame chessFrame;
	private ChessAI ai;
	private boolean playAI = false;
	private boolean showValidMoves = true;
	private String autoPromotePiece;
	private boolean autoPromote;
	
	public ChessEngine(JFrame frame) {
		this.turn = 0;
		this.chessFrame = frame;
		this.ai = new ChessAI(this);
	}
	
	public void setAutoPromote(boolean condition) {
		this.autoPromote = condition;
	}
	
	public String getAutoPromotePiece() {
		return this.autoPromotePiece;
	}
	
	public void setAutoPromotePiece(String piece) {
		this.autoPromotePiece = piece;
	}
	
	public void setShowValidMoves(boolean condition) {
		this.showValidMoves = condition;
	}
	
	public void setPlayingAI(boolean condition) {
		this.playAI = condition;
	}
	
	public void setTile(int row, int col, ChessTile tile) {
		board[row][col] = tile;
	}
	
	public ChessTile[][] getGameState() {
		return this.board;
	}
	
	public void promotePiece(String piece, ChessTile tile) {
		tile.setPiece(new Piece(piece));
	}
	
	public void resetTileColors() {
		for (ChessTile[] row : this.board) {
			for (ChessTile currentTile : row) {
				currentTile.setBackground(currentTile.getTileColor());
			}
		}
	}
	
	public void highlightValidTiles(ChessTile tile) {
		if (this.showValidMoves) {
			ArrayList<ChessTile> validTiles = new ArrayList<ChessTile>();
			for (ChessTile[] row : this.board) {
				for (ChessTile currentTile : row) {
					if (currentTile.hasPiece()) {
						if (currentTile.getPiece().getPiece().charAt(0) != tile.getPiece().getPiece().charAt(0)) {
							validTiles.add(currentTile);
						}
					} else {
						validTiles.add(currentTile);
					}
				}
			}
			for (ChessTile valid : validTiles) {
				if (this.checkValidMove(valid.getPosition().calculateDistance(tile.getPosition()), tile, valid)) {
					if (valid.hasPiece()) {
						valid.setBackground(Color.RED);
					} else {
						valid.setBackground(Color.GREEN);
					}
				}
			}
		}
	}
	
	public void setCurrentSelected(ChessTile tile) {
		this.resetTileColors();
		if (currentlySelected == null) {
			if (tile.hasPiece()) {
				if (turn%2 == 0 && tile.getPiece().getPiece().charAt(0) == 'W') {
					currentlySelected = tile;
					currentlySelected.setBackground(Color.BLUE);
					this.highlightValidTiles(tile);
				} else if (turn%2 != 0 && tile.getPiece().getPiece().charAt(0) == 'B') {
					currentlySelected = tile;
					currentlySelected.setBackground(Color.BLUE);
					this.highlightValidTiles(tile);
				}
			}
		} else {
			// Already selected a previous valid tile
			currentlySelected.setBackground(currentlySelected.getTileColor());
			if (currentlySelected == tile) {
				// Selected self (unselect)
				currentlySelected = null;
			} else {
				Pos originalPosition = currentlySelected.getPosition();
				Pos newPosition = tile.getPosition();
				Pos difference = newPosition.calculateDistance(originalPosition);
				Piece targetPiece = currentlySelected.getPiece();
				
				if (checkValidMove(difference, currentlySelected, tile)) {	
					// Valid move, swap pieces
					currentlySelected.setPiece(null);
					
					if (targetPiece.getPiece().charAt(1) == 'P') {
						targetPiece.setMoved(true);
					}
					
					if (tile.hasPiece()) {
						// Reason while tile set so many times is so it doesn't set before it checks
						if (tile.getPiece().getPiece().equals("WK")) {
							tile.setPiece(targetPiece);
							JOptionPane.showMessageDialog(null, "BLACK WINS!", "Game", JOptionPane.INFORMATION_MESSAGE);
							this.chessFrame.dispatchEvent(new WindowEvent(this.chessFrame, WindowEvent.WINDOW_CLOSING));
						} else if (tile.getPiece().getPiece().equals("BK")) {
							tile.setPiece(targetPiece);
							JOptionPane.showMessageDialog(null, "WHITE WINS!", "Game", JOptionPane.INFORMATION_MESSAGE);
							this.chessFrame.dispatchEvent(new WindowEvent(this.chessFrame, WindowEvent.WINDOW_CLOSING));
						}
					}
					if (newPosition.getY() == 0 && targetPiece.getPiece() == "WP") {
						if (this.autoPromote) {
							tile.setPiece(new Piece(this.autoPromotePiece));
						} else {
							tile.setPiece(new Piece("WP"));
							SelectionPanel selectionPanel = new SelectionPanel(this, tile, 0);
							JOptionPane.showMessageDialog(null, selectionPanel, "Piece Promotion", JOptionPane.PLAIN_MESSAGE);
						}
					} else if (newPosition.getY() == 7 && targetPiece.getPiece() == "BP") {
						if (this.playAI) {
							tile.setPiece(new Piece("BQ"));
						} else {
							tile.setPiece(new Piece("BP"));
							SelectionPanel selectionPanel = new SelectionPanel(this, tile, 1);
							JOptionPane.showMessageDialog(null, selectionPanel, "Piece Promotion", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						tile.setPiece(targetPiece);
					}
					
					currentlySelected = null;
					turn += 1;
					
					if (this.playAI && turn%2 != 0) {
						// Some recursive ai functions? 
						Move aiMove = this.ai.getBestMove();
						this.setCurrentSelected(aiMove.getSelf());
						this.setCurrentSelected(aiMove.getTarget());
					}
					
				} else {
					if (tile.hasPiece()) {
						if (currentlySelected.getPiece().getPiece().charAt(0) == tile.getPiece().getPiece().charAt(0)) {
							currentlySelected = tile;
							this.highlightValidTiles(tile);
						}
					}
					currentlySelected.setBackground(Color.BLUE);
				}
			}
		}
	}
	
	// Some very repetitive neighbor check functions to check if theres a piece in the current path
	public boolean checkNeighboringUp(ChessTile self, ChessTile target) {
		int i = self.getPosition().getY()-1;
		while (i > target.getPosition().getY()) {
			if (board[i][self.getPosition().getX()].hasPiece()) {
				return false;
			}
			i --;
		}
		return true;
	}
	
	public boolean checkNeighboringDown(ChessTile self, ChessTile target) {
		int i = self.getPosition().getY()+1;
		while (i < target.getPosition().getY()) {
			if (board[i][self.getPosition().getX()].hasPiece()) {
				return false;
			}
			i ++;
		}
		return true;
	}
	
	public boolean checkNeighboringRight(ChessTile self, ChessTile target) {
		int i = self.getPosition().getX()+1;
		while (i < target.getPosition().getX()) {
			if (board[self.getPosition().getY()][i].hasPiece()) {
				return false;
			}
			i ++;
		}
		return true;
	}
	
	public boolean checkNeighboringLeft(ChessTile self, ChessTile target) {
		int i = self.getPosition().getX()-1;
		while (i > target.getPosition().getX()) {
			if (board[self.getPosition().getY()][i].hasPiece()) {
				return false;
			}
			i --;
		}
		return true;
	}
	
	public boolean checkDiagonalBottomLeft(ChessTile self, ChessTile target) {
		int x = self.getPosition().getX()-1;
		int y = self.getPosition().getY()+1;
		while (y < target.getPosition().getY()) {
			if (board[y][x].hasPiece()) {
				return false;
			}
			x --;
			y ++;
		}
		return true;
	}
	
	public boolean checkDiagonalBottomRight(ChessTile self, ChessTile target) {
		int x = self.getPosition().getX()+1;
		int y = self.getPosition().getY()+1;
		while (y < target.getPosition().getY()) {
			if (board[y][x].hasPiece()) {
				return false;
			}
			x ++;
			y ++;
		}
		return true;
	}
	
	public boolean checkDiagonalTopLeft(ChessTile self, ChessTile target) {
		int x = self.getPosition().getX()-1;
		int y = self.getPosition().getY()-1;
		while (y > target.getPosition().getY()) {
			if (board[y][x].hasPiece()) {
				return false;
			}
			x --;
			y --;
		}
		return true;
	}
	
	public boolean checkDiagonalTopRight(ChessTile self, ChessTile target) {
		int x = self.getPosition().getX()+1;
		int y = self.getPosition().getY()-1;
		while (y > target.getPosition().getY()) {
			if (board[y][x].hasPiece()) {
				return false;
			}
			x ++;
			y --;
		}
		return true;
	}
	
	public boolean checkValidMove(Pos pos, ChessTile self, ChessTile target) {
		// pos is just coordinate difference between the two squares, self and target
		String selfPiece = self.getPiece().getPiece();
		String targetPiece;
		
		if (target.getPiece() == null) {
			targetPiece = " ";
		} else {
			targetPiece = target.getPiece().getPiece();
		}
		
		if (selfPiece.charAt(0) == targetPiece.charAt(0)) {
			// Eating it's own troop
			return false;
		}
		
		if (board[target.getPosition().getY()][target.getPosition().getX()].hasPiece() && selfPiece.charAt(1) == 'P') {
			// Check if there's an opponent piece in the way for PAWN
			// Allow diagonal opponents to be taken B)
			if (selfPiece.equals("BP")) {
				if (pos.getY() == -1 && Math.abs(pos.getX()) == 1) {
					return true;
				}
			} else {
				if (pos.getY() == 1 && Math.abs(pos.getX()) == 1) {
					return true;
				}
			}
			return false;
		}
		
		if (selfPiece.equals("WP")) {
			// WHITE PAWN
			if ((pos.getY() < 2 && pos.getY() >= 1) && pos.getX() == 0) {
				return true;
			} else {
				if ((pos.getY() < 3 && pos.getY() >= 1) && pos.getX() == 0 && (self.getPiece().hasMoved() == false)) {
					if (checkNeighboringUp(self, target)) {
						return true;
					} else {
						return false;
					}
				} else {return false;}
			}
		} else if (selfPiece.equals("BP")) {
			// BLACK PAWN
			// Slightly different code because of how the black pawn is oriented
			if ((pos.getY() > -2 && pos.getY() < 0) && pos.getX() == 0) {
				return true;
			} else {
				if ((pos.getY() > -3 && pos.getY() < 0) && pos.getX() == 0 && (self.getPiece().hasMoved() == false)) {
					if (checkNeighboringDown(self, target)) {	
						return true;
					} else {
						return false;
					}
				} else {return false;}
			}
		} else if (selfPiece.equals("WR") || selfPiece.equals("BR")) {
			// WHITE ROOK
			if (pos.getY() == 0 || pos.getX() == 0) {
				if (pos.getY() == 0) {
					// Horiziontal movement
					if (pos.getX() > 0) {
						// Move right
						return checkNeighboringRight(self, target);
					} else {
						return checkNeighboringLeft(self, target);
					}
				} else {
					// Vertical movement
					if (pos.getY() > 0) {
						// Move up
						return checkNeighboringUp(self, target);
					} else {
						// Move down
						return checkNeighboringDown(self, target);
					}
				}
			} else {
				return false;
			}
		} else if (selfPiece.equals("WKn") || selfPiece.equals("BKn")) {
			// WHITE KNIGHT
			// hard code checks for knight :)
			if (Math.abs(pos.getY()) == 2 && Math.abs(pos.getX()) == 1) {
				return true;
			} else if (Math.abs(pos.getY()) == 1 && Math.abs(pos.getX()) == 2) {
				return true;
			} else {
				return false;
			}
		} else if (selfPiece.equals("WB") || selfPiece.equals("BB")) {
			// WHITE BISHOP
			// :\
			if (-pos.getX() == pos.getY()) {}
			else if (pos.getX() == pos.getY()) {} 
			else {
				return false;
			}
			
			if (pos.getX() < 0) {
				if (pos.getY() < 0) {
					// DOWN LEFT
					return checkDiagonalBottomLeft(self, target);
				} else {
					return checkDiagonalTopLeft(self, target);
				}
			} else {
				if (pos.getY() < 0) {
					// DOWN RIGHT
					return checkDiagonalBottomRight(self, target);
				} else {
					// UP RIGHT
					return checkDiagonalTopRight(self, target);
				}
			}
		} else if (selfPiece.equals("WQ") || selfPiece.equals("BQ")) {
			// WHITE QUEEN
			// Reuse a mixture of Rook and Bishop
			if (pos.getX() == 0 || pos.getY() == 0) {
				if (pos.getY() == 0) {
					// Horiziontal movement
					if (pos.getX() > 0) {
						// Move right
						return checkNeighboringRight(self, target);
					} else {
						return checkNeighboringLeft(self, target);
					}
				} else {
					// Vertical movement
					if (pos.getY() > 0) {
						// Move up
						return checkNeighboringUp(self, target);
					} else {
						// Move down
						return checkNeighboringDown(self, target);
					}
				}
			} else if (-pos.getX() == pos.getY()) {}
			else if (pos.getX() == pos.getY()) {} 
			else {
				return false;
			}
			
			if (pos.getX() < 0) {
				if (pos.getY() < 0) {
					// DOWN LEFT
					return checkDiagonalBottomLeft(self, target);
				} else {
					return checkDiagonalTopLeft(self, target);
				}
			} else {
				if (pos.getY() < 0) {
					// DOWN RIGHT
					return checkDiagonalBottomRight(self, target);
				} else {
					// UP RIGHT
					return checkDiagonalTopRight(self, target);
				}
			}
		} else if (selfPiece.equals("WK") || selfPiece.equals("BK")) {
			// WHITE KNIGHT
			if (Math.abs(pos.getX()) == 1 && Math.abs(pos.getY()) == 1) {
				return true;
			} else if (pos.getX() == 0 && Math.abs(pos.getY()) == 1) {
				return true;
			} else if (pos.getY() == 0 && Math.abs(pos.getX()) == 1) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
}
