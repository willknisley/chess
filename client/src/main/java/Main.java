import chess.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
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
        boolean running = true;
        while (running) {

            System.out.print("[LOGGED_OUT] >>> ");
            String input = scanner.nextLine();
            String command = input.trim().toLowerCase();

            if (command.equals("help")) {
                help();
            } else if (command.equals("quit")) {
                System.out.println("exiting application");
                running = false;
            } else if (!command.isEmpty()) {
                System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }

        scanner.close();
    }

}