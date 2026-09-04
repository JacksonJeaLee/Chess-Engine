package swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel {

    public static int SCREEN_WIDTH = 600;
    public static int SCREEN_HEIGHT = 800;

    static Color GREEN_COLOR = new Color(105,146,62);
    static Color BACKGROUND_COLOR = new Color(59, 54, 54);

    static Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public Panel() {
        this.setLayout(null);
        this.setBackground(new Color(59, 54, 54));
        this.setBounds(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(GREEN_COLOR);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFocusable(true);
        button.setVisible(true);

        return button;
    }

    public JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField(15);
        textField.setBounds(x,y,width, height);
        textField.setFont(new Font("Arial", Font.BOLD, 16));
//        textField.setFocusable(true);
        textField.setForeground(Color.BLACK);
        textField.setMargin(new Insets(5,10,5,10));
        textField.setVisible(true);

        return textField;
    }

    public JPasswordField createPasswordField(int x, int y, int width, int height) {
        JPasswordField textField = new JPasswordField(15);
        textField.setBounds(x,y,width, height);
        textField.setFont(new Font("Arial", Font.BOLD, 16));
//        textField.setFocusable(true);
        textField.setForeground(Color.BLACK);
        textField.setMargin(new Insets(5,10,5,10));
        textField.setVisible(true);

        return textField;
    }

    public Image formatImage(String url, int width, int height) throws IOException {
        File file = new File(url);
        Image bufferedImage = ImageIO.read(file);

        bufferedImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        if (bufferedImage == null)
            throw new IOException();

        return bufferedImage;

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    }
}
