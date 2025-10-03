package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.Arrays;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
       // public Collection<ChessMove> validMoves (validMoves copy) {
        //}
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);
        int startRow = startPosition.getRow();
        int endRow = endPosition.getRow();
        int startCol = startPosition.getColumn();
        int endCol = endPosition.getColumn();

        board.addPiece(startPosition, null);

        if (piece == null) {
            throw new InvalidMoveException();
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            //if (isInCheck(TeamColor.BLACK)) {
                //throw new InvalidMoveException("Piece is in check");
            //}
            if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == TeamColor.BLACK) {
                throw new InvalidMoveException();
            }

        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //if (isInCheck(TeamColor.WHITE)) {
                //throw new InvalidMoveException("Piece is in check");
            //}
            if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == TeamColor.WHITE) {
                throw new InvalidMoveException();
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (startRow != 2 && endRow - startRow == 2) {
                throw new InvalidMoveException();
            } else if (startRow != 7 && endRow - startRow == 7) {
                throw new InvalidMoveException();
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            if(endRow - startRow > 7) {
                throw new InvalidMoveException();
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if(endRow - startRow > 7) {
                throw new InvalidMoveException();
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            if(endRow - startRow > 7) {
                throw new InvalidMoveException();
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (endRow - startRow > 1) {
                throw new InvalidMoveException();
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            if (endRow - startRow > 3) {
                throw new InvalidMoveException();
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (endRow - startRow > 2) {
                throw new InvalidMoveException();
            }
        }

        if (move.getPromotionPiece() != null) {
            board.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        } else {
            board.addPiece(endPosition, piece);
        }
        
        if (teamTurn == TeamColor.WHITE) {
            TeamColor currentColor = TeamColor.WHITE;
            if (piece.getTeamColor() != currentColor) {
                throw new InvalidMoveException();
            }
            teamTurn = TeamColor.BLACK;
        } else if (teamTurn == TeamColor.BLACK){
            TeamColor currentColor = TeamColor.BLACK;
            if (piece.getTeamColor() != currentColor) {
                throw new InvalidMoveException();
            }
            teamTurn = TeamColor.WHITE;
        }





        //check about jumping enemy
        //check if captured piece
        //check if move out of turn
        //check about diagonal capture
        //check about move through piece
        //check for invalid move

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
