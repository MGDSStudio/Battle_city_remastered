package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.LevelEndConditionController;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class Camp extends Wall{
    //

    private boolean side;
    private boolean destroyed;
    private GraphicElementInGame destroyedImage;


    //protected Wall(IEngine engine, PhysicGameWorld physicGameWorld, Coordinate pos, int angle, int width, int height, int thirdDim) {
    //

    public Camp(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height){
        super(engine, physicWorld, pos, angle, width, height, BodyForms.RECT);
        loadGraphicDefaultData(engine);
    }

    public static Camp create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        Camp wall = new Camp (engine, physicWorld, pos, values[2], values[3], values[4]);
        wall.setId(entityData.getId());
        return wall;
    }

    public String getDataString(){
        int posX = (int) pos.x;
        Logger.correct("To implement");
        return null;
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        if (!destroyed){
          if (life <= 0){              
              gameRound.campKilled(side);  
              destroyed = true;
          }            
        }
    }

    public boolean attackBy(Entity attackingObject, GameRound gameRound){
        if (!destroyed) {
            destroyed = true;
            graphicElementInGame = destroyedImage;
            gameRound.setLevelEnd(LevelEndConditionController.LevelEndType.PLAYER_CAMP_KILLED);
        }
        return !KILLED;
    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(896,0, 896+64,64);
        loadImage(engine, path, width, height, data);
        loadDestroyedImageGraphic(engine);
    }

    private void loadDestroyedImageGraphic(IEngine engine) {
        final ImageZoneSimpleData data = new ImageZoneSimpleData(896+64,0, 896+64+64,64);
        //GraphicManager manager = GraphicManager.getManager(engine.getEngine());
        Image graphicImage = graphicElementInGame.getImage();
        destroyedImage = new ImageInGame(graphicImage, width, height, data);
    }



}
