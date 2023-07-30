package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.EntityVisibilityController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.HaloController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.IActivateable;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ObjectActivatingController;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.game.textes.AbstractText;
import io.itch.mgdsstudio.battlecity.game.textes.DissolvingAndUpwardsMovingText;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.GraphicManager;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.prefs.PreferencesFactory;

public class Collectable extends SolidObject implements IActivateable {
    public final static int COLLECTABLE_NORMAL_DIM = 24;
    private ObjectActivatingController activatingController;
    private boolean active;

    private interface ImageZones{
        ImageZoneSimpleData WEAPON_UPGRADE_GRAPHIC = new ImageZoneSimpleData(799,992, 799+32,992+32);
        ImageZoneSimpleData GEAR_GRAPHIC = new ImageZoneSimpleData(832,992, 832+32,992+32);
        ImageZoneSimpleData MINE_GRAPHIC = new ImageZoneSimpleData(865,992, 865+32,992+32);
        ImageZoneSimpleData ARMOUR_GRAPHIC = new ImageZoneSimpleData(898,992, 898+32,992+32);
        //ImageZoneSimpleData RADAR_GRAPHIC = new ImageZoneSimpleData(929,992, 955,922+32);
        ImageZoneSimpleData RADAR_GRAPHIC = new ImageZoneSimpleData(926,992, 958,992+32);
        ImageZoneSimpleData AI_TURRET_GRAPHIC = new ImageZoneSimpleData(959,992, 959+32,992+32);
        ImageZoneSimpleData CLOCK = new ImageZoneSimpleData(992,992, 1024,1024);
        ImageZoneSimpleData MONEY = new ImageZoneSimpleData(718-16,992, 718+16,1024);

        ImageZoneSimpleData RANDOM_GRAPHIC = new ImageZoneSimpleData(734,992, 766,992+32);
        ImageZoneSimpleData LIFE_GRAPHIC = new ImageZoneSimpleData(766,992, 766+32,992+32);



    }

    public interface Types{
        int LIFE = 0;   //More tanks
        int WEAPON = 1;     //Rocket launcher
        int ENGINE = 2;    //Motor
        int MINE = 3;       //Mines
        int ARMOUR = 4;
        int RADAR = 5;  //Enemies to flag

        int AI_TURRET = 6;  //Enemies to flag

        int RANDOM = 7;  //Enemies to flag

        int CLOCK = 8;  //Enemies to flag
        int MONEY_1 = 21;
        int MONEY_2 = 22;
        int MONEY_3 = 23;
        int MONEY_5 = 24;
        int MONEY_10 = 25;
        int MONEY_15 = 26;
        int MONEY_20 = 27;
        int MONEY_25 = 28;
        int MONEY_30 = 29;
        int MONEY_40 = 30;
        int MONEY_50 = 31;

    }

    boolean isMoney(int type){
        if (type >= Types.MONEY_1 && type <= Types.MONEY_50) return true;
        else return false;
    }

    private interface GotTextes{
        String TEXT_FOR_LIFE = "1UP";
        String TEXT_FOR_WEAPON_UPGRADE = "WEAPON";
        String TEXT_FOR_ENGINE = "ENGINE";
        String TEXT_FOR_MINE = "MINE";
        String TEXT_FOR_ARMOUR = "ARMOUR";
        String TEXT_FOR_RADAR = "RADAR";

        String TEXT_FOR_TURRET = "TURRET";

        String TEXT_FOR_CLOCK = "FREEZING";
        String TEXT_FOR_MONEY = " $";
    }

    int getValueForType(int type){
        if (isMoney(type)){
            if (type == Types.MONEY_1) return  1;
            else if (type == Types.MONEY_2) return  2;
            else if (type == Types.MONEY_3) return  3;
            else if (type == Types.MONEY_5) return  5;
            else if (type == Types.MONEY_10) return  10;
            else if (type == Types.MONEY_15) return  15;
            else if (type == Types.MONEY_20) return  20;
            else if (type == Types.MONEY_25) return  25;
            else if (type == Types.MONEY_30) return  30;
            else if (type == Types.MONEY_40) return  40;
            else if (type == Types.MONEY_50) return  50;
            else return 1;
        }
        else return 1;
    }

    private EntityVisibilityController entityVisibilityController;
    private HaloController haloController;
    private int type;

    public Collectable(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int type, ArrayList <Integer> activatingValues){
        super(engine, physicWorld, pos, 0, 1, getWidthForEntity(type), getHeightForEntity(type), BodyForms.RECT, BodyType.DYNAMIC, -1);
        this.type = type;
        loadGraphicDefaultData(engine);
        entityVisibilityController = new EntityVisibilityController(this);
        setType(type);
        activatingController = ObjectActivatingController.createAppearingController(activatingValues, this, engine);
        if (activatingController.isActivated()){
            active = true;
        }
        else deactivate();
    }

    private void deactivate() {
        body.setActive(false);
    }


    private void setType(int type) {
        if (type == Types.RANDOM){
            int i = (int) engine.getEngine().random(Types.RANDOM);
            if (i == Types.RANDOM){
                i--;
            }
            type = i;
            Logger.debug("Random object will have type: " + type + "; This info must be transferred to other users");
        }
        this.type = type;
    }

