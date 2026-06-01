package server;

import server.data.UserInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket serverSocket;
    private Map<String, ClientHandler> players;
    private Queue<UserInfo> lobby;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.players = new ConcurrentHashMap<>();
        this.lobby = new LinkedList<>();
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, players, lobby);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e) {

        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
