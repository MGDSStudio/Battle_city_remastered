package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PApplet;
import processing.core.PGraphics;

class TurretGraphic extends GraphicPart{
    final static float START_ANGLE = 180;

    TurretGraphic(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, int turretGraphicType) {
        super(parent, tank, engine, relativeGraphicScale);
        if (tank instanceof EnemyTank) {
            Logger.correct("I NEED SELECT right turret for enemies");
            if (tank.getType() == EnemyTank.Types.SIMPLE_TANK){
                imageZoneSimpleData = ENEMY_BASIC_TURRET;
            }
            else if (tank.getType() == EnemyTank.Types.FAST_TANK){
                imageZoneSimpleData = ENEMY_LONG_CANNON_TURRET;
            }
            else if (tank.getType() == EnemyTank.Types.EASY_ARMORED_TANK){
                imageZoneSimpleData = ENEMY_DOUBLE_CANNON_TURRET;
            }
            else if (tank.getType() == EnemyTank.Types.GOOD_ARMORED_TANK){
                imageZoneSimpleData = ENEMY_ROCKET_LAUNCHER_TURRET;
            }

        }
        else {
            if (turretGraphicType == PlayerTank.TurretTypes.SIMPLE_TURRET) imageZoneSimpleData = PLAYER_BASIC_TURRET;
            else  if (turretGraphicType == PlayerTank.TurretTypes.LONG_CANNON_TURRET) imageZoneSimpleData = PLAYER_LONG_CANNON_TURRET;
            else  if (turretGraphicType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET) imageZoneSimpleData = PLAYER_DOUBLE_CANNON_TURRET;
            else  if (turretGraphicType == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET) imageZoneSimpleData = PLAYER_ROCKET_LAUNCHER_TURRET;
            else imageZoneSimpleData = PLAYER_BASIC_TURRET;
        }


        offset.x = -5;
    }

    @Override
    protected void update(PGraphics graphics) {
        graphics.translate(offset.x, offset.y);
        //graphics.rotate();
        graphics.rotate(-PApplet.radians(tank.getAngle())+PApplet.radians(tank.getTurretAbsoluteAngle()));
    }

    @Override
    void draw(PGraphics graphics, Camera gameCamera){
        //graphics.translate(offset.x, offset.y);
        super.draw(graphics, gameCamera);
    }


}
