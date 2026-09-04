# Chess Engine

A Java chess application with a Swing UI, full move generation and check/checkmate rules, plus a multithreaded socket server, a MySQL-backed account system, and online matchmaking.

The project has grown from a single-window local chess game into three cooperating layers:

- **`logic`** — the chess rules engine and the board/game rendering (Swing).
- **`server`** — a `ServerSocket` game server, a per-connection `ClientHandler`, a `Request`/`Response` object protocol, and a JDBC `Database` layer.
- **`swing`** — the application shell: a `CardLayout`-driven set of screens for signup, login, mode selection, pass-and-play, and online matches.

It is configured as a plain IntelliJ IDEA Java project (no Maven/Gradle), targeting **JDK 17**, with `mysql-connector-j-9.7.0.jar` in `lib/`.

## What's New

Compared to the earlier single-file local game:

- **Package restructure.** Sources moved out of a flat `src/` into `logic`, `server`, `server.data`, and `swing` packages.
- **Client/server multiplayer.** A `Server` accepts connections on port `1234` and spawns a `ClientHandler` thread per client. Matchmaking pairs waiting clients out of a shared lobby queue and assigns colors.
- **Serialized object protocol.** `Request` (type + `HashMap` payload) and `Response` (HTTP-style status code + payload) are sent over `ObjectOutputStream`/`ObjectInputStream`. `Move` and `BoardPosition` are `Serializable` so moves travel over the wire directly.
- **Accounts in MySQL.** `Database` uses `PreparedStatement`s for signup/login/lookup against a `users` table. The old ad-hoc `Account` class is gone, replaced by `server.data.User` (server-side, includes the password) and `server.data.UserInfo` (safe to send to clients: username, id, assigned color).
- **A real application shell.** `PanelController` builds a `CardLayout` container with `SIGNUP`, `LOGIN`, `SELECTION`, `PLAY`, and `MATCH` screens. A shared `Panel` base class provides styled buttons, text/password fields, and image scaling helpers, and every subscreen gets a back button.
- **`ChessColor` enum.** Piece and player color is now `WHITE`/`BLACK`/`NONE` instead of ints/booleans. `NONE` doubles as "pass-and-play, no fixed side".
- **Game modes.** `ChessGame` now has three constructors backing a `Mode` of `PASS_AND_PLAY`, `CLIENT`, or `SERVER`, which changes how the board flips and where moves are routed.
- **Non-blocking opponent moves.** `ChessPanel.waitForOpponentMove()` blocks on a background thread and applies the received move back on the EDT via `SwingUtilities.invokeLater`.
- **Deep board copies for check testing.** `ChessBoard` has a copy constructor plus a `copy()` on every piece, so `wouldPutInCheck` simulates a move on a throwaway board instead of mutating and undoing the live one.
- **`MoveFormatException`.** `Move` can be parsed from strings like `e2 e4` and rejects malformed input with a checked exception.

## Project Structure

```text
Chess-Engine/
|-- lib/
|   `-- mysql-connector-j-9.7.0.jar
|-- src/
|   |-- images/
|   |   |-- white*.png / black*.png     (piece sprites)
|   |   |-- chessIcon.png               (window icon)
|   |   |-- BackButton.png
|   |   |-- DefaultProfile.png
|   |   `-- trophy.png
|   |-- logic/
|   |   |-- ChessDriver.java            (legacy entry point, currently a no-op)
|   |   |-- ChessGame.java              (turn/mode/match orchestration)
|   |   |-- ChessBoard.java             (board state + rules)
|   |   |-- ChessPanel.java             (board rendering + mouse input)
|   |   |-- ChessGui.java               (checkmate overlay + rematch)
|   |   |-- ChessFrame.java             (JFrame wrapper)
|   |   |-- ChessPiece.java             (abstract base)
|   |   |-- Pawn/Rook/Knight/Bishop/Queen/King.java
|   |   |-- Move.java
|   |   |-- MoveFormatException.java
|   |   |-- BoardPosition.java
|   |   |-- ChessColor.java
|   |   `-- Player.java
|   |-- server/
|   |   |-- Server.java                 (accept loop, lobby, player registry)
|   |   |-- ClientHandler.java          (per-client request dispatch)
|   |   |-- Client.java                 (client-side API + console harness)
|   |   |-- Database.java               (JDBC / MySQL)
|   |   |-- Request.java
|   |   |-- Response.java
|   |   `-- data/
|   |       |-- User.java
|   |       `-- UserInfo.java
|   `-- swing/
|       |-- PanelController.java        (main application entry point)
|       |-- Panel.java                  (shared styling + helpers)
|       |-- SelectionPanel.java         (mode menu)
|       |-- LoginPanel.java
|       |-- SignupPanel.java
|       `-- BackingPanel.java           (hosts the chess board; pass-and-play + match)
|-- Chess.iml
`-- README.md
```

