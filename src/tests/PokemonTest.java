package tests;

import api.*;
import ex4_java_client.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {
    private final double EPSILON = .0001;
    private DirectedWeightedGraph graph;
    private Pokemon pokemon;

    public PokemonTest() {

        graph = new DirectedWeightedGraphImpl(new HashMap<>(), new HashMap<>());
        // vertices //
        int node_id = 0, node_id2 = 1, tag = 2;
        String info = "info_v";
        double weight = 1.42321;
        GeoLocation p = new GeoLocationImpl(3,5,0);
        GeoLocation p2 = new GeoLocationImpl(3,0,0);

        NodeData v = new NodeDataImpl(node_id,tag,info, weight,p);
        NodeData v2 = new NodeDataImpl(node_id2,tag,info, weight,p2);

        graph.addNode(v);
        graph.addNode(v2);
        graph.connect(0, 1, 1.463541);
        graph.connect(1, 0, 2.12654);

        double val = 5.0;
        int type = 1;
        pokemon = new Pokemon(val, type, new GeoLocationImpl(3, 2, 0));

        pokemon.setType(1); // 0->1
        pokemon.updateEdge(graph);
    }


    @Test
    void isOnEdge() {
        assertTrue(pokemon.isOnEdge(graph.getEdge(0,1),graph));
        assertFalse(pokemon.isOnEdge(graph.getEdge(1,0),graph));
    }

    @Test
    void getValue() {
        assertEquals(pokemon.getValue(), 5.0);
    }

    @Test
    void getPos() {
        assertEquals(pokemon.getPos().toString(), new GeoLocationImpl(3, 2, 0).toString());
    }

    @Test
    void getType() {
        assertEquals(pokemon.getType(), 1);
        assertNotEquals(pokemon.getType(), -1);
    }

    @Test
    void setPos() {
        pokemon.setPos(new GeoLocationImpl(3, 4, 0));
        pokemon.setType(-1);
        assertNotEquals(pokemon.getType(), 1);
        assertEquals(pokemon.getPos().y(), 4);
        assertEquals(pokemon.getPos().x(), 3);
        pokemon.updateEdge(graph);
        assertTrue(pokemon.isOnEdge(graph.getEdge(1,0),graph));
    }

    @Test
    void getCurrEdge() {
        assertEquals(graph.getEdge(0,1),pokemon.getCurrEdge());
    }

    @Test
    void setCurrEdge() {
        pokemon.setCurrEdge(graph.getEdge(1,0));
        assertEquals(pokemon.getCurrEdge(), graph.getEdge(1,0));
    }

    @Test
    void setType() {
        pokemon.setType(-1);
        assertEquals(pokemon.getType(), -1);
        pokemon.setType(1);
        assertEquals(pokemon.getType(), 1);
    }

    @Test
    void setValue() {
        pokemon.setValue(10.0);
        assertEquals(pokemon.getValue(), 10.0);
        pokemon.setValue(3.0);
        assertEquals(pokemon.getValue(), 3.0);
    }

    @Test
    void getDist() {
        pokemon.setDist(1.2143654);
        assertEquals(pokemon.getDist(), 1.2143654);
        pokemon.setDist(2345);
        assertEquals(pokemon.getDist(), 2345);
    }

    @Test
    void setDist() {
        pokemon.setDist(124);
        assertNotEquals(pokemon.getDist(), 151);
        pokemon.setDist(5.4654);
        assertEquals(pokemon.getDist(), 5.4654);
    }

    @Test
    void testEquals() {
        double val = 5.0;
        int type = 1;
        Pokemon other = new Pokemon(val, type, new GeoLocationImpl(3, 2, 0));
        assertFalse(pokemon.equals(other));
        val = 5.1;
        type = -1;
        other = new Pokemon(val, type, new GeoLocationImpl(3, 2, 0));
        other.setCurrEdge(graph.getEdge(0,1));
        assertTrue(pokemon.equals(other));
    }
}