package ex4_java_client;
import api.*;
import org.json.*;
import java.util.*;

/*************************************************
 * StageController Class                         *
 *                                               *
 * @author  Gal Koaz , Amir Gillette             *
 * @version 1.0                                  *
 * @since   07-01-2022                           *
 *************************************************/


/**
 * Class details: this class is the controller of MVC implementation.
 */
public class StageController {

    private DirectedWeightedGraph map;
    private DirectedWeightedGraphAlgorithms algo;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Agent> agents;
    private Client game;
    private int agents_num;
    private HashMap<Integer, Pokemon> agentsDest;
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
     * The method initializes the game's agents amount.
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
     * Since initialization of the agents with addAgent server's method is needed,
     * when starting the game, we iterate the pokemons from the biggest pokemon's value
     * til we reached the agents number.
     *
     * The method wisely takes the pokemons which have the biggest value,
     * and places the agent there, so when starting the game we can see
     * that the agents are firstly placed at the nodes which connected
     * on edges, that have the pokemons with the biggest values.
     *
     * The method checks the pokemon's type then:
     *      if type = 1 -> low to high (means with take the minimum between src and dst nodes)
     *      else type = -1 -> high to low (means with take the maximum between src and dst nodes).
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

    /**
     * The method reads the json string line, which got from the server,
     * and initializes the updates the agents, this is why we create a new
     * agents list to contain the agents.
     * @param json gets a json file as a string line, which has the agents.
     */
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


    /**
     * The method reads the json string line, which got from the server,
     * and initializes the current pokemons, this is why we create a new
     * pokemons list to contain the pokemones.
     *
     * Additionally, we update the pokemons' current edge, to know where they
     * are located on the map, in terms of on which edge they are.
     *
     * @param json gets a json file as a string line, which has the pokemons.
     */
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

    /**
     * This method moves each agent following chooseNextEdgeAlgo method.
     * The method first initializes the current agents and pokemons,
     * and then check if the agent is not on a way to a certain pokemon,
     * if so we call the algorithm to choose for the agent a next edge.
     * Finally, the method moves each agent to his next destination.
     */
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
     * This method uses dijkstra's algorithm in order to check the next edge.
     * The method initializes a priority queue with the pokemon which always compares
     * the pokemon's current shortest path from the current position of the agent
     * towards the pokemon (pokemon's edge inclusive - 'addition').
     * After going over each pokemon, the method checks if there is an available pokemon,
     * and if so she takes the one with the lowest dist value, which means that he is the closest
     * to the agent current position.
     *
     * In addition, if there isn't an available pokemon, then the method moves the agent to his dest,
     * just to make him move and not stuck where there isn't an available pokemon.
     *
     * If we indeed succeeded taking a such closest pokemon, we take the path(1) which means we take
     * the destination of first edge in the path, because we need to move the agent to a neighbour.
     *
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

    /**
     * Method checks if there is an agent close to a pokemon.
     * @return true iff there is an agent closes to a pokemon, o.w. returns false.
     */
    public boolean checkIfNear(){
        for (Agent agent : agents){
            for(Pokemon poke : pokemons){
                if (agent.isCloseToPokemon(poke)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The method sets the game server details to the controller's properties (moves, grade, lvl, grade).
     * @param info the game server details as a string given by the server.
     * @param timeMilli time in milliseconds, also given as a string.
     */
    public void setGameServerDetails(String info, String timeMilli){
        JSONObject obj = new JSONObject(info);
        JSONObject game_details = obj.getJSONObject("GameServer");
        moves = game_details.getInt("moves");
        grade = game_details.getInt("grade");
        lvl = game_details.getInt("game_level");
        time = Integer.parseInt(timeMilli)/1000;
    }

    /******** Getters & Setters *********/

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

    public DirectedWeightedGraph getMap() {
        return map;
    }
}
