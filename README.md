# Chess Engine

A Java chess application with a Swing-based board, image-rendered server, turn handling, move validation, check detection, checkmate handling, and a rematch screen.

This project is organized as an IntelliJ IDEA Java project. The main entry point is `ChessDriver`, which creates a `ChessGame`, opens a 600x600 chess board, and lets two local players play from the same computer.

## Features

- Interactive 8x8 chess board built with Java Swing
- Click-and-release piece movement
- Board flips after each turn so the current player sees their side from the bottom
- Legal move generation for standard chess server
- Move validation that prevents a player from moving into check
- Check highlighting on the board
- Checkmate detection with a win overlay
- Rematch button after a checkmate
- Piece image assets for white and black server
- Early account/profile model support for future player features

## Project Structure

```text
Chess-Engine/
|-- lib/
|   `-- mysql-connector-j-9.4.0.jar
|-- src/
|   |-- images/
|   |   |-- white*.png
|   |   |-- black*.png
|   |   |-- chessIcon.png
|   |   |-- DefaultProfile.png
|   |   `-- trophy.png
|   |-- ChessDriver.java
|   |-- ChessGame.java
|   |-- ChessBoard.java
|   |-- ChessPanel.java
|   |-- ChessGui.java
|   |-- ChessFrame.java
|   |-- ChessPiece.java
|   |-- Pawn.java
|   |-- Rook.java
|   |-- Knight.java
|   |-- Bishop.java
|   |-- Queen.java
|   |-- King.java
|   |-- Move.java
|   |-- BoardPosition.java
|   |-- Player.java
|   `-- Account.java
|-- Chess.iml
`-- README.md
```

## How It Works

The game uses a two-dimensional `ChessPiece[][]` array to represent the board. Each piece extends `ChessPiece`, which itself extends `BoardPosition`, allowing every piece to track its own square and calculate legal moves from its current location.

`ChessBoard` owns the core board state and rule checks. It initializes the starting position, updates piece move lists, validates moves, checks whether a king is in check, and tests whether a move would leave the current player in check.

`ChessGame` coordinates the match. It owns the board, the players, the current turn, the Swing frame, and the checkmate/rematch flow.

`ChessPanel` handles the interactive board. It converts mouse coordinates into chess positions, draws the board and server, highlights selected squares, displays possible moves, and sends moves into the game controller.

## Requirements

- Java Development Kit 8 or newer
- IntelliJ IDEA, recommended

The project does not use Maven or Gradle at the moment. It is configured as a plain IntelliJ Java project.

## Running the Project

1. Open the project folder in IntelliJ IDEA.
2. Make sure the `src` folder is marked as a source root.
3. Open `src/ChessDriver.java`.
4. Run the `main` method in `ChessDriver`.

The chess window should open automatically.

## Controls

- Click a piece to select it.
- Release on a target square to move it.
- You can also click one square, then click another square to complete a move.
- The board flips after each valid turn.
- When checkmate is detected, a win screen appears with a rematch button.

## Current Limitations

This project is still in active development. Some chess features and supporting systems are incomplete or still being refined:

- Pawn promotion is not implemented yet.
- En passant is marked as a future task.
- Castling support exists, but still needs more testing and cleanup.
- Account/profile and Elo features are early-stage.
- There is no AI opponent yet.
- There is no automated test suite yet.

## Main Classes

- `ChessDriver` starts the application.
- `ChessGame` controls the game flow, players, turns, and rematches.
- `ChessBoard` stores the board and enforces move/check rules.
- `ChessPanel` draws the board and handles mouse input.
- `ChessGui` displays the checkmate overlay.
- `ChessFrame` creates the application window.
- `ChessPiece` is the abstract base class for all server.
- `Move` parses and stores starting and ending board positions.
- `BoardPosition` represents chess coordinates such as `e4`.
- `Player` stores player names and colors.
- `Account` is the beginning of a future account/profile system.

## Roadmap Ideas

- Complete promotion, en passant, and castling behavior
- Add stalemate and draw detection
- Add move history
- Add undo/redo
- Add timers
- Add saved games
- Add tests for every piece and rule
- Add an AI opponent
- Connect account data to persistent storage

## License

No license has been added yet. Add a license file before sharing or publishing the project publicly.
