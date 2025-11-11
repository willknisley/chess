import chess.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }

    public static void help() {
        System.out.println("Available commands: ");
        System.out.println();
        System.out.println("help - displays this help information");
        System.out.println();
        System.out.println("register <username> <password> <email> - create account");
        System.out.println();
        System.out.println("login <username> <password> - sign in");
        System.out.println();
        System.out.println("quit - exit application");
    }

}