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
    private JMenuBar menuBar;
    private JMenuItem saveItem,exitItem, shortestPathMenu, isConnectedMenu, centerMenu ,tspMenu, aboutMenu;
    private JMenu fileMenu, runMenu, editMenu,  helpMenu, loadMenu, viewMenu;
    private ImageIcon loadIcon, saveIcon, exitIcon, gitIcon;
    private Image main_menuBar;
    private JMenuItem loadFile, G1, G2, G3, RandomGraph;
    private JMenuItem verticesTableMenu, edgesTableMenu;
    private JMenu edgeItem, vertexItem;
    private JMenuItem clearItem, addEdgeItem, removeEdgeItem, addVertexItem, removeVertexItem;
    private File jsonFileSelected;
    private StageController stageController;
    private Menu menuPanel;
    private ImageIcon[] gifs;


    public FrameGraph(DirectedWeightedGraph graph, StageController stage) { // get here a graph.

        this.graph = graph;
        this.stageController = stage;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the app
        this.setTitle("Directed Weighted Graph by Gal & Amir"); // title
        this.setResizable(true); // prevent this to resize
        this.copyGraphAtBeginning = new DirectedWeightedGraphAlgorithmsImpl();
        this.copyGraphAtBeginning.init(graph);
        this.copyGraph = copyGraphAtBeginning.copy();


        gifs = new ImageIcon[]{new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/frame_b.gif")))};



        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        viewMenu = new JMenu("View");
        runMenu = new JMenu("Run");
        helpMenu = new JMenu("Help");


        loadMenu = new JMenu("Load");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");


        fileMenu.add(loadMenu);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);


        /**
         * sub files to load initialization:
         */
        G1 = new JMenuItem("G1.json");
        G2 = new JMenuItem("G2.json");
        G3 = new JMenuItem("G3.json");
        RandomGraph = new JMenuItem("Create Random Graph");
        loadFile = new JMenuItem("Select Json File");

        loadMenu.add(G1);
        loadMenu.add(G2);
        loadMenu.add(G3);
        loadMenu.add(RandomGraph);
        loadMenu.add(loadFile);



        G1.addActionListener(this);
        G2.addActionListener(this);
        G3.addActionListener(this);
        RandomGraph.addActionListener(this);
        loadFile.addActionListener(this);


        /**
         * View menus:
         */
        verticesTableMenu = new JMenuItem("Vertices Table");
        edgesTableMenu = new JMenuItem("Edges Table");
        viewMenu.add(verticesTableMenu);
        viewMenu.add(edgesTableMenu);



        /*
         * Algorithm which belongs to the run menu
         */
        shortestPathMenu = new JMenuItem("ShortestPath");
        isConnectedMenu = new JMenuItem("IsConnected");
        centerMenu = new JMenuItem("Center");
        tspMenu = new JMenuItem("TSP");


        runMenu.add(shortestPathMenu);
        runMenu.add(isConnectedMenu);
        runMenu.add(centerMenu);
        runMenu.add(tspMenu);


        aboutMenu = new JMenuItem("About");
        helpMenu.add(aboutMenu);

        /**
         * Belongs to the edit menu of the menu bar.
         */
        edgeItem = new JMenu("Edge");
        vertexItem = new JMenu("Vertex");
        clearItem = new JMenuItem("clear");
        editMenu.add(edgeItem);
        editMenu.add(vertexItem);
        editMenu.add(clearItem);
        addEdgeItem = new JMenuItem("add");
        removeEdgeItem = new JMenuItem("remove");
        addVertexItem = new JMenuItem("add");
        removeVertexItem = new JMenuItem("remove");
        edgeItem.add(addEdgeItem);
        edgeItem.add(removeEdgeItem);
        vertexItem.add(addVertexItem);
        vertexItem.add(removeVertexItem);
        clearItem.addActionListener(this);
        removeVertexItem.addActionListener(this);
        addVertexItem.addActionListener(this);
        addEdgeItem.addActionListener(this);
        removeEdgeItem.addActionListener(this);

        /**
         * Adding icons:
         */
        loadIcon = new ImageIcon("src\\GraphGui\\Icons\\file_upload_icon.png");
        saveIcon = new ImageIcon("src\\GraphGui\\Icons\\file_save_icon.png");
        exitIcon = new ImageIcon("src\\GraphGui\\Icons\\file_exit_icon.png");
        gitIcon = new ImageIcon("src\\GraphGui\\Icons\\git.png");
        main_menuBar = Toolkit.getDefaultToolkit().getImage("src\\GraphGui\\Icons\\logo.png");



        this.setIconImage(main_menuBar);
        loadMenu.setIcon(loadIcon);
        saveItem.setIcon(saveIcon);
        exitItem.setIcon(exitIcon);
        aboutMenu.setIcon(gitIcon);

        /**
         *  Actions when pressed:
         */
        loadMenu.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        helpMenu.addActionListener(this);
        aboutMenu.addActionListener(this);
        edgeItem.addActionListener(this);
        vertexItem.addActionListener(this);
        shortestPathMenu.addActionListener(this);
        isConnectedMenu.addActionListener(this);
        centerMenu.addActionListener(this);
        tspMenu.addActionListener(this);
        verticesTableMenu.addActionListener(this);
        edgesTableMenu.addActionListener(this);

        /**
         * Keyboard shortcuts:
         */
        fileMenu.setMnemonic(KeyEvent.VK_F);
        loadMenu.setMnemonic(KeyEvent.VK_F1);
        saveItem.setMnemonic(KeyEvent.VK_S);
        exitItem.setMnemonic(KeyEvent.VK_DELETE); //pressing delete will close the program.
        helpMenu.setMnemonic(KeyEvent.VK_H);



        G1.addActionListener(this);
        G2.addActionListener(this);
        G3.addActionListener(this);
        /**
         * add each menu to the menu bar.
         */
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(runMenu);
        menuBar.add(helpMenu);


        this.setJMenuBar(menuBar);

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
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 7);
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

    public ImageIcon[] getGifs() {
        return gifs;
    }
}