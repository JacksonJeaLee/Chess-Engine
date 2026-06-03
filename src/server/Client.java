package server;

import logic.ChessColor;
import logic.ChessGame;
import logic.Move;
import server.data.User;
import server.data.UserInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
//    private BlockingQueue<Response> responseQ;

    private int userId;
    private String username;

    private boolean isLoggedIn;

    private boolean playingMatch;

//    final long MAX_WAIT_MILLIS = 3000L;

    public Client(Socket socket) {
        this.socket = socket;
        this.isLoggedIn = false;
        this.playingMatch = false;
    }

    public void start() throws IOException {
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
//        responseQ = new LinkedBlockingQueue<>();
//        Thread readerThread = new Thread(() -> {
//            try {
//                while (!socket.isClosed()) {
//                    Response response = (Response) objectInputStream.readObject();
//                    responseQ.add(response);
//                }
//            } catch (Exception e) {
//                closeEverything();
//            }
//        });
    }

    public Move getMove() {
        if (!playingMatch) {
            System.out.println("Need to play a match before receiving a move.");
            return null;
        }

        Request request = new Request("get_move", null);

        Response response = sendRequest(request);

        if (response.getStatusCode() != 200) {
            printError(response);
            return null;
        }

        Move move = (Move) response.getData().get("move");
        return move;
    }

    public void sendMove(Move move) {
        if (!playingMatch) {
            System.out.println("Need to play a match before making a move.");
            return;
        }
        HashMap<String, Object> requestData = new HashMap<>();
        requestData.put("move", move);

        Request request = new Request("make_move", requestData);

        Response response = sendRequest(request);

        if (response.getStatusCode() != 200) {
            printError(response);
            return;
        }
        System.out.println("Success!");
    }

    public UserInfo findMatch() {
        if (!isLoggedIn) {
            System.out.println("Please login before looking for a match.");
            return null;
        }

        Request request = new Request("find_match", null);

        Response response = sendRequest(request);

        HashMap<String, Object> responseData = response.getData();
        if (response.getStatusCode() != 200) {
            printError(response);
            return null;
        }
        UserInfo opponentInfo = (UserInfo) responseData.get("user_info");

        System.out.println(opponentInfo);
        playingMatch = true;

        return opponentInfo;
    }

    public int signUp(String username, String password) {
        if (isLoggedIn) {
            System.out.println("Please logout before signing up.");
            return -1;
        }
        HashMap<String, Object> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        Request request = new Request("sign_up", requestData);

        Response response = sendRequest(request);

        if (response.getStatusCode() != 200) {
            printError(response);
            return -1;
        }
        else {
            HashMap<String, Object> responseData = response.getData();
            this.username = username;
            this.userId = (Integer) responseData.get("user_id");
            this.isLoggedIn = true;
            System.out.println(username + " successfully registered.");
            System.out.println("Welcome USER:" + userId + " - " + username);
            return userId;
        }
    }

    public UserInfo login(String username, String password) {

        if (isLoggedIn) {
            System.out.println("Failure to login, please logout first.");
            return null;
        }

        HashMap<String, Object> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        Request request = new Request("login", requestData);

        Response response = sendRequest(request);

        if (response.getStatusCode() != 200) {
            printError(response);
            return null;
        }
        else {
            HashMap<String, Object> responseData = response.getData();
            this.username = username;
            this.userId = (Integer) responseData.get("user_id");
            this.isLoggedIn = true;
            System.out.println("Welcome USER:" + userId + " - " + username);
            return new UserInfo(ChessColor.NONE, username, userId);
        }
    }

    public void logout() {
        Request request = new Request("logout", null);
        Response response = sendRequest(request);

        if (response.getStatusCode() != 200) {
            System.out.println("Failed to logout");
        }
        else {
            userId = 0;
            username = null;
            isLoggedIn = false;
            System.out.println("Successfully logged out.");
        }
    }

    public void printDatabase() {
        HashMap<String, Object> requestData = new HashMap<>();
        Request request = new Request("get_data", requestData);

        Response response = sendRequest(request);

        HashMap<String, Object> responseData = response.getData();

        if (response.getStatusCode() != 200) {
            printError(response);
        }
        else {
            List<User> data = (List<User>) responseData.get("data");
            for (User user : data) {
                System.out.println("ID: " + user.getId() + " " + user.getUsername() + " " + user.getPassword());
            }

        }

    }

    private Response sendRequest(Request request) {
        Response response = null;
        try {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
//            long startTime = System.currentTimeMillis();
            response = (Response) objectInputStream.readObject();
        } catch (IOException e) {
            System.out.println("Failed to send Request");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid Response");
            e.printStackTrace();
        }
        return response;
    }

    public String getUsername() {
        return username;
    }

    private static void printError(Response response) {
        HashMap<String, Object> responseData = response.getData();
        System.out.println("Error " + response.getStatusCode() + ": " + responseData.get("error"));
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {

            Client client = new Client(new Socket("localhost", 1234));
            client.start();
//            client.printDatabase();
            client.login("Arkofawesome", "12345");
//            client.login("Raiden", "RuffRuff");

            client.findMatch();
            System.out.println();

//            client.logout();
//            client.login("Arkofawesome", "2231");
//            client.logout();
//            client.signUp("Mike", "6831");
//            client.printDatabase();
//            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
