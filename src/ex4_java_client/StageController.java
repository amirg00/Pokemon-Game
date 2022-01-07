package ex4_java_client;

import api.*;
import org.json.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class StageController {

    private DirectedWeightedGraph map;
    private DirectedWeightedGraphAlgorithms algo;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Agent> agents;
    private Client game;
    private int agents_num;
    private HashMap<Integer, Pokemon> agentsDest;
    private int diffTime;
    private String info;
    private int lvl, grade, time, moves;


    public StageController(Client game){
        agentsDest = new HashMap<>();
        algo = new DirectedWeightedGraphAlgorithmsImpl();
        this.game = game;
        setStageProps(game);
        initStageGraph(game.getGraph());
        initPokemons(game.getPokemons());
        pokemons.sort(Comparator.comparingDouble(Pokemon::getValue));
        algo.init(map);
        initAgents();
        updateAgents(game.getAgents());
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
        for (int i = pokemons.size() - 1, m = 0; i >= 0; i--,m++) {
             if (m == agents_num) {return;}
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
        agentsDest = new HashMap<>();
        JSONObject obj = new JSONObject(json);
        JSONArray agents = obj.getJSONArray("Agents");

        for (int i = 0; i < agents.length(); i++) {
            JSONObject agent = agents.getJSONObject(i);
            JSONObject agent_values = agent.getJSONObject("Agent");
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
            Pokemon poke = new Pokemon(val, type, gp);
            poke.updateEdge(map);
            pokemons.add(poke);
        }
    }

    public void moveAgents(){
        updateAgents(game.getAgents());
        initPokemons(game.getPokemons());

        for(Agent agent : agents){
            if (!agent.isOnWay()){
                int dstNode = chooseNextEdgeAlgo(agent);
                game.chooseNextEdge("{\"agent_id\":" + agent.getId() + ", \"next_node_id\":" + dstNode + '}');
                agent.setSrc(agent.getDest());
                agent.setDest(dstNode);
            }
        }
        game.move();
    }

    /**
     *
     * @param agent current agent.
     * @return the next node's id that agent needs to go to.
     */
    public int chooseNextEdgeAlgo(Agent agent){
        PriorityQueue<Pokemon> leastDist = new PriorityQueue<>(Comparator.comparingDouble(Pokemon::getDist));
        for (int p = pokemons.size() - 1; p >= 0; p--){
            Pokemon currPoke = pokemons.get(p);
            if(isPokemonAvailable(pokemons.get(p))) {
                EdgeData curr_poke_edge = currPoke.getCurrEdge();
                double addition = curr_poke_edge.getWeight();
                currPoke.setDist(algo.shortestPathDist(agent.getSrc(), curr_poke_edge.getSrc()) + addition);
                leastDist.add(currPoke);
            }
        }
        // exceptional case:
        if (leastDist.isEmpty()) {
            return map.edgeIter(agent.getSrc()).next().getDest();
        }

        Pokemon leastPoke = leastDist.poll();
        if (agent.getSrc() == leastPoke.getCurrEdge().getDest()){
            return leastPoke.getCurrEdge().getSrc();
        }

        agentsDest.put(agent.getId(), leastPoke);
        return new ArrayList<>(algo.shortestPath(agent.getSrc(), leastPoke.getCurrEdge().getDest())).get(1).getKey();
    }

    /**
     * The method checks whether the given pokemon is available,
     * or it isn't when a certain agent on the way to catch the given pokemon.
     * @param poke a given pokemon.
     * @return true iff the pokemon is available, o.w. return false.
     */
    public boolean isPokemonAvailable(Pokemon poke){
        for (Pokemon agentPoke : agentsDest.values()){
            if (poke.equals(agentPoke)){
                return false;
            }
        }
        return true;
    }

    //TODO: complete documentation...
    /**
     * Method checks if there is an agent close to a pokemon.
     */
    public void checkIfNear(){
        for (Agent agent : agents){
            for(Pokemon poke : pokemons){
                if (agent.isCloseToPokemon(poke)){
                    setDiffTime(20);
                    return;
                }
            }
        }
        setDiffTime(120);
    }

    public int getDiffTime() {return diffTime;}

    public void setDiffTime(int diffTime) {this.diffTime = diffTime;}

    public DirectedWeightedGraph getMap() {
        return map;
    }

    public void setGameServerDetails(String info, String timeMilli){
        JSONObject obj = new JSONObject(info);
        JSONObject game_details = obj.getJSONObject("GameServer");
        moves = game_details.getInt("moves");
        grade = game_details.getInt("grade");
        lvl = game_details.getInt("game_level");
        time = Integer.parseInt(timeMilli)/1000;
    }

    public int getTime() {
        return time;
    }

    public int getGrade() {
        return grade;
    }

    public int getMoves() {
        return moves;
    }

    public int getLvl() {
        return lvl;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }
}
