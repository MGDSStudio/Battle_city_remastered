package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

class BodyGraphic extends GraphicPart{


    BodyGraphic(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, int bodyGraphicType) {
        super(parent, tank, engine, relativeGraphicScale);
        imageZoneSimpleData = PLAYER_BASIC_BODY;
    }
}
