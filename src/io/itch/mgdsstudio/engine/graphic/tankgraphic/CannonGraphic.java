package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PConstants;
import processing.core.PGraphics;

class CannonGraphic extends GraphicPart{
    final static float START_ANGLE = PConstants.PI;
    float angle = 0;

    CannonGraphic(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, int cannonGraphicType) {
        super(parent, tank, engine, relativeGraphicScale);
        imageZoneSimpleData = PLAYER_BASIC_CANNON;
    }

    @Override
    void draw(PGraphics graphics, Camera gameCamera){
        graphics.pushMatrix();
        super.draw(graphics, gameCamera);
        graphics.popMatrix();
    }

    public void update(PGraphics graphics) {

        
    }
}


