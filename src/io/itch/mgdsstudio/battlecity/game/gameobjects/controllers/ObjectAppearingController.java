package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

public abstract class ObjectAppearingController{
    public final static int BY_TIMER_ACTIVATION = 0;
    public final static int BY_REST_ENEMIES_ACTIVATION = 1;
    public final static int BY_KILLED_ENEMIES_ACTIVATION = 2;
    public final static int BY_KILLED_PLAYER_ACTIVATION = 3;
    public final static int BY_PLAYER_IN_ZONE_ACTIVATION = 4;
    public final static int BY_PLAYER_NOT_IN_ZONE_ACTIVATION = 5;
    public final static int BY_ENEMIES_IN_ZONE_ACTIVATION = 6;
    public final static int BY_ENEMIES_NOT_IN_ZONE_ACTIVATION = 7;
    
  
  
    protected final Entity;
   // private final ArrayList <Integer> values;
    protected boolean activated;
    private int type;
    protected GameRound gr;
    protected IEngine engine;

    protected ObjectAppearingController(Entity entity, ArrayList <Integer> values){
            type = values.get(0);
            this.entity = entity;
    }

    public static ObjectAppearingController createAppearingController(ArrayList <Integer> values, GameRound gr){
      if (values == null || values.size(<2)){
        Logger.error("Can not create appearing controller");
        //return null controller
        return null;
      }
      else{
        if (values.get(0) == BY_TIMER_ACTIVATION){

        }

      }
      Logger.error("Can not create appearing controller for this type " + values.get(0));
      return null;
    }

    public abstract void update ();
      
    public final boolean isActivated(){
        return activated;
    }

    
      
}