## Architecture

### Rules engine (`logic`)

The board is a `ChessPiece[][]` of size 8x8. Each piece extends `ChessPiece`, which extends `BoardPosition`, so a piece *is* its own square and can generate moves from where it stands.

`BoardPosition` stores algebraic coordinates (`column` as `'a'`–`'h'`, `row` as `1`–`8`) and converts to array indices and to pixel coordinates. It exposes both `getX()/getY()` and `getFlippedX()/getFlippedY()` so the same position can be drawn from either side of the board.

`ChessBoard` owns board state and the rule checks:

- `newBoard()` builds the starting position.
- `updateBoard()` recomputes every piece's move list after each change.
- `possibleMove(Move)` asks the piece whether the target is in its move list.
- `inCheck(King)` scans every enemy piece's moves for the king's square.
- `wouldPutInCheck(Move)` clones the board, force-applies the move, and re-tests check.
- `validMove(Move)` = `possibleMove && !wouldPutInCheck`.
- `anyValidMoves(piece)` backs checkmate detection.
- `canCastle(Move)` / `clearToCastle(...)` handle castling.

`ChessGame` holds the board, the two `Player`s, the current turn, the mode, and the local player's color. `turn(Move)` validates against the current turn and, in `CLIENT` mode, forwards the accepted move to the server via `Client.sendMove`. `serverTurn(Move)` applies an opponent move that arrived from the network without re-forwarding it.

`ChessPanel` draws the board, pieces, selection highlight, check highlight, and move dots, and translates mouse coordinates into `BoardPosition`s — accounting for whether the board is flipped. Board orientation depends on mode: pass-and-play flips after every move so the player to move always sees their own side; online play keeps the board fixed to the local player's color.

### Server (`server`)

`Server` runs an accept loop on port `1234`. Every connection gets a `ClientHandler` on its own thread. Shared state is a `ConcurrentHashMap<String, ClientHandler>` of logged-in players and a lobby `Queue<UserInfo>` of clients waiting for a match.

`ClientHandler` reads a `Request`, dispatches on its type, and writes back a `Response`. Handler-to-handler communication (the matchmaking handshake and relayed moves) goes through each handler's `BlockingQueue<Object>`, so `get_move` simply blocks on `take()` until the opponent's handler pushes a move.

Matchmaking: the first client to ask finds an empty lobby, enqueues itself, and blocks; the second client dequeues the waiting `UserInfo`, resolves its handler, pushes a handshake through that handler's queue, and both sides get an opponent `UserInfo` carrying the assigned colors. First in the lobby plays White.

### Protocol

| Request type | Request data | Success response data |
| --- | --- | --- |
| `sign_up` | `username`, `password` | `user_id` |
| `login` | `username`, `password` | `user_id` |
| `logout` | — | — |
| `find_match` | — | `user_info` (`UserInfo` with opponent name, id, color) |
| `make_move` | `move` (`Move`) | — |
| `get_move` | — | `move` (`Move`, blocks until available) |
| `get_data` | — | `data` (`List<User>`, debug/admin dump) |

Status codes follow HTTP conventions: `200` success, `400` bad client data, `403` invalid request type, `404` server-side exception. Failure responses carry an `error` string.

### Application shell (`swing`)

`PanelController` opens the socket, constructs the `Client`, and registers five cards in a `CardLayout`:

- `SELECTION` — the menu: Pass and Play, Sign Up, Log In, Find Match.
- `SIGNUP` / `LOGIN` — credential forms (Enter submits).
- `PLAY` — a `BackingPanel` wrapping a local pass-and-play `ChessGame`.
- `MATCH` — a `BackingPanel` with a "Find Match" button that builds the online game once the server returns an opponent.

`BackingPanel` puts the 600x600 `ChessPanel` in a `JLayeredPane` offset below the header so the back button and match button can sit alongside it.

## Requirements

- **JDK 17** (the IntelliJ project sets language level `JDK_17`)
- **MySQL** running on `localhost:3306` with a schema named `mydb`
- `lib/mysql-connector-j-9.7.0.jar` on the classpath (already wired up as an IntelliJ project library)
- IntelliJ IDEA recommended

