package GraphGui;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.DirectedWeightedGraphAlgorithmsImpl;
import ex4_java_client.StageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class FrameGraph extends JFrame implements ActionListener {

    private DirectedWeightedGraph graph, copyGraph;
    private DirectedWeightedGraphAlgorithms copyGraphAtBeginning;
    private PanelGraph panel;
    private StageController stageController;
    private Menu menuPanel;


    public FrameGraph(DirectedWeightedGraph graph, StageController stage) {
        this.graph = graph;
        this.stageController = stage;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the app
        this.setTitle("Directed Weighted Graph by Gal & Amir"); // title
        this.setResizable(true); // prevent this to resize
        this.copyGraphAtBeginning = new DirectedWeightedGraphAlgorithmsImpl();
        this.copyGraphAtBeginning.init(graph);
        this.copyGraph = copyGraphAtBeginning.copy();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
    /**
     * This method centre the new window opening.
     * @param frame the frame to set its location.
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 8);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 11);
        frame.setLocation(x, y);
    }


    public void PlayButtonPressed(boolean flag){
        if (!flag){
            this.menuPanel = new Menu();
            this.setLocationRelativeTo(null);
            this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/logo.png")));
            this.setTitle("Login");
            this.add(menuPanel);
            centreWindow(this);
            this.pack();
        }
        else{
            this.panel = new PanelGraph(graph, stageController);
            menuPanel.setVisible(false);
            this.add(panel);
            this.setTitle("Pokémon v1.0");
            this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/PokemonLogo.png")));
            this.pack();
            this.setVisible(true);
        }
    }

    public boolean getPlayButtonState(){
       return this.menuPanel.getPlayButtonState();
    }
}