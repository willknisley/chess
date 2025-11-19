import chess.*;
import model.AuthData;
import model.GameData;
import server.ServerFacade;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class Main {

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, PAWN);
        //System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println("♕ Welcome to 240 Chess Client: Type help to get started");
        preloginUI();
    }

    public static void help() {
        System.out.println("Available commands: ");
        System.out.println("help - gives possible commands");
        System.out.println("register <username> <password> <email> - create account");
        System.out.println("login <username> <password> - sign in");
        System.out.println("quit - exit application");
    }





    public static void preloginUI() {
        Scanner scanner = new Scanner(System.in);
        ServerFacade server = new ServerFacade("http://localhost:8080");
        boolean running = true;

        while (running) {
            System.out.print("[LOGGED_OUT] >>> ");
            String input = scanner.nextLine();
            String[] bits = input.split("\\s+");
            String command = bits.length > 0 ? bits[0].toLowerCase() : "";

            if (command.equals("help")) {
                help();
            } else if (command.equals("quit")) {
                System.out.println("Exiting application");
                running = false;
            } else if (command.equals("register")) {
                if (bits.length == 4) {
                    String username = bits[1];
                    String password = bits[2];
                    String email = bits[3];

                    try {
                        AuthData result = server.register(username, password, email);
                        System.out.println("Registration successful");
                        postLoginUI(result.authToken());
                    } catch (Exception e) {
                        System.out.println("Registration failed: " + e.getMessage());
                    }
                } else {
                    System.out.println("Proper register format: <username> <password> <email>");
                }
            } else if (command.equals("login")) {
                if (bits.length == 3) {
                    String username = bits[1];
                    String password = bits[2];

                    try {
                        AuthData result = server.login(username, password);
                        System.out.println("Login successful");
                        postLoginUI(result.authToken());
                    } catch (Exception e) {
                        System.out.println("Login failed: " + e.getMessage());
                    }
                } else {
                    System.out.println("Proper login format: <username> <password>");
                }
            } else if (!command.isEmpty()) {
                System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }
    }

    public static void postLoginUI(String authToken) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade server = new ServerFacade("http://localhost:8080");
        boolean running = true;

        while (running) {
            System.out.print("[LOGGED_IN] >>> ");
            String input = scanner.nextLine();
            String[] bits = input.split("\\s+");
            String command = bits.length > 0 ? bits[0].toLowerCase() : "";

            if (command.equals("help")) {
                System.out.println("Available commands: ");
                System.out.println("help - gives possible commands");
                System.out.println("create <NAME> - creates a game");
                System.out.println("list - lists games");
                System.out.println("join <ID> [WHITE|BLACK] - joins a game");
                System.out.println("logout - logs out the user");
                System.out.println("observe <ID> - watch a game");
            } else if (command.equals("logout")) {
                try {
                    server.logout(authToken);
                    System.out.println("Logout successful");
                    running = false;
                } catch (Exception e) {
                    System.out.println("Logout failed: " + e.getMessage());
                }
            } else if (command.equals("create")) {
                if (bits.length == 2) {
                    String name = bits[1];
                    try {
                        GameData result = server.createGame(name, authToken);
                        System.out.println("Game creation successful: " + result.gameID());
                    } catch (Exception e){
                        System.out.println("Game creation failed: " + e.getMessage());
                    }
                } else {
                    System.out.println("Proper create format: <NAME>");
                }

            } else if (command.equals("list")) {
                try {
                    var games = server.listGames(authToken);
                    if (games.isEmpty()) {
                        System.out.println("No games available");
                    } else {
                        System.out.println("Games:");
                        int count = 1;
                        for (GameData game : games) {
                            System.out.println(count + ". ID: " + game.gameID() +
                                    ", Name: " + game.gameName() +
                                    ", White: " + game.whiteUsername() +
                                    ", Black: " + game.blackUsername());
                            count++;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Game listing failed: " + e.getMessage());
                }
            } else if (command.equals("join")) {
                if (bits.length == 3) {
                    String gameIDString = bits[1];
                    String playerColor = bits[2];
                    try {
                        int gameID = Integer.parseInt(gameIDString);
                        server.joinGame(gameID, playerColor, authToken);
                        System.out.println("Game joined successfully");

                        ChessBoard board = new ChessBoard();
                        board.resetBoard();
                        drawChessBoard(System.out, board, playerColor);

                    } catch (NumberFormatException e) {
                            System.out.println("Invalid game ID. Please enter a valid number.");
                    } catch (Exception e) {
                        System.out.println("Game joining failed: " + e.getMessage());
                    }
                } else {
                    System.out.println("Proper join format: <ID> [WHITE|BLACK]");
                }
            } else if (command.equals("observe")) {
                if (bits.length == 2) {
                    String gameIDString = bits[1];
                    try {
                        int gameID = Integer.parseInt(gameIDString);
                        server.observeGame(gameID, authToken);
                        System.out.println("Game observation successful");

                        ChessBoard board = new ChessBoard();
                        board.resetBoard();
                        String playerColor = "WHITE";
                        drawChessBoard(System.out, board, playerColor);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game ID. Please enter a valid number.");
                    } catch (Exception e) {
                        System.out.println("Game observation failed: " + e.getMessage());
                    }
                } else {
                    System.out.println("Proper observe format: <ID>");
                }

            } else if (!command.isEmpty()) {
                System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }
    }

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String EMPTY = "   ";


    public static String convertPieceToString(ChessPiece piece) {
        if (piece == null) {
            return "   ";
        }

        if (piece.getPieceType() == KING) {
            return " K ";
        } else if (piece.getPieceType() == QUEEN) {
            return " Q ";
        } else if (piece.getPieceType() == BISHOP) {
            return " B ";
        } else if (piece.getPieceType() == KNIGHT) {
            return " N ";
        } else if (piece.getPieceType() == ROOK) {
            return " R ";
        } else if (piece.getPieceType() == PAWN) {
            return " P ";
        }
        return "   ";
    }

    private static void drawSide(PrintStream out, String headerText) {
        out.print(" ");
        printHeaderText(out, headerText);
        out.print(" ");
    }

    private static void printSideText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawHeaders(PrintStream out, boolean flip) {

        setBlack(out);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            int index = flip ? (BOARD_SIZE_IN_SQUARES - 1 - boardCol) : boardCol;
            drawHeader(out, headers[index]);
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(" ");
        printHeaderText(out, headerText);
        out.print(" ");
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

        private static void drawChessBoard(PrintStream out, ChessBoard chess, String playerColor) {
        setBlack(out);
            out.print(EMPTY);

            if (Objects.equals(playerColor, "WHITE")) {
                drawHeaders(out, false);
                for (int boardRow = 7; boardRow >= 0; --boardRow) {
                    drawRowOfSquares(out, boardRow, chess, boardRow + 1, false);
                }
            } else if (Objects.equals(playerColor, "BLACK")) {
                drawHeaders(out, true);
                for (int boardRow = 0; boardRow < 8; ++boardRow) {
                    drawRowOfSquares(out, boardRow, chess, 8 - boardRow, true);
                }
            }
            setBlack(out);
            out.print(EMPTY);
            drawHeaders(out, !Objects.equals(playerColor, "WHITE"));

            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

        private static void drawRowOfSquares(PrintStream out, int boardRow, ChessBoard chess, int rowNumber, boolean flip) {
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    setBlack(out);
                    drawSide(out, String.valueOf(rowNumber));
                } else {
                    setBlack(out);
                    out.print(EMPTY);
                }

                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    if ((boardRow + boardCol) % 2 == 0) {
                        setWhite(out);
                        boolean isWhiteSquare = true;

                        if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                            int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                            int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                            int col = flip ? (BOARD_SIZE_IN_SQUARES - 1 - boardCol) : boardCol;
                            ChessPiece piece = chess.squares[boardRow][col];
                            out.print(EMPTY.repeat(prefixLength));
                            String pieceString = convertPieceToString(piece);
                            printPlayer(out, pieceString, piece, isWhiteSquare);
                            out.print(EMPTY.repeat(suffixLength));
                        } else {
                            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                        }

                    } else {
                        setBlack(out);
                        boolean isWhiteSquare = false;
                        if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                            int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                            int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                            int col = flip ? (BOARD_SIZE_IN_SQUARES - 1 - boardCol) : boardCol;
                            ChessPiece piece = chess.squares[boardRow][col];
                            out.print(EMPTY.repeat(prefixLength));
                            String pieceString = convertPieceToString(piece);
                            printPlayer(out, pieceString, piece, isWhiteSquare);
                            out.print(EMPTY.repeat(suffixLength));
                        } else {
                            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                        }
                    }

                }
                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    setBlack(out);
                    drawSide(out, String.valueOf(rowNumber));
                } else {
                    setBlack(out);
                    out.print(EMPTY);
                }

                out.println();
            }
        }

        private static void setWhite(PrintStream out) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_WHITE);
        }

        private static void setBlack(PrintStream out) {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLACK);
        }

        private static void printPlayer(PrintStream out, String player, ChessPiece piece, boolean isWhiteSquare) {
            if (isWhiteSquare) {
                out.print(SET_BG_COLOR_WHITE);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }

            if (piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_BLUE);
            } else if (piece != null) {
                out.print(SET_TEXT_COLOR_RED);
            }

            out.print(player);

            if (isWhiteSquare) {
                setWhite(out);
            } else {
                setBlack(out);
            }
        }
}