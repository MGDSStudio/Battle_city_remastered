package io.itch.mgdsstudio.battlecity.game.control;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;

import java.util.ArrayList;
import java.util.HashMap;

public class GameProcessController implements GlobalListener {
    private GameRound gameRound;
    private Hud hud;
    private final ArrayList <GLobalSerialAction> actionsList = new ArrayList<>();

    private HashMap<Integer, Long> levelStartTimesForPlayersIds = new HashMap<>();
    private boolean singleplayer;
    private boolean getCommandsFromInternet;
    private int playerNumberInMultiplayerMode;

    public GameProcessController(GameRound gameRound, Hud inGameHud, boolean singleplayer, int playerNumber, int playerNumberInMultiplayerMode) {
        this.gameRound = gameRound;
        this.hud = inGameHud;
        this.singleplayer = singleplayer;
        this.playerNumberInMultiplayerMode = playerNumberInMultiplayerMode;
        hud.initHudStartData(gameRound);
        if (!singleplayer){
            if (playerNumber > 0){  // This player is the second player in multiplayer mode
                getCommandsFromInternet = true;
            }
        }
    }

    public void setLevelStartTimeForUsers(int playerId, long startTime){
        levelStartTimesForPlayersIds.put(playerId, startTime);
        Logger.debug("for user with id: " + playerId + " level started at " + startTime/1000 + " sec");
    }

    /*private void initHudAngles() {

        PlayerTank playerTank = gameRound.getPlayer();
        int angle = (int) playerTank.getAngle();
        int turretAngle = (int) playerTank.getTurretAbsoluteAngle();
        hud.getLowerPanel().setAngleForMovementStick(angle);
        hud.getLowerPanel().setAngleForAimStick(turretAngle);
        //Logger.debugLog("Player angle: " + angle + "; turret angle: " + turretAngle);
    }*/

    public void update(long deltaTime){
        if (getCommandsFromInternet){

        }
        for (int i = actionsList.size()-1; i >= 0; i--){
            boolean succesfully = executeAction(actionsList.get(i), deltaTime);
            if (succesfully) actionsList.remove(i);
        }
    }

    private boolean executeAction(GLobalSerialAction action, long deltaTime) {
        if (ActionPrefixes.isTankControlSpecificAction(action.getPrefix())){
            Entity entity = gameRound.getObjectById(action.getId());
            if (entity != null) return entity.executeAction(action);
            else Logger.error("Object with id: " + action.getId() + " was not founded");
        }
        else
            if (ActionPrefixes.isControllerSpecificAction(action.getPrefix())){
                ArrayList < Controller> controllers = gameRound.getControllers();
                for (Controller controller : controllers){
                    controller.executeAction(gameRound.getEngine(), gameRound.getPhysicWorld(), action);
                }
        }
        else Logger.debug(" this action is not knew for this listener " + action);
        return true;
    }


    @Override
    public void appendCommand(GLobalSerialAction action) {
        if (action != null){
            actionsList.add(action);
            if (action.getId() == 14) Logger.debug("Got action from the friend : " + action);
        }
        else Logger.error("Null action can not be append");
    }
}
