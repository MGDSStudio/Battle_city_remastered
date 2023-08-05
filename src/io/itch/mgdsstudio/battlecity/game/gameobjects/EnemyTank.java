package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ai.Ai;

import io.itch.mgdsstudio.battlecity.game.net.ISerializeable;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;
import processing.data.IntList;

import java.util.ArrayList;

public class EnemyTank extends Tank implements ISerializeable {
    public final static int CONTROL_PER_AI = 0;
    public final static int CONTROL_PER_INTERNET = -1;
    public final static int CONTROL_IN_EDITOR = 1;
    private Ai ai;



    public interface Types{
        int SIMPLE_TANK = 0;
        int FAST_TANK = 1;
        int EASY_ARMORED_TANK = 2;
        int GOOD_ARMORED_TANK = 3;
    }
    private int type;
    private int controlFrom;
    private int aiType;



    public EnemyTank(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int turretAbsoluteAngle, int life, int role, int type, int controlFrom, int aiType){
        super(engine, physicWorld, pos, angle, turretAbsoluteAngle, getTypeSpecificLife(life, type), role, getWidthForType(type), getHeightForType(type));
        init(type, controlFrom, getRealAiModel(aiType));
    }


    public EnemyTank(IEngine engine, PhysicWorld physicWorld, IntList intList) {
        super(engine, physicWorld, new Coordinate(intList.get(0), intList.get(1)), intList.get(2), intList.get(3), intList.get(4), intList.get(5), getWidthForType(intList.get(6)), getHeightForType(intList.get(6)));
        int type = intList.get(6);
        int controlFrom = intList.get(7);
        int aiType = EnemyTank.CONTROL_PER_INTERNET;
        init(type, controlFrom, aiType);
    }

    private static int getRealAiModel(int selectedAi){
        if (selectedAi == CONTROL_PER_INTERNET) return selectedAi;
        else if (MainController.isEditor()){
            return CONTROL_IN_EDITOR;
        }
        else return selectedAi;
    }

    private void init(int type, int controlFrom, int aiType){
        this.type = type;
        this.controlFrom = controlFrom;
        this.aiType = aiType;
        ai = Ai.createAi(aiType, this, controlFrom);

    }

    @Override
    protected void setBodyData() {
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT_OR_ENEMY);
    }

    @Override
    public IntList getSerializedIntData(){
        IntList intList = new IntList();
        intList.append((int)pos.x);
        intList.append((int)pos.y);
        intList.append((int)angle);
        intList.append((int) turret.absoluteAngle);
        intList.append(life);
        intList.append(role);
        intList.append(type);
        intList.append(controlFrom);
        intList.append(aiType);
        return intList;
    }

    private static int getTypeSpecificLife(int life, int type) {
        if (life<=0){
            if (type == Types.SIMPLE_TANK || type == Types.EASY_ARMORED_TANK){
                life = 1;
            }
            else if (type == Types.EASY_ARMORED_TANK) life = 2;
            else life = 3;
            return life;
        }
        else return life;
    }

    public static EnemyTank create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        EnemyTank tank = new EnemyTank(engine, physicWorld, pos, values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
        tank.setId(entityData.getId());
        return tank;
    }

    public static int getWidthForType(int type){    //public for editor
        if (type == Types.SIMPLE_TANK) return ENTITY_NORMAL_DIM-10;
        else if (type == Types.FAST_TANK) return ENTITY_NORMAL_DIM-12;
        else if (type == Types.EASY_ARMORED_TANK) return ENTITY_NORMAL_DIM-8;
        else return ENTITY_NORMAL_DIM-4;
    }

    @Override
    public int getBulletType() {
        if (type == Types.SIMPLE_TANK) return Bullet.Types.FAST_BULLET;
        else if (type == Types.GOOD_ARMORED_TANK) return Bullet.Types.MISSILE;
        else return Bullet.Types.SIMPLE_BULLET;
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        super.update(gameRound, deltaTime);
        ai.update(gameRound,deltaTime);
    }

    @Override
    protected void applyEffect(Collectable collectable, GameRound gameRound) {
        if (collectable.getType() == Collectable.Types.LIFE){
            this.life++;
        }
        else if (collectable.getType() == Collectable.Types.ENGINE){
            ArrayList <Entity> entities = gameRound.getEntities();
            for (int i = 0; i < entities.size(); i++){

            }
        }
        else Logger.debug("Not implemented yet");

    }

    @Override
    public boolean attackBy(Entity attackingObject, GameRound gameRound){
        if (attackingObject.getClass() == Mine.class){
            //Mine kills always
            life = 0;
            gameRound.removeEntity(this);

            return KILLED;
        }
        else {
            if (attackingObject.getClass() == Bullet.class){
                if (type == Types.SIMPLE_TANK || type == Types.FAST_TANK){
                    life = 0;
                    return KILLED;
                }
                else {
                    if (type == Types.EASY_ARMORED_TANK || type == Types.GOOD_ARMORED_TANK){
                        Bullet bullet = (Bullet) attackingObject;
                        if (bullet.canCrushArmoredObjects()){
                            life = 0;
                            return KILLED;
                        }
                        else {
                            life--;
                            if (life <= 0) return KILLED;
                            else return !KILLED;
                        }
                    }
                }
            }
        }
        return !KILLED;
    }

    @Override
    public void appendListener(GlobalListener globalListener) {
        ai.appendListener(globalListener);

    }

    @Override
    public void updateAmmoForTurret() {
        ammoForActualWeapon = INFINITY_AMMO;
    }

    public static int getHeightForType(int type){
        return getWidthForType(type);
    }


    @Override
    protected void blockForTime(int timeToBlock) {
        getTankController().blockForTime(timeToBlock, engine.getProcessing().millis());
    }


    public void draw(PGraphics graphics, Camera gameCamera) {
        //It must be in Tank class
        // tankGraphic.update(graphics);
        tankGraphic.draw(graphics, gameCamera);
        //graphicElementInGame.draw(graphics, gameCamera, pos.x, pos.y);
    }
}
