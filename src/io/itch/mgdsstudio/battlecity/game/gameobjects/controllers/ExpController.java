package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;

import java.util.ArrayList;

public class ExpController {
    private Entity player;
    private int exp;
    private ArrayList <GLobalSerialAction> commands;
    private int killedInLine = 0;
    private PApplet engine;

    public ExpController(PApplet engine,Entity player)
    {
        this.engine = engine;
        this.player = player;
        commands = new ArrayList<>();
    }

    public void update(long actualTime){
        if (commands.size()>0){

        }
    }

    public void addCommand(GLobalSerialAction command){

    }


    private class PrevKilledController{
        private long prevKilledTime = 0;
        private Timer lastKilledTimer;
        private int timeForNextKillingInLine =2500;	//from player progress parameters
        private int killedInLine;
        private float actualExpCoef = 1.0f;
        private float inLineKilledEnemiesCoef = 0.4f;	//From player progress parameters


        PrevKilledController(int timeForNextKillingInLine, float inLineKilledEnemiesCoef){
            this.timeForNextKillingInLine = timeForNextKillingInLine;
            this.inLineKilledEnemiesCoef = inLineKilledEnemiesCoef;
            lastKilledTimer = new Timer(1, engine);
        }

        float getNextExpCoef(int theoreticalExpForKilling, long actualTime){
            if (lastKilledTimer.isTime()){
                killedInLine = 0;
                lastKilledTimer.setNewTimer(timeForNextKillingInLine);
                actualExpCoef = 1.0f;
            }
            else {
                lastKilledTimer.setNewTimer(timeForNextKillingInLine);
                killedInLine++;
                actualExpCoef+=inLineKilledEnemiesCoef;
            }
            prevKilledTime = actualTime;
            return actualExpCoef;
        }
    }

}
