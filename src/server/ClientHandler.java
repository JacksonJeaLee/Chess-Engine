package server;

import logic.ChessGame;
import logic.Move;
import server.data.User;
import server.data.UserInfo;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler implements Runnable {

    private UserInfo userInfo;
    private Map<String, ClientHandler> players; // For match finder.
    private Queue<UserInfo> lobby;

    private ClientHandler opponent;

    private ChessGame chessGame;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private BlockingQueue<Object> internalQueue; // Made for moves and handshake agreements between ClientHandlers.

    public ClientHandler(Socket socket, Map<String, ClientHandler> players, Queue<UserInfo> lobby) {
        this.players = players;
        this.socket = socket;
        this.lobby = lobby;

        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            close(socket);
        }
    }

    @Override
    public void run() {

        while (socket.isConnected() && !socket.isClosed()) {
            handleRequests();
        }
        close(socket);
    }

    private void handleRequests() {
        Response response;
        try {
            Request request = (Request) objectInputStream.readObject();
            switch (request.getType()) {
                case "get_data":
                    response = getData();
                    break;

                case "login":
                    response = login(request);
                    break;

                case "logout":
                    response = logout();
                    break;

                case "sign_up":
                    response = signUp(request);
                    break;

                case "find_match":
                    response = findMatch();
                    break;

                case "make_move":
                    response = makeMove(request);
                    break;

                default:
                    response = invalidRequest();
                    break;
            }
        } catch (Exception e) {
            response = responseError(e);
        }
        sendResponse(response);
    }

    private Response makeMove(Request request) {
        HashMap<String, Object> requestData = request.getData();
        Move move = (Move) requestData.get("move");

        // TODO
//        if (!validMove(move)) return

    }

    private Response findMatch() throws InterruptedException {
        internalQueue = new LinkedBlockingQueue<>(); // Init to be used for future moves.
        UserInfo opponentInfo;
        if (lobby.isEmpty()) {
            lobby.add(userInfo);
            // Wait for match
            opponentInfo = (UserInfo) internalQueue.take();
        }
        else {
            opponentInfo = lobby.remove(); // Remove and obtain the opponents username
            ClientHandler opponent = players.get(opponentInfo.getUsername());
            // Use the username to get the ClientHandler and send handshake through their blocking queue.
            opponent.internalQueue.put(userInfo);
        }

        HashMap<String, Object> responseData = new HashMap<>();
//        responseData.put("username", opponentInfo.getUsername());
//        responseData.put("id", opponentInfo.getId());
        responseData.put("user_info", opponentInfo);

        return new Response(200, responseData);
    }

    private Response signUp(Request request) throws SQLException{
        HashMap<String, Object> requestData = request.getData();
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");

        UserInfo userInfo = Database.addUser(username, password);
        boolean successful = userInfo != null; // Unsuccessful due to already used username

        Response response;
        HashMap<String, Object> responseData = new HashMap<>();
        if (successful) {
//            this.username = username;
            this.userInfo = userInfo;
            players.put(username, this);

            responseData.put("user_id", userInfo.getId());
            response = new Response(200, responseData);
        }
        else {
            responseData.put("error", "Username is already registered. Please try again.");
            response = new Response(400, responseData);
        }
        return response;
    }

    private Response logout() {
        players.remove(userInfo.getUsername());
        userInfo = null;

        return new Response(200, null);
    }

    private Response login(Request request) throws SQLException {
        HashMap<String, Object> requestData = request.getData();
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");

        UserInfo userInfo = Database.login(username, password);
        boolean successful = userInfo != null; // Returns -1 if failure

        Response response;
        HashMap<String, Object> responseData = new HashMap<>();
        if (successful) {
            // Add this clientHandler to the hashmap for currentPlayers.
            // Then send response
            this.userInfo = userInfo;
            players.put(username, this);

            responseData.put("user_id", userInfo.getId());
            response = new Response(200, responseData);
        }
        else { // Failure
            responseData.put("error", "Invalid username or password. Please try again.");
            response = new Response(400, responseData);
        }
        return response;
    }

    private Response getData() throws SQLException {
        List<User> data = Database.getData();
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("data", data);
        return new Response(200, responseData);
    }

    private Response invalidRequest() {
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("error", "Request type was invalid.");
        return new Response(403, responseData);
    }

    private void sendResponse(Response response) {
        try {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            close(socket);
        }
    }

    public void removeClientHandler() {
        if (userInfo != null) { // If the user is logged in.
            players.remove(userInfo.getUsername());
            lobby.remove(userInfo);
        }
    }

    private Response responseError(Exception e) {
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("error", e.getMessage());
        return new Response(404, responseData);
    }

    public void close(Socket socket) {
        removeClientHandler();
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
