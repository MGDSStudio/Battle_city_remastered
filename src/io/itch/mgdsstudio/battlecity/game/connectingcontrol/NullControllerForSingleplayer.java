package io.itch.mgdsstudio.battlecity.game.connectingcontrol;

import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.net.Net;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.util.ArrayList;

public class NullControllerForSingleplayer extends ConnectingController implements GlobalListener {
    //private int id;
    ////private int playerNumberInMultiplayerMode;
    //private final SerialAction wantToConnectAction;



    NullControllerForSingleplayer(int playerNumberInMultiplayerMode, Net netController, int id, IEngine engine) {
        globalListeners = new ArrayList<>();
        gameCanBeStarted = true;
        /*this.playerNumberInMultiplayerMode = playerNumberInMultiplayerMode;
        this.id = id;
        final IntList values = new IntList();
        values.append(playerNumberInMultiplayerMode);
        wantToConnectAction = new SerialAction(DataPrefixes.WANT_TO_CONNECT_FOR_PLAY, values, id, -1, -1);        this.netController = netController;*/

    }



    @Override
    public void update() {
        /*if (!gameCanBeStarted){
            if (gotAction != null){
                encryptGotAction();
            }
            else if (!gameCanBeStarted) netController.appendCommand(wantToConnectAction);
        }*/
    }

    private void encryptGotAction() {
        /*if (gotAction.getPrefix() == DataPrefixes.START_GAME){
            Logger.netLog("Client with number: " + playerNumberInMultiplayerMode + " and id: " + id + " can start the game");
            gameCanBeStarted = true;
        }*/
    }
}
