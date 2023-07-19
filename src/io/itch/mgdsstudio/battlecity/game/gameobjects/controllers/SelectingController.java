package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.GameElement;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

public class SelectingController {

    private boolean selected;
    private float actualAlpha;
    private final static float MAX_ALPHA  = 225, MIN_ALPHA = 30;
    private final static float DELTA_ALPHA = MAX_ALPHA-MIN_ALPHA;

    public SelectingController() {
        
    }

  private void update(int millis){
      actualAlpha = MIN_ALPHA+ DELTA_ALPHA*PApplet.sin(millis/1000f);
  }

 private int getSlpha(){
return alpha;
 }

    
}
