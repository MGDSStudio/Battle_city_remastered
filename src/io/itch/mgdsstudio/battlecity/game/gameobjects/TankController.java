package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.game.GameController;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.Keys;
import io.itch.mgdsstudio.battlecity.game.dataloading.PlayerProgressControllerSingleton;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.EngineController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.TankBlockController;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.Timer;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class TankController {



    public interface MovementConstants{

        int NOTHING = 0;
        int ROTATE_CW = 1;
        int ROTATE_CCW = 2;
        int MOVE_FORWARD = 3;
        int MOVE_BACKWARD = 4;
        int MOVE_FORWARD_ROTATE_CW = 5;
        int MOVE_FORWARD_ROTATE_CCW = 6;
    }

    private int lastMovementStatement = MovementConstants.NOTHING;

    //public static final boolean FORWARD = true;
    //public static final boolean BACKWARD = false;


    public interface Statements{
        int STAY = 0;
        int MOVE_FORWARD = 10;
        int MOVE_BACKWARD = -10;
        int ROTATE_BODY_CW = 1;
        int ROTATE_BODY_CCW = 2;
        int MOVE_FORWARD_AND_ROTATE_CW = 11;
        int MOVE_FORWARD_AND_ROTATE_CCW = 12;
        int MOVE_BACKWARD_AND_ROTATE_CW = -11;
        int MOVE_BACKWARD_AND_ROTATE_CCW = -12;
        int ROTATE_TURRET_CW = 101;
        int ROTATE_TURRET_CCW = 102;
        int NO_TURRET_ROTATION = 100;
        int SHOT = 102;
    }



    private int movingStatement, prevMovingStatement;
    private int turretStatement, prevTurretStatement;
    //private boolean shotingStatement;
    public static final boolean CCW = false;
    public static final boolean CW = true;
    public static final boolean FORWARD = false;
    public static final boolean BACKWARD = true;


    protected float linearDamping, angularDamping; // is bodyBrakingAccelerate
    protected TankParameters tankParameters;

    private Tank tank;


    private TurretController turretController;
    private TankBlockController tankBlockController;

    private final ArrayList <GLobalSerialAction> actions = new ArrayList<>();
    private final Vec2 mutForceVector = new Vec2();
    private final static int INFINITY_BULLETS = 9999;

    protected EngineController engineController;
    protected boolean lastMovementDirection = FORWARD;
    private int lastMovingAction;
    //private int bullets = INFINITY_BULLETS;


    public boolean getLastMovementDirection() {
        return lastMovementDirection;
    }

    public TankController(Tank tank) {
        this.tank = tank;
        engineController = new EngineController(tank);
        tankBlockController = new TankBlockController(tank);
        turretController = new TurretController(tank);
        setTankSpecificParameters(tank);
        tank.getBody().setAngularDamping(tankParameters.getAngularDampingForBody());
        tank.getBody().setLinearDamping(tankParameters.getLinearDampingForBody());
    }

    public float getForwardMaxVelocityInWorldUnits() {
        return tankParameters.getForwardMaxVelocityInWorldUnits(tank.engineUpgrade);
    }
    public float getBackwardMaxVelocityInWorldUnits() {
        return tankParameters.getBackwardMaxVelocityInWorldUnits(tank.engineUpgrade);
    }

    private void setTankSpecificParameters(Tank tank) {
        tankParameters = new TankParameters(tank, GameRound.getDifficulty());

    }

    void update(GameRound gameRound, long deltaTime){
        tankBlockController.update(gameRound.getEngine().getProcessing().millis());
        engineController.update((int) deltaTime);
        if (tankBlockController.isFree()) {
            for (int i = (actions.size() - 1); i >= 0; i--) {
                applyAction(actions.get(i), deltaTime, gameRound);

            }
        }
        else{
            if (tankBlockController.isVibrated()){
                updateTurretRotation(tankBlockController.getRandomRotatingDirection(), deltaTime);
            }
        }
        prevTurretStatement = turretStatement;
        prevMovingStatement = movingStatement;
    }

    private void updateBodyRotations(int mainValue){
        if (mainValue == ActionPrefixes.CW){
            tank.getBody().applyTorque(tankParameters.getRotatingTorque());
            if (lastMovingAction != MovementConstants.ROTATE_CW) lastMovingAction = MovementConstants.ROTATE_CW;
        }
        else if (mainValue == ActionPrefixes.CCW){
            tank.getBody().applyTorque(-1f*tankParameters.getRotatingTorque());
            if (lastMovingAction != MovementConstants.ROTATE_CCW) lastMovingAction = MovementConstants.ROTATE_CCW;
        }
        else if (mainValue == ActionPrefixes.FORWARD || mainValue == ActionPrefixes.BACKWARD || mainValue == ActionPrefixes.DOES_NOT_TOUCHED){
        }
    }

    public Vec2 getRelativeVibrationPos(){
        return tankBlockController.getVibrationRelativePos();
        /*if (tankBlockController.isVibrated()){

        }
        else */
    }

    public boolean isLastMovementDirectionForward() {
        if (lastMovementDirection == FORWARD) return true;
        else return false;
    }

    private Vec2 getMovingForceVector(boolean dir){
        PVector force = tankParameters.getMovingForceVector();
        mutForceVector.x = force.x;
        mutForceVector.y = force.y;
        if (dir == BACKWARD) mutForceVector.negateLocal();
        return mutForceVector;
    }

    private void updateBodyMoveAndRotate(int mainValue) {

        if (mainValue == ActionPrefixes.CW){
            tank.getBody().applyTorque(tankParameters.getRotatingTorque());
            addMovement(FORWARD);
            if (lastMovingAction != MovementConstants.MOVE_FORWARD_ROTATE_CW) lastMovingAction = MovementConstants.MOVE_FORWARD_ROTATE_CW;
            if (lastMovementDirection != FORWARD) lastMovementDirection = FORWARD;
        }
        else if (mainValue == ActionPrefixes.CCW){
            tank.getBody().applyTorque(-1f*tankParameters.getRotatingTorque());
            addMovement(FORWARD);
            if (lastMovingAction != MovementConstants.MOVE_FORWARD_ROTATE_CCW) lastMovingAction = MovementConstants.MOVE_FORWARD_ROTATE_CCW;
            if (lastMovementDirection != FORWARD) lastMovementDirection = FORWARD;
        }
        else if (mainValue == ActionPrefixes.FORWARD){
            addMovement(FORWARD);
            if (lastMovementDirection != FORWARD) lastMovementDirection = FORWARD;
            if (lastMovingAction != MovementConstants.MOVE_FORWARD) lastMovingAction = MovementConstants.MOVE_FORWARD;
            //PVector force = new PVector(getMovingForceVector(FORWARD).x, getMovingForceVector(FORWARD).y);
        }
        else if (mainValue == ActionPrefixes.BACKWARD){
            addMovement(BACKWARD);
            if (lastMovingAction != MovementConstants.MOVE_BACKWARD) lastMovingAction = MovementConstants.MOVE_BACKWARD;
            if (lastMovementDirection != BACKWARD) lastMovementDirection = BACKWARD;
        }
    }

    public int getLastMovingAction() {
        return lastMovingAction;
    }

    public void blockForTime(int timeToBlock, int millis) {
        tankBlockController.blockForTime(timeToBlock, millis);

    }
    private void addMovement(boolean dir) {
        tank.getBody().applyForceToCenter(getMovingForceVector( dir));
        controlMaxVelocities(dir);
    }

    private void controlMaxVelocities(boolean movementSide) {
        float maxVel = tankParameters.getForwardMaxVelocityInWorldUnits(tank.engineUpgrade);
        //Logger.debug("MAX VELO: " + maxVel);
        if (movementSide == BACKWARD)  maxVel = tankParameters.getBackwardMaxVelocityInWorldUnits(tank.engineUpgrade);
        float actualVel = tank.getBody().getLinearVelocity().normalize();
            if (actualVel > maxVel) {
                float coef = maxVel / tank.getBody().getLinearVelocity().normalize() + 0.001f;
                tank.getBody().setLinearVelocity(tank.getBody().getLinearVelocity().mul(coef));
            }
            else {


            }

    }

    public int getBulletVelocity(int bulletType) {
        return tankParameters.getBulletVelocity(bulletType);
    }

    public void updateTankForActualTurretStatement() {
        tank.updateGraphicForTurret();
        tank.updateAmmoForTurret();
        updateBulletVelocityForActualTurret();
    }

    protected void updateBulletVelocityForActualTurret() {
        if (tank.turretType == PlayerTank.TurretTypes.SIMPLE_TURRET){

            //getTankController().tankParameters.get
        }
        else if (tank.turretType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET){

        }
    }

    public void switchOnUpgradedEngine(int time) {
        engineController.start(time);
    }


    private void updateTurretRotation(int mainValue, long deltaTime) {
        if (mainValue == ActionPrefixes.CW){
            turretController.rotateTurret(CW, deltaTime);
        }
        else if (mainValue == ActionPrefixes.CCW){
            turretController.rotateTurret(CCW, deltaTime);
        }
        else if (mainValue == ActionPrefixes.FORWARD){

        }
    }

    private void applyAction(GLobalSerialAction action, long deltaTime, GameRound gameRound) {
        if (action.getPrefix() == ActionPrefixes.MOVEMENT_STICK_BODY_ROTATION){
            updateBodyRotations(action.getMainValue());
        }
        else if (action.getPrefix() == ActionPrefixes.MOVEMENT_STICK_RUN_AND_ROTATION){
            updateBodyMoveAndRotate(action.getMainValue());
        }
        else if (action.getPrefix() == ActionPrefixes.AIMING_STICK_TURRET_ROTATION){
            updateTurretRotation(action.getMainValue(), deltaTime);
        }
        else if (action.getPrefix() == ActionPrefixes.AIMING_STICK_SHOOTING){
            updateShooting(action.getMainValue(), deltaTime, gameRound);
        }
    }



    private void updateShooting(int mainValue, long deltaTime, GameRound gameRound) {
        if (mainValue != ActionPrefixes.DOES_NOT_TOUCHED) {
            if (turretController.canBeShotMade()) {
                Coordinate bulletStartPos = calculateBulletStartPos();
                int angle = (int) tank.getTurretAbsoluteAngle();
                Bullet bullet = new Bullet(gameRound.getEngine(), gameRound.getPhysicWorld(), bulletStartPos, angle, 0, tank.getBulletType(), tank.getType(), tank.getBulletVelocity());
                gameRound.addEntityToEnd(bullet);
                ShotDustEffect shotDustEffect = new ShotDustEffect(gameRound.getEngine(), bulletStartPos.clone(), angle, tank);
                gameRound.addEntityToEnd(shotDustEffect);
                turretController.makeShot(gameRound.getEngine());
                tank.decrementAmmo();
                if (tank instanceof PlayerTank){
                    //Logger.debug("This weapon has only: " + tank.ammoForActualWeapon);
                }
                if (tank.ammoForActualWeapon <=0){
                    tank.ammoForActualWeapon = Tank.INFINITY_AMMO;
                    tank.turretType= tank.turretType-1;
                    updateTankForActualTurretStatement();
                }
            }
        }
    }

    private Coordinate calculateBulletStartPos() {
        float x = tank.getPos().x;
        float y = tank.getPos().y;
        float cannonEndX = (tank.getWidth()/2);
        float cannonEndY = (tank.getHeight()/2);
        PVector relativeCenterShotVector = turretController.getRelativeBulletAppearingPlace();
        PVector vector = new PVector(cannonEndX, cannonEndY);
        vector.mult(1.5f);
        vector.rotate(-PApplet.QUARTER_PI);
        vector.x+= relativeCenterShotVector.x*1.5f;
        vector.y+= relativeCenterShotVector.y*1.5f;
        vector.rotate(PApplet.radians(tank.getTurretAbsoluteAngle()));

        Coordinate coordinate = new Coordinate(vector.x+x, vector.y+y);
        return coordinate;
    }


    private void move(boolean dir) {

    }

    public boolean addAction(GLobalSerialAction action) {

        if (actions.size()==0){
            actions.add(action);
        }
        else {
            GLobalSerialAction conflicted = getConflictedActionWithSamePrefix(action);
            if (conflicted != null) {
                //Logger.debugLog("Conflicted: " + conflicted.getPrefix() + " value: " + conflicted.getMainValue());
                actions.remove(conflicted);
            }

            actions.add(action);

        }

        return true;
    }

    private GLobalSerialAction getConflictedActionWithSamePrefix(GLobalSerialAction action){
        GLobalSerialAction actionWithSamePrefix = getActionByPrefix(action.getPrefix());
        if (actionWithSamePrefix != null){
            return actionWithSamePrefix;
        }
        return null;
    }

    private GLobalSerialAction getActionByPrefix(char prefix){
        for (GLobalSerialAction action : actions){
            if (action.getPrefix() == prefix){
                return action;
            }
        }
        return null;
    }

    public void removeMotorPerformance() {
        tank.engineUpgrade = 0;
        //tankParameters.
        Logger.debug("Motor is not more upgraded");
    }


    private class TurretController{
        private float actualRotatingVelocity;
        private int timeForReload;
        private Timer timer;
        private Tank.Turret turret;
        private TurretController(Tank tank){
            this.turret = tank.getTurret();
            resetTimeToReloadData();
        }

        private void resetTimeToReloadData(){
            int t = 1500;
            if (tank.getRole() == Tank.ENEMY){  //if (tank.getType() == Tank.ENEMY){
                if (GameController.getDifficulty() == GlobalConstants.EASY_DIFFICULTY){
                    t = 2500;
                }
                else if (GameController.getDifficulty() == GlobalConstants.MEDIUM_DIFFICULTY){
                    t = 2000;
                }
                else t = 1500;
            }
            else {
                PlayerTank playerTank = (PlayerTank) tank;
                if (playerTank.getTurretType() == PlayerTank.TurretTypes.SIMPLE_TURRET || playerTank.getTurretType() == PlayerTank.TurretTypes.LONG_CANNON_TURRET){
                    t = 2000;
                }
                else if (playerTank.getTurretType() == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET || playerTank.getTurretType() == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET){
                    t = 1000;
                }
            }
            timeForReload = t;
        }

        private boolean canBeShotMade(){
            if (timer == null) return true;
            else {
                if (timer.isTime()) return true;
                else return false;
            }
        }

        private void makeShot(IEngine engine){
            timer = new Timer(getTimeToReload(), engine.getProcessing());
        }

        private int getTimeToReload() {
            if (tank.getRole() == Tank.ENEMY){
                if (GameController.getDifficulty() == GlobalConstants.EASY_DIFFICULTY){
                    return  2500;
                }
                else if (GameController.getDifficulty() == GlobalConstants.MEDIUM_DIFFICULTY){
                    return 2000;
                }
                else return  1500;
            }
            else {
                PlayerProgressControllerSingleton controller = PlayerProgressControllerSingleton.getInstance();
                PlayerTank playerTank = (PlayerTank) tank;
                if (playerTank.getTurretType() == PlayerTank.TurretTypes.SIMPLE_TURRET || playerTank.getTurretType() == PlayerTank.TurretTypes.LONG_CANNON_TURRET){
                    return controller.getIntegerValue(Keys.TIME_BETWEEN_SHOTS_FOR_SIMPLE_CANNON);
                }
                else if (playerTank.getTurretType() == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET || playerTank.getTurretType() == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET){
                    return controller.getIntegerValue(Keys.TIME_BETWEEN_SHOTS_FOR_UPGRADED_CANNON);
                }
                return controller.getIntegerValue(Keys.EXP);
            }
        }

        protected void rotateTurret(boolean dir, long deltaTime) {
            brake(deltaTime);
            if (dir == CW) {
                actualRotatingVelocity +=tankParameters.getTurretRotatingAccelerate();
                if (actualRotatingVelocity >tankParameters.getTurretRotatingMaxVelocity()) {
                    actualRotatingVelocity =tankParameters.getTurretRotatingMaxVelocity();
                }
            }
            else {
                actualRotatingVelocity -=tankParameters.getTurretRotatingAccelerate();
                if (PApplet.abs(actualRotatingVelocity)>tankParameters.getTurretRotatingMaxVelocity()) {
                    actualRotatingVelocity =-1f*tankParameters.getTurretRotatingMaxVelocity();
                }
            }
            tank.getTurret().absoluteAngle+= actualRotatingVelocity *deltaTime;
        }

        private void brake(long deltaTime){
            if (actualRotatingVelocity > 0){
                actualRotatingVelocity -=(tankParameters.getTurretBrakingAccelerate()*deltaTime);
                if (actualRotatingVelocity <0) actualRotatingVelocity = 0;
            }
            else if (actualRotatingVelocity < 0) {
                actualRotatingVelocity +=(tankParameters.getTurretBrakingAccelerate()*deltaTime);
                if (actualRotatingVelocity >0) actualRotatingVelocity = 0;
            }
        }

        public PVector getRelativeBulletAppearingPlace() {
            /*
            if (tank.turretType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET){
                return turret.getRelativeBulletAppearingPlace();

            }*/
            return turret.getRelativeBulletAppearingPlace();
        }

        /*public void decrementBullets() {
            bullets--;
            if (bullets <= 0){
                if (tank.turretType == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET){
                    bullets = INFINITY_BULLETS;
                    !
                }
            }
        }*/
    }
}
