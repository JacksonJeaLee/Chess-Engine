package logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChessPanel extends JPanel {

    private ChessGame chessGame;

    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    static final int SQUARE_LENGTH = 75;

    private boolean flipped;

    private boolean winScreenVisible = false;

    private BoardPosition highlightPosition = null;

    private ChessColor chessColor;
//    private ChessBoard chessBoard;

    public ChessPanel(ChessGame chessGame) {
        this.chessGame = chessGame;
        this.setLayout(null);
//        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.flipped = chessGame.flipped();
        this.chessColor = chessGame.getChessColor();

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }



    public BoardPosition coordsToPosition(int x, int y) {
//        boolean flipped = chessGame.flipped();

        if (!flipped) {
            x = x / 75;
            y = Math.abs(y - 675); // Calculation to get normalized board layout

            int column = y / 75;

            char row = (char) (x + 97);
    //        System.out.println(new BoardPosition(row, column));
            return new BoardPosition(row, column);

        }
        else {
            x = x / 75;
            y = y / 75;

            char row = (char) (104 - x);
            int column = y + 1;

            return new BoardPosition(row, column);

        }
    }

    public void drawPieces(Graphics g) {
//        boolean flipped = chessGame.flipped();
        ChessPiece[][] board = chessGame.getBoard();

        // White is moving
        if (!flipped) {
            for (int r = 0; r < SCREEN_HEIGHT / SQUARE_LENGTH; r++) {
                for (int c = 0; c < SCREEN_WIDTH / SQUARE_LENGTH; c++) {
                    if (board[r][c] != null) {
    //                    System.out.println(board[r][c].getName());
                        drawPiece(g, board[r][c], c * SQUARE_LENGTH, r * SQUARE_LENGTH);
                    }
                }
            }

        }
        else {
            for (int r = 7; r >= 0; r--) {
                for (int c = 7; c >= 0; c--) {
                    if (board[r][c] != null) {
                        //                    System.out.println(board[r][c].getName());
                        drawPiece(g, board[r][c], Math.abs(c - 7) * SQUARE_LENGTH, Math.abs(r - 7) * SQUARE_LENGTH);
                    }
                }
            }
        }


//        holdPiece();

    }

    private void drawPiece(Graphics g, ChessPiece piece, int x, int y) {
        Image image = piece.pieceIcon.getImage();
        g.drawImage(image, x, y, SQUARE_LENGTH, SQUARE_LENGTH, this);
    }

    public void makeMove(Move move) {
//        System.out.println(move.toString());
        // TODO

        if (chessColor == ChessColor.NONE) { // Pass and play
            chessGame.turn(move);
            flipped = chessGame.flipped();

            if (!winScreenVisible && checkForMate()) {
                winScreenVisible = true;

                SwingUtilities.invokeLater(() ->
                        winScreen(chessGame.getWinner())
                );
            }
        }
        else {
            if (chessGame.getChessBoard().getMoveColor(move) == chessColor) {
                if (chessGame.turn(move)) { // Sends move to server
                    repaint();
                    waitForOpponentMove();
                }
            }
        }
        repaint();
    }

    public void waitForOpponentMove() {
        new Thread(() -> {
            Move opponentMove = chessGame.getOpponentMove();
            if (opponentMove == null) {
                return;
            }

            SwingUtilities.invokeLater(() -> {
                chessGame.serverTurn(opponentMove);
                repaint();

                if (!winScreenVisible && checkForMate()) {
                    winScreenVisible = true;
                    winScreen(chessGame.getWinner());
                }
            });
        }).start();
    }

//    public void createHeldPiece(Point point) {
////        System.out.println("Creating piece at: (" + point.toString() + ") ");
//        ChessPiece piece = chessGame.getChessBoard().getPieceAt(highlightPosition);
//        System.out.println(piece.toString());
//        ImageIcon icon = new ImageIcon(piece.pieceIcon.getImage().getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH));
//        JLabel heldImage = new JLabel(icon);
////        heldImage.setBounds((int) point.getX(), (int) point.getY(), icon.getIconWidth(), icon.getIconHeight());
//        heldImage.setBounds(100, 100, icon.getIconWidth(), icon.getIconHeight());
//        System.out.println(heldImage.getBounds());
//        heldImage.setVisible(true);
//        this.add(heldImage);
//    }
//
//    public void holdPiece() {
//        if (highlightPosition != null && chessGame.getChessBoard().getPieceAt(highlightPosition) != null) {
//
//            this.addMouseMotionListener(new MouseAdapter() {
//
//                @Override
//                public void mouseMoved(MouseEvent e) {
//                    createHeldPiece(e.getPoint());
//                }
//            });
//
//        }
//    }

    public void listenForMove() {
        System.out.println("Listening for Move");
        this.addMouseListener(new MouseAdapter() {
            private boolean pressed = false;

            private BoardPosition pressedOn = null;

            private BoardPosition firstPosition;

            @Override
            public void mousePressed(MouseEvent e) {
                if (!pressed) {
                    pressed = true;
                    pressedOn = coordsToPosition(e.getX(), e.getY());
                    System.out.println("Pressed on: " + pressedOn);
                    highlightPosition = pressedOn;
                    repaint();

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (pressed) {
                    pressed = false;
                    BoardPosition releasedOn = coordsToPosition(e.getX(), e.getY());
//                    System.out.println("Released on: " + releasedOn);
                    // If the piece you released on is not the same as the one you pressed on
                    if (!pressedOn.equals(releasedOn)) {
                        System.out.println(pressedOn + " to " + releasedOn);
                        makeMove(new Move(pressedOn, releasedOn));
                        pressedOn = null;
                        firstPosition = null;
                        highlightPosition = null;
                    }
                    else if (firstPosition != null && !firstPosition.equals(releasedOn)) {
//                        System.out.println(firstPosition + " to " + releasedOn);
                        makeMove(new Move(firstPosition, releasedOn));
                        pressedOn = null;
                        firstPosition = null;
                        highlightPosition = null;
                    }
                    else {
                        firstPosition = pressedOn;
                        pressedOn = null;
                    }


                }
            }
        });
    }

    public void drawBoard(Graphics g) {
//        boolean flipped = chessGame.flipped();
        // If it starts on an odd num its black, else its green

        int currentX = 0;
        int currentY = 0;
        Color whiteSquare = new Color(255, 238, 176);
        Color blackSquare = new Color(128, 81, 37);
//        Color highlightedSquare = new Color(117, 143, 113);

        for (int r = 0; r < SCREEN_HEIGHT / SQUARE_LENGTH; r++) {
            currentX = 0;
            if (!flipped){
                if (r % 2 == 0) {
                    g.setColor(whiteSquare);
                }
                else {
                    g.setColor(blackSquare);
                }

            }
            else {
                if (r % 2 == 1) {
                    g.setColor(whiteSquare);
                }
                else {
                    g.setColor(blackSquare);
                }
            }
            for (int c = 0; c < SCREEN_WIDTH / SQUARE_LENGTH; c++) {
                if (c != 0) {
//                    if (c == highlightPosition.column && r == highlightPosition.row)
                    if (g.getColor().equals(whiteSquare)) {
                        g.setColor(blackSquare);
                    }
                    else {
                        g.setColor(whiteSquare);
                    }
                }
                g.fillRect(currentX, currentY, SQUARE_LENGTH, SQUARE_LENGTH);
                currentX += SQUARE_LENGTH;
            }
            currentY += SQUARE_LENGTH;
        }

        highlight(g);

//        paintComponent(g);
    }

    public void drawPossibleMoves(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        if (highlightPosition != null && chessGame.getChessBoard().getPieceAt(highlightPosition) != null) {
            ArrayList<BoardPosition> possiblePositions = chessGame.getChessBoard().getPieceAt(highlightPosition).getPossibleMoves();
            for (int r = 0; r < SCREEN_WIDTH / SQUARE_LENGTH; r++) {
                for (int c = 0; c < SCREEN_WIDTH / SQUARE_LENGTH; c++) {
                    BoardPosition position = new BoardPosition(c, r);
                    for (BoardPosition possiblePosition : possiblePositions) {
                        if (position.equals(possiblePosition) /*&& chessGame.getChessBoard().validMove(new Move(highlightPosition, possiblePosition))*/) {
//                            System.out.println("Drawing move at: " + position.toString());
                            if (!flipped)
                                g.fillOval(position.getX() + SQUARE_LENGTH / 3, position.getY() + SQUARE_LENGTH / 3, SQUARE_LENGTH / 3, SQUARE_LENGTH / 3);
                            else
                                g.fillOval(position.getFlippedX() + SQUARE_LENGTH / 3, position.getFlippedY() + SQUARE_LENGTH / 3, SQUARE_LENGTH / 3, SQUARE_LENGTH / 3);
                        }
                    }
                }
            }
        }
    }

    public void highlight(Graphics g) {
        // For Pressing on squares
        if (!flipped) {
            g.setColor(new Color(117, 143, 113));
            if (highlightPosition != null) {
                g.fillRect(highlightPosition.getX(), highlightPosition.getY(), SQUARE_LENGTH, SQUARE_LENGTH);
            }

            g.setColor(new Color(250, 93, 93));
            if (chessGame.getChessBoard().inCheck(chessGame.getChessBoard().blackKing)) {
                g.fillRect(chessGame.getChessBoard().blackKing.getX(), chessGame.getChessBoard().blackKing.getY(), SQUARE_LENGTH, SQUARE_LENGTH);
            }
            else if (chessGame.getChessBoard().inCheck(chessGame.getChessBoard().whiteKing)) {
                g.fillRect(chessGame.getChessBoard().whiteKing.getX(), chessGame.getChessBoard().whiteKing.getY(), SQUARE_LENGTH, SQUARE_LENGTH);
            }

        }
        else {
            g.setColor(new Color(117, 143, 113));
            if (highlightPosition != null) {
                g.fillRect(highlightPosition.getFlippedX(), highlightPosition.getFlippedY(), SQUARE_LENGTH, SQUARE_LENGTH);
            }

            g.setColor(new Color(250, 93, 93));
            if (chessGame.getChessBoard().inCheck(chessGame.getChessBoard().blackKing)) {
                g.fillRect(chessGame.getChessBoard().blackKing.getFlippedX(), chessGame.getChessBoard().blackKing.getFlippedY(), SQUARE_LENGTH, SQUARE_LENGTH);
            }
            else if (chessGame.getChessBoard().inCheck(chessGame.getChessBoard().whiteKing)) {
                g.fillRect(chessGame.getChessBoard().whiteKing.getFlippedX(), chessGame.getChessBoard().whiteKing.getFlippedY(), SQUARE_LENGTH, SQUARE_LENGTH);
            }
        }

    }

    public void start() {
//        chessGame.startGame();
//        drawBoard();
        listenForMove();
        repaint();
    }

    // TODO
    public void winScreen(Player winner) {
        String color;
//        System.out.println("Winner :" + king.toString() + " " + king.color);
        if (winner.getColor() == ChessColor.WHITE)
            color = "White";
        else
            color = "Black";
        chessGame.showWinScreen();
//        JButton rematch = new JButton("Rematch");
//        rematch.setBounds((SCREEN_WIDTH - 200) / 2, (SCREEN_HEIGHT - 100) / 2, 200, 100);
//        this.add(rematch);
        JLabel winText = new JLabel(color + " King Wins!");
        winText.setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        winText.setVisible(true);
        this.add(winText);
    }

//    public King getWinner() {
//        if (chessGame.checkMate(chessGame.getChessBoard().whiteKing)) {
//            return chessGame.getChessBoard().blackKing;
//        }
//        else {
//            return chessGame.getChessBoard().whiteKing;
//        }
//    }

    public boolean checkForMate() {
        if (chessGame.checkMate(chessGame.getChessBoard().whiteKing) || chessGame.checkMate(chessGame.getChessBoard().blackKing)) {
            System.out.println("CheckMate");
            return true;
        }
        return false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        drawBoard(g);
//            chessGame.printBoard();
        drawPieces(g);
        drawPossibleMoves(g);


    }
}

