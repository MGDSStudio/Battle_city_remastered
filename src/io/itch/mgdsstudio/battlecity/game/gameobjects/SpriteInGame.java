package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class SpriteInGame extends GraphicObject{

    public SpriteInGame(IEngine engine, Coordinate pos, int angle, int width, int height, int thirdDim, int layer, int spritesheetNumber, int left, int up, int right, int down){
        super(engine, pos, angle, width, height, thirdDim, layer);
        loadGraphicDefaultData(engine, spritesheetNumber, left, up,  right, down);
    }

    public static SpriteInGame create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        SpriteInGame wall = new SpriteInGame(engine, pos, values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11]);
        wall.setId(entityData.getId());
        return wall;
    }

    public void loadGraphicDefaultData(IEngine engine, int spritesheetNumber, int left, int up, int right, int down){
        String path = engine.getPathToSpriteInAssets(spritesheetNumber);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(left,up, right, down);
        loadImage(engine, path, width, height, data);
    }
}
