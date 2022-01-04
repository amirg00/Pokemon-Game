package ex4_java_client;

import api.DirectedWeightedGraph;
import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;

public class Agent {

    private int id, src, dest;
    private double value, speed;
    private GeoLocation pos;
    private ArrayList<NodeData> path;
    private DirectedWeightedGraph graph;


    /******** Constructor *********/

    public Agent(int id, double value, int src, int dest, double speed, GeoLocation pos) {
        this.id = id;
        this.src = src;
        this.dest = dest;
        this.value = value;
        this.speed = speed;
        this.pos = pos;
    }

    public Agent(DirectedWeightedGraph g, int startNode){

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
}
