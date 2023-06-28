package io.itch.mgdsstudio.battlecity.game.net;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.util.ArrayList;

public abstract class Net implements GlobalListener {
    private final ArrayList<GlobalListener> globalListeners = new ArrayList<>();
    protected final static int DEFAULT_DESKTOP_PORT = 5204;
    protected boolean activated = true;

    protected int playerNumberInMultiplayerMode;

    protected String incomingData;

    public void activate(){
        activated = true;
    }

    public void deactivated(){
        activated = false;
    }

    public abstract void update();


    public abstract void sendData(String data);

    public void appendCommand(GLobalSerialAction action){
        sendData(action.getSerialized());
    }

    public void appendListener(GlobalListener globalListener) {
        globalListeners.add(globalListener);
    }

    protected void encryptData() {
        try {
            GLobalSerialAction[] actionsList = GLobalSerialAction.encryptStringsInCommand(incomingData);
            //Logger.debugLog("Data will be ecrypted from string: " + incomingData );
            for (GlobalListener globalListener : globalListeners){
                for (GLobalSerialAction GLobalSerialAction : actionsList) {
                    //Logger.netLog("listener: " + listener + " receives action " + serialAction.getId() + " x " + serialAction.getPrefix() + " x " + serialAction.getSerialized());
                    globalListener.appendCommand(GLobalSerialAction);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            incomingData = null;
        }

    }

    public static Net CreateNetController(int playerNumberInMultiplayerMode, IEngine engine){
        Net netController;
        if (playerNumberInMultiplayerMode == GlobalConstants.PLAYER_AS_SERVER){
            netController = new MainPlayerServer(engine, playerNumberInMultiplayerMode);
        }
        else {
            netController = new PlayerClient(engine, playerNumberInMultiplayerMode);
        }
        return netController;
    }
}
