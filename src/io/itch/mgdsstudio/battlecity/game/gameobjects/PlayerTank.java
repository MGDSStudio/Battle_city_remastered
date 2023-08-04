package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameController;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.dataloading.Keys;
import io.itch.mgdsstudio.battlecity.game.dataloading.PlayerProgressControllerSingleton;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.MinesOnFieldGenerator;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

import java.util.ArrayList;

public class PlayerTank extends Tank{

    public interface TurretTypes{
        int SIMPLE_TURRET = 0;
        int LONG_CANNON_TURRET = 1;
        int DOUBLE_CANNONS_TURRET = 2;
        int ROCKET_LAUNCHER_TURRET = 3;
    }

    public interface EngineTypes {
        int SIMPLE = 0;
        int UPGRADED = 1;
    }
    private int lifes = 1;


    public PlayerTank(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int role){
        super(engine, physicWorld, pos, angle, angle, getLifeFromSavedFile(), role, getWidthForChassis(getChassisTypeFromSavedFile()), getHeightForChassis(getChassisTypeFromSavedFile()));
        PlayerProgressControllerSingleton controllerSingleton = PlayerProgressControllerSingleton.getInstance();
        lifes = controllerSingleton.getIntegerValue(Keys.LIFES);
        this.engineUpgrade = controllerSingleton.getIntegerValue(Keys.CHASSIS_UPGRADE);
        this.turretType = controllerSingleton.getIntegerValue(Keys.TOWER_UPGRADE);
        ammoForActualWeapon = controllerSingleton.getIntegerValue(Keys.AMMO_FOR_ACTUAL_WEAPON);
        Logger.debug("Player tank was created at: " + (int)pos.x + "x" + (int)pos.y);
    }

    private static int getChassisTypeFromSavedFile() {
        PlayerProgressControllerSingleton controllerSingleton = PlayerProgressControllerSingleton.getInstance();
        return controllerSingleton.getIntegerValue(Keys.CHASSIS_UPGRADE);
    }

    private static int getLifeFromSavedFile() {
        PlayerProgressControllerSingleton controllerSingleton = PlayerProgressControllerSingleton.getInstance();
        return controllerSingleton.getIntegerValue(Keys.HP);
    }

