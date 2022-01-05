package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */

import GUI.FrameGraph;
import org.json.JSONObject;

import java.io.IOException;

public class StudentCode implements Runnable {
    private static Client client;
    private static long id;
    private FrameGraph world;

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



    @Override
    public void run() {
        //setStageProps(client);
        //login();
        StageController stageController = new StageController(client);
        world = new FrameGraph(stageController.getMap());
        client.start();
        int frames = 0;


        // Game loop
        while (client.isRunning().equals("true")) {
            stageController.moveAgents();
            stageController.checkIfNear();
            world.repaint();
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
