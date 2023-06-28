package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;

public class EngineController {
    private Tank tank;
    private final float minVelocityToActivate = 2.5f;
    private long restTime;


    private final static boolean NOW_RUNNING = true;
    private final static boolean NOW_STAYING = false;
    private boolean statement = NOW_STAYING;
    private boolean prevStatement = NOW_STAYING;
    private boolean activated;

    public EngineController(Tank tank, int timeToWork) {
        this.tank = tank;
        start(timeToWork);
        /*
        restTime = timeToWork;
        if (restTime > 0) {
            activated = true;
            Logger.debug("Motor upgraded for: " + timeToWork/1000 + " sec ");
        }*/
    }

    public EngineController(Tank tank) {
        this.tank = tank;
    }

    public void start(int timeToWork){
        restTime = timeToWork;
        if (restTime > 0) {
            activated = true;
            Logger.debug("Motor upgraded for: " + timeToWork/1000 + " sec ");
        }
    }



    public void update(int deltaTime){
        if (activated) {
            float velocity = tank.getBody().getLinearVelocity().length();
            prevStatement = statement;
            if (statement == NOW_STAYING) {
                if (velocity > minVelocityToActivate) {
                    statement = NOW_RUNNING;
                }
            } else if (velocity < minVelocityToActivate) {
                statement = NOW_STAYING;
            }
            if (statement != prevStatement) {
                statementWasChanged(deltaTime);
            }
            updateRestTime(deltaTime);
            //Logger.debug("Velocityu: " + velocity);
        }
    }

    private void updateRestTime(int deltaTime) {
        if (statement == NOW_RUNNING) restTime-=deltaTime;
        if (restTime <= 0){
            activated = false;
            tank.removeMotorPerformance();
            Logger.debug("Motor upgrade is ended");
        }
    }


    private void statementWasChanged(int deltaTime) {
        if (statement == NOW_RUNNING){

        }
    }
}
