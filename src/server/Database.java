package server;

import logic.ChessColor;
import server.data.User;
import server.data.UserInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    protected String connectionURL = "jdbc:mysql://localhost:3306/mydb";
    protected String user = "root";
    protected String password;


    public Database(String password) {
        this.password = password;
    }
    private Connection establishConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    connectionURL, user, password
            );
//            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }

    /**
     * Uses a Database Connection to check if username or password is correct.
     * If so, returns the user Id.
     * @param username
     * @param password
     * @return userId returns a valid id if correct, or -1 if incorrect.
     * @throws SQLException
     */
    public UserInfo login(String username, String password) throws SQLException {
        Connection connection = establishConnection();
//        Statement statement = connection.createStatement();
        // TODO REPLACE WITH PREPARED STATEMENT!!
//        ResultSet resultSet = statement.executeQuery("SELECT user_id, password FROM users WHERE username = '" + user + "'");
        PreparedStatement statement = connection.prepareStatement(
                "SELECT user_id, password FROM users WHERE username = ?"
        );
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        boolean correctPassword = resultSet.getString("password").equals(password);

        int userId = -1;
        if (correctPassword) {
            userId = resultSet.getInt("user_id");
        }
        statement.close();
        resultSet.close();
        connection.close();

        return new UserInfo(ChessColor.NONE, username, userId);
    }

    public UserInfo addUser(String username, String password) throws SQLException {
        Connection connection = establishConnection();
        if (uniqueUsername(connection, username)) {
//            Statement statement = connection.createStatement();
//            String query = "INSERT INTO users (username, password) VALUES ('" +
//                    user + "', '" + pass +"')";
//            statement.execute(query);
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + user +"'");
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)"
            );
            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();

            statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int userId = resultSet.getInt("user_id");
            resultSet.close();
            statement.close();

            return new UserInfo(ChessColor.NONE, username, userId);
        }
        return null; // Failure
    }

    private boolean uniqueUsername(Connection connection, String username) throws SQLException{
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + user +"'");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        boolean isNotEmpty = !resultSet.next();
        resultSet.close();
        statement.close();
        return isNotEmpty;
    }

    public List<User> getData() throws SQLException {
        Connection connection = establishConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("user_id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            users.add(new User(id, username, password));
        }
        resultSet.close();
        connection.close();
        statement.close();
        return users;
    }
}
