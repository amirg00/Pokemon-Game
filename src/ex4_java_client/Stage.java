package ex4_java_client;

import api.*;
import org.json.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Stage {

    private final double EPSILON = .001;
    private DirectedWeightedGraph map;
    private DirectedWeightedGraphAlgorithms algo;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Agent> agents;
    private Client game;
    private int agents_num;

    public Stage (Client game){
        algo = new DirectedWeightedGraphAlgorithmsImpl();
        this.game = game;
        setStageProps(game);
        initStageGraph(game.getGraph());
        initPokemons(game.getPokemons());
        initPokemonsEdge();
        pokemons.sort(Comparator.comparingDouble(Pokemon::getValue));
        algo.init(map);
        initAgents();
        updateAgents(game.getAgents());
    }




    public void initPokemonsEdge(){
        for(Pokemon pokemon : pokemons){
            pokemon.updateEdge(map);
        }
    }

    /**
     * The method initializes the game's properties, such as: agents number, stage number.
     * @param client gets a client reference, which communicates with the server.
     */
    public void setStageProps(Client client){
        String info = client.getInfo();
        JSONObject obj = new JSONObject(info);
        JSONObject game_details = obj.getJSONObject("GameServer");
        agents_num = game_details.getInt("agents");
    }

    public void initStageGraph(String json){
        map = new DirectedWeightedGraphImpl(new HashMap<>(), new HashMap<>());
        JSONObject obj = new JSONObject(json);

        JSONArray edges = obj.getJSONArray("Edges");
        JSONArray nodes = obj.getJSONArray("Nodes");

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            int id = node.getInt("id");
            String[] pos = node.getString("pos").split(",");
            double pos_x = Double.parseDouble(pos[0]);
            double pos_y = Double.parseDouble(pos[1]);
            double pos_z = Double.parseDouble(pos[2]);
            String info = "ID:" + id + ",\n" + "POS:" + pos_x + "," + pos_y + "," + pos_z;
            GeoLocation gp = new GeoLocationImpl(pos_x, pos_y, pos_z);
            map.addNode(new NodeDataImpl(id, Integer.MAX_VALUE,info, Double.MAX_VALUE,gp));
        }

        for (int i = 0; i < edges.length(); i++) {
            JSONObject edge = edges.getJSONObject(i);
            int src = edge.getInt("src");
            double weight = edge.getDouble("w");
            int dest = edge.getInt("dest");
            map.connect(src, dest, weight);
        }

    }

    /**
     * if type = 1 -> low to high
     * else type = -1 -> high to low.
     */
    public void initAgents() {
        for (int i = agents_num; i >= 0; i--) {
             Pokemon poke = pokemons.get(i);
             EdgeData poke_edge = poke.getCurrEdge();
             int src = poke_edge.getSrc();
             int dst = poke_edge.getDest();
             int nodeNum = poke.getType() == 1 ? Math.min(src, dst): Math.max(src, dst);
             game.addAgent("{\"id\":"+nodeNum+'}');
        }
    }

    public void updateAgents(String json){
        agents = new ArrayList<>();
        JSONObject obj = new JSONObject(json);
        JSONArray agents = obj.getJSONArray("Agents");

        for (int i = 0; i < agents.length(); i++) {
            JSONObject agent = agents.getJSONObject(i);
            JSONObject agent_values = agent.getJSONObject("Pokemon");
            int id = agent_values.getInt("id");
            double val = agent_values.getInt("value");
            int src = agent_values.getInt("src");
            int dest = agent_values.getInt("dest");
            double speed = agent_values.getDouble("speed");

            String[] pos = agent_values.getString("pos").split(",");
            double pos_x = Double.parseDouble(pos[0]);
            double pos_y = Double.parseDouble(pos[1]);
            double pos_z = Double.parseDouble(pos[2]);
            GeoLocation gp = new GeoLocationImpl(pos_x, pos_y, pos_z);
            this.agents.add(new Agent(id, val, src, dest, speed, gp));
        }
    }



    public void initPokemons(String json){
        pokemons = new ArrayList<>();
        JSONObject obj = new JSONObject(json);
        JSONArray pokes = obj.getJSONArray("Pokemons");

        for (int i = 0; i < pokes.length(); i++) {
            JSONObject pokemon = pokes.getJSONObject(i);
            JSONObject poke_values = pokemon.getJSONObject("Pokemon");
            double val = poke_values.getDouble("value");
            int type = poke_values.getInt("type");
            String[] pos = poke_values.getString("pos").split(",");
            double pos_x = Double.parseDouble(pos[0]);
            double pos_y = Double.parseDouble(pos[1]);
            double pos_z = Double.parseDouble(pos[2]);
            GeoLocation gp = new GeoLocationImpl(pos_x, pos_y, pos_z);
            pokemons.add(new Pokemon(val, type, gp));
        }
    }
}
