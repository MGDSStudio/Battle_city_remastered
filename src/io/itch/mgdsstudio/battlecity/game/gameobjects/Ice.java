package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class Ice extends GraphicObject{


    //protected GraphicObject(IEngine engine, Coordinate pos, int angle, int width, int height, int thirdDim, int layer) {
    //
    public Ice(IEngine engine, Coordinate pos, int angle, int width, int height){
        super(engine , pos, angle, width, height, 0, GraphicLayers.GROUND_LAYER);
        loadGraphicDefaultData(engine);
    }


    public static Ice create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        Ice ice = new Ice(engine, pos, values[2], values[3], values[4]);
        ice.setId(entityData.getId());
        return ice;
    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(896,128, 960, 192);
        loadImage(engine, path, width, height, data);
    }


    /*
    @Override
    public void draw(PGraphics graphics, GameCamera gameCamera) {
        Logger.debugLog("WxH" + width + "x" + height);
        if (image != null) image.draw(graphics, gameCamera, pos.x, pos.y);
    }*/
}
