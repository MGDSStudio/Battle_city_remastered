package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

public abstract class ByTimerActivatingController extends ObjectAppearingController{

    private final int activatingTime;

    ByTimerActivatingController(IActivateable entity, ArrayList <Integer> values){
          super(entity, values);
          activatingTime = values.get(1);
    }

    public void update (){
        if (!activated){
              if (engine.getEngine().millis()>=activatingTime){
                notify();
                activated = true;
              }
        }

    }
    

    
      
}
