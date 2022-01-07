package ex4_java_client; /**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */

import GraphGui.FrameGraph;
import GraphGui.Menu;
import GraphGui.PanelGraph;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StudentCode implements Runnable {
    public static Client client;
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
//        Menu menuGame = new Menu();
//        //Waiting 'til the user will press start button.
//        while(true){
//            if (menuGame.getPlayButtonState()){
//                break;
//            }
//        }
        //setStageProps(client);
        //login();

        StageController stageController = new StageController(client);
        world = new FrameGraph(stageController.getMap(), stageController);
        //Menu.centreWindow(world);
        world.PlayButtonPressed(false);

        while(!world.getPlayButtonState()){
            Thread.onSpinWait();
        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        world.PlayButtonPressed(true);

        client.start();
        int frames = 0;


        // Game loop
        while (client.isRunning().equals("true")) {
            stageController.moveAgents();
            stageController.checkIfNear();
            stageController.setGameServerDetails(client.getInfo(), client.timeToEnd());
            world.repaint();
            int gdt = stageController.getDiffTime();
            System.out.println(client.getAgents());
            System.out.println(gdt);
            try{
                Thread.sleep(gdt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frames++;
        }
        client.stop();
    }
}
