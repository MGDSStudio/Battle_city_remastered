package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.Keys;
import io.itch.mgdsstudio.battlecity.game.dataloading.PlayerProgressControllerSingleton;
import processing.core.PApplet;
import processing.core.PVector;

public class TankParameters {
    protected float rotatingMaxVelocity, turretRotatingMaxVelocity;   //gradInMillisecond
    protected float rotatingAccelerate, rotatingBrakingAccelerate, turretRotatingAccelerate;   //gradInMillisecond
    protected float rotatingTorque;
    private PVector movingForceVector;
    protected float forwardMaxVelocityInWorldUnits, backwardMaxVelocityInWorldUnits;
    protected float forwardMaxVelocityInWorldUnitsAfterMotorUpgrade, backwardMaxVelocityInWorldUnitsAfterMotorUpgrade;
    protected float accelerateForce, turretBrakingAccelerate;
    protected float angularDampingForBody, linearDampingForBody;

    protected int money, lifes, exp, level;
    protected int normalBulletVelocity, fastBulletVelocity;

    private final Tank tank;

    private final PVector mutVec1 = new PVector();
    public TankParameters(Tank tank, int difficulty){
        this.tank = tank;
        if (tank instanceof PlayerTank){
            initPlayerTankData((PlayerTank) tank, difficulty);
        }
        else initEnemyTankData((EnemyTank) tank, difficulty);
    }

    private void initEnemyTankData(EnemyTank tank, int difficulty) {
        initDefaultData(tank);
    }

    private void initPlayerTankData(PlayerTank tank, int difficulty) {
        initDataFromProgressFile(tank);
    }


    private void initDataFromProgressFile(Tank tank){
        Logger.debug("Start to load player data");
        PlayerProgressControllerSingleton playerProgressController = PlayerProgressControllerSingleton.getInstance();
        rotatingMaxVelocity = playerProgressController.getFloatValueWithDoublePrecision(Keys.ROTATING_MAX_VELOCITY);
        rotatingAccelerate = playerProgressController.getFloatValueWithDoublePrecision(Keys.ROTATING_ACCELERATE);
        forwardMaxVelocityInWorldUnits = playerProgressController.getFloatValueWithDoublePrecision(Keys.FORWARD_MAX_VELOCITY_IN_WORLD_UNITS);
        backwardMaxVelocityInWorldUnits = playerProgressController.getFloatValueWithDoublePrecision(Keys.BACKWARD_MAX_VELOCITY_IN_WORLD_UNITS);
        forwardMaxVelocityInWorldUnitsAfterMotorUpgrade = playerProgressController.getFloatValueWithDoublePrecision(Keys.FORWARD_MAX_VELOCITY_IN_WORLD_UNITS_AFTER_MOTOR_UPGRADE);
        backwardMaxVelocityInWorldUnitsAfterMotorUpgrade = playerProgressController.getFloatValueWithDoublePrecision(Keys.BACKWARD_MAX_VELOCITY_IN_WORLD_UNITS_AFTER_MOTOR_UPGRADE);


        float accelerate = forwardMaxVelocityInWorldUnits * 1000f;
        accelerateForce = accelerate*tank.getBody().getMass();
        turretRotatingMaxVelocity =  playerProgressController.getFloatValueWithDoublePrecision(Keys.TURRET_ROTATING_MAX_VELOCITY);    //grad in millisecond
        turretRotatingAccelerate = playerProgressController.getFloatValueWithDoublePrecision(Keys.TURRET_ROTATING_ACCELERATE);
        turretBrakingAccelerate = turretRotatingMaxVelocity /6f;
        rotatingBrakingAccelerate = turretBrakingAccelerate;
        angularDampingForBody = 2.9f;
        linearDampingForBody = 5.5f;
        normalBulletVelocity = playerProgressController.getIntegerValue(Keys.NORMAL_BULLET_VELOCITY);
        fastBulletVelocity = playerProgressController.getIntegerValue(Keys.FAST_BULLET_VELOCITY);
        movingForceVector = new PVector(accelerateForce*PApplet.cos(tank.getBody().getAngle()), accelerateForce*PApplet.sin(tank.getBody().getAngle()));
        rotatingTorque = rotatingAccelerate*tank.getBody().getMass();

        money = playerProgressController.getIntegerValue(Keys.MONEY);
        lifes = playerProgressController.getIntegerValue(Keys.LIFES);
        level = playerProgressController.getIntegerValue(Keys.LEVEL);
        exp = playerProgressController.getIntegerValue(Keys.EXP);
        Logger.debug("End to load player data. Max vel:" + forwardMaxVelocityInWorldUnits + "/" + forwardMaxVelocityInWorldUnitsAfterMotorUpgrade);
    }

