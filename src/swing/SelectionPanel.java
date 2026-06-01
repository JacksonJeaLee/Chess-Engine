package swing;

import logic.ChessFrame;
import logic.ChessGame;
import logic.ChessPanel;
import logic.MoveFormatException;
import server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

public class SelectionPanel extends Panel {

    private Client client;
    private CardLayout cardLayout;
    private JPanel container;

    private JButton passSelectionButton;
    private JButton signupSelectionButton;
    private JButton loginSelectionButton;

    private JLabel usernameLabel;

//    private JTextField usernameEnter;
//    private JPasswordField passwordEnter;

    public SelectionPanel(CardLayout cardLayout, JPanel container, Client client) {
        super();

        this.cardLayout = cardLayout;
        this.container = container;
        this.client = client;

        this.passSelectionButton = createButton("Pass and Play", 100, 200, 400, 75);
        this.signupSelectionButton = createButton("Sign Up", 100, 300, 400, 75);
        this.loginSelectionButton = createButton("Log In", 100, 400, 400, 75);

        this.usernameLabel = createUserLabel(client.getUsername());

        passSelectionButton.addActionListener(e -> cardLayout.show(container, "PLAY"));
        signupSelectionButton.addActionListener(e -> cardLayout.show(container, "SIGNUP"));
        loginSelectionButton.addActionListener(e -> cardLayout.show(container, "LOGIN"));

        add(passSelectionButton);
        add(signupSelectionButton);
        add(loginSelectionButton);

//        this.setLayout(new BorderLayout());

    }

//    public void handleSinglePlay() {
//        ChessPanel chessPanel = new ChessPanel(new ChessGame());
//        ChessFrame chessFrame = new ChessFrame(chessPanel);
//
//    }

//    public void start() throws IOException {
//        client = new Client(new Socket("localhost", 1234));
//        client.start();
//        createLabels();
//        createButtons();
//        repaint();
//        this.setVisible(true);
//    }


    public JLabel createUserLabel(String username) {
        if (username == null || username.isEmpty()) {
            usernameLabel = new JLabel("Please Login to Play");
        }
        else {
            usernameLabel = new JLabel(username);
        }
        usernameLabel.setBounds(SCREEN_WIDTH / 2, 10, SCREEN_WIDTH / 2, 75);
        usernameLabel.setFont(BUTTON_FONT);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setVisible(true);

        return usernameLabel;
    }



}
