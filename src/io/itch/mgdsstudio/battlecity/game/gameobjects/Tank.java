package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.control.Control;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.debuggraphic.TankDebugGraphic;
import io.itch.mgdsstudio.engine.graphic.tankgraphic.TankGraphic;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;

public abstract class Tank extends SolidObject {

    public static final int PLAYER = 0;
    public static final int ENEMY = -1;

    public static final int FRIEND_1 = 1;
    public static final int FRIEND_2 = 2;
    public static final int FRIEND_3 = 3;
    public static final int FRIEND_4 = 4;

    protected Control control;
    private TankController tankController;
    protected Turret turret;
    protected int type;

    protected int role;
    protected final static int INFINITY_AMMO = 9999;
    protected int ammoForActualWeapon = INFINITY_AMMO;
    protected TankGraphic tankGraphic;
    protected int turretType, engineUpgrade;

    //, Coordinate pos, int angle, int life, int width, int height, int bodyForm, BodyType bodyType, int additionalDim) {
    protected Tank(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int turretAbsoluteAngle, int life, int role, int width, int height){
        super(engine, physicWorld, pos, angle, life, width, height, BodyForms.TANK, BodyType.DYNAMIC, ENTITY_NORMAL_DIM);
        turret = new Turret(turretAbsoluteAngle);
        this.role = role;
        this.maxLife = life;
        tankController = new TankController(this);
        tankGraphic = new TankGraphic(this, engine);
    }

    public float getForwardMaxVelocityInWorldUnits(){
        return tankController.getForwardMaxVelocityInWorldUnits();
    }

    public float getBackwardMaxVelocityInWorldUnits(){
        return tankController.getBackwardMaxVelocityInWorldUnits();
    }

    public int getDyingAnimationType() {
        return IAnimations.TANK_DESTRUCTION;
    }



    protected void createDebugGraphic(IEngine engine) {
        debugGraphic = new TankDebugGraphic(this, engine.getProcessing());
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        super.update(gameRound, deltaTime);

        tankController.update(gameRound, deltaTime);
        turret.update();
    }


    public void gotCollectable(Collectable collectable, GameRound gameRound) {
        boolean canBeGot = false;
        if (this instanceof PlayerTank){
            applyEffect(collectable, gameRound);
            canBeGot = true;
            gameRound.removeEntity(collectable);
        }
        else {
            if (GameRound.getDifficulty() == GlobalConstants.EASY_DIFFICULTY){
                canBeGot = false;
            }
            else if (GameRound.getDifficulty() == GlobalConstants.MEDIUM_DIFFICULTY){
                canBeGot = true;
                gameRound.removeEntity(collectable);
            }
            else {
                applyEffect(collectable, gameRound);
                canBeGot = true;
                gameRound.removeEntity(collectable);
            }
        }
        if (canBeGot) Logger.debug("Tank: " + getId() + " got object: " + collectable.getType());
    }

    protected abstract void applyEffect(Collectable collectable, GameRound gameRound);

    public void setTurretAbsoluteAngle(float turretAbsoluteAngle) {
        turret.absoluteAngle = turretAbsoluteAngle;
    }

    public int getType() {
        return type;
    }

    public float getTurretAbsoluteAngle() {
        return turret.absoluteAngle;
    }

    public Turret getTurret() {
        return turret;
    }

    public int getAngleCircleDiam() {
        return width/8;
    }

    public float getRelativeTurretAngle() {
        return (turret.absoluteAngle-angle);
    }

    public int getRole() {
        return role;
    }

    public TankController getTankController() {
        return tankController;
    }


    @Override
    public boolean executeAction(GLobalSerialAction action) {
        //Logger.debugLog("Id: " + getId() + " executes the action " + action );
        return tankController.addAction(action);
    }

    public abstract int getBulletType();

    public void appendListener(GlobalListener globalListener) {

    }

    public int getBulletVelocity() {
        return tankController.getBulletVelocity(getBulletType());
    }

    protected void updateGraphicForTurret() {
        tankGraphic.updateGraphicForActualStatement(turretType, engineUpgrade);
    }

    public void decrementAmmo() {
        ammoForActualWeapon--;
    }

    public abstract void updateAmmoForTurret();

    protected void blockForTime(int timeToBlock) {
    }

    public void removeMotorPerformance() {
        tankController.removeMotorPerformance();
    }

    protected class Turret{
        protected float absoluteAngle;
        protected int actualCannonNumber, maxCannonsNumber;
        private final PVector relativeBulletPlace = new PVector();
        private int barrelNumber = 0;
        protected float RELATIVE_BULLET_START_PLACE_SHIFT_FOR_DOUBLE = 2.5f;
        protected float RELATIVE_BULLET_START_PLACE_SHIFT_FOR_TURRET = 1.5f;
        private int bulletsInLine = 5;


        Turret(int turretAbsoluteAngle){
            this.absoluteAngle = turretAbsoluteAngle;
            relativeBulletPlace.x = 0;
        }


        public PVector getRelativeBulletAppearingPlace() {
            if (turretType == PlayerTank.TurretTypes.DOUBLE_CANNONS_TURRET){
                if (barrelNumber>1) barrelNumber = 0;
                if (barrelNumber == 0){
                    relativeBulletPlace.y = RELATIVE_BULLET_START_PLACE_SHIFT_FOR_DOUBLE;
                }
                else relativeBulletPlace.y = -RELATIVE_BULLET_START_PLACE_SHIFT_FOR_DOUBLE;

                Logger.debug("Relative place: " + relativeBulletPlace.x + "x" +relativeBulletPlace.y + " for number: " + barrelNumber);

                barrelNumber++;
            }
            else if (turretType == PlayerTank.TurretTypes.ROCKET_LAUNCHER_TURRET){
                if (barrelNumber>=(bulletsInLine-1)) barrelNumber = 0;
                if (barrelNumber == 0) relativeBulletPlace.y = -RELATIVE_BULLET_START_PLACE_SHIFT_FOR_TURRET*2f;
                else if (barrelNumber == 1) relativeBulletPlace.y = -RELATIVE_BULLET_START_PLACE_SHIFT_FOR_TURRET*1f;
                else if (barrelNumber == 2) relativeBulletPlace.y = 0;
                else if (barrelNumber == 3) relativeBulletPlace.y = RELATIVE_BULLET_START_PLACE_SHIFT_FOR_TURRET*1f;
                else if (barrelNumber == 4) relativeBulletPlace.y = RELATIVE_BULLET_START_PLACE_SHIFT_FOR_TURRET*2f;
                barrelNumber++;
            }
            else {
                relativeBulletPlace.y = 0;
                relativeBulletPlace.x = 0;
            }

            return relativeBulletPlace;
        }

        private void update() {

        }
    }





}
