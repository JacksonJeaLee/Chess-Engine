package logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGui extends JPanel {
    private final int SCREEN_WIDTH = 450;
    private final int SCREEN_HEIGHT = 275;

    private ChessGame chessGame;

    public ChessGui(ChessGame chessGame) {
        this.setLayout(null);
//        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBounds(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.chessGame = chessGame;
    }

    public void checkMate() {
        this.setVisible(true);
        this.setBackground(new Color(38, 38, 38));
        drawTrophy();
        writeText();
        rematchButton();
    }

    public void writeText() {
        Font headingFont = new Font("Arial", Font.BOLD, 30);
        Font subHeadingFont = new Font("Arial", Font.BOLD, 18);

        JLabel mateText = new JLabel();
        if (chessGame.getWinner().getColor() == ChessColor.WHITE) {
            mateText.setText("White Wins!");
        }
        else {
            mateText.setText("Black Wins!");
        }
        mateText.setFont(headingFont);
        mateText.setBounds(150, 30, 200, 50);
        mateText.setForeground(Color.WHITE);
        mateText.setVisible(true);
        this.add(mateText);

        JLabel explanationText = new JLabel("by checkmate");
        explanationText.setFont(subHeadingFont);
        explanationText.setBounds(175, 60, 200, 50);
        explanationText.setForeground(new Color(143, 143, 143));
        explanationText.setVisible(true);
        this.add(explanationText);
    }

    public void rematchButton() {
        Font buttonFont = new Font("Arial", Font.BOLD, 25);
        JButton rematch = new JButton();
        rematch.setBackground(new Color(69, 110, 60));
        rematch.setOpaque(true);
        rematch.setForeground(Color.WHITE);
        rematch.setFont(buttonFont);
        rematch.setText("Rematch");
        rematch.setBounds(SCREEN_WIDTH / 2 - 150,130, 300, 75);
        rematch.setVisible(true);
        this.add(rematch);

        rematch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Rematching!");
                chessGame.rematch();
                closeGUI();
            }
        });


    }

    public void closeGUI() {
        this.setVisible(false);
    }

    public void drawTrophy() {
        ImageIcon imageIcon = new ImageIcon("src/images/trophy.png");
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(60,60, java.awt.Image.SCALE_SMOOTH));
        JLabel trophy = new JLabel(imageIcon);
        trophy.setBounds(30,30,60,60);
        trophy.setVisible(true);
        this.add(trophy);
    }
}
