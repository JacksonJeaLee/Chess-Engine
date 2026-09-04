package logic;

import javax.swing.*;
import java.util.ArrayList;

public class Bishop extends ChessPiece{

    public Bishop(BoardPosition position, ChessColor color) {
        super(position, color);
        super.setImage(findImage(color));
    }

    @Override
    public ChessPiece copy() {
        ChessPiece copy = new Bishop(new BoardPosition(this.column, this.row), this.color);

        copy.hasMoved = this.hasMoved;
        copy.pieceIcon = this.pieceIcon;


        return copy;
    }

    @Override
    public ImageIcon findImage(ChessColor color) {
        if (color == ChessColor.BLACK) {
            return new ImageIcon("src/images/blackBishop.png");
        }
        else {
            return new ImageIcon("src/images/whiteBishop.png");
        }
    }

    public ArrayList<BoardPosition> findPossibleMoves(ChessBoard board) {
        ArrayList<BoardPosition> possibleMoves = new ArrayList<>(); // Return

        BoardPosition currentPosition = this; // The current pos of the piece into a BoardPosition

        BoardPosition testPosition = new BoardPosition();
        int xDiff = -1;
        int yDiff = 1;

        // While the position is empty, keep checking till it comes up
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        while (testPosition.isNotOutOfBounds() && board.isEmpty(testPosition)){
            possibleMoves.add(testPosition);
            xDiff--;
            yDiff++;
            testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        }

        // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }
        xDiff = 1;
        yDiff = 1;

        // While the position is empty, keep checking till it comes up
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        while (testPosition.isNotOutOfBounds() && board.isEmpty(testPosition)){
            possibleMoves.add(testPosition);
            xDiff++;
            yDiff++;
            testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        }

        // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }
        xDiff = 1;
        yDiff = -1;

        // While the position is empty, keep checking till it comes up
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        while (testPosition.isNotOutOfBounds() && board.isEmpty(testPosition)){
            possibleMoves.add(testPosition);
            xDiff++;
            yDiff--;
            testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        }

        // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }
        xDiff = -1;
        yDiff = -1;

        // While the position is empty, keep checking till it comes up
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        while (testPosition.isNotOutOfBounds() && board.isEmpty(testPosition)){
            possibleMoves.add(testPosition);
            xDiff--;
            yDiff--;
            testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        }

        // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        testPosition = new BoardPosition((char) (currentPosition.column - xDiff), currentPosition.row - yDiff);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }

        return possibleMoves;
    }

    @Override
    public String getName() {
        return "Bishop:" + this.color;
    }
}
