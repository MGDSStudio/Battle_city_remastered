package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

public abstract class ByTimerActivatingController extends ObjectAppearingController{

    private final int activatingTime;
    protected boolean firstLoopEnded;

    ByTimerActivatingController(IActivateable entity, ArrayList <Integer> values, GameRound gr){
          super(entity, values, gr);
        // gr can be null
          activatingTime = values.get(1);
    }

    public void update (){
        if (!firstLoopEnded){

        }
        if (!activated){
              if (engine.getEngine().millis()>=activatingTime){
                notify();
                activated = true;
              }
        }

    }
    

    
      
}
