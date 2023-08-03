package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class VfxAnimation extends GraphicObject {
    private int type;

    public VfxAnimation(IEngine engine, Coordinate pos, int angle, int width, int height, int type){
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

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        super.update(gameRound, deltaTime);
        if (!graphicObject.isActive()){
            gameRound.deleteObjectAfterActualLoop(this);
        }
    }

    public void loadGraphicDefaultData(IEngine engine){
        loadAnimationFromPool(engine, type);
    }


    @Override
    public String getDataString() {
        return null;
    }
}
