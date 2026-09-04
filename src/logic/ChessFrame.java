package logic;

import swing.Panel;

import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {


    public ChessFrame(JPanel panel) {
        this.setTitle("Chess");
        System.out.println(panel.getSize());
        this.setSize(Panel.SCREEN_WIDTH, Panel.SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(createLogo());
        this.setLocationRelativeTo(null);
        this.add(panel);
        revalidate();
        repaint();
        this.setVisible(true);
    }


    public ChessFrame(ChessGui chessGui) {
        this.add(chessGui);
        this.setSize(chessGui.getSize());
        this.setTitle("Chess");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(createLogo());
//        this.pack();
        this.setVisible(true);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
    }

    public ChessFrame(JLayeredPane layeredPane) {
        this.add(layeredPane);
        this.setSize(600,600);
        this.setTitle("Chess");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(createLogo());
        this.pack();
        this.setVisible(true);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
    }

    // TODO
//    public MenuBar createMenu() {}

    public Image createLogo() {
        String path = "src/images/chessIcon.png";
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage();
    }
}