### Database setup

`Database` connects as `root` to `jdbc:mysql://localhost:3306/mydb` and expects a `users` table with `user_id`, `username`, and `password` columns:

```sql
CREATE DATABASE IF NOT EXISTS mydb;
USE mydb;

CREATE TABLE IF NOT EXISTS users (
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
```

The connection URL and user are currently hardcoded in `Database`; the password is typed in at server startup.

## Running the Project

Open the project folder in IntelliJ, make sure `src` is a source root and the MySQL connector library is attached, then:

### 1. Start the server

Run `main` in `src/server/Server.java`. It prompts on stdin:

```text
Enter MySQL password:
```

Enter your MySQL `root` password. The server then listens on port `1234` and prints a line for each connection.

### 2. Start a client

Run `main` in `src/swing/PanelController.java`. This is the real entry point for the app — it connects to `localhost:1234` and opens the window on the selection screen.

> Note: `logic.ChessDriver.main` is the historical entry point and is currently an empty body of commented-out experiments. Use `PanelController`.

For online play, run two clients (in IntelliJ, enable "Allow multiple instances" on the run configuration), log in as two different accounts, and click **Find Match** on both.

### 3. Optional: console client

`server.Client.main` is a scriptable harness that logs in and calls `findMatch()` / `sendMove()` / `printDatabase()` directly, with no GUI. Handy for testing the protocol.

## Controls

- Click a piece to select it — legal destinations appear as dots.
- Drag and release on a target square to move, or click the origin square then click the destination.
- Pass-and-play flips the board after every valid move. Online play keeps your color at the bottom.
- A king in check is highlighted in red.
- On checkmate, a `ChessGui` overlay appears with a trophy, the winner, and a **Rematch** button.

## Known Issues and Limitations

Still very much in development. The notable gaps:

**Security**
- Passwords are stored and compared in **plaintext** (`User.password` is marked `// TODO CHANGE`). No hashing or salting yet.
- `Database.login` returns a `UserInfo` with `id == -1` on a wrong password instead of `null`, and `ClientHandler.login` only checks for `null` — so **a wrong password for an existing username is currently accepted** (with a bogus user id). This needs fixing before the server is exposed anywhere.
- `Database.login` calls `resultSet.next()` without checking it, so an unknown username surfaces as a generic `404` error rather than a clean "invalid credentials".
- The MySQL user is hardcoded to `root`.

**Server**
- Server-side move validation is stubbed out. `makeMove`/`getMove` in `ClientHandler` relay moves without verifying them against the server's own `ChessGame`, so a modified client could send illegal moves.
- No handling for disconnects mid-match, resignation, draws, or timeouts.
- The lobby is a plain `LinkedList` accessed from multiple handler threads.
- No Elo or match history; games are not persisted.

**UI**
- After a successful login, `LoginPanel` re-shows the `LOGIN` card instead of returning to `SELECTION`, and the username label on the selection screen is built once at construction so it does not refresh.
- Match finding blocks on the calling thread with only a console "Finding match..." message — no loading screen.
- Rematch is wired for pass-and-play only.
- `DefaultProfile.png` exists but profile pictures are not shown yet.

**Chess rules**
- Pawn promotion is not implemented.
- En passant is not implemented (`// TODO` in `Pawn.findPossibleMoves`).
- Castling works but is rough: the black-king branches pass `whiteKing` to `clearToCastle`, and one branch moves the white king's reference. Needs cleanup and tests.
- No stalemate, threefold repetition, fifty-move rule, or insufficient-material draw detection.
- Move generation runs on every board update with no caching, and `wouldPutInCheck` allocates a full board copy per candidate move.

**General**
- No AI opponent.
- No automated test suite.
- No build tool — IntelliJ project files only.
- Image paths are relative to the working directory (`src/images/...`), so the app must be run from the project root.
- Debug output is printed liberally to stdout.

## Roadmap

- Hash passwords and fix the credential-check bug
- Enforce move legality server-side using the `SERVER`-mode `ChessGame`
- Handle disconnects, resignation, and draw offers
- Complete promotion and en passant; harden castling
- Stalemate and draw detection
- Move history, undo/redo, and PGN export
- Game clocks
- Elo ratings, profiles, and persisted match history
- Unit tests for every piece and rule
- An AI opponent
- Migrate to Maven or Gradle

## License

No license has been added yet. Add a license file before sharing or publishing the project publicly.