    private void initDefaultData(Tank tank){
        float physicWorldCoef = 30;
        rotatingMaxVelocity = 360f/3000f;  //grad in millisecond
        rotatingAccelerate = rotatingMaxVelocity*5;
        forwardMaxVelocityInWorldUnits = physicWorldCoef*(512f/14f)/3000f;     //pixels in millisecond
        backwardMaxVelocityInWorldUnits = forwardMaxVelocityInWorldUnits *0.5f;
        forwardMaxVelocityInWorldUnitsAfterMotorUpgrade = forwardMaxVelocityInWorldUnits*1.5f;
        backwardMaxVelocityInWorldUnitsAfterMotorUpgrade = backwardMaxVelocityInWorldUnits*1.5f;
        float accelerate = forwardMaxVelocityInWorldUnits * 1000;
        accelerateForce = accelerate*tank.getBody().getMass();

        turretRotatingMaxVelocity = 360f/2000f;    //grad in millisecond
        turretRotatingAccelerate = turretRotatingMaxVelocity/2;
        turretBrakingAccelerate = turretRotatingMaxVelocity /6f;

        rotatingBrakingAccelerate = turretBrakingAccelerate;

        angularDampingForBody = 2.9f;
        linearDampingForBody = 5.5f;
        normalBulletVelocity = 12;
        fastBulletVelocity = 6;

        movingForceVector = new PVector(accelerateForce*PApplet.cos(tank.getBody().getAngle()), accelerateForce*PApplet.sin(tank.getBody().getAngle()));

        rotatingTorque = rotatingAccelerate*tank.getBody().getMass();
    }

    public float getRotatingMaxVelocity() {
        return rotatingMaxVelocity;
    }

    public float getTurretRotatingMaxVelocity() {
        return turretRotatingMaxVelocity;
    }

    public float getForwardMaxVelocityInWorldUnits(int engineUpgrade) {
        if (engineUpgrade == 0) return forwardMaxVelocityInWorldUnits;
        else return forwardMaxVelocityInWorldUnitsAfterMotorUpgrade;
    }


    public float getTurretBrakingAccelerate() {
        return turretBrakingAccelerate;
    }

    public float getRotatingAccelerate() {
        return rotatingAccelerate;
    }

    public float getTurretRotatingAccelerate() {
        return turretRotatingAccelerate;
    }

    public float getBackwardMaxVelocityInWorldUnits(int engineUpgrade) {
        if (engineUpgrade == 0) return backwardMaxVelocityInWorldUnits;
        else return backwardMaxVelocityInWorldUnitsAfterMotorUpgrade;
    }

    public float getBodyRotationBrakingAccelerate() {
        return rotatingBrakingAccelerate;
    }

    public float getAngularDampingForBody() {
        return angularDampingForBody;
    }

    public float getLinearDampingForBody() {
        return linearDampingForBody;
    }

    public PVector getMovingForceVector() {
        float angle = tank.getBody().getAngle();
        movingForceVector.x = accelerateForce*PApplet.cos(angle);
        movingForceVector.y = -1f*accelerateForce*PApplet.sin(angle);
        return movingForceVector;
    }

    public float getRotatingTorque() {
        return rotatingTorque;
    }

    public int getBulletVelocity(int bulletType) {
        if (bulletType == Bullet.Types.SIMPLE_BULLET) return normalBulletVelocity;
        else return fastBulletVelocity;
    }
}
