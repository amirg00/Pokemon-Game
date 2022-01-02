package ex4_java_client;

import api.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Stage {

    private DirectedWeightedGraph map;
    private DirectedWeightedGraphAlgorithms algo;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Agent> agents;


    public Stage (){
        algo = new DirectedWeightedGraphAlgorithmsImpl();



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



    public void initAgents(String json){
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

    public static void main(String[] args) {
        Stage s = new Stage();
        String graph = "{\"Edges\":[{\"src\":0,\"w\":1.4004465106761335,\"dest\":1},{\"src\":0,\"w\":1.4620268165085584,\"dest\":10},{\"src\":1,\"w\":1.8884659521433524,\"dest\":0},{\"src\":1,\"w\":1.7646903245689283,\"dest\":2},{\"src\":2,\"w\":1.7155926739282625,\"dest\":1},{\"src\":2,\"w\":1.1435447583365383,\"dest\":3},{\"src\":3,\"w\":1.0980094622804095,\"dest\":2},{\"src\":3,\"w\":1.4301580756736283,\"dest\":4},{\"src\":4,\"w\":1.4899867265011255,\"dest\":3},{\"src\":4,\"w\":1.9442789961315767,\"dest\":5},{\"src\":5,\"w\":1.4622464066335845,\"dest\":4},{\"src\":5,\"w\":1.160662656360925,\"dest\":6},{\"src\":6,\"w\":1.6677173820549975,\"dest\":5},{\"src\":6,\"w\":1.3968360163668776,\"dest\":7},{\"src\":7,\"w\":1.0176531013725074,\"dest\":6},{\"src\":7,\"w\":1.354895648936991,\"dest\":8},{\"src\":8,\"w\":1.6449953452844968,\"dest\":7},{\"src\":8,\"w\":1.8526880332753517,\"dest\":9},{\"src\":9,\"w\":1.4575484853801393,\"dest\":8},{\"src\":9,\"w\":1.022651770039933,\"dest\":10},{\"src\":10,\"w\":1.1761238717867548,\"dest\":0},{\"src\":10,\"w\":1.0887225789883779,\"dest\":9}],\"Nodes\":[{\"pos\":\"35.18753053591606,32.10378225882353,0.0\",\"id\":0},{\"pos\":\"35.18958953510896,32.10785303529412,0.0\",\"id\":1},{\"pos\":\"35.19341035835351,32.10610841680672,0.0\",\"id\":2},{\"pos\":\"35.197528356739305,32.1053088,0.0\",\"id\":3},{\"pos\":\"35.2016888087167,32.10601755126051,0.0\",\"id\":4},{\"pos\":\"35.20582803389831,32.10625380168067,0.0\",\"id\":5},{\"pos\":\"35.20792948668281,32.10470908739496,0.0\",\"id\":6},{\"pos\":\"35.20746249717514,32.10254648739496,0.0\",\"id\":7},{\"pos\":\"35.20319591121872,32.1031462,0.0\",\"id\":8},{\"pos\":\"35.19597880064568,32.10154696638656,0.0\",\"id\":9},{\"pos\":\"35.18910131880549,32.103618700840336,0.0\",\"id\":10}]}\n";
        s.initStageGraph(graph);
    }

}
