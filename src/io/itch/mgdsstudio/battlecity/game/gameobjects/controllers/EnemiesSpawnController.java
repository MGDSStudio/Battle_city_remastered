package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.util.ArrayList;

public abstract class EnemiesSpawnController extends Controller{


    public static EnemiesSpawnController create(GameRound gameRound, EntityData controllerData, boolean playerIsServerOrSingleplayer) {
        int [] values = controllerData.getValues();
        ArrayList <Coordinate> spawnPos = new ArrayList<>();
        for (int i = 17; i < values.length; i+=2) spawnPos.add(new Coordinate(values[i], values[i+1]));
        int controlBy;
        if (gameRound.isSinglePlayer()) controlBy  =EnemyTank.CONTROL_PER_AI;
        else {
            if (gameRound.isActualPlayerMaster()){
                controlBy  = EnemyTank.CONTROL_PER_AI;
            }
            else controlBy  =EnemyTank.CONTROL_PER_INTERNET;
        }
        EnemiesSpawnController controller;
        if (playerIsServerOrSingleplayer) controller = new EnemiesSpawnServerController(gameRound.getEngine(), values[0], values[1], values[2],values[3], values[4], values[5], values[6], values[7], controlBy, values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], spawnPos);
        else controller = new EnemiesSpawnClientController(gameRound.getEngine(), values[0], values[1], values[2],values[3], values[4], values[5], values[6], values[7], controlBy, values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15], spawnPos);

        return controller;
    }


}