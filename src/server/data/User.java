package server.data;

import java.io.Serializable;

/**
 * User Class
 * Only for Database, Server and ClientHandler usage
 * Use UserInfo for the rest.
 */
public class User
        implements Serializable {

    private int id;
    private String username;
    private String password; // TODO CHANGE

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
