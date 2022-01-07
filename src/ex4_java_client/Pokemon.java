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
 * Pokemon Class contains all the relevant information for the Pokemon such as Value(how much worth the pokemon)
 *  Geographical location, current edge the pokemon is placed.
 * The number of points he will add based on the value of Pokémon production
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
     * This method over the all edges and update from the edge iterator the current edges.
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
