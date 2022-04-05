package GraphGui;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import ex4_java_client.Agent;
import ex4_java_client.Pokemon;
import ex4_java_client.StageController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PanelGraph extends JPanel {
    private DirectedWeightedGraph graph;
    private HashMap<Integer,GraphPoint> points;
    private List<GraphEdge> edges;
    private int radius = 6; //good for 100 vertices.
    private double Phi = Math.toRadians(40);
    private double virtualScale = 1.0;
    private Graphics2D g2d;
    private Point2D minRange;
    private Point2D maxRange;
    private double insets;
    private StageController stage;
    private static boolean stopButtonPressed = false;



    // GIFS:
    private final ImageIcon[] gifs = new ImageIcon[]{
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/AshRunner.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Bulbasaur.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Charmander.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Squirtle.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Ivysaur.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Charmeleon.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Wartortle.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Venusaur.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Charizard.gif"))),
            new ImageIcon(Objects.requireNonNull(getClass().getResource("/GraphGui/Icons/Blastoise.gif")))
    };


    // GIFS:
    private final AnimatedGif[] animationGifs = new AnimatedGif[]{
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/AshRunner.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Bulbasaur.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Charmander.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Squirtle.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Ivysaur.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Charmeleon.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Wartortle.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Venusaur.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Charizard.gif")),
            new AnimatedGif(this, getClass().getResource("/GraphGui/Icons/Blastoise.gif"))
    };

    // GIFS:
    private final String[] urls = new String[]{
            "/GraphGui/Icons/Bulbasaur.gif",
            "/GraphGui/Icons/Charmander.gif",
            "/GraphGui/Icons/Squirtle.gif",
            "/GraphGui/Icons/Ivysaur.gif",
            "/GraphGui/Icons/Charmeleon.gif",
            "/GraphGui/Icons/Wartortle.gif",
            "/GraphGui/Icons/Venusaur.gif",
            "/GraphGui/Icons/Charizard.gif",
            "/GraphGui/Icons/Blastoise.gif"
    };


    PanelGraph(DirectedWeightedGraph graph, StageController stage) throws IOException {
        this.setPreferredSize(new Dimension(1100,750));
        this.points = new HashMap<>();
        this.graph = graph;
        this.stage = stage;
        pointInit();
        EdgeInit();
        setMinMaxRange();
        int numberOfZeros = (int) Math.log10(graph.nodeSize());
        if (numberOfZeros>2){radius = (int) (radius/Math.pow(2,numberOfZeros-2));}
        JButton btn = new JButton("Stop");
        btn.setFont(new Font("Calibri", Font.PLAIN, 20));
        btn.setBackground(Color.red);
        btn.setForeground(Color.BLACK);
        btn.setUI(new StyledButtonUI());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.add(btn);
        btn.addActionListener(e -> {
            System.out.println("pressed!");
            setStopButtonPressed(true);
        });

        // Start gifs
        for(AnimatedGif animationGif: animationGifs){
            animationGif.play();
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100,750);
    }

    /********************************************************************************************************
     * <paintComponent>
     * paintComponent method draw the all edges and points in the graph.
     * </paintComponent>
     *
     *********************************************************************************************************/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g.create();
        FontMetrics fm = g2d.getFontMetrics();
        insets = fm.getHeight() + radius;

        // Background:
        Image background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/background.png"));
        g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

        // Draw arena section:
        ArrayList<String> LineSave = new ArrayList<>();
        for (GraphEdge ed : edges) {
            ArrayList<GraphPoint> p = ed.getPoints();
            LineSave.add(""+p.get(0).getId()+"-"+p.get(1).getId());
            paintLine(g2d, p.get(0), p.get(1), insets, ed.getWeight(),LineSave, ed.getTag(),2,ed.getTag_2());
        }
        for (GraphPoint gp : points.values()) {
            paintPoint(g2d, gp, insets, gp.getTag(), gp.getTag_2());
        }
        try {
            paintPoke(g, g2d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        paintAgent(g, g2d);

        g.drawString("Time: " + stage.getTime() + " sec",15,30);
        g.drawString("Total: " + stage.getGrade(),15,45);
        g.drawString("Moves: " + stage.getMoves(),15,60);

        g2d.dispose();
    }



    private boolean CheckerEdgeDraw(ArrayList<String> check,String name){
        for (int i = 0; i < check.size()-1; i++) {
            if(Objects.equals(check.get(i), name))return true;
        }
        return false;
    }

    private void paintPoke(Graphics g, Graphics2D g2d) throws IOException {
        List <Pokemon> pokemons = stage.getPokemons();
        if(!pokemons.isEmpty()){
            for (Pokemon p: pokemons) {
                if(p.getPos()!=null){
                    GraphPoint poke = new GraphPoint("Value: " + p.getValue(),new Point2D.Double(p.getPos().x(),p.getPos().y()),Color.red,Color.red);
                    paintPoint2(g2d,poke,insets,Color.red,Color.red, (int) p.getValue());
                }
            }
        }
    }

    /**
     * Method gets a value of a certain pokemon to draw, and pick
     * from the list of the pokemon, a pokemon which is corresponds to the given value.
     * @param value a given value of a certain pokemon.
     * @return the gif that found corresponded to the value.
     */
    public AnimatedGif getRandomPokemonByValue(int value){
        // 0 - 6 (not included), 6 - 11 (not included), 11 - 15 (last included)

        int chosenPokemon = 0;

        if (value < 3 ){
            chosenPokemon = 1;
        }
        else if (value < 5){
            chosenPokemon = 2;
        }
        else if (value < 6){
            chosenPokemon = 3;
        }
        else if (value < 8){
            chosenPokemon = 4;
        }

        else if (value == 9){
            chosenPokemon = 5;
        }

        else if (value == 10){
            chosenPokemon = 6;
        }

        else if (value < 13){
            chosenPokemon = 7;
        }
        else if (value < 15){
            chosenPokemon = 8;
        }
        else if (value < 20){
            chosenPokemon = 9;
        }

        return animationGifs[chosenPokemon];
    }

    private void paintAgent(Graphics g,Graphics2D g2d){
        List<Agent> ag = stage.getAgents();
        int i = 0;
        while(i < ag.size()){
           if(ag.get(i).getPos() != null){
               GraphPoint agent = new GraphPoint("Agent "+String.valueOf(i) + " (" + ag.get(i).getValue()+")",new Point2D.Double(ag.get(i).getPos().x(),ag.get(i).getPos().y()),Color.black,Color.black);
               paintPoint3(g2d,agent,insets,Color.black,Color.black);
           }
            i++;
        }
    }

    protected double angleBetween(Point2D from, Point2D to) {
        double x = from.getX();
        double y = from.getY();
        double deltaX = to.getX() - x;
        double deltaY = to.getY() - y;
        double rotation = -Math.atan2(deltaX, deltaY);
        rotation = Math.toRadians(Math.toDegrees(rotation) + 180);
        return rotation;
    }

    protected Point2D getPointOnCircle(Point2D center, double radians) {
        double x = center.getX();
        double y = center.getY();
        radians = radians - Math.toRadians(90.0);
        double xPosy = Math.round((float) (x + Math.cos(radians) * radius));
        double yPosy = Math.round((float) (y + Math.sin(radians) * radius));
        return new Point2D.Double(xPosy, yPosy);
    }

    /********************************************************************************************************
     * @name: paintLine Method take the graphics2D, 2 points and weight.
     * we calculate the scale of the points in the graph because we want the graph accuracy.
     * we calculate the angel between two point in the graph and draw line to the tip of the point.
     *********************************************************************************************************/

    private void paintLine(Graphics2D g2d, GraphPoint from, GraphPoint to, double insets, String weight, ArrayList<String> list, Color color, int stroke, Color color_2) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //boolean flag = CheckEdge(to,from);
        boolean flag = CheckerEdgeDraw(list,to.getId()+"-"+from.getId());
        Point2D fromPoint = translate(from, insets);
        Point2D toPoint = translate(to, insets);
        double fromp = angleBetween(fromPoint, toPoint);
        double top = angleBetween(toPoint, fromPoint);
        Point2D pointFrom = getPointOnCircle(fromPoint, fromp);
        Point2D pointTo = getPointOnCircle(toPoint, top);
        Line2D line = new Line2D.Double(pointFrom, pointTo);

        g2d.setStroke(new BasicStroke(stroke));
        g2d.setColor(Color.black);
        g2d.draw(line);
        g2d.setStroke(new BasicStroke(1));
    }

    public static void drawRotate(Graphics2D g2d, double x, double y, double angle, String text) {
        g2d.translate((float)x,(float)y);
        g2d.rotate(angle);
        g2d.drawString(text,0,0);
        g2d.rotate(-angle);
        g2d.translate(-(float)x,-(float)y);
    }

    private static double[][] Verticle(double x1, double y1, double m_Segment, int length){
        double m = -1/m_Segment;
        // y = mx - mx1 + y1
        // System.out.println(1/Math.pow(m_Segment,2));
        double k = 1 + (1/Math.pow(m_Segment,2));
        // System.out.println(k);
        double x1_ans = x1 + (length/Math.sqrt(k));
        double x2_ans = x1 - (length/Math.sqrt(k));

        // finds the y for the resulted points.
        double y1_ans = m*x1_ans - m*x1 + y1;
        double y2_ans = m*x2_ans - m*x1 + y1;

        return new double[][]{{x1_ans,y1_ans},{x2_ans,y2_ans}};
    }

    void paintPoint(Graphics2D g2d, GraphPoint gp, double insets, Color color, Color color_2) {
        Graphics2D g2 = (Graphics2D) g2d.create();
        Point2D translated = translate(gp, insets);

        double xPos = translated.getX();
        double yPos = translated.getY();

        double offset = radius;

        g2.translate(xPos - offset, yPos - offset);
        g2.setPaint(color);
        g2.fill(new Ellipse2D.Double(0, 0, offset * 2, offset * 2));
        g2.setPaint(Color.black);
        g2.draw(new Ellipse2D.Double(0, 0, offset * 2, offset * 2));
        FontMetrics fm = g2d.getFontMetrics();
        String text = gp.getId();
        double x = xPos - (fm.stringWidth(text) / 2);
        double y = (yPos - radius - fm.getHeight()) + fm.getAscent();
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Cabin", Font.BOLD, 14));
        g2d.drawString(text, (float) x, (float) y);
        g2.dispose();
    }
    void paintPoint2(Graphics2D g2d, GraphPoint gp, double insets, Color color, Color color_2, int value) throws IOException {
        Graphics2D g2 = (Graphics2D) g2d.create();
        Point2D translated = translate(gp, insets);
        double xPos = translated.getX();
        double yPos = translated.getY();
        double offset = radius;
        g2.translate(xPos - offset, yPos - offset);

        // Draw pokemon with frames smooth
        AnimatedGif animatedGif = getRandomPokemonByValue(value);
        BufferedImage currentFrame = animatedGif.getCurrentFrame();
        g2.drawImage(currentFrame, 0, 0, (int)offset * 5, (int)offset * 5,this);

        g2.setPaint(color);
        g2.setPaint(Color.black);

        FontMetrics fm = g2d.getFontMetrics();
        String text = gp.getId();
        double x = xPos - (fm.stringWidth(text) / 2);
        double y = (yPos - radius - fm.getHeight()) + fm.getAscent();
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Cabin", Font.BOLD, 14));
        g2d.drawString(text, (float) x, (float) y);
        g2.dispose();
    }

    void paintPoint3(Graphics2D g2d, GraphPoint gp, double insets, Color color, Color color_2) {
        Graphics2D g2 = (Graphics2D) g2d.create();

        Point2D translated = translate(gp, insets);
        double xPos = translated.getX();
        double yPos = translated.getY();
        double offset = radius;

        g2.translate(xPos - offset, yPos - offset);
        Image image = gifs[0].getImage();
        g2.drawImage(image, 0, -55, (int)offset * 10, (int)offset * 10,this);
        FontMetrics fm = g2d.getFontMetrics();
        String text = gp.getId();
        double x = xPos - (fm.stringWidth(text) / 2);
        double y = (yPos - radius - fm.getHeight()) + fm.getAscent();
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Cabin", Font.BOLD, 14));
        g2d.drawString(text, (float) x, (float) y-40);
        g2.dispose();
    }



    protected Point2D translate(GraphPoint gp, double insets) {
        double xRange = maxRange.getX() - minRange.getX();
        double yRange = maxRange.getY() - minRange.getY();

        double offset = insets;
        double width = getWidth() - (offset * 2);
        double height = getHeight() - (offset * 2);

        double xScale = width / xRange;
        double yScale = height / yRange;

        Point2D original = gp.getPoint();

        double x = offset + ((original.getX() - minRange.getX()) * xScale);
        double y = offset + ((original.getY() - minRange.getY()) * yScale);

        // System.out.println(gp.getId() + " " + x + " x " + y);

        return new Point2D.Double(x, y);
    }

    /*
     * the for loop is init all the edges existed in the graph and take each point already exists,
     * make a new arraylist of points and know to the correct edges between them.
     * */
    public void EdgeInit() {
       edges = new ArrayList<>();
       Iterator<EdgeData> E = graph.edgeIter();
       while(E.hasNext()){
          EdgeData curr = E.next();
          ArrayList<GraphPoint> temp = new ArrayList<>();
          temp.add(new GraphPoint(String.valueOf(graph.getNode(curr.getSrc()).getKey()), new Point2D.Double(graph.getNode(curr.getSrc()).getLocation().x(), graph.getNode(curr.getSrc()).getLocation().y()),Color.blue, Color.red));
          temp.add(new GraphPoint(String.valueOf(graph.getNode(curr.getDest()).getKey()), new Point2D.Double(graph.getNode(curr.getDest()).getLocation().x(), graph.getNode(curr.getDest()).getLocation().y()),Color.blue,Color.red));
          edges.add(new GraphEdge(String.valueOf(curr.getWeight()), temp,Color.red, 1, Color.red));
       }
    }

    /*
     * the for loop is init all the points in the graph.
     * By given a name of the vertex and the positions x,y.
     * */
    public void pointInit() {
        Iterator<NodeData> nodes = graph.nodeIter();
        while (nodes.hasNext()){
            NodeData curr = nodes.next();
            if (curr != null) {
                String currKey = String.valueOf(curr.getKey());
                double currPosX = curr.getLocation().x();
                double currPosY = curr.getLocation().y();
                points.put(curr.getKey(),new GraphPoint(currKey, new Point2D.Double(currPosX, currPosY),Color.blue, Color.red));
            }
        }
    }

    /*
     * search if the point is equal to the string id  and get the point from the points objects.
     * draw each line have edges.
     */
    public void setMinMaxRange() {

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (GraphPoint gp : points.values()) {
            minX = Math.min(minX, gp.getPoint().getX());
            maxX = Math.max(maxX, gp.getPoint().getX());
            minY = Math.min(minY, gp.getPoint().getY());
            maxY = Math.max(maxY, gp.getPoint().getY());
        }

        minRange = new Point2D.Double(minX, minY);
        maxRange = new Point2D.Double(maxX, maxY);
    }

    public Graphics2D getG2d() {
        return g2d;
    }
    public double get_Insets(){
        return insets;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public HashMap<Integer,GraphPoint> getPoints() {
        return points;
    }


    public void setStopButtonPressed(boolean stopButtonPressed) {
        this.stopButtonPressed = stopButtonPressed;
    }

    public static boolean isStopButtonPressed() {
        return stopButtonPressed;
    }
}