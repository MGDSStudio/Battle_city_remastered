package io.itch.mgdsstudio.engine.graphic;

import io.itch.mgdsstudio.battlecity.game.Logger;

public class SimpleSpriteAnimationController {
    private final int NO_SET_TIME = -1;
    private int actualNumber, numbers;
    private final int STATEMENT_PAUSE = 0;
    private final int STATEMENT_REVERCE = 1;
    private final int STATEMENT_FORWARD = 2;
    private int statement = STATEMENT_PAUSE;
    private long nextTransferTime= NO_SET_TIME, prevTransferTime = NO_SET_TIME;
    private int timeBetweenChangings;
    private int actualFramesPerSecond;

    public SimpleSpriteAnimationController(int numbers, int framesPerSecond, long actualTime) {
        this.numbers = numbers;
        this.actualFramesPerSecond = framesPerSecond;
        timeBetweenChangings = 1000 / framesPerSecond;
        nextTransferTime = actualTime+timeBetweenChangings;
        prevTransferTime = actualTime;
    }

    public void update(long time){
        if (statement == STATEMENT_PAUSE){
            // NOTHING
        }
        else{
            if (time>nextTransferTime){
                transferToNextSprite(time);
            }
        }
    }

    public void startForwardAnimation(int framesPerSec, long time){
        if (statement != STATEMENT_FORWARD){
            statement = STATEMENT_FORWARD;
            actualFramesPerSecond = framesPerSec;
            timeBetweenChangings = 1000 / framesPerSec;
            nextTransferTime = time+timeBetweenChangings;
        }
        else {
            Logger.debug("Can not start forward animation");
        }
    }

    public void startReverseAnimation(int framesPerSec, long time){
        if (statement != STATEMENT_REVERCE){
            statement = STATEMENT_REVERCE;
            actualFramesPerSecond = framesPerSec;
            timeBetweenChangings = 1000 / framesPerSec;
            nextTransferTime = time+timeBetweenChangings;
        }
        else {
            Logger.debug("Can not start backward animation");
        }
    }

    public void pauseAnimation(){
        if (statement != STATEMENT_PAUSE) {
            statement = STATEMENT_PAUSE;
            nextTransferTime = NO_SET_TIME;
        }
        else{

        }
    }

    public void setFramesPerSec(int framesPerSec, long time){
        if (actualFramesPerSecond != framesPerSec) {
            actualFramesPerSecond = framesPerSec;
            timeBetweenChangings = 1000 / framesPerSec;
            if (timeBetweenChangings < (nextTransferTime-time)){
                nextTransferTime = time;
            }
            Logger.debug("Time set: " + nextTransferTime);
        }
    }

    private void transferToNextSprite(long actualTime) {
        if (statement == STATEMENT_FORWARD) {
            actualNumber++;
            if (actualNumber >= numbers){
                actualNumber = 0;
            }
            else if (actualNumber < 0) actualNumber = (numbers-1);
        }
        else if (statement == STATEMENT_REVERCE){
            actualNumber--;
            if (actualNumber<0){
                actualNumber = (numbers-1);
            }
            else if (actualNumber>= numbers) actualNumber = 0;
        }
        nextTransferTime = actualTime+timeBetweenChangings;
    }

    public int getActualNumber() {
        return actualNumber;
    }

    public boolean isActive() {
        if (statement == STATEMENT_FORWARD || statement == STATEMENT_REVERCE){
            return true;
        }
        else return false;
    }

    public boolean isStatementForward(){
        if (statement == STATEMENT_FORWARD){
            return true;
        }
        else return false;
    }

    public boolean isStatementBackward(){
        if (statement == STATEMENT_REVERCE){
            return true;
        }
        else return false;
    }
}