    public static Collectable create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        ArrayList <Integer> activatingValues = new ArrayList<>();
        for (int i  = 2; i < values.length; i++) activatingValues.add(values[i]);
        Logger.debug("Collectable in point: " + pos.x + "x" + pos.y + " and type: " + values[2]);
        Collectable collectable = new Collectable (engine, physicWorld, pos, values[2], activatingValues);
        collectable.setId(entityData.getId());
        return collectable;
    }

    public String getDataString(){
        int posX = (int) pos.x;
        int posY = (int) pos.y;
        int value = type;
        Logger.correct("To implement");
        return null;
    }

    public void loadGraphicDefaultData(IEngine engine){

        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        ImageZoneSimpleData data = null;
        if (type == Types.LIFE) data =  ImageZones.LIFE_GRAPHIC;
        else if (type == Types.WEAPON) data = ImageZones.WEAPON_UPGRADE_GRAPHIC;
        else if (type == Types.ENGINE) data = ImageZones.GEAR_GRAPHIC;
        else if (type == Types.MINE) data = ImageZones.MINE_GRAPHIC;
        else if (type == Types.ARMOUR) data = ImageZones.ARMOUR_GRAPHIC;
        else if (type == Types.RADAR) data = ImageZones.RADAR_GRAPHIC;
        else if (type == Types.AI_TURRET) data = ImageZones.AI_TURRET_GRAPHIC;
        else if (type == Types.RANDOM) data = ImageZones.RANDOM_GRAPHIC;
        else if (type == Types.CLOCK) data = ImageZones.CLOCK;
        else if (isMoney(type)) data = ImageZones.MONEY;
        else {
            Logger.error("No graphic for type: " + type);
        }
        Logger.debug("Collectable created with graphic: " + data);
        loadImage(engine, path, width, height, data);
        haloController = new HaloController(engine.getEngine(), GraphicManager.getManager(engine.getEngine()).getImage(path), this);
    }
    protected void setBodyData() {
        if (GameRound.getDifficulty() == GlobalConstants.EASY_DIFFICULTY){
            body.setType(BodyType.DYNAMIC);
        }
        else body.setType(BodyType.STATIC);
        body.setUserData(BodyData.COLLECTABLE);
    }

    private static int getWidthForEntity(int type) {
        return COLLECTABLE_NORMAL_DIM;
    }

    private static int getHeightForEntity(int type) {
        return getWidthForEntity(type);
    }


    @Override
    public int collisionWithObject(Body tankBody, GameRound gameRound){
        Entity object = gameRound.getObjectByBody(tankBody);
        if (object instanceof Tank){
            Tank tank = (Tank) object;
            tank.gotCollectable(this, gameRound);
            DissolvingAndUpwardsMovingText text = new DissolvingAndUpwardsMovingText(gameRound.getEngine(), tank.getPos().clone(), getTextForGotObject(), AbstractText.WHITE, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY);
            gameRound.addEntityToEnd(text);
            body.setActive(false);
            gameRound.deleteObjectAfterActualLoop(this);
        }
        else if (object instanceof Bullet) attackBy(object, gameRound);
        return 0;
    }

    private String getTextForGotObject() {
        if (type == Types.LIFE) return GotTextes.TEXT_FOR_LIFE;
        else if (type == Types.WEAPON) return GotTextes.TEXT_FOR_WEAPON_UPGRADE;
        else if (type == Types.ENGINE) return GotTextes.TEXT_FOR_ENGINE;
        else if (type == Types.MINE) return GotTextes.TEXT_FOR_MINE;
        else if (type == Types.ARMOUR) return GotTextes.TEXT_FOR_ARMOUR;
        else if (type == Types.RADAR) return GotTextes.TEXT_FOR_RADAR;
        else if (type == Types.CLOCK) return GotTextes.TEXT_FOR_CLOCK;
        else if (type == Types.AI_TURRET) return GotTextes.TEXT_FOR_TURRET;
        else if (isMoney(type)){
            return '+'+getValueForType(type)+GotTextes.TEXT_FOR_MONEY;
        }


        else return "";
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        if (!active) activatingController.update(gameRound);
        else {
            super.update(gameRound, deltaTime);
            if (type == Types.LIFE) entityVisibilityController.update(gameRound, deltaTime);
            haloController.update();
        }
    }

    public void draw(PGraphics graphics, GameCamera gameCamera) {
        if (active) {
            super.draw(graphics, gameCamera);
            graphicElementInGame.setAlpha(entityVisibilityController.getAlphaUpTo255());
            haloController.draw(graphics, gameCamera);
        }
    }

    public int getType() {
        return type;
    }



    @Override
    public boolean attackBy(Entity attackingObject, GameRound gameRound){
        if (attackingObject instanceof Bullet){

            gameRound.addExplosion(attackingObject.pos.clone(), 0f, IAnimations.MINE_EXPLOSION);
            gameRound.addExplosion(pos.clone(), 0f, IAnimations.DUST_SPLASH);
            body.setActive(false);
            gameRound.deleteObjectAfterActualLoop(this);
            gameRound.deleteObjectAfterActualLoop(attackingObject);
            return KILLED;
        }
        return !KILLED;
    }

    @Override
    public void activate() {
        active = true;
        body.setActive(true);
        //activatingController = null;
    }
}
