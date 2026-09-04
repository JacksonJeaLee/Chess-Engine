package logic;

import java.io.Serializable;

public class BoardPosition
        implements Serializable {
    char column;
    int row;

    public BoardPosition() {
        this.column = 'a';
        this.row = 1;
    }

    public BoardPosition(char column, int row) {
        this.column = column;
        this.row = row;
    }

    public BoardPosition(BoardPosition position) {
        this.column = position.column;
        this.row = position.row;
    }

    // For getting straight from the 2D Array
    public BoardPosition(int column, int row) {
        this.column = (char) (column + 97);
        this.row = Math.abs(row - 8);
    }

    public int getX() {
        int x = (column - 97) * ChessPanel.SQUARE_LENGTH;
        return x;
    }

    public int getY() {
        int y = (Math.abs(row - 8)) * ChessPanel.SQUARE_LENGTH;
        return y;
    }

    public int getFlippedX() {
        int x = Math.abs(104 - column) * ChessPanel.SQUARE_LENGTH;
        return x;
    }

    public int getFlippedY() {
        int y = (row - 1) * ChessPanel.SQUARE_LENGTH;
        return y;
    }

    public String toString() {
        return "" + column + row;
    }

    public boolean equals(BoardPosition position) {
        return position.row == row && position.column == column;
    }

    public boolean isNotOutOfBounds() {
        return (column >= 'a' && column <= 'h') && (row >= 1 && row <= 8);
    }

    public void move(BoardPosition pos) {
        this.column = pos.column;
        this.row = pos.row;
    }
}
