package logic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Knight extends ChessPiece{

    public Knight(BoardPosition position, ChessColor color) {
        super(position, color);
        super.setImage(findImage(color));
    }

    @Override
    public ChessPiece copy() {
        ChessPiece copy = new Knight(new BoardPosition(this.column, this.row), this.color);

        copy.hasMoved = this.hasMoved;
        copy.pieceIcon = this.pieceIcon;


        return copy;
    }

    @Override
    public ImageIcon findImage(ChessColor color) {
        if (color == ChessColor.BLACK) {
            return new ImageIcon("src/images/blackKnight.png");
        }
        else {
            return new ImageIcon("src/images/whiteKnight.png");
        }
    }

    @Override
    public ArrayList<BoardPosition> findPossibleMoves(ChessBoard board) {
        // Hardcoding knight moves
        ArrayList<BoardPosition> possibleMoves = new ArrayList<>(); // Return

        BoardPosition currentPosition = this; // The current pos of the piece into a BoardPosition

        BoardPosition frontLeft = new BoardPosition((char) (currentPosition.column + 2), currentPosition.row - 1);
        if (frontLeft.isNotOutOfBounds() && board.isEmpty(frontLeft))
            possibleMoves.add(frontLeft);
        // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (frontLeft.isNotOutOfBounds() && !board.isEmpty(frontLeft) && board.getPieceAt(frontLeft).color != this.color) {
            possibleMoves.add(frontLeft);
        }

        BoardPosition frontRight = new BoardPosition((char) (currentPosition.column + 2), currentPosition.row + 1);
        if (frontRight.isNotOutOfBounds() && board.isEmpty(frontRight))
            possibleMoves.add(frontRight);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (frontRight.isNotOutOfBounds() && !board.isEmpty(frontRight) && board.getPieceAt(frontRight).color != this.color) {
            possibleMoves.add(frontRight);
        }

        BoardPosition rightUp = new BoardPosition((char) (currentPosition.column + 1), currentPosition.row + 2);
        if (rightUp.isNotOutOfBounds() && board.isEmpty(rightUp))
            possibleMoves.add(rightUp);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (rightUp.isNotOutOfBounds() && !board.isEmpty(rightUp) && board.getPieceAt(rightUp).color != this.color) {
            possibleMoves.add(rightUp);
        }

        BoardPosition rightDown = new BoardPosition((char) (currentPosition.column - 1), currentPosition.row + 2);
        if (rightDown.isNotOutOfBounds() && board.isEmpty(rightDown))
            possibleMoves.add(rightDown);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (rightDown.isNotOutOfBounds() && !board.isEmpty(rightDown) && board.getPieceAt(rightDown).color != this.color) {
            possibleMoves.add(rightDown);
        }

        BoardPosition backRight = new BoardPosition((char) (currentPosition.column - 2), currentPosition.row + 1);
        if (backRight.isNotOutOfBounds() && board.isEmpty(backRight))
            possibleMoves.add(backRight);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (backRight.isNotOutOfBounds() && !board.isEmpty(backRight) && board.getPieceAt(backRight).color != this.color) {
            possibleMoves.add(backRight);
        }

        BoardPosition backLeft = new BoardPosition((char) (currentPosition.column - 2), currentPosition.row - 1);
        if (backLeft.isNotOutOfBounds() && board.isEmpty(backLeft))
            possibleMoves.add(backLeft);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (backLeft.isNotOutOfBounds() && !board.isEmpty(backLeft) && board.getPieceAt(backLeft).color != this.color) {
            possibleMoves.add(backLeft);
        }

        BoardPosition leftDown = new BoardPosition((char) (currentPosition.column - 1), currentPosition.row - 2);
        if (leftDown.isNotOutOfBounds() && board.isEmpty(leftDown))
            possibleMoves.add(leftDown);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (leftDown.isNotOutOfBounds() && !board.isEmpty(leftDown) && board.getPieceAt(leftDown).color != this.color) {
            possibleMoves.add(leftDown);
        }

        BoardPosition leftUp = new BoardPosition((char) (currentPosition.column + 1), currentPosition.row - 2);
        if (leftUp.isNotOutOfBounds() && board.isEmpty(leftUp))
            possibleMoves.add(leftUp);
            // Code to find the color at the next piece, and add it to the arraylist if it is the opposite color
        else if (leftUp.isNotOutOfBounds() && !board.isEmpty(leftUp) && board.getPieceAt(leftUp).color != this.color) {
            possibleMoves.add(leftUp);
        }

        return possibleMoves;
    }

    @Override
    public String getName() {
        return "Knight:" + this.color;
    }
}
