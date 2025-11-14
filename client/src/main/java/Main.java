import chess.*;
import model.AuthData;
import service.UserService;
import server.ServerFacade;
import java.util.Scanner;

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
            }
        }
        scanner.close();
    }

}