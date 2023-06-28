package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ai;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameController;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;

class AiControllerModel1 extends Ai {
    private Timer nextActionTimer;
    private int timeToNextMovementAction;
    AiControllerModel1(int type, EnemyTank enemyTank) {
        super(type, enemyTank);
        Logger.debug(": Enemy with id " + enemyTank.getId() + " will be controlled per " + this.getClass().toString());
        int difficulty = GameController.getDifficulty();
        if (difficulty == GlobalConstants.EASY_DIFFICULTY){
            timeToNextMovementAction = 2500;
        }
        else if (difficulty == GlobalConstants.MEDIUM_DIFFICULTY){
            timeToNextMovementAction = 2000;
        }
        else timeToNextMovementAction = 1000;
        //init()
    }

    @Override
    protected void updatingAction(GameRound gameRound, long deltaTime) {
        if (nextActionTimer == null){
            setMovementWithRotationToRandomDir(gameRound);
            nextActionTimer = new Timer(timeToNextMovementAction, gameRound.getEngine().getEngine());
        }
        else if (nextActionTimer.isTime()){
            setMovementWithRotationToRandomDir(gameRound);
            nextActionTimer.setNewTimer(timeToNextMovementAction);
            nextActionTimer.restart();
        }
    }

    private void setMovementWithRotationToRandomDir(GameRound gameRound) {
        int value = PApplet.floor(gameRound.getEngine().getEngine().random(2));
        GLobalSerialAction GLobalSerialAction = new GLobalSerialAction(ActionPrefixes.MOVEMENT_STICK_RUN_AND_ROTATION, value, enemyTank.getId(), gameRound.getTimeFromLevelBegan(), getCommandNumber());
        for (GlobalListener globalListener : globalListeners){
            globalListener.appendCommand(GLobalSerialAction);
        }
    }

    /*@Override
    public void update(GameRound gameRound, long deltaTime) {
        levelStartWaitingUpdate(gameRound);
    }*/
}
