package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.libs.Timer;

import java.util.ArrayList;

public class LevelEndConditionController extends Controller{

    public interface LevelEndType{
        int PLAYER_KILLED = 0;
        int PLAYER_RETURNED_BACK = 3;
        int PLAYER_CAMP_KILLED = 1;
        int PLAYER_WON = 2;
    }

    public interface WinConditions{
        int KILL_ALL_ENEMIES = 0;
        int KILL_ENEMIES_CAMPS = -1;
        int HOLD_ALIVE_FOR_TIME_IN_SEC = 10;    //or more
    }

    public interface LoseConditions{
        int LOSE_CAMP = 0;
    }

    private int winCondition, loseCondition;
    private ArrayList < Entity > entities;
    private Timer timerToHold;
    private Timer startTimer;
    private boolean levelEndNotified;
    private final static int START_NOT_ACTIVE_TIME = 5000;

    // EnemiesSpawnServerController(IEngine engine, int maxTanksOnScreen, int spawnTime, int tanks0, int tanks1, int tanks2, int tanks3, int tanks4, int tanks5, int controlBy, int aiModelForTanks0, int aiModelForTanks1, int aiModelForTanks2, int aiModelForTanks3, int aiModelForTanks4, int aiModelForTanks5, int additionalData1, int additionalData2, ArrayList<Coordinate> placesForEnemiesSpawn) {

    public LevelEndConditionController(GameRound gameRound, int winCondition, int loseCondition){
        this.winCondition = winCondition;
        this.loseCondition = loseCondition;
        if (winCondition >= WinConditions.HOLD_ALIVE_FOR_TIME_IN_SEC){
            timerToHold = new Timer(winCondition*1000, gameRound.getEngine().getEngine());
        }
        else if (winCondition == WinConditions.KILL_ALL_ENEMIES){
            entities = new ArrayList<>();
            Logger.debug("Player will win when all the enemies will be killed!");
            //gameRound.getE
        }
        startTimer = new Timer(START_NOT_ACTIVE_TIME, gameRound.getEngine().getEngine());
    }

    public static LevelEndConditionController create(GameRound gameRound, EntityData entityData){
        int [] values = entityData.getValues();
        LevelEndConditionController controller = new LevelEndConditionController(gameRound, values[0], values[1]);
        return controller;
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        if (!firstUpdated){
            if (startTimer.isTime()){
                firstUpdating(gameRound,deltaTime);
            }
        }
        else {
            if (winCondition >= WinConditions.HOLD_ALIVE_FOR_TIME_IN_SEC) {
                if (timerToHold.isTime()) {
                    if (!ended) {
                        ended = true;
                        gameRound.setLevelEnd(LevelEndType.PLAYER_WON);
                    }
                }
            } else if (winCondition == WinConditions.KILL_ALL_ENEMIES) {
                entities = gameRound.getEntitiesForType(EnemyTank.class, entities);
                if (entities.size() == 0) {
                    if (!ended) {
                        ended = true;
                        gameRound.setLevelEnd(LevelEndType.PLAYER_WON);
                        Logger.debug("I need to test: maybe the tank spawn controllers did not created all the tanks!");
                    }

                }
            }
        }
    }
}
