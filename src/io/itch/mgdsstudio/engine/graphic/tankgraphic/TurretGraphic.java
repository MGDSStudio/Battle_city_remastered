package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

class TurretGraphic extends GraphicPart{
    final static float START_ANGLE = 180;

    TurretGraphic(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, int turretGraphicType) {
        super(parent, tank, engine, relativeGraphicScale);
        imageZoneSimpleData = PLAYER_BASIC_TURRET;
        offset.x = -5;
    }

    @Override
    protected void update(PGraphics graphics) {
        graphics.translate(offset.x, offset.y);
        //graphics.rotate();
        graphics.rotate(-PApplet.radians(tank.getAngle())+PApplet.radians(tank.getTurretAbsoluteAngle()));
    }

    @Override
    void draw(PGraphics graphics, GameCamera gameCamera){
        //graphics.translate(offset.x, offset.y);
        super.draw(graphics, gameCamera);
    }


}
