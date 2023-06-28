package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class Animation extends GraphicObject {
    private int type;

    public Animation(IEngine engine, Coordinate pos, int angle, int width, int height, int type){
        super(engine , pos, angle, width, height, 0, getLayerForAnimationType(type));
        this.type = type;
        loadGraphicDefaultData(engine);
    }

    private static int getLayerForAnimationType(int type){
        if (type == IAnimations.BULLET_EXPLOSION || type == IAnimations.MINE_EXPLOSION){
            return GraphicLayers.ON_GROUND_LAYER;
        }
        else if (type == IAnimations.TANK_DESTRUCTION) return GraphicLayers.OVER_SKY_LEVEL;
        else return GraphicLayers.GROUND_LAYER;
    }

    /*public static Ice create(IEngine engine, PhysicGameWorld physicGameWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        Ice ice = new Ice(engine, pos, values[2], values[3], values[4]);
        ice.setId(entityData.getId());
        return ice;
    }*/

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        super.update(gameRound, deltaTime);
        if (!graphicObject.isActive()){
            //Logger.debug("Animation ended and can be deleted");
            gameRound.deleteObjectAfterActualLoop(this);
        }
    }

    public void loadGraphicDefaultData(IEngine engine){
        //final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_LEVEL_GRAPHIC_FILE);
        //final ImageZoneSimpleData data = new ImageZoneSimpleData(64,128, 127, 191);
        loadAnimation(engine, type);
    }


}