    @Override
    protected void setBodyData() {
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_PLAYER);
    }

    @Override
    protected void applyEffect(Collectable collectable, GameRound gameRound) {
        Logger.debug("Collectable effect implemented only for player");
        int type = collectable.getType();
        PlayerProgressControllerSingleton controller =  PlayerProgressControllerSingleton.getInstance();
        if (type == Collectable.Types.LIFE){
            lifes++;
            controller.setValueForKey(Keys.LIFES, lifes);
        }
        else if (type == Collectable.Types.WEAPON){
            turretType++;
            int rocketsMustBeAdditionalAdded = 0;
            if (turretType > TurretTypes.ROCKET_LAUNCHER_TURRET){
                rocketsMustBeAdditionalAdded = controller.getIntegerValue(Keys.MAX_ROCKETS);
                turretType = TurretTypes.ROCKET_LAUNCHER_TURRET;
                Logger.debug("Player has already max upgraded turret and have " + ammoForActualWeapon + " ammo");
            }
            controller.setValueForKey(Keys.TOWER_UPGRADE, turretType);
            ammoForActualWeapon+=rocketsMustBeAdditionalAdded;
            controller.setValueForKey(Keys.AMMO_FOR_ACTUAL_WEAPON, ammoForActualWeapon);
            getTankController().updateTankForActualTurretStatement();
        }
        else if (type == Collectable.Types.ARMOUR){
            life++;
        }
        else if (type == Collectable.Types.MINE){
            generateMines(gameRound);
        }
        else if (type == Collectable.Types.CLOCK){
            stopEnemiesForTime(gameRound);
        }
        else if (type == Collectable.Types.ENGINE){
            switchOnEngine();
        }
        else if (type == Collectable.Types.RADAR){
            switchOnRadarForTime();
        }
    }

    private void switchOnRadarForTime() {
        Logger.debug("Radar must be switched on ");
    }

    private void switchOnEngine() {
        int time = PlayerProgressControllerSingleton.getInstance().getIntegerValue(Keys.MAX_TIME_FOR_ENGINE_PERFORMANCE)*1000;
        engineUpgrade++;
        getTankController().switchOnUpgradedEngine(time);
        Logger.debug("Engine must be switched on ");
    }

    private void stopEnemiesForTime(GameRound gameRound) {

        int timeToBlock = PlayerProgressControllerSingleton.getInstance().getIntegerValue(Keys.TIME_TO_BLOCK_ENEMIES)*1000;
        int count = 0;
        for (Entity entity : gameRound.getEntities()){
            if (entity instanceof EnemyTank){
                EnemyTank enemyTank = (EnemyTank) entity;
                enemyTank.blockForTime(timeToBlock);
                count++;
            }
        }
        Logger.debug(count + " enemies were blocked for: " + timeToBlock);
    }

    private void generateMines(GameRound gameRound) {
        //Logger.debug("Not implemented yet");
        int minesMustBeGenerated = PlayerProgressControllerSingleton.getInstance().getIntegerValue(Keys.MINES);
        MinesOnFieldGenerator minesOnFieldGenerator = new MinesOnFieldGenerator();
        ArrayList <Coordinate> arrayList = minesOnFieldGenerator.generateMinesPositions(minesMustBeGenerated, gameRound);
        for (int i  = 0 ; i < arrayList.size(); i++) {
            Mine mine = new Mine(engine, gameRound.getPhysicWorld(), arrayList.get(i), PLAYER);
            gameRound.addEntityToEnd(mine);
        }
    }


    public static PlayerTank create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);

        PlayerTank tank = new PlayerTank(engine, physicWorld, pos, values[2], values[3]);
        tank.setId(entityData.getId());
        return tank;
    }

    public static int getWidthForChassis(int type){
        if (type == TurretTypes.SIMPLE_TURRET) return ENTITY_NORMAL_DIM-10;
        else if (type == TurretTypes.LONG_CANNON_TURRET) return ENTITY_NORMAL_DIM-12;
        else if (type == TurretTypes.DOUBLE_CANNONS_TURRET) return ENTITY_NORMAL_DIM-8;
        else return ENTITY_NORMAL_DIM-4;
    }

    public static int getHeightForChassis(int type){
        return getWidthForChassis(type);
    }

    public int getTurretType() {
        return turretType;
    }

    public int getChassisType() {
        return engineUpgrade;
    }

    public int getBulletType() {
        if (turretType == TurretTypes.ROCKET_LAUNCHER_TURRET) return Bullet.Types.MISSILE;
        else if (turretType == TurretTypes.SIMPLE_TURRET) return Bullet.Types.SIMPLE_BULLET;
        else return Bullet.Types.FAST_BULLET;
    }

    @Override
    public void updateAmmoForTurret() {
        ammoForActualWeapon = INFINITY_AMMO;
        if (turretType == TurretTypes.ROCKET_LAUNCHER_TURRET){
            ammoForActualWeapon =  PlayerProgressControllerSingleton.getInstance().getIntegerValue(Keys.MAX_ROCKETS);
            }
        }




    @Override
    public boolean attackBy(Entity entity, GameRound gameRound){
        int degradationStep = 1;
        if (entity instanceof Bullet){
            Bullet bullet = (Bullet) entity;
            if (bullet.getOwnerType() != Tank.ENEMY){
                if (GameController.getDifficulty() == GlobalConstants.EASY_DIFFICULTY || GameController.getDifficulty() == GlobalConstants.MEDIUM_DIFFICULTY){
                    degradationStep = 0;
                }
            }
            else {

            }
        }
        if (degradationStep != 0) {
            //Logger.debug("I must implement player hitting");
            if (life == 1){
                return KILLED;
            }
            else {
                turretType = TurretTypes.SIMPLE_TURRET;
                engineUpgrade = EngineTypes.SIMPLE;
                life--;
                if (entity instanceof Bullet){
                    Bullet bullet = (Bullet) entity;
                    if (bullet.isMissile()){
                        life = 0;
                    }
                }
                return !KILLED;
            }
            //life = EngineTypes.SIMPLE;
            //if (GameController.getDifficulty() == GlobalConstants.HARD_DIFFICULTY) degradationStep = 3;
            //else if (GameController.getDifficulty() == GlobalConstants.MEDIUM_DIFFICULTY) degradationStep = 2;

            //turretType -= degradationStep;
            /*if (turretType <= TurretTypes.SIMPLE_TURRET) {
                turretType = TurretTypes.SIMPLE_TURRET;
                return KILLED;
            } else return KILLED;*/

        }
        else return !KILLED;
    }

    public void draw(PGraphics graphics, Camera gameCamera) {
        //It must be in Tank class
        // tankGraphic.update(graphics);
        tankGraphic.draw(graphics, gameCamera);
        //graphicElementInGame.draw(graphics, gameCamera, pos.x, pos.y);
    }
}
