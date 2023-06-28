package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class BrickWall extends Wall{

    public BrickWall(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int thirdDim){
        super(engine, physicWorld, pos, angle, width, height, thirdDim);
        loadGraphicDefaultData(engine);

    }
    public static BrickWall create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        BrickWall wall = new BrickWall (engine, physicWorld, pos, values[2], values[3], values[4], values[5]);
        wall.setId(entityData.getId());
        return wall;
    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(896,320, 960,384);
        loadImage(engine, path, width, height, data);
    }

    @Override
    public boolean attackBy(Entity attackingObject, GameRound gameRound){
         return KILLED;
    }



}
