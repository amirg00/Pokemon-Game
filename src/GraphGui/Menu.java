package GraphGui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class Menu extends JPanel {

    private static boolean isPlayButtonPressed;
    private Graphics2D g2d;

    Menu() {
        setPlayButtonState(false);
        this.setPreferredSize(new Dimension(1100,750));
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
            System.out.println("pressed!");
            setPlayButtonState(true);
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100,750);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g.create();
        // Background:
        Image background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/Login.jpg"));
        g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        g2d.dispose();
    }

    public boolean getPlayButtonState(){
        return isPlayButtonPressed;
    }

    public void setPlayButtonState(boolean b){
        isPlayButtonPressed = b;
    }

    public void killMenu(){

    }

    /**
     * This method centre the new window opening.
     * @param frame the frame to set its location.
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 8);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 7);
        frame.setLocation(x, y);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("GAME");
        frame.add(new Menu());
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2.6);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2.6);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(x, y);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

