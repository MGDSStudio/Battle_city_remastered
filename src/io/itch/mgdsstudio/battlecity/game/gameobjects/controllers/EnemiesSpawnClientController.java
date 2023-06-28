package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.data.IntList;

import java.util.ArrayList;

class EnemiesSpawnClientController extends EnemiesSpawnController{
    private ArrayList<Tank> tanksToBeAdded = new ArrayList<>();
    EnemiesSpawnClientController(IEngine engine, int maxTanksOnScreen, int spawnTime, int tanks0, int tanks1, int tanks2, int tanks3, int tanks4, int tanks5, int controlBy, int aiModelForTanks0, int aiModelForTanks1, int aiModelForTanks2, int aiModelForTanks3, int aiModelForTanks4, int aiModelForTanks5, int additionalData1, int additionalData2, ArrayList<Coordinate> placesForEnemiesSpawn) {
        //super(engine, maxTanksOnScreen, spawnTime, tanks0, tanks1, tanks2, tanks3, tanks4, tanks5, controlBy, aiModelForTanks0, aiModelForTanks1, aiModelForTanks2, aiModelForTanks3, aiModelForTanks4, aiModelForTanks5, additionalData1, additionalData2, placesForEnemiesSpawn);
        //Logger.debugLog("This user has a client based spawner");
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        if (tanksToBeAdded.size()>0){
            for (int i = tanksToBeAdded.size()-1; i>=0; i--){
                gameRound.addEntityToStart(tanksToBeAdded.get(i));
                tanksToBeAdded.remove(i);
            }
        }
    }

    public void executeAction(IEngine engine, PhysicWorld physicWorld, GLobalSerialAction action) {

        if (action.getPrefix() == ActionPrefixes.ENEMY_TANK_CREATED){
            IntList intList = action.getValuesList();
            Tank tank = new EnemyTank(engine, physicWorld, intList);
            tank.setId(action.getId());
            tanksToBeAdded.add(tank);
            Logger.debug("New tank created with id: " + tank.getId() + " but must be: " + action.getId());
        }
    }
}
