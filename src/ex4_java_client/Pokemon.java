package ex4_java_client;
import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import java.util.Iterator;

/*************************************************
 * Pokemon Class                                 *
 *                                               *
 * @author  Gal Koaz , Amir Gillette             *
 * @version 1.0                                  *
 * @since   07-01-2022                           *
 *************************************************/


/*
 *  Class details: Pokemon Class contains all the relevant information for the Pokemon,
 *  such as Value(how much worth the pokemon), Geographical location, current edge the pokemon is placed.
 *  Moreover, the number of points he will add based on the value of Pokémon production.
 */

public class Pokemon {

    private final double EPSILON = .000001;
    private double value;
    private int type;
    private GeoLocation pos;
    private EdgeData currEdge;
    private double dist;


    /******** Constructor *********/

    public Pokemon(double value, int type, GeoLocation pos){
        this.value = value;
        this.type = type;
        this.pos = pos;
    }

    /**
     * This method goes over the all edges and update from the edge iterator their current edges.
     * @param graph the stage graph.
     */
    public void updateEdge(DirectedWeightedGraph graph){
        Iterator<EdgeData> edges = graph.edgeIter();
        while(edges.hasNext()){
            EdgeData curr = edges.next();
            if (isOnEdge(curr, graph)){
                setCurrEdge(curr);
            }
        }
    }

    /**
     * The method checks whether the current pokemon is onto the edge (i.e. whithin epsilon enviroment).
     *
     * Exceptional cases:
     *                  1. if the type of the pokemon is 1, and we get the inverse meaning where src > dest,
     *                  then we cannot place the pokemon there, by def.
     *
     *                  2. if the type of the pokemon is -1, and we get the inverse meaning where dest > src,
     *                     then we cannot place the pokemon there, by def.
     *
     * Otherwise, we can place the pokemon there, and therefore, we take each node's geolocation points,
     * and check if the pokemon on the edge (on epsilon enviroment of the edge), by the following math condition:
     * if the total edge between the nodes plus epsilon is bigger then the addition of the nodes to the pokemon,
     * it means that the pokemon inside epsilon enviroment, since the cos(x) which is the different
     * between the original edge to the current, isn't big.
     *
     * @param edge a certain edge.
     * @param graph the stage graph.
     * @return True,False.
     */
    public boolean isOnEdge(EdgeData edge, DirectedWeightedGraph graph){
        NodeData srcNode = graph.getNode(edge.getSrc());
        NodeData destNode = graph.getNode(edge.getDest());

        if(type == 1 && srcNode.getKey() > destNode.getKey()){
            return false;
        }

        if (type == -1 && destNode.getKey() > srcNode.getKey()){
            return false;
        }

        GeoLocation p_src = srcNode.getLocation();
        GeoLocation p_dst = destNode.getLocation();

        double edge_size = p_src.distance(p_dst);
        double partOfEdge_1 = p_src.distance(pos);
        double partOfEdge_2 = pos.distance(p_dst);

        return edge_size + EPSILON > partOfEdge_1 + partOfEdge_2;
    }

    /******** Getters & Setters *********/

    public double getValue() {
        return value;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public int getType() {
        return type;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    public EdgeData getCurrEdge() {return currEdge;}

    public void setCurrEdge(EdgeData currEdge) {this.currEdge = currEdge;}

    public void setType(int type) {
        this.type = type;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }
    /************* Equals ***************/

    /**
    * @param other:
     * @return if the current edges are equals.
    * */
    public boolean equals(Pokemon other){
        return this.getCurrEdge().equals(other.getCurrEdge());
    }

}
