package logic;

import java.util.ArrayList;

public class ChessBoard {
    private ChessPiece[][] board;

    King blackKing;
    King whiteKing;

    public ChessBoard() {
        blackKing = new King(new BoardPosition('e', 8), ChessColor.BLACK);
        whiteKing = new King(new BoardPosition('e', 1), ChessColor.WHITE);
        this.board = newBoard();
        updateBoard();
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public void setBoard(ChessPiece[][] board) {
        this.board = board;
    }

    public ChessBoard(ChessPiece[][] board, King blackKing, King whiteKing) {

        this.board = new ChessPiece[8][8];

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = board[r][c];
                if (p != null) {
                    this.board[r][c] = p.copy();
                }
            }
        }

        this.whiteKing = (King)this.board[Math.abs(whiteKing.row - 8)][whiteKing.column - 97];
        this.blackKing = (King)this.board[Math.abs(blackKing.row - 8)][blackKing.column - 97];

        updateBoard();
    }

    public ChessColor getMoveColor(BoardPosition position) {
        if (position == null || this.getPieceAt(position) == null)
            return ChessColor.NONE;
        return this.getPieceAt(position).color;
    }

    public ChessColor getMoveColor(Move move) {
        BoardPosition position = move.getInitPosition();
        if (position == null || this.getPieceAt(position) == null)
            return ChessColor.NONE;
        return this.getPieceAt(position).color;
    }

    public boolean anyValidMoves(ChessPiece piece) {
        ArrayList<BoardPosition> positions = piece.getPossibleMoves();
        for (BoardPosition position : positions) {
            Move move = new Move(piece, position);
            if (validMove(move)) {
//                System.out.println(piece.getName() + " can move to " + positions.toString());
                return true;
            }
        }
        return false;
    }
    public boolean validMove(Move move) {
        return possibleMove(move) && !wouldPutInCheck(move);
    }

    public boolean possibleMove(Move move) {
        ChessPiece piece = getPieceAt(move.getInitPosition());
        ArrayList<BoardPosition> possibleMoves = piece.findPossibleMoves(this);

        for (BoardPosition possibleMove : possibleMoves) {
//            System.out.println("Checking " + possibleMove.toString() + " == " + move.getMovePosition());
            if (possibleMove.equals(move.getMovePosition())) {
                return true;
            }
        }
        System.out.print("Possible valid moves: ");
        piece.printPossibleMoves();
        return false;
    }


    // boolean inCheck(King k) is used for telling if you are in check at this moment
    public boolean inCheck(King king) {
        for (int r = 0; r < board.length; r++) {

            for (int c = 0; c <board[0].length; c++) {

                if (board[r][c] != null && board[r][c].color != king.color) {

//                    System.out.println(board[r][c].getName());
//                    System.out.println(board[r][c].toString());
//                    board[r][c].printPossibleMoves();
//                    System.out.println(king.toString());
//                    System.out.println("Checking " + board[r][c].getName());
                    ArrayList<BoardPosition> possibleMoves = board[r][c].getPossibleMoves();

//                    board[r][c].printPossibleMoves();

                    BoardPosition kingPosition = king;

                    for (BoardPosition move : possibleMoves) {
//                        System.out.println(move.toString() + kingPosition.toString());

                        if (move.equals(kingPosition)) {
                            System.out.println("Im in Check!");
                            printBoard();
                            return true;
                        }
                    }
                }
            }
        }
//        System.out.println("In check wouldPutInCheck(king)");
        return false;
    }

    // boolean wouldPutInCheck(ChessPiece p, BoardPosition pos) is used for if moving will put you in check.
    public boolean wouldPutInCheck(Move move) {
        // Index through every piece on the board that isn't yours. if one of their possible moves

        // A temp board to test if this move would put my king in check.
        ChessBoard testBoard = new ChessBoard(board, blackKing, whiteKing);
        King testKing = testBoard.findKing(testBoard.getPieceAt(move.getInitPosition()));
        testBoard.forceMove(move);

        if (testBoard.inCheck(testKing)) {
            System.out.println("That would put me in check");
//            this.printBoard();
            return true;
        }

        return false;


    }

    // Simple function to find which king is being played based on the piece it was given
    public King findKing(ChessPiece piece) {
        if (piece.color == ChessColor.WHITE)
            return whiteKing;
        else
            return blackKing;
    }



    /**
     * To try to move a piece no matter what
     * @param move
     */
    public void forceMove(Move move) {
        System.out.println("Forcing " + move.toString());
        // if it is a valid move and the king is not in check.
        ChessPiece piece = this.getPieceAt(move.getInitPosition());
        this.board[Math.abs(piece.row - 8)][piece.column - 97] = null;
        this.board[Math.abs(move.getMovePosition().row - 8)][move.getMovePosition().column - 97] = piece;
        piece.move(move.getMovePosition());
        this.updateBoard();

    }

    /**
     * clearToCastle
     * Checks all the moves between two board positions, and makes sure if they're all empty and won't put the king in check.
     * @return true if clear and empty, false if not.
     */
    public boolean clearToCastle(King king, ChessPiece rook) {
        //Castling queen side
        if (king.color == ChessColor.WHITE) {
            if (rook.column < king.column) {
                System.out.println("Castling queen side");
                for (char i = (char) (king.column - 1); i > rook.column; i--) {
                    if (!isEmpty(new BoardPosition(i, king.row)) || wouldPutInCheck(new Move(king, new BoardPosition(i, king.row)))) {
                        System.out.println("Not clear to castle!");
                        return false;
                    }
                }
                return true;
            }
            //Castling king side
            else if (rook.column > king.column) {
                System.out.println("Castling king side");
                for (char i = (char) (king.column + 1); i < rook.column; i++) {
                    BoardPosition checkingPosition = new BoardPosition(i, king.row);
                    System.out.println("Checking " + checkingPosition.toString());
                    if (!isEmpty(new BoardPosition(i, king.row)) || wouldPutInCheck(new Move(king, new BoardPosition(i, king.row)))) {
                        System.out.println("Not clear to castle!");
                        return false;
                    }
                }
                return true;
            }
        }
        // King color == black
        else {
            if (rook.column > king.column) {
                System.out.println("Castling queen side");
                for (char i = (char) (king.column - 1); i > rook.column; i--) {
                    if (!isEmpty(new BoardPosition(i, king.row)) || wouldPutInCheck(new Move(king, new BoardPosition(i, king.row)))) {
                        System.out.println("Not clear to castle!");
                        return false;
                    }
                }
                return true;
            }
            //Castling king side
            else if (rook.column < king.column) {
                System.out.println("Castling king side");
                for (char i = (char) (king.column + 1); i < rook.column; i++) {
                    BoardPosition checkingPosition = new BoardPosition(i, king.row);
                    System.out.println("Checking " + checkingPosition.toString());
                    if (!isEmpty(new BoardPosition(i, king.row)) || wouldPutInCheck(new Move(king, new BoardPosition(i, king.row)))) {
                        System.out.println("Not clear to castle!");
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    public boolean canCastle(Move move) {
        BoardPosition movingTo = move.getMovePosition();
        // If the piece is the white king.
        if (getPieceAt(move.getInitPosition()).equals(whiteKing)) {
            System.out.println("Castling White King");
            // If its castling queenside
            if (movingTo.equals(new BoardPosition('a', 1)) || movingTo.equals(new BoardPosition('b', 1)) || movingTo.equals(new BoardPosition('c' , 1))) {
                // If neither pieces have moved
                ChessPiece rook = getPieceAt(new BoardPosition('a', 1));
                if ((rook != null && !rook.hasMoved) && (!whiteKing.hasMoved) && (clearToCastle(whiteKing, rook))) {
                    // Castle
                    //TODO make sure nothing is in check first and is empty
                    board[Math.abs(whiteKing.row - 8)][whiteKing.column - 97] = null;
                    board[7][2] = whiteKing;
                    whiteKing.move(movingTo);

                    board[Math.abs(rook.row - 8)][rook.column - 97] = null;
                    board[7][3] = rook;
                    rook.move(movingTo);
                    updateBoard();
                    return true;
                }
                System.out.println("Pieces have already moved");
            }


            // The king is castling queenside.
            if (movingTo.equals(new BoardPosition('g', 1)) || movingTo.equals(new BoardPosition('h', 1))) {
                // If neither pieces have moved
                ChessPiece rook = getPieceAt(new BoardPosition('h', 1));
                if ((rook != null && !rook.hasMoved) && (!whiteKing.hasMoved) && (clearToCastle(whiteKing, rook))) {
                    // Castle
                    //TODO make sure nothing is in check first and is empty
                    board[Math.abs(whiteKing.row - 8)][whiteKing.column - 97] = null;
                    board[7][6] = whiteKing;
                    whiteKing.move(movingTo);

                    board[Math.abs(rook.row - 8)][rook.column - 97] = null;
                    board[7][5] = rook;
                    rook.move(movingTo);
                    updateBoard();
                    return true;
                }
                System.out.println("Pieces have already moved");
            }
        }
        else if (getPieceAt(move.getInitPosition()).equals(blackKing)) {
            System.out.println("Castling Black King");
            // If its castling queenside
            if (movingTo.equals(new BoardPosition('a', 8)) || movingTo.equals(new BoardPosition('b', 8)) || movingTo.equals(new BoardPosition('c' , 8))) {
                // If neither pieces have moved
                ChessPiece rook = getPieceAt(new BoardPosition('a', 8));
                if ((rook != null && !rook.hasMoved) && (!blackKing.hasMoved) && (clearToCastle(whiteKing, rook))) {
                    // Castle
                    //TODO make sure nothing is in check first and is empty
                    board[Math.abs(blackKing.row - 8)][blackKing.column - 97] = null;
                    board[0][2] = blackKing;
                    whiteKing.move(movingTo);

                    board[Math.abs(rook.row - 8)][rook.column - 97] = null;
                    board[0][3] = rook;
                    rook.move(movingTo);
                    updateBoard();
                    return true;
                }
                System.out.println("Pieces have already moved");
            }


            // The king is castling queenside.
            if (movingTo.equals(new BoardPosition('g', 8)) || movingTo.equals(new BoardPosition('h', 8))) {
                // If neither pieces have moved
                ChessPiece rook = getPieceAt(new BoardPosition('h', 8));
                if ((rook != null && !rook.hasMoved) && (!blackKing.hasMoved) && (clearToCastle(whiteKing, rook))) {
                    // Castle
                    //TODO make sure nothing is in check first and is empty
                    board[Math.abs(blackKing.row - 8)][blackKing.column - 97] = null;
                    board[0][6] = blackKing;
                    blackKing.move(movingTo);

                    board[Math.abs(rook.row - 8)][rook.column - 97] = null;
                    board[0][5] = rook;
                    rook.move(movingTo);
                    updateBoard();
                    return true;
                }
                System.out.println("Pieces have already moved");
            }
        }
        return false;
    }

    public boolean movePiece(Move move) {
//        System.out.println(move.getMovePosition());

        ChessPiece piece = getPieceAt(move.getInitPosition());
        BoardPosition position = move.getMovePosition();


        // if it is a valid move and the king is not in check.
        if (possibleMove(move)){
            System.out.println("Is a possible move");
            if (!wouldPutInCheck(move)){
                System.out.println("Moving!!!" + move.toString());
                board[Math.abs(piece.row - 8)][piece.column - 97] = null;
                board[Math.abs(position.row - 8)][position.column - 97] = piece;

                piece.move(position);
                updateBoard();
                return true;

            }
            else
                System.out.println("In check: " + piece.getName() + " " + piece.toString() + " - " + position.toString());
        }
        else if (canCastle(move)) {
            return true;
        }
        else {
            System.out.println("Invalid Move! Line 328");
        }
        return false;
    }

    /**
     * @param position Position being checked
     * @return the chess piece at said position
     */
    public ChessPiece getPieceAt(BoardPosition position) {
        return board[Math.abs(position.row - 8)][position.column - 97];
    }

    /**
     * Updates the board by indexing through the 2D array and initializing ever Chess piece
     */
    public void updateBoard() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] != null) {
                    board[r][c].init(this);
                }
            }
        }
    }

    /**
     * checks if the position given is empty or occupied
     * @param position the position being checked
     * @return true if empty, false if not or null
     */
    public boolean isEmpty(BoardPosition position) {
        try {
            return (position.row > 0 && position.row < 9) && (position.column > 96 && position.column < 105 &&
                    board[Math.abs(position.row - 8)][position.column - 97] == null);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }


    }

    /**
     * For debugging, goes through ever piece on the board and prints it out to the terminal
     */
    public void printBoard() {
        for (ChessPiece[] pieces : board) {
            for (ChessPiece piece : pieces) {
                if (piece == null) {
                    System.out.print("[  null  ]");
                }
                else {
                    System.out.print("[" + piece.getName() + "]");
                }
            }
            System.out.println();
        }
    }

    /**
     * Creates a new set-up 2D array of ChessPieces
     * @return a new board
     */
    public ChessPiece[][] newBoard() {
        ChessPiece[][] board = new ChessPiece[8][8];
        board[0] = new ChessPiece[]{new Rook(new BoardPosition('a', 8), ChessColor.BLACK),
                new Knight(new BoardPosition('b', 8), ChessColor.BLACK),
                new Bishop(new BoardPosition('c', 8), ChessColor.BLACK),
                new Queen(new BoardPosition('d', 8), ChessColor.BLACK),
                blackKing,
                new Bishop(new BoardPosition('f', 8), ChessColor.BLACK),
                new Knight(new BoardPosition('g', 8), ChessColor.BLACK),
                new Rook(new BoardPosition('h', 8), ChessColor.BLACK)};

        board[1] = new ChessPiece[]{new Pawn(new BoardPosition('a', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('b', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('c', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('d', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('e', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('f', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('g', 7), ChessColor.BLACK),
                new Pawn(new BoardPosition('h', 7), ChessColor.BLACK)};

        board[6] = new ChessPiece[]{new Pawn(new BoardPosition('a', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('b', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('c', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('d', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('e', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('f', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('g', 2), ChessColor.WHITE),
                new Pawn(new BoardPosition('h', 2), ChessColor.WHITE)};

        board[7] = new ChessPiece[]{new Rook(new BoardPosition('a', 1), ChessColor.WHITE),
                new Knight(new BoardPosition('b', 1), ChessColor.WHITE),
                new Bishop(new BoardPosition('c', 1), ChessColor.WHITE),
                new Queen(new BoardPosition('d', 1), ChessColor.WHITE),
                whiteKing,
                new Bishop(new BoardPosition('f', 1), ChessColor.WHITE),
                new Knight(new BoardPosition('g', 1), ChessColor.WHITE),
                new Rook(new BoardPosition('h', 1), ChessColor.WHITE)};

        return board;
    }


}
