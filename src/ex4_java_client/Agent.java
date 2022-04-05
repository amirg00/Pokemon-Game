package ex4_java_client;
import api.GeoLocation;


/*************************************************
 * Agent Class                                   *
 *                                               *
 * @author  Gal Koaz , Amir Gillette             *
 * @version 1.0                                  *
 * @since   07-01-2022                           *
 *************************************************/


/*
 *  Class details: Agent Class contains all the relevant information for the Agent,
 *  such as: speed factor Agent number, Geographical location, etc...
 *  In addition, the number of points he will add based on the value of Pokémon production.
 */

public class Agent {

    private final double EPSILON = .001;
    private int id, src, dest;
    private double value, speed;
    private GeoLocation pos;
    private String state;

    /******** Constructor *********/

    public Agent(int id, double value, int src, int dest, double speed, GeoLocation pos) {
        this.id = id;
        this.src = src;
        this.dest = dest;
        this.value = value;
        this.speed = speed;
        this.pos = pos;
        this.state = "right";
    }

    /**
     * @return true iff the agent is on the way to catch a pokemon (dest = 1), o.w. return false.
     */
    public boolean isOnWay() { return dest != -1;}

    /**
     *
     * @param poke a certain given pokemon.
     * @return true iff the given pokemon close to the current agent (i.e. if agent is whithin
     * epsilon enviroment of the given pokemon).
     */
    public boolean isCloseToPokemon(Pokemon poke){
        return poke.getCurrEdge().getSrc() == src
            && poke.getCurrEdge().getDest() == dest
            && pos.distance(poke.getPos()) < EPSILON * speed;
    }

    /******** Getters & Setters *********/

    public GeoLocation getPos() {
        return pos;
    }

    public double getValue() {
        return value;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDest() {
        return dest;
    }

    public int getId() {
        return id;
    }

    public int getSrc() {
        return src;
    }

    public String getState() {return state;}

    public void setValue(double value) {
        this.value = value;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void setState(String state) {this.state = state;}
}