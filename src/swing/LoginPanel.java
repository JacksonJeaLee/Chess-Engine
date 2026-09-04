package swing;

import server.Client;
import server.data.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LoginPanel extends Panel {

    private Client client;
    private CardLayout cardLayout;
    private JPanel container;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginButton;

    private final String backButtonURL = "src/images/BackButton.png";

    public LoginPanel(CardLayout cardLayout, JPanel container, Client client) throws IOException {
        super();

        this.client = client;
        this.cardLayout = cardLayout;
        this.container = container;

        this.usernameField = createTextField(150,200,300, 45);
        this.passwordField = createPasswordField(150,300,300, 45);

        this.loginButton = createButton("Log In" , 150,550,300, 100);

        add(usernameField);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(e -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    loginButton.doClick();
            }
        });

        createBackButton();

        repaint();
        this.setVisible(true);
    }

    public void handleLogin() throws IOException{
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            // TODO replace with better handling
            System.out.println("Empty Field, please try again");
            return;
        }

        // Signup now
        // We can assume the client has been started
//        System.out.println(username + ", " + password);
        UserInfo userInfo = client.login(username, password);

        if (userInfo != null) {
            cardLayout.show(container, "LOGIN"); // TODO
            System.out.println("Welcome " + userInfo.getUsername());
        }
        else {
            System.out.println("Failure");
        }
    }

    public void createBackButton() throws IOException {
        int width = 75;
        int height = 75;
        ImageIcon buttonIcon = new ImageIcon(formatImage(backButtonURL, width, height));
        JLabel backButton = new JLabel(buttonIcon);
        backButton.setBounds(0,0, width, height);

        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new MouseAdapter() {
            // TODO
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cardLayout.show(container, "SELECTION");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }
        });

        backButton.setVisible(true);
        this.add(backButton);
    }
}
