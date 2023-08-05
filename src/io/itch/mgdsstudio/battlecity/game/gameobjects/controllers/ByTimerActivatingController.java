package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.util.ArrayList;

public class ByTimerActivatingController extends ObjectActivatingController {

    private final int activatingTime;
    private int startTime;
    private int endTime;
    protected boolean firstLoopEnded;

    ByTimerActivatingController(ArrayList<Integer> values, IActivateable entity, IEngine engine){
          super(entity, values, engine);
        // gr can be null
          activatingTime = values.get(1);
          if (activatingTime == 0){
              Logger.debug("Activated from level start");
              firstLoopEnded = true;
              activated = true;
              notifyObject();
          }
    }

    public void update (GameRound gr){
        if (!firstLoopEnded){
            endTime = engine.getProcessing().millis() + activatingTime;
            firstLoopEnded = true;    
        }
        else if (!activated){
              if (engine.getProcessing().millis()>=endTime){
                notifyObject();
                activated = true;
              }
        }

    }
    

    
      
}
