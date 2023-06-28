package io.itch.mgdsstudio.battlecity.game.net;


import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.net.*;

class MainPlayerServer extends Net {

    private Server server;

    MainPlayerServer(IEngine engine, int port, int playerNumber) {
        init(engine, port, playerNumber);
    }

    MainPlayerServer(IEngine engine, int playerNumber) {
        init(engine, DEFAULT_DESKTOP_PORT, playerNumber);
        //new MainPlayerServer(engine, DEFAULT_DESKTOP_PORT, playerNumber);
    }

    private void init(IEngine engine, int port, int playerNumber){
        server = new Server(engine.getEngine(), port);
        playerNumberInMultiplayerMode = playerNumber;
        Logger.net("This user with the number " + playerNumber + " added as a server");
    }

    @Override
    public void update(){
        if (activated){
            if (server != null) {
                if (server.clientCount > 0) {
                    for (Client client : server.clients) {
                        if (client != null) {
                            incomingData = client.readString();
                            if (incomingData != null) {
                                encryptData();
                            }
                            //else Logger.debugLog("Got data");
                        }
                    }
                    //incomingData = client.readString();
                }
                //else Logger.debugLog("No clients");
            }
            else {

            }
        }
    }

    /*
    private void encryptData() {
        SerialAction serialAction = SerialAction.encryptStringInCommand(incomingData);
        for (Listener listener : )
            incomingData = null;
    }
*/

    @Override
    public void sendData(String data){  //To client
        if (activated) {
            if (server != null) {
                server.write(data);
                //Logger.netLog("Data " + data + " was sent to all the clients");
                //data = null;

            }
        }
        else {
            Logger.net("Server is deactivated. Can not send data");
            //data = null;
        }
    }
}
