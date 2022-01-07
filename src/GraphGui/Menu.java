package GraphGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu{
    private static boolean isPlayButtonPressed;

    public Menu(){
        setPlayButtonState(false);
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
            Panel background = new Panel();
            JFrame frame = new JFrame("Game");
            frame.setPreferredSize(new Dimension(1100,750));
            frame.setContentPane(background);
            centreWindow(frame);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new pane(frame, this));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }




    /**
     * Panel to draw the image.
     */
    public static class Panel extends JPanel {

        BufferedImage image;

        public Panel() {
            setLayout(new BorderLayout());
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Login.jpg")));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @Override
        public Dimension getPreferredSize() {
            return image == null ? super.getPreferredSize() : new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }


    }

    /**
     * Secondary panel class.
     */
    public static class pane extends JPanel {

        private MainMenuPane mainMenuPane;
        private CardLayout cardLayout;

        public pane(JFrame frame, Menu menuRef) {
            setOpaque(false);
            setVisible(true);
            cardLayout = new CardLayout();
            setLayout(cardLayout);
            mainMenuPane = new MainMenuPane(frame, menuRef);
            add(mainMenuPane, "MainMenu");
            cardLayout.show(this, "MainMenu");
        }

    }

    /**
     * Main menu class panel:
     */
    public static class MainMenuPane extends JPanel {

        public MainMenuPane(JFrame frame, Menu menuRef) {

            setLayout(new GridBagLayout());
            setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(500, 0, 0, 0);
            gbc.ipadx = 0;
            gbc.ipady = 0;


            JButton btn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/PlayButton_d.gif"))));;
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setRolloverIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/frame_b.gif"))));
            add(btn, gbc);
            btn.addActionListener(e -> {
                menuRef.setPlayButtonState(true);
                System.out.println("pressed!");
                frame.dispose();
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            Composite c = g2d.getComposite();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            g2d.setComposite(c);
        }

    }
    /**
     * This method centre the new window opening.
     * @param frame the frame to set its location.
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2.6);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2.6);
        frame.setLocation(x, y);
    }


    public boolean getPlayButtonState(){
        return isPlayButtonPressed;
    }

    public void setPlayButtonState(boolean b){
        isPlayButtonPressed = b;
    }
    public static void main(String[] args) {
        new Menu();
    }

}

