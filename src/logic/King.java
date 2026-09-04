package logic;

import javax.swing.*;
import java.util.ArrayList;

public class King extends ChessPiece{

    public King(BoardPosition position, ChessColor color) {
        super(position, color);
        super.setImage(findImage(color));
    }

    @Override
    public ChessPiece copy() {
        ChessPiece copy = new King(new BoardPosition(this.column, this.row), this.color);

        copy.hasMoved = this.hasMoved;
        copy.pieceIcon = this.pieceIcon;


        return copy;
    }

    @Override
    public void move(BoardPosition pos) {
        super.move(pos);
    }

    @Override
    public ImageIcon findImage(ChessColor color) {
        if (color == ChessColor.BLACK) {
            return new ImageIcon("src/images/blackKing.png");
        }
        else {
            return new ImageIcon("src/images/whiteKing.png");
        }
    }

    @Override
    public ArrayList<BoardPosition> findPossibleMoves(ChessBoard board) {
        ArrayList<BoardPosition> possibleMoves = new ArrayList<>(); // Return

        BoardPosition currentPosition = this; // The current pos of the piece into a BoardPosition

        BoardPosition testPosition = new BoardPosition();
//        System.out.println(color +": " +currentPosition.toString());

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                testPosition = new BoardPosition((char) (currentPosition.column - 1 + c), currentPosition.row + 1 - r);
                if (r != 1 || c != 1) {
                    if (testPosition.isNotOutOfBounds() && board.isEmpty(testPosition)) {
//                        System.out.println(color + ": Board is empty at: " + testPosition.toString());
                        possibleMoves.add(testPosition);
                    }
                    // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
                    else if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
//                        System.out.println(color + ": I can take at: " + testPosition.toString());
                        possibleMoves.add(testPosition);
                    }
                }
            }
        }


        return possibleMoves;
    }


    @Override
    public String getName() {
        return " King:" + this.color + " ";
    }
}
