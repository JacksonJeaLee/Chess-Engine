package swing;

import logic.ChessFrame;
import logic.ChessGame;
import logic.ChessPanel;
import server.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class PanelController {

    public PanelController() throws IOException {
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        Client client = new Client(new Socket("localhost", 1234));
        client.start();

        SignupPanel signupPanel = new SignupPanel(cardLayout, container, client);
        LoginPanel loginPanel = new LoginPanel(cardLayout, container, client);
        SelectionPanel selectionPanel = new SelectionPanel(cardLayout, container, client);
        BackingPanel backingPanel = new BackingPanel(cardLayout, container);
        BackingPanel matchPanel = new BackingPanel(cardLayout, container, client);

        container.add(signupPanel, "SIGNUP");
        container.add(loginPanel, "LOGIN");
        container.add(selectionPanel, "SELECTION");
        container.add(backingPanel, "PLAY");
        container.add(matchPanel, "MATCH");

        cardLayout.show(container, "SELECTION");
        new ChessFrame(container);

        container.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            new PanelController();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
