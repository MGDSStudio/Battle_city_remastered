package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;

public class TrackGraphicOld extends GraphicPart{
    private boolean side;
    private float maxForward, maxBackward;
    private float maxUpdatingFrequency;
    private float actualVelocity;

    TrackGraphicOld(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, boolean side) {
        super(parent, tank, engine, relativeGraphicScale);

        this.side = side;
        if (side == true){
            imageZoneSimpleData = PLAYER_BASIC_LEFT_TRACK;
        }
        else imageZoneSimpleData = PLAYER_BASIC_RIGHT_TRACK;
        maxForward = tank.getForwardMaxVelocityInWorldUnits();
        maxBackward = tank.getBackwardMaxVelocityInWorldUnits();
    }

    @Override
    void draw(PGraphics graphics, GameCamera gameCamera){
        update();
        super.draw(graphics, gameCamera);
    }

    private void update() {
        actualVelocity = tank.getBody().getLinearVelocity().length();
        float relative = (actualVelocity/maxForward);
        if (relative>1) relative = 1f;

    }

}

