package ex4_java_client;

import api.GeoLocation;

public class Pokemon {

    private double value;
    private int type;
    private GeoLocation pos;



    /******** Constructor *********/

    public Pokemon(double value, int type, GeoLocation pos){
        this.value = value;
        this.type = type;
        this.pos = pos;
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

    public void setType(int type) {
        this.type = type;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
