package server.data;

import java.io.Serializable;

public class UserInfo
        implements Serializable {
    private String username;
    private int id;
    // TODO profile photo, elo

    public UserInfo(String username, int id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return username + " : " + id;
    }
}
