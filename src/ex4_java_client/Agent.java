package ex4_java_client;

import api.GeoLocation;

public class Agent {

    private int id,src,dest;
    private double value,speed;
    private GeoLocation pos;






    /******** getters and setters *********/

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
