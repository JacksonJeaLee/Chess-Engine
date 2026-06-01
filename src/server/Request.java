package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Request
        implements Serializable {
    private String type;
    private HashMap<String, Object> data;

    /**
     * @param type What kind of request is this?
     * @param data A map of data the client wants to pass to the server with the request.
     */
    public Request(String type, HashMap<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    /**
     * @return The kind of request this is, as a String.
     */
    public String getType() {
        return type;
    }

    /**
     * @return The data inside this request.
     */
    public HashMap<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Request(\"" + type + "\", " + data + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Request)) {
            return false;
        }

        Request other = (Request) obj;

        return Objects.equals(type, other.type)
                && Objects.equals(data, other.data);
    }
}
