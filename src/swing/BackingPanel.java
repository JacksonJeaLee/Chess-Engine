package swing;

import logic.ChessFrame;
import logic.ChessGame;
import logic.ChessPanel;

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

    public BackingPanel(CardLayout cardLayout, JPanel container) throws IOException {
        super();

        this.cardLayout = cardLayout;
        this.container = container;

        this.chessGame = new ChessGame();
        this.chessPanel = new ChessPanel(chessGame);

        chessPanel.start();

//        chessPanel.setVisible();

        createBackButton();

        JLayeredPane jLayeredPane = new JLayeredPane();

//        jLayeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane.setBounds(0, 75, SCREEN_WIDTH, ChessPanel.SCREEN_HEIGHT);
        jLayeredPane.add(chessPanel, JLayeredPane.PALETTE_LAYER);

        this.add(jLayeredPane);

//        ChessFrame chessFrame = new ChessFrame(chessPanel);
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
