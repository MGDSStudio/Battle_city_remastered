package io.itch.mgdsstudio.battlecity.game.connectingcontrol;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.net.Net;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.IntList;

import java.util.ArrayList;

class ConnectingControllerAsServer extends ConnectingController implements GlobalListener {
    private int playerNumberInMultiplayerMode,  playersNumber;
    private boolean allPlayersConnected;
    private boolean connectedPlayersList[];

    private GLobalSerialAction startGameAction;
    private int id;

    ConnectingControllerAsServer(int playerNumberInMultiplayerMode, int playersNumber, Net netController, IEngine engine, int id) {
        if (playersNumber == 0) {
            gameCanBeStarted = true; //Singleplayer
            Logger.net("Game is singleplayer");
        }
        else if (netController == null) {
            gameCanBeStarted = true;
            Logger.net("Game is singleplayer. NET controller is null");
        }
        else {
            this.playerNumberInMultiplayerMode = playerNumberInMultiplayerMode;
            this.playersNumber = playersNumber;
            this.id = id;
            //this.netController = netController;
            connectedPlayersList = new boolean[playersNumber];
            for (int i = 0; i < playersNumber; i++){
                connectedPlayersList[i] = false;
            }
            connectedPlayersList[0] = true; //Server already connected
            globalListeners = new ArrayList<>();
            //
            final IntList values = new IntList();
            values.append(1);
            startGameAction  = new GLobalSerialAction(ActionPrefixes.START_GAME, values, id, engine.getEngine().millis(), -1);
            this.netController = netController;
            //startGameAction = new SerialAction();
        }

    }

    public void update(){
        if (!allPlayersConnected) {
            if (gotAction != null) {
                encryptGotAction();
                testAllPlayersConnected();
                if (allPlayersConnected){
                    startGame();
                }
            }
        }

    }

    private void startGame() {
        for (GlobalListener globalListener : globalListeners){
            globalListener.appendCommand(startGameAction);
        }
        Logger.net(globalListeners.size() + " listeners must got message that the game can be started");
        gameCanBeStarted = true;
    }

    private void testAllPlayersConnected() {
        for (int i = 0; i < playersNumber; i++){
            boolean allConnected = true;
            if (connectedPlayersList[i] == false){
                allConnected = false;
            }
            if (allConnected) {
                allPlayersConnected = true;
                Logger.net("All players connected");
            }
        }
    }

    private void encryptGotAction() {
        char prefix = gotAction.getPrefix();
        if (prefix == ActionPrefixes.WANT_TO_CONNECT_FOR_PLAY){
            int playerNumberWasConnected = gotAction.getMainValue();
            connectedPlayersList[playerNumberWasConnected] = true;
            String notConnectedList = "";
            for (int i = 0; i < playersNumber; i++) {
                if (connectedPlayersList[i] == false) {
                    notConnectedList += (i + ",");
                }
                Logger.net("Player with id: " + gotAction.getId() + " and number " + gotAction.getMainValue() + "  was connected. We waiting for players with ids: " + notConnectedList + " to be connected");
            }
        }
        gotAction = null;
    }


    /*

    @Override
    public void appendCommand(SerialAction action) {
        char prefix = action.getPrefix();
        if (prefix == DataPrefixes.WANT_TO_CONNECT_FOR_PLAY){
            int idWasConnected = action.getId();
            connectedPlayersList[idWasConnected] = true;
            String notConnectedList = "";
            for (int i = 0; i < playersNumber; i++) {
                if (connectedPlayersList[i] == false) {
                    notConnectedList += (i + ",");
                }
                Logger.netLog("Player with id: " + idWasConnected + " was connected. We waiting for players with ids: " + notConnectedList + " to be connected");
            }
        }
    }*/
}
