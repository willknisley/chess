package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        int currentRow = myPosition.getRow();
        int currentColumn = myPosition.getColumn();

        if (piece.getPieceType() == PieceType.KING) {
            List<ChessMove> kingMoves = new ArrayList<>();
            for (int i = 1; i <= 8; i++ ) {
                for (int j = 1; j <= 8; j++) {
                    if (i == currentRow && j == currentColumn) {
                        int newRow, newCol;
                        ChessGame.TeamColor currentColor = piece.getTeamColor();
                            newRow = i; newCol = j;
                            ++newRow; ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                }
                            }
                        newRow = i; newCol = j;
                            ++newRow; --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                }
                            }
                        newRow = i; newCol = j;
                            --newRow; ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                }
                            }
                        newRow = i; newCol = j;
                            --newRow; --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                }
                            }
                        newRow = i; newCol = j;
                        ++newRow;
                        if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            }
                        }
                        newRow = i; newCol = j;
                        ++newCol;
                        if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            }
                        }
                        newRow = i; newCol = j;
                        --newRow;
                        if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            }
                        }
                        newRow = i; newCol = j;
                        --newCol;
                        if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                            } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                kingMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                            }
                        }
                    }
                }
            }
            return kingMoves;
        } else if (piece.getPieceType() == PieceType.QUEEN) {
            List<ChessMove> queenMoves = new ArrayList<>();
            for (int i = 1; i <= 8; i++ ) {
                for (int j = 1; j <= 8; j++) {
                    if (i == currentRow && j == currentColumn) {
                        int newRow, newCol;
                        ChessGame.TeamColor currentColor = piece.getTeamColor();

                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newRow; ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newRow; --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newRow; ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newRow; --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }

                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newRow;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newRow;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    queenMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return queenMoves;
        } else if (piece.getPieceType() == PieceType.BISHOP) {
            List<ChessMove> bishopMoves = new ArrayList<>();
            for (int i = 1; i <= 8; i++ ) {
                for (int j = 1; j <= 8; j++) {
                    if (i == currentRow && j == currentColumn) {
                        int newRow, newCol;
                        ChessGame.TeamColor currentColor = piece.getTeamColor();

                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newRow; ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newRow; --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newRow; ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newRow; --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    bishopMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            return bishopMoves;

        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            //filler
        } else if (piece.getPieceType() == PieceType.ROOK) {
            List<ChessMove> rookMoves = new ArrayList<>();
            for (int i = 1; i <= 8; i++ ) {
                for (int j = 1; j <= 8; j++) {
                    if (i == currentRow && j == currentColumn) {
                        int newRow, newCol;
                        ChessGame.TeamColor currentColor = piece.getTeamColor();

                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newRow;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            ++newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newRow;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                        newRow = i; newCol = j;
                        while (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                            --newCol;
                            if (newRow <= 8 && newCol <= 8 && newRow >=1 && newCol >= 1) {
                                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) == currentColor) {
                                    break;
                                } else if (((board.getPiece(new ChessPosition(newRow, newCol)).getTeamColor())) != currentColor) {
                                    rookMoves.add(new ChessMove(new ChessPosition(i, j), new ChessPosition(newRow, newCol), null));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return rookMoves;
        } else if (piece.getPieceType() == PieceType.PAWN) {
            //filler
        }
        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
