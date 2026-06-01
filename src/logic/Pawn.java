package logic;

import javax.swing.*;
import java.util.ArrayList;

public class Pawn extends ChessPiece{

    public Pawn(BoardPosition position, ChessColor color) {
        super(position, color);
        super.setImage(findImage(color));
    }

    @Override
    public ChessPiece copy() {
        ChessPiece copy = new Pawn(new BoardPosition(this.column, this.row), this.color);

        copy.hasMoved = this.hasMoved;
        copy.pieceIcon = this.pieceIcon;


        return copy;
    }


    @Override
    public ImageIcon findImage(ChessColor color) {
        if (color == ChessColor.BLACK) {
            return new ImageIcon("src/images/blackPawn.png");
        }
        else {
            return new ImageIcon("src/images/whitePawn.png");
        }
    }

    @Override
    public ArrayList<BoardPosition> findPossibleMoves(ChessBoard board) {
        ArrayList<BoardPosition> possibleMoves = new ArrayList<>(); // Return

        BoardPosition currentPosition = this;

        BoardPosition testingPosition = new BoardPosition();

        // If the color is white and first space is empty and
        if (this.color == ChessColor.WHITE){
            if (testingPosition.isNotOutOfBounds() && board.isEmpty(new BoardPosition(currentPosition.column, currentPosition.row + 1))) {
                possibleMoves.add(new BoardPosition(currentPosition.column, currentPosition.row + 1));

                // If the piece is on the 2nd row and the next two spaces are empty
                if (testingPosition.isNotOutOfBounds() && currentPosition.row == 2 && board.isEmpty(new BoardPosition(currentPosition.column, currentPosition.row + 2))) {
                    // If the second space is empty
                    possibleMoves.add(new BoardPosition(currentPosition.column, currentPosition.row + 2));
                }
            }
            // Taking
            // Code to find if they are able to take
            testingPosition = new BoardPosition((char) (currentPosition.column - 1), currentPosition.row + 1);
            if (testingPosition.isNotOutOfBounds() && !board.isEmpty(testingPosition) && board.getPieceAt(testingPosition).color != this.color) {
                possibleMoves.add(testingPosition);
            }

            testingPosition = new BoardPosition((char) (currentPosition.column + 1), currentPosition.row + 1);
            if (testingPosition.isNotOutOfBounds() && !board.isEmpty(testingPosition) && board.getPieceAt(testingPosition).color != this.color) {
                possibleMoves.add(testingPosition);
            }

        }

        // If the color is black and first space is empty and
        if (this.color == ChessColor.BLACK){
            if (testingPosition.isNotOutOfBounds() && board.isEmpty(new BoardPosition(currentPosition.column, currentPosition.row - 1))) {
                possibleMoves.add(new BoardPosition(currentPosition.column, currentPosition.row - 1));

                // If the piece is on the 2nd row and the next two spaces are empty
                if (testingPosition.isNotOutOfBounds() && currentPosition.row == 7 && board.isEmpty(new BoardPosition(currentPosition.column, currentPosition.row - 2))) {
                    // If the second space is empty
                    possibleMoves.add(new BoardPosition(currentPosition.column, currentPosition.row - 2));
                }
            }
            // Taking
            // Code to find if they are able to take
            testingPosition = new BoardPosition((char) (currentPosition.column - 1), currentPosition.row - 1);
            if (testingPosition.isNotOutOfBounds() && !board.isEmpty(testingPosition) && board.getPieceAt(testingPosition).color != this.color) {
                possibleMoves.add(testingPosition);
            }

            testingPosition = new BoardPosition((char) (currentPosition.column + 1), currentPosition.row - 1);
            if (testingPosition.isNotOutOfBounds() && !board.isEmpty(testingPosition) && board.getPieceAt(testingPosition).color != this.color) {
                possibleMoves.add(testingPosition);
            }
        }

        // En passant
        // TODO



        return possibleMoves;
    }

    @Override
    public String getName() {
        return " Pawn:" + this.color + " ";
    }
}
