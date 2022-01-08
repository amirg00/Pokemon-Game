package ex4_java_client;

import GraphGui.FrameGraph;
import GraphGui.PanelGraph;

import java.io.IOException;

/*************************************************
 * Agent Class                                   *
 *                                               *
 * @author  Gal Koaz , Amir Gillette             *
 * @version 1.0                                  *
 * @since   07-01-2022                           *
 *************************************************/


public class GameEngine implements Runnable {
    public static Client client;
    private FrameGraph world;
    private static Thread player;
    private static long intialTime;

    public static void main(String[] args) {
        client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = new Thread(new GameEngine());
        player.start();
    }

    @Override
    public void run() {
        StageController stageController = new StageController(client);
        world = new FrameGraph(stageController.getMap(), stageController);
        world.PlayButtonPressed(false);

        while(!world.getPlayButtonState()){
            Thread.onSpinWait();
        }
        world.PlayButtonPressed(true);
        client.start();
        intialTime = 1 + (Integer.parseInt(client.timeToEnd())/1000);

        // Game loop
        while (client.isRunning().equals("true")) {
            if (PanelGraph.isStopButtonPressed()){
                player.stop();
            }

            stageController.moveAgents();
            stageController.setGameServerDetails(client.getInfo(), client.timeToEnd());
            if (stageController.getMoves() == intialTime * 10){break;}
            world.repaint();
            int gdt = stageController.checkIfNear()? 30 : 100;
            try{
                Thread.sleep(gdt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        client.stop();
    }
}