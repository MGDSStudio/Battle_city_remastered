package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

public abstract class ByTimerActivatingController extends ObjectAppearingController{

    private final int activatingTime;
    private int startTime;
    provate int endTime;
    protected boolean firstLoopEnded;

    ByTimerActivatingController(IActivateable entity, ArrayList <Integer> values, GameRound gr){
          super(entity, values, gr);
        // gr can be null
          activatingTime = values.get(1);
    }

    public void update (){
        if (!firstLoopEnded){
            endTime = engine.getEngine().millis() + activatingTime;
            firstLoopEnded = true;    
        }
        else if (!activated){
              if (engine.getEngine().millis()>=endTime){
                notify();
                activated = true;
              }
        }

    }
    

    
      
}
