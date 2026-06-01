package logic;

public class Player {
    private String name;
    private ChessColor color;

    public Player(String name, ChessColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChessColor getColor() {
        return color;
    }
}