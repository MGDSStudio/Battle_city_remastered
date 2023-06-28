package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class Forest extends GraphicObject{


    public Forest(IEngine engine, Coordinate pos, int angle, int width, int height, int thirdDim){
        super(engine, pos, angle, width, height, thirdDim, GraphicLayers.OVER_SKY_LEVEL);
        loadGraphicDefaultData(engine);
    }

    public static Forest create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        Forest wall = new Forest(engine, pos, values[2], values[3], values[4], values[5]);
        wall.setId(entityData.getId());
        return wall;
    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(896,64, 960, 128);
        loadImage(engine, path, width, height, data);
    }



}
