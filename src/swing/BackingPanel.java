package swing;

import logic.*;
import server.Client;
import server.data.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BackingPanel extends Panel {

    private final String backButtonURL = "src/images/BackButton.png";

    private CardLayout cardLayout;
    private JPanel container;

    private ChessGame chessGame;
    private ChessPanel chessPanel;

    private Client client;

    private JButton matchFinderButton;

    private JLayeredPane jLayeredPane;

    public BackingPanel(CardLayout cardLayout, JPanel container) throws IOException {
        super();

        this.cardLayout = cardLayout;
        this.container = container;

        this.chessGame = new ChessGame();
        this.chessPanel = new ChessPanel(chessGame);

        chessPanel.start();

//        chessPanel.setVisible();

        createBackButton();

        jLayeredPane = new JLayeredPane();

//        jLayeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.setBounds(0, 75, SCREEN_WIDTH, ChessPanel.SCREEN_HEIGHT);
        jLayeredPane.add(chessPanel, JLayeredPane.PALETTE_LAYER);

        this.add(jLayeredPane);

//        ChessFrame chessFrame = new ChessFrame(chessPanel);
    }

    public BackingPanel(CardLayout cardLayout, JPanel container, Client client) throws IOException{
        super();

        this.cardLayout = cardLayout;
        this.container = container;
        this.client = client;

//        chessPanel.setVisible();

        // Match games are created only after the server finds an opponent.
        matchFinderButton = createButton("Find Match" , 150,550,300, 100);
        matchFinderButton.addActionListener(e -> {
            findMatch();
            matchFinderButton.setVisible(false);
        });
        add(matchFinderButton);

        createBackButton();

        jLayeredPane = new JLayeredPane();

//        jLayeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.setBounds(0, 75, SCREEN_WIDTH, ChessPanel.SCREEN_HEIGHT);
//        jLayeredPane.add(chessPanel, JLayeredPane.PALETTE_LAYER);

        this.add(jLayeredPane);
    }

    public void findMatch() {
        //TODO Change with a loading panel or something
        System.out.println("Finding match...");

        UserInfo opponentInfo = client.findMatch();
        if (opponentInfo == null) {
            matchFinderButton.setVisible(true);
            return;
        }

        ChessColor clientColor = opponentInfo.getChessColor() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;

        this.chessGame = new ChessGame(clientColor, client);
        this.chessPanel = new ChessPanel(chessGame);


        chessPanel.start();

        jLayeredPane.add(chessPanel, JLayeredPane.PALETTE_LAYER);

        // Wait for move if black
        if (clientColor == ChessColor.BLACK) {
            chessPanel.waitForOpponentMove();
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
