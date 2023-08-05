package io.itch.mgdsstudio.battlecity.game.net;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.net.Client;

class PlayerClient extends Net{
    private Client client;
    private final static String DEFAULT_IP =  "127.0.0.1";

    PlayerClient(IEngine engine, int playerNumber) {
        init(engine, DEFAULT_IP, DEFAULT_DESKTOP_PORT, playerNumber);
    }

     PlayerClient(IEngine engine, String ip, int port, int playerNumber) {
        init(engine, ip, port, playerNumber);
    }

    private void init(IEngine engine, String ip, int port, int playerNumber){
        playerNumberInMultiplayerMode = playerNumber;
        try {
            client = new Client(engine.getProcessing(), ip, port);
            Logger.debug("This user with the number "+ playerNumber+ " added as a client. It is active: " + client.active());
        }
        catch (Exception e){
            Logger.error("This user can not be added as a client. " + e);
        }

    }



    public void update(){
        if (activated) {
            if (client != null) {
                if (client.available() > 0) {
                    incomingData = client.readString();
                }
                if (incomingData != null) {
                    Logger.net("Client "+ playerNumberInMultiplayerMode + "  got data: " + incomingData);
                    encryptData();
                }
            }
            else Logger.net("Client is null: ");
        }
        else Logger.net("Client is not activated: ");
    }

    @Override
    public void sendData(String data){  //To server
        if (activated) {
            if (client != null) {
                client.write(data);
                Logger.net("Data " + data + " was sent to the server");
            }
        }
        else {
            Logger.error("Client is deactivated. Can not send data");
        }
    }
}
