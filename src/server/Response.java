package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Response
        implements Serializable {

    private int statusCode;
    private HashMap<String, Object> data;

    /**
     * @param statusCode An integer code that denotes the status of the request that we are responding to:
     *                   <ul>
     *                   <li>200 = Success!</li>
     *                   <li>400 = Failure: The client provided bad data of some sort.</li>
     *                   <li>401 = Failure: The client needs to log in before performing this action.</li>
     *                   <li>403 = Failure: The client is not permitted to perform the action that it wants to do.</li>
     *                   <li>404 = Failure: The server was unable to find what the client is looking for.</li>
     *                   <li>409 = Failure: There was a data conflict with some already-existing data in the database.</li>
     *                   <li>500 = Failure: The server encountered some kind of internal error or problem
     *                                      (and it's NOT the client's fault).</li>
     *                   <li>503 = Failure: The server is not currently available to respond.</li>
     *                   </ul>
     *                   <p>
     *                      See:
     *                      <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status">
     *                          https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status
     *                      </a>
     *                   </p>
     * @param data A bundle of data that the server is passing back to the client with this response
     */
    public Response(int statusCode, HashMap<String, Object> data) {
        this.statusCode = statusCode;
        this.data = data;
    }
    /**
     * @return The integer code that denotes the status of the request that we are responding to
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return The bundle of data that the server is passing back to the client with this response
     */
    public HashMap<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response(" + statusCode + ", " + data + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Response)) {
            return false;
        }

        Response other = (Response) obj;

        return Objects.equals(statusCode, other.statusCode)
                && Objects.equals(data, other.data);
    }
}
