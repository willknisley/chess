package chess;

import java.util.ArrayList;
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
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);

        Collection<ChessMove> copyMoves = piece.pieceMoves(board, startPosition);

        for (ChessMove move : copyMoves) {
            ChessBoard Copy = new ChessBoard(board);

            Copy.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() != null) {
                Copy.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            } else {
                Copy.addPiece(move.getEndPosition(), piece);
            }

            ChessBoard ogBoard = this.board;
            this.board = Copy;

            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }

            this.board = ogBoard;
        }




        return validMoves;
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
        ChessPiece pieceGoal = board.getPiece(endPosition);

        board.addPiece(startPosition, null);

        if (piece == null) {
            throw new InvalidMoveException();
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (isInCheck(TeamColor.BLACK)) {
                //board.addPiece(startPosition, piece);
                //board.addPiece(endPosition, pieceGoal);
                throw new InvalidMoveException("Piece is in check");
            }
            if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == TeamColor.BLACK) {
                throw new InvalidMoveException();
            }

        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (isInCheck(TeamColor.WHITE)) {
                //board.addPiece(startPosition, piece);
                //board.addPiece(endPosition, pieceGoal);
                throw new InvalidMoveException("Piece is in check");
            }
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

            int rowWalk = startRow;
            int colWalk = startCol;

                if (endRow - startRow == 0) { //horizontal
                    if (colWalk > endCol) {
                        colWalk = startCol - 1;
                        while (colWalk > endCol) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            colWalk--;
                        }
                    } else {
                        colWalk = startCol + 1;
                        while (colWalk < endCol) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            colWalk++;
                        }
                    }
                } else if (endCol - startCol == 0) { //vertical
                    if (rowWalk > endRow) {
                        rowWalk = startRow - 1;
                        while (rowWalk > endRow) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk--;
                        }
                    } else {
                        rowWalk = startRow + 1;
                        while (rowWalk < endRow) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk++;
                        }
                    }
                } else if ((endRow - startRow) * (endRow - startRow) == (endCol - startCol) * (endCol - startCol)) { //diagonal
                    if (endRow > startRow && endCol > startCol) {
                        rowWalk++; colWalk++;
                        while (rowWalk < endRow ) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk++; colWalk++;
                        }
                    } else if (endRow > startRow && endCol < startCol) {
                        rowWalk++; colWalk--;
                        while (rowWalk < endRow ) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk++; colWalk--;
                        }
                    } else if (endRow < startRow && endCol > startCol) {
                        rowWalk--; colWalk++;
                        while (rowWalk > endRow ) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk--; colWalk++;
                        }
                    } else if (endRow < startRow && endCol < startCol) {
                        rowWalk--; colWalk--;
                        while (rowWalk > endRow ) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk--; colWalk--;
                        }
                    }
                }

        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if(endRow - startRow > 7) {
                throw new InvalidMoveException();
            }

            int rowWalk = startRow;
            int colWalk = startCol;

            if (endRow - startRow == 0) { //horizontal
                if (colWalk > endCol) {
                    colWalk = startCol - 1;
                    while (colWalk > endCol) {
                        if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                            throw new InvalidMoveException();
                        }
                        colWalk--;
                    }
                } else {
                    colWalk = startCol + 1;
                    while (colWalk < endCol) {
                        if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                            throw new InvalidMoveException();
                        }
                        colWalk++;
                    }
                }
            } else if (endCol - startCol == 0) { //vertical
                if (rowWalk > endRow) {
                    rowWalk = startRow - 1;
                    while (rowWalk > endRow) {
                        if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                            throw new InvalidMoveException();
                        }
                        rowWalk--;
                    }
                } else {
                    rowWalk = startRow + 1;
                    while (rowWalk < endRow) {
                        if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                            throw new InvalidMoveException();
                        }
                        rowWalk++;
                    }
                }
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                if (endRow - startRow > 7) {
                    throw new InvalidMoveException();
                }

                if ((endRow - startRow) * (endRow - startRow) == (endCol - startCol) * (endCol - startCol)) { //diagonal
                    if (endRow > startRow && endCol > startCol) {
                        rowWalk++;
                        colWalk++;
                        while (rowWalk < endRow) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk++;
                            colWalk++;
                        }
                    } else if (endRow > startRow && endCol < startCol) {
                        rowWalk++;
                        colWalk--;
                        while (rowWalk < endRow) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk++;
                            colWalk--;
                        }
                    } else if (endRow < startRow && endCol > startCol) {
                        rowWalk--;
                        colWalk++;
                        while (rowWalk > endRow) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk--;
                            colWalk++;
                        }
                    } else if (endRow < startRow && endCol < startCol) {
                        rowWalk--;
                        colWalk--;
                        while (rowWalk > endRow) {
                            if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                throw new InvalidMoveException();
                            }
                            rowWalk--;
                            colWalk--;
                        }
                    }
                }
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

            if (piece.getTeamColor() == TeamColor.WHITE) {
                if (startRow + 1 == endRow && startCol + 1 == endCol) {
                    if (pieceGoal == null ) {
                        throw new InvalidMoveException();
                    } else if (pieceGoal.getTeamColor() == piece.getTeamColor()) {
                        throw new InvalidMoveException();
                    }
                } else if (startRow + 1 == endRow && startCol - 1 == endCol) {
                    if (pieceGoal == null) {
                        throw new InvalidMoveException();
                    } else if (pieceGoal.getTeamColor() == piece.getTeamColor()) {
                        throw new InvalidMoveException();
                    }
                }
            } else if (piece.getTeamColor() == TeamColor.BLACK) {
                if (startRow - 1 == endRow && startCol + 1 == endCol) {
                    if (pieceGoal == null ) {
                        throw new InvalidMoveException();
                    } else if (pieceGoal.getTeamColor() == piece.getTeamColor()) {
                        throw new InvalidMoveException();
                    }
                } else if (startRow - 1 == endRow && startCol - 1 == endCol) {
                    if (pieceGoal == null) {
                        throw new InvalidMoveException();
                    } else if (pieceGoal.getTeamColor() == piece.getTeamColor()) {
                        throw new InvalidMoveException();
                    }
                }
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
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = new ChessPosition(i, j);
                    break;
                }
            }
            if (kingPosition!= null) break;
        }
        if (kingPosition == null) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    ChessPosition position = new ChessPosition(i, j);
                    int rowWalk = position.getRow();
                    int colWalk = position.getColumn();
                    int startRow = position.getRow();
                    int endRow = kingPosition.getRow();
                    int startCol = position.getColumn();
                    int endCol = kingPosition.getColumn();

                    if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        if ((endRow - startRow) * (endRow - startRow) == (endCol - startCol) * (endCol - startCol)) {
                            if (endRow > startRow && endCol > startCol) {
                                rowWalk++;
                                colWalk++;
                                while (rowWalk <= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk++;
                                    colWalk++;
                                }
                            } else if (endRow > startRow && endCol < startCol) {
                                rowWalk++;
                                colWalk--;
                                while (rowWalk <= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }

                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk++;
                                    colWalk--;
                                }
                            } else if (endRow < startRow && endCol > startCol) {
                                rowWalk--;
                                colWalk++;
                                while (rowWalk >= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk--;
                                    colWalk++;
                                }
                            } else if (endRow < startRow && endCol < startCol) {
                                rowWalk--;
                                colWalk--;
                                while (rowWalk >= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk--;
                                    colWalk--;
                                }
                            } else if (endRow - startRow == 0) {
                            if (colWalk > endCol) {
                                colWalk = startCol - 1;
                                while (colWalk >= endCol) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    colWalk--;
                                }
                            } else {
                                colWalk = startCol + 1;
                                while (colWalk <= endCol) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    colWalk++;
                                }
                            }
                        }
                        } else if (endCol - startCol == 0) {
                        if (rowWalk > endRow) {
                            rowWalk = startRow - 1;
                            while (rowWalk >= endRow) {
                                if (rowWalk == endRow && colWalk == endCol) {
                                    return true;
                                }
                                if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                    break;
                                }
                                rowWalk--;
                            }
                        } else {
                            rowWalk = startRow + 1;
                            while (rowWalk <= endRow) {
                                if (rowWalk == endRow && colWalk == endCol) {
                                    return true;
                                }
                                if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                    break;
                                }
                                rowWalk++;
                            }
                        }
                    }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        if ((endRow - startRow) * (endRow - startRow) == (endCol - startCol) * (endCol - startCol)) {
                            if (endRow > startRow && endCol > startCol) {
                                rowWalk++;
                                colWalk++;
                                while (rowWalk <= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk++;
                                    colWalk++;
                                }
                            } else if (endRow > startRow && endCol < startCol) {
                                rowWalk++;
                                colWalk--;
                                while (rowWalk <= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }

                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk++;
                                    colWalk--;
                                }
                            } else if (endRow < startRow && endCol > startCol) {
                                rowWalk--;
                                colWalk++;
                                while (rowWalk >= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk--;
                                    colWalk++;
                                }
                            } else if (endRow < startRow && endCol < startCol) {
                                rowWalk--;
                                colWalk--;
                                while (rowWalk >= endRow) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                    if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                        break;
                                    }
                                    rowWalk--;
                                    colWalk--;
                                }
                            }
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        if (endRow - startRow == 0) {
                            if (colWalk > endCol) {
                                colWalk = startCol - 1;
                                while (colWalk >= endCol) {
                                    if (rowWalk == endRow && colWalk == endCol) {
                                        return true;
                                    }
                                if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                    break;
                                }
                                colWalk--;
                            }
                        } else {
                            colWalk = startCol + 1;
                            while (colWalk <= endCol) {
                                if (rowWalk == endRow && colWalk == endCol) {
                                    return true;
                                }
                                if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                    break;
                                }
                                colWalk++;
                            }
                        }
                    } else if (endCol - startCol == 0) {
                        if (rowWalk > endRow) {
                            rowWalk = startRow - 1;
                            while (rowWalk >= endRow) {
                                if (rowWalk == endRow && colWalk == endCol) {
                                    return true;
                                }
                                if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                    break;
                                }
                                rowWalk--;
                            }
                        } else {
                            rowWalk = startRow + 1;
                            while (rowWalk <= endRow) {
                                if (rowWalk == endRow && colWalk == endCol) {
                                    return true;
                                }
                                if (board.getPiece(new ChessPosition(rowWalk, colWalk)) != null) {
                                    break;
                                }
                                rowWalk++;
                            }
                        }
                    }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        if (endRow == startRow + 2 && endCol == startCol + 1) {
                            return true;
                        } else if (endRow == startRow + 2 && endCol == startCol - 1) {
                            return true;
                        } else if (endRow == startRow - 2 && endCol == startCol + 1) {
                            return true;
                        } else if (endRow == startRow - 2 && endCol == startCol - 1) {
                            return true;
                        } else if (endRow == startRow + 1 && endCol == startCol + 2) {
                            return true;
                        } else if (endRow == startRow + 1 && endCol == startCol - 2) {
                            return true;
                        } else if (endRow == startRow - 1 && endCol == startCol + 2) {
                            return true;
                        } else if (endRow == startRow - 1 && endCol == startCol - 2) {
                            return true;
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                            if (piece.getTeamColor() == TeamColor.WHITE) {
                                    if (startRow + 1 == endRow && (startCol - 1 == endCol || startCol + 1 == endCol)) {
                                        return true;
                                    }
                            } else if (piece.getTeamColor() == TeamColor.BLACK) {
                                    if (startRow - 1 == endRow && (startCol - 1 == endCol || startCol + 1 == endCol)) {
                                        return true;
                                    }
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        if ((endRow - startRow) * (endRow - startRow) <= 1 && (endCol - startCol) * (endCol - startCol) <= 1) {
                            if (endRow != startRow || endCol != startCol) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
    return true;
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
