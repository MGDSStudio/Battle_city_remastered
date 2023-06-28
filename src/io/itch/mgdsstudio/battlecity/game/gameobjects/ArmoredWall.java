package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class ArmoredWall extends Wall{


    //(PhysicGameWorld physicGameWorld, Coordinate pos, int angle, int width, int height) {
    public ArmoredWall(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int thirdDim){
        super(engine, physicWorld, pos, angle, width, height, thirdDim);
        loadGraphicDefaultData(engine);
    }



    public static ArmoredWall create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        ArmoredWall wall = new ArmoredWall(engine, physicWorld, pos, values[2], values[3], values[4], values[5]);
        wall.setId(entityData.getId());
        return wall;
    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(960,64, 960+64,128);
        loadImage(engine, path, width, height, data);
    }
/*
    protected void loadImage(IEngine engine, String path, int graphicWidth, int graphicHeight, ImageZoneSimpleData data){
        GraphicManager manager = GraphicManager.getManager(engine.getEngine());
        Image graphicImage = manager.getImage(path);
        image = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
    }*/

    @Override
    public boolean attackBy(Entity attackingObject, GameRound gameRound){
        if (attackingObject instanceof Bullet){
            Bullet bullet = (Bullet) attackingObject;
            if (bullet.canCrushArmoredObjects()){
                return KILLED;
            }
        }
        return !KILLED;
    }

}
