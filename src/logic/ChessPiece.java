package logic;

import javax.swing.*;
import java.util.ArrayList;

public abstract class ChessPiece extends BoardPosition {
    // 0: My Side
    // 1: Opponent Side
    ChessColor color;
    ArrayList<BoardPosition> possibleMoves;
    ImageIcon pieceIcon;
    boolean hasMoved;

    public ChessPiece() {
        super();
        this.color = ChessColor.WHITE;
        pieceIcon = null;
        hasMoved = false;
    }

    public ChessPiece(BoardPosition position, ChessColor color) {
        super(position);
        this.color = color;
        pieceIcon = null;
        hasMoved = false;
    }

    @Override
    public void move(BoardPosition pos) {
        super.move(pos);
        if (!hasMoved)
            hasMoved = true;
    }

    public void setImage(ImageIcon pieceIcon) {
        this.pieceIcon = pieceIcon;
    }

    public void init(ChessBoard board) {
        possibleMoves = findPossibleMoves(board);
    }

    public ArrayList<BoardPosition> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(ArrayList<BoardPosition> positions) {
        this.possibleMoves = positions;
    }

    public ImageIcon getImage() {
        return pieceIcon;
    }

    public abstract ImageIcon findImage(ChessColor color);

    public abstract ArrayList<BoardPosition> findPossibleMoves(ChessBoard board);

    public abstract String getName();

    public abstract ChessPiece copy();

    public void printPossibleMoves() {
        for (BoardPosition position : possibleMoves) {
            System.out.print(position.toString() + ", ");
        }
        System.out.println();
    }

}
