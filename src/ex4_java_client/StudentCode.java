package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */

import org.json.JSONObject;

import java.io.IOException;

public class StudentCode implements Runnable {
    private static Client client;
    private static long id;
    private static int lvl, agents_num, pokes_num;

    public static void main(String[] args) {
        client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread player = new Thread(new StudentCode());
        player.start();
    }


    public static void login() {
        String temp = String.valueOf(id);
        String id_new = "";
        if (id == -1 || temp.length() != 9) {
            System.out.println("Please Enter your ID");
        } else {
            id = Long.parseLong(id_new);
        }
    }


    /**
     * The method initializes the game's properties, such as: typed player's id, stage number, etc...
     *
     * @param client gets a client reference, which communicates with the server.
     */
    public void setStageProps(Client client) {
        String info = client.getInfo();
        JSONObject obj = new JSONObject(info);

        id = obj.getInt("id");
        lvl = obj.getInt("game_level");
        pokes_num = obj.getInt("pokemons");
        agents_num = obj.getInt("agents");
    }


    @Override
    public void run() {

        //setStageProps(client);
        //login();
        StageController stageController = new StageController(client);
        client.start();
        int frames = 0;


        // Game loop
        while (client.isRunning().equals("true")) {
            stageController.moveAgents();
            stageController.checkIfNear();
            int gdt = stageController.getDiffTime();
            System.out.println(client.getAgents());
            try{
                Thread.sleep(gdt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frames++;
        }




    }
}
