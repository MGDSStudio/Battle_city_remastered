package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.util.ArrayList;

public abstract class ObjectActivatingController {
    public final static int BY_TIMER_ACTIVATION = 0;
    public final static int BY_REST_ENEMIES_ACTIVATION = 1;
    public final static int BY_KILLED_ENEMIES_ACTIVATION = 2;
    public final static int BY_KILLED_PLAYER_ACTIVATION = 3;
    public final static int BY_PLAYER_IN_ZONE_ACTIVATION = 4;
    public final static int BY_PLAYER_NOT_IN_ZONE_ACTIVATION = 5;
    public final static int BY_ENEMIES_IN_ZONE_ACTIVATION = 6;
    public final static int BY_ENEMIES_NOT_IN_ZONE_ACTIVATION = 7;
    
  
  
    protected final IActivateable entity;
   // private final ArrayList <Integer> values;
    protected boolean activated;
    private int type;
    //protected GameRound gr;
    protected IEngine engine;

    protected ObjectActivatingController(IActivateable entity, ArrayList<Integer> values, IEngine engine){
            type = values.get(0);
            this.entity = entity;
            this.engine = engine;
    }

    public static ObjectActivatingController createAppearingController(ArrayList <Integer> values, IActivateable entity, IEngine engine){
      ObjectActivatingController controller = null;
        if (values == null || values.size()<2){
        Logger.error("Can not create appearing controller");
      }
      else{
        if (values.get(0) == BY_TIMER_ACTIVATION){
             controller = new ByTimerActivatingController(values, entity, engine);
        }
        else Logger.error("Can not create appearing controller for this type " + values.get(0));
      }
      return controller;
    }

    public abstract void update (GameRound gr);
      
    public final boolean isActivated(){
        return activated;
    }

    protected void notifyObject(){
        entity.activate();
    }
      
}
