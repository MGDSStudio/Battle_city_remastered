package io.itch.mgdsstudio.battlecity.game.connectingcontrol;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.net.Net;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.util.ArrayList;

public abstract class ConnectingController implements GlobalListener {
    protected Net netController;
    protected ArrayList<GlobalListener> globalListeners;
    protected boolean gameCanBeStarted;
    protected GLobalSerialAction gotAction;


    public void appendListener(GlobalListener globalListener){
        globalListeners.add(globalListener);
    }

    public boolean isGameCanBeStarted() {
        return gameCanBeStarted;
    }


    public abstract void update();

    @Override
    public final void appendCommand(GLobalSerialAction action) {
        gotAction = action;
    }

    public static ConnectingController createController(int playerNumberInMultiplayerMode, int playersNumber, Net netController, IEngine engine, int id, boolean multiplayer){
        ConnectingController controller;
        if (multiplayer) {
            if (playersNumber == 1 || playerNumberInMultiplayerMode == 0) {
                controller = new ConnectingControllerAsServer(playerNumberInMultiplayerMode, playersNumber, netController, engine, id);
            }
            /*} else if (playersNumber == 1)*/
            else
                controller = new ConnectingControllerAsClient(playerNumberInMultiplayerMode, netController, id, engine);
        }
        else controller = new NullControllerForSingleplayer(playerNumberInMultiplayerMode, netController, id, engine);
         //(int playerNumberInMultiplayerMode, Net netController, int id, IEngine engine)
        Logger.net("This player will play as " + controller.getClass());
        return controller;
    }

}
