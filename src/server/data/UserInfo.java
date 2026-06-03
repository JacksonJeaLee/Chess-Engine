package server.data;

import logic.ChessColor;

import java.io.Serializable;

public class UserInfo
        implements Serializable {
    private String username;
    private int id;

    private ChessColor chessColor;
    // TODO profile photo, elo

    public UserInfo(ChessColor chessColor, String username, int id) {
        this.chessColor = chessColor;
        this.username = username;
        this.id = id;
    }

    public void setChessColor(ChessColor chessColor) { this.chessColor = chessColor; }

    public ChessColor getChessColor() { return chessColor; }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return username + " : " + id;
    }
}
