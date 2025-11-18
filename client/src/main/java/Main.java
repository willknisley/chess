import chess.*;
import model.AuthData;
import model.GameData;
import server.ServerFacade;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
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
            } else {
                System.out.println("Proper register format: <username> <password> <email>");
            }
        }


        scanner.close();
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
                System.out.println("observe - watch a game");
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
                        AuthData result = server.createGame(name, authToken);
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
                    System.out.println("Game listing failed" + e.getMessage());
                }
            } else if (command.equals("join")) {
                if (bits.length == 3) {
                    String gameIDString = bits[1];
                    String playerColor = bits[2];
                    int gameID = Integer.parseInt(gameIDString);
                    try {
                        server.joinGame(gameID, playerColor, authToken);
                        System.out.println("Game joined successfully");

                        ChessBoard board = new ChessBoard();
                        board.resetBoard();
                        drawChessBoard(System.out, board);
                    } catch (Exception e) {
                        System.out.println("Game joining failed" + e.getMessage());
                    }
                } else {
                    System.out.println("Proper join format: <ID> [WHITE|BLACK]");
                }
            } else if (command.equals("observe")) {
                if (bits.length == 2) {
                    String gameIDString = bits[1];
                    int gameID = Integer.parseInt(gameIDString);
                    try {
                        server.observeGame(gameID, authToken);
                        System.out.println("Game observation successful");

                        ChessBoard board = new ChessBoard();
                        board.resetBoard();
                        drawChessBoard(System.out, board);
                    } catch (Exception e) {
                        System.out.println("Game observation failed" + e.getMessage());
                    }
                } else {
                    System.out.println("Proper observe format: <ID>");
                }

            } else if (!command.isEmpty()) {
                System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }
        scanner.close();
    }

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 6;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
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

    private static void drawSides(PrintStream out) {

        setBlack(out);

        String[] headers = {"1", "2", "3", "4", "5", "6", "7", "8"};
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawSide(out, headers[boardRow]);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawSide(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printSideText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printSideText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

        private static void drawChessBoard(PrintStream out, ChessBoard chess) {
            drawHeaders(out);
            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

                drawRowOfSquares(out,boardRow, chess);
                if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                    setBlack(out);
                }
            }
            drawHeaders(out);
        }

        private static void drawRowOfSquares(PrintStream out, int boardRow, ChessBoard chess) {

            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    if ((boardRow + boardCol) % 2 == 0) {
                        setWhite(out);

                        if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                            int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                            int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                            ChessPiece piece = chess.squares[boardRow][boardCol];
                            out.print(EMPTY.repeat(prefixLength));
                            String pieceString = convertPieceToString(piece);
                            printPlayer(out, pieceString);
                            out.print(EMPTY.repeat(suffixLength));
                        } else {
                            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                        }

                        if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                            out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                        }


                    } else {
                        setBlack(out);
                        if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                            int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                            int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                            ChessPiece piece = chess.squares[boardRow][boardCol];
                            out.print(EMPTY.repeat(prefixLength));
                            String pieceString = convertPieceToString(piece);
                            printPlayer(out, pieceString);
                            out.print(EMPTY.repeat(suffixLength));
                        } else {
                            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                        }

                        if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                            out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                        }
                    }

                    out.println();
                }
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

        private static void printPlayer(PrintStream out, String player) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLACK);

            out.print(player);

            setWhite(out);
        }
    }

}