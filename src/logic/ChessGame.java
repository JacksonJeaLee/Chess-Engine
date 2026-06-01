package logic;

import server.Client;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.Timer;


public class ChessGame {

    private enum Mode {PASS_AND_PLAY, SERVER}
    private ChessBoard board;
//    private Timer timer;
    private Client client;
    private Player player1;
    private Player player2;
    private Player currentTurnsPlayer;

    private JLayeredPane layeredPane;
    private ChessFrame chessFrame;

    private Mode mode;
    private ChessColor chessColor;

    public ChessGame() {
        mode = Mode.PASS_AND_PLAY;
        chessColor = ChessColor.NONE;
        board = new ChessBoard();
        board.newBoard();
//        timer = new Timer();
        player1 = new Player("Player 1", ChessColor.WHITE);
        player2 = new Player("Player 2", ChessColor.BLACK);
        currentTurnsPlayer = player1;
        initLayeredPane();
    }

    public ChessGame(ChessColor chessColor, Client client) { // For server play
        mode = Mode.SERVER;
        board = new ChessBoard();
        board.newBoard();

        this.client = client;
        this.chessColor = chessColor;
        player1 = new Player("Player 1", ChessColor.WHITE);
        player2 = new Player("Player 2", ChessColor.BLACK);
        currentTurnsPlayer = player1;
        initLayeredPane();
    }

    public void startGame() throws MoveFormatException {
        initLayeredPane();
        ChessPanel panel = new ChessPanel(this);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        chessFrame = new ChessFrame(layeredPane);
        panel.start();
    }

    public void rematch() {
        board = new ChessBoard();
        board.newBoard();
//        timer = new Timer();
        player1 = new Player("Player 1", ChessColor.WHITE);
        player2 = new Player("Player 2", ChessColor.BLACK);
        currentTurnsPlayer = player1;

        initLayeredPane();
        ChessPanel panel = new ChessPanel(this);
        panel.setVisible(true);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        chessFrame.add(layeredPane);

//        panel.removeAll();

    }

    public void initLayeredPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 600));
        layeredPane.setVisible(true);
    }

    public void showWinScreen() {
        ChessGui gui = new ChessGui(this);
        gui.setVisible(true);
        gui.checkMate();
        layeredPane.add(gui, JLayeredPane.PALETTE_LAYER);
    }

    public ChessBoard getChessBoard() {
        return board;
    }

    public ChessPiece[][] getBoard() {
        return board.getBoard();
    }

    public boolean checkMate(King king) {
//        System.out.print("King possibleMoves: ");
//        king.printPossibleMoves();
        return board.inCheck(king) && !board.anyValidMoves(king);
    }

    public void printBoard() {
        board.printBoard();
    }

    public boolean flipped() {
        if (mode == Mode.PASS_AND_PLAY) { // Pass and play, flip after every move.
            return currentTurnsPlayer.getColor() == ChessColor.BLACK;
        }
        // The board is always flipped for black and never for white.
        else {
            return mode != Mode.SERVER;
        }
    }

    public Player getWinner() {
        if (checkMate(board.whiteKing)) {
            return player1;
        }
        else {
            return player2;
        }
    }

    public boolean turn(Move move) {
        if (board.getMoveColor(move) == currentTurnsPlayer.getColor()) {
            if (board.movePiece(move)) {
                currentTurnsPlayer = nextPlayer(currentTurnsPlayer);

                // Since we are playing with a server, needs to send the move through the client
                if (mode == Mode.SERVER) {
                    client.sendMove(move);
                }
                return true;
            }
            return false;

        }
        else {
            if (board.getMoveColor(move) != currentTurnsPlayer.getColor()) {
                System.out.println("Not ur turn buddy");
            }
            System.out.println("Invalid Move");
            return false;
        }
    }

    public Player nextPlayer(Player player) {
        // Returns the other player when one player is inputted.
        if (player.equals(player1)) {
            return player2;
        }
        else {
            return player1;
        }
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

}
