package logic;

import javax.swing.*;
import java.util.ArrayList;

public class Rook extends ChessPiece{

    public Rook(BoardPosition position, ChessColor color) {
        super(position, color);
        super.setImage(findImage(color));
    }

    @Override
    public ChessPiece copy() {
        ChessPiece copy = new Rook(new BoardPosition(this.column, this.row), this.color);

        copy.hasMoved = this.hasMoved;
        copy.pieceIcon = this.pieceIcon;


        return copy;
    }

    @Override
    public void move(BoardPosition pos) {
        super.move(pos);
        if (!hasMoved)
            hasMoved = true;
    }

    @Override
    public ImageIcon findImage(ChessColor color) {
        if (color == ChessColor.BLACK) {
            return new ImageIcon("src/images/blackRook.png");
        }
        else {
            return new ImageIcon("src/images/whiteRook.png");
        }
    }

    @Override
    public ArrayList<BoardPosition> findPossibleMoves(ChessBoard board) {
        ArrayList<BoardPosition> possibleMoves = new ArrayList<>(); // Return
        BoardPosition currentPosition = this;
        BoardPosition testPosition = new BoardPosition();

        int index = 1;
        // Forward
        testPosition = new BoardPosition(currentPosition.column, currentPosition.row + index);
        while (testPosition.isNotOutOfBounds() && board.isEmpty(testPosition)) {
            possibleMoves.add(testPosition);
            index++;
            testPosition = new BoardPosition(currentPosition.column, currentPosition.row + index);
        }
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }

        index = 1;
        // Rightward
        testPosition = new BoardPosition((char) (currentPosition.column + index), currentPosition.row);
        while (board.isEmpty(testPosition)) {
            possibleMoves.add(testPosition);
            index++;
            testPosition = new BoardPosition((char) (currentPosition.column + index), currentPosition.row);
        }
        testPosition = new BoardPosition((char) (currentPosition.column + index), currentPosition.row);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }

        index = 1;
        // Backward
        testPosition = new BoardPosition(currentPosition.column, currentPosition.row - index);
        while (board.isEmpty(testPosition)) {
            possibleMoves.add(testPosition);
//            System.out.println(testPosition.toString());
            index++;
            testPosition = new BoardPosition(currentPosition.column, currentPosition.row - index);
        }
        testPosition = new BoardPosition(currentPosition.column, currentPosition.row - index);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }

        index = 1;
        // Leftward
        testPosition = new BoardPosition((char) (currentPosition.column - index), currentPosition.row);
        while (board.isEmpty(testPosition)) {
            possibleMoves.add(testPosition);
            index++;
            testPosition = new BoardPosition((char) (currentPosition.column - index), currentPosition.row);
        }
        testPosition = new BoardPosition((char) (currentPosition.column - index), currentPosition.row);
        if (testPosition.isNotOutOfBounds() && !board.isEmpty(testPosition) && board.getPieceAt(testPosition).color != this.color) {
            possibleMoves.add(testPosition);
        }

        return possibleMoves;
    }

    @Override
    public String getName() {
        return " Rook:" + this.color + " ";
    }
}
