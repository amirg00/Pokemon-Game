package tests;

import api.*;
import ex4_java_client.Agent;
import ex4_java_client.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    private final double EPSILON = .0001;
    private DirectedWeightedGraph graph;
    private Pokemon pokemon;
    private Agent agent;
    public AgentTest() {

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

        agent = new Agent(0, 0.0, 0, -1, 1.0, graph.getNode(1).getLocation());
    }


    @Test
    void isOnWay() {
        assertFalse(agent.isOnWay());
    }

    @Test
    void isCloseToPokemon() {
        assertFalse(agent.isCloseToPokemon(pokemon));
    }

    @Test
    void getPos() {
        assertNotEquals(agent.getPos(), pokemon.getPos());
        agent.setPos(new GeoLocationImpl(3,2,0));
        assertEquals(agent.getPos().toString(), pokemon.getPos().toString());
        agent.setPos(new GeoLocationImpl(1,2,0));
        assertEquals(agent.getPos().toString(), new GeoLocationImpl(1,2,0).toString());
    }

    @Test
    void getValue() {
        assertEquals(agent.getValue(), 0.0);
        agent.setValue(12.0);
        assertEquals(agent.getValue(), 12.0);
        agent.setValue(24.0);
        assertEquals(agent.getValue(), 24.0);
        agent.setValue(39.0);
        assertEquals(agent.getValue(), 39.0);
        agent.setValue(72.0);
        assertEquals(agent.getValue(), 72.0);
    }

    @Test
    void getSpeed() {
        assertEquals(agent.getSpeed(), 1.0);
        agent.setSpeed(2.0);
        assertEquals(agent.getSpeed(), 2.0);
        agent.setSpeed(3.0);
        assertEquals(agent.getSpeed(), 3.0);
        agent.setSpeed(4.0);
        assertEquals(agent.getSpeed(), 4.0);
        agent.setSpeed(5.0);
        assertEquals(agent.getSpeed(), 5.0);
    }

    @Test
    void getDest() {
        assertEquals(agent.getDest(), -1);
        agent.setDest(2);
        assertEquals(agent.getDest(), 2);
    }

    @Test
    void getId() {
        assertEquals(agent.getId(), 0);
        agent.setId(3);
        assertEquals(agent.getId(), 3);
    }

    @Test
    void getSrc() {
        assertEquals(agent.getSrc(), 0);
        agent.setSrc(1);
        assertEquals(agent.getSrc(), 1);
    }

}