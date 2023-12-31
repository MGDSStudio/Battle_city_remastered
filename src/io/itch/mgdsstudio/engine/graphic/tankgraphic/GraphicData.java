package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

interface GraphicData {

    ImageZoneSimpleData ENEMY_SIMPLE_BODY = new ImageZoneSimpleData(0,347-40, 80,347+40);
    ImageZoneSimpleData ENEMY_FAST_BODY = new ImageZoneSimpleData(162,347-40, 162+80,347+40);
    ImageZoneSimpleData ENEMY_EASY_ARMORED_BODY = new ImageZoneSimpleData(413-40,0, 413+40,80);
    ImageZoneSimpleData ENEMY_GOOD_ARMORED_BODY = new ImageZoneSimpleData(529-40,0, 529+40,80);

    ImageZoneSimpleData ENEMY_BASIC_TURRET = new ImageZoneSimpleData(77,347-40, 75+38*2,347+40);
    ImageZoneSimpleData ENEMY_LONG_CANNON_TURRET = new ImageZoneSimpleData(77,587-40, 75+38*2,587+40);
    ImageZoneSimpleData ENEMY_DOUBLE_CANNON_TURRET = new ImageZoneSimpleData(77,507+40, 75+38*2,507-40);
    ImageZoneSimpleData ENEMY_ROCKET_LAUNCHER_TURRET = new ImageZoneSimpleData(77,426-40, 75+38*2,426+40);

    ImageZoneSimpleData PLAYER_BASIC_BODY = new ImageZoneSimpleData(0,685-685, 80,685+80-685);
    ImageZoneSimpleData PLAYER_BASIC_TURRET = new ImageZoneSimpleData(77,0, 75+38*2,80);
    ImageZoneSimpleData PLAYER_LONG_CANNON_TURRET = new ImageZoneSimpleData(77,224-40, 75+38*2,224+40);
    ImageZoneSimpleData PLAYER_DOUBLE_CANNON_TURRET = new ImageZoneSimpleData(77,58, 75+38*2,58+80);
    ImageZoneSimpleData PLAYER_ROCKET_LAUNCHER_TURRET = new ImageZoneSimpleData(77,117, 75+38*2,117+80);

    ImageZoneSimpleData PLAYER_BASIC_CANNON = new ImageZoneSimpleData(145,0, 210,80);
    ImageZoneSimpleData PLAYER_LONG_CANNON = new ImageZoneSimpleData(145,48, 210,80+48);
    ImageZoneSimpleData PLAYER_DOUBLE_CANNON = new ImageZoneSimpleData(145,140-40, 210,140+40);
    ImageZoneSimpleData PLAYER_ROCKETS_CANNON = new ImageZoneSimpleData(0,0,1,1);   // No graphic

    ImageZoneSimpleData PLAYER_BASIC_RIGHT_TRACK = new ImageZoneSimpleData(145,685+46*2, 210,80+46*2);

    ImageZoneSimpleData PLAYER_BASIC_LEFT_TRACK = new ImageZoneSimpleData(334,685, 451,685+80);

    ImageZoneSimpleData PLAYER_LEFT_TRACK_FULL = new ImageZoneSimpleData(0,685-685+81, 80,685+80-685+81+80*2);
    ImageZoneSimpleData PLAYER_LEFT_TRACK_0 = new ImageZoneSimpleData(0,81, 80,81+80);
    ImageZoneSimpleData PLAYER_LEFT_TRACK_1 = new ImageZoneSimpleData(0,81+80, 80,81+80*2);
    ImageZoneSimpleData PLAYER_LEFT_TRACK_2 = new ImageZoneSimpleData(0,81+80*2, 80,81+80*3);

    int TYPE_PLAYER_BASIC = -5;
    int TYPE_PLAYER_TURRET_2 = -6;

    static ImageZoneSimpleData getGraphicForPlayerTurret(int turretType) {
        if (turretType == PlayerTank.TurretTypes.SIMPLE_TURRET) return PLAYER_BASIC_TURRET;
        else if (turretType == PlayerTank.TurretTypes.LONG_CANNON_TURRET) return PLAYER_LONG_CANNON_TURRET;
        else if (turretType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET) return PLAYER_DOUBLE_CANNON_TURRET;
        else if (turretType == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET) return PLAYER_ROCKET_LAUNCHER_TURRET;
        else {
            Logger.error("No data for this turret");
            return PLAYER_BASIC_TURRET;
        }
    }

    static ImageZoneSimpleData getGraphicForEnemyTurret(int turretType) {
        if (turretType == PlayerTank.TurretTypes.SIMPLE_TURRET) return ENEMY_BASIC_TURRET;
        else if (turretType == PlayerTank.TurretTypes.LONG_CANNON_TURRET) return ENEMY_LONG_CANNON_TURRET;
        else if (turretType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET) return ENEMY_DOUBLE_CANNON_TURRET;
        else if (turretType == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET) return ENEMY_ROCKET_LAUNCHER_TURRET;
        else {
            Logger.error("No data for this turret");
            return PLAYER_BASIC_TURRET;
        }
    }


    /*static ImageZoneSimpleData getGraphicForPlayerCannon(int turretType) {
        if (turretType == PlayerTank.TurretTypes.SIMPLE_TURRET) return PLAYER_BASIC_CANNON;
        else if (turretType == PlayerTank.TurretTypes.LONG_CANNON_TURRET) return PLAYER_LONG_CANNON;
        else if (turretType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET) return PLAYER_DOUBLE_CANNON;
        else if (turretType == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET) return PLAYER_ROCKETS_CANNON;
        else {
            Logger.error("No data for this turret");
            return PLAYER_BASIC_TURRET;
        }
    }*/
}
