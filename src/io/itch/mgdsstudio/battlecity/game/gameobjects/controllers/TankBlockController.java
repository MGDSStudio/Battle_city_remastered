package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import org.jbox2d.common.Vec2;

public class TankBlockController {

    private final static long TIME_TO_START_FAST_BLINK = 5000;
    private final Tank tank;
    private long endTime;
    private long readyTime;

    private int randomRotatingDirection;
    private final static int VIBRATION = 0, BLINKING = 1, ENDED = 2;
    private int statement;
    private final Vec2 vibrationPos = new Vec2();


    public TankBlockController(Tank tank) {
        this.tank = tank;
        statement = ENDED;
    }

    public void blockForTime(long timeToBlock, long actualTime){
        if (actualTime < endTime){
            endTime+=timeToBlock;
            readyTime+=timeToBlock;
            if (actualTime < readyTime) statement = VIBRATION;
            else if (actualTime < endTime) statement = BLINKING;
        }
        else {
            endTime = actualTime + timeToBlock;
            readyTime = actualTime + timeToBlock - TIME_TO_START_FAST_BLINK;
            statement = VIBRATION;
        }
        randomRotatingDirection = GlobalVariables.getRandomIntValue(0,1);
    }

    public TankBlockController(Tank tank, long timeToBlock, long actualTime) {
        this.tank = tank;
        endTime = actualTime+timeToBlock;
        readyTime = actualTime+timeToBlock-TIME_TO_START_FAST_BLINK;
        statement = VIBRATION;
    }

    public void update(long time){
        if (statement != ENDED) {
            if (time > readyTime) {
                vibrationPos.x = GlobalVariables.getRandomIntValue(0,20)/10f;
                vibrationPos.y = GlobalVariables.getRandomIntValue(0,20)/10f;
                if (statement == VIBRATION) {
                    statement = BLINKING;
                }
                if (time > endTime) {
                    if (statement != ENDED) {
                        vibrationPos.x = 0;
                        vibrationPos.y = 0;
                        statement = ENDED;
                    }
                }
            }
        }
    }

    public boolean isFree(){
        if (statement == ENDED) return true;
        else return false;
    }

    public boolean isVibrated(){
        if (statement == VIBRATION) return true;
        else return false;
    }

    public boolean isBlinked(){
        if (statement == BLINKING) return true;
        else return false;
    }

    public int getRandomRotatingDirection() {
        return  randomRotatingDirection;
    }

    public Vec2 getVibrationRelativePos() {
        return vibrationPos;
    }
}
