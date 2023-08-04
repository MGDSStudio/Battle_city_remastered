package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

class BodyGraphic extends GraphicPart{


    BodyGraphic(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, int bodyGraphicType) {
        super(parent, tank, engine, relativeGraphicScale);
        if (tank instanceof EnemyTank) {
            if (tank.getType() == EnemyTank.Types.SIMPLE_TANK){
                imageZoneSimpleData = ENEMY_SIMPLE_BODY;
            }
            else if (tank.getType() == EnemyTank.Types.FAST_TANK){
                imageZoneSimpleData = ENEMY_FAST_BODY;
            }
            else if (tank.getType() == EnemyTank.Types.EASY_ARMORED_TANK){
                imageZoneSimpleData = ENEMY_EASY_ARMORED_BODY;
            }
            else if (tank.getType() == EnemyTank.Types.GOOD_ARMORED_TANK){
                imageZoneSimpleData = ENEMY_GOOD_ARMORED_BODY;
            }
        }
        else imageZoneSimpleData = PLAYER_BASIC_BODY;
    }
}
