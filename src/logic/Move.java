package logic;

import java.io.Serializable;

public class Move
        implements Serializable {

    private BoardPosition initPosition;
    private BoardPosition movePosition;

    public Move(BoardPosition initPosition, BoardPosition movePosition) {
        this.initPosition = initPosition;
        this.movePosition = movePosition;
    }

    public Move(String move) throws MoveFormatException {
        if (move == null || move.length() != 5) {
            throw new MoveFormatException("Move string out of bounds");
        }

        this.initPosition = toBoardPosition(move.substring(0,2));
        this.movePosition = toBoardPosition(move.substring(3,5));

    }

    public BoardPosition getMovePosition() {
        return movePosition;
    }

    public BoardPosition getInitPosition() {
        return initPosition;
    }

    public BoardPosition toBoardPosition(String position) throws MoveFormatException{
        position = testFormat(position);

        char positionCol = position.charAt(0);
        int positionRow = position.charAt(1) - 48;

        return new BoardPosition(positionCol, positionRow);

    }

    public String testFormat(String positionStr) throws MoveFormatException{
        if (positionStr == null || positionStr.length() != 2)
            throw new MoveFormatException("Invalid length");

        char positionCol = positionStr.charAt(0);
        int positionRow = positionStr.charAt(1) - 48;

//        System.out.println("debug 3");

        if (!validBounds(positionCol, 97, 104) || !validBounds(positionRow, 1, 8)) {
            throw new MoveFormatException("Invalid current position");
        }

        return positionStr;
    }

    /**
     * Helper method to test if a number is between two bounds
     * @param tester
     * @param lower
     * @param upper
     * @return true if it's a valid bound and false if not
     */
    public boolean validBounds(int tester, int lower, int upper) {
        return tester >= lower && tester <=upper;
    }

    public String toString() {
        return initPosition.toString() + " to " + movePosition.toString();
    }
}
