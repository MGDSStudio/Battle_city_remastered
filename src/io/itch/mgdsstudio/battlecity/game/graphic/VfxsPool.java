package io.itch.mgdsstudio.battlecity.game.graphic;


import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.AnimationInGame;
import io.itch.mgdsstudio.engine.graphic.GraphicManagerSingleton;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

//Singleton
public class VfxsPool implements IAnimations {

    private interface ImageZoneData{
        ImageZoneSimpleData BULLET_EXPLOSION = new ImageZoneSimpleData(352-3*32, 1024-32, 352+3*32, 1024);
        ImageZoneSimpleData TANK_DESTRUCTION = new ImageZoneSimpleData(0, 1024-4*64, 4*64, 1024);
        ImageZoneSimpleData DUST_SPLASH = new ImageZoneSimpleData(463, 1024-10*64, 463+64, 1024);
    }
    public final static int NORMAL_ANIMATION_FREQUENCY = 30; //Sprites per second
    public final static float NORMAL_GRAPHIC_SCALE = 1.1f;



    //private IEngine engine;
    private VfxsPool vfxsPool;

    public static AnimationInGame createAnimation(int type, IEngine engine){
        return createAnimation(type, engine, getWidthForType(type), getHeightForType(type));

    }

    private static int getWidthForType(int type) {
        return getSizeForType(type);
    }

    private static int getHeightForType(int type) {
        return getSizeForType(type);
    }

    private static int getSizeForType(int type) {
        if (type == BULLET_EXPLOSION || type == MINE_EXPLOSION){
            return 35;
        }
        else if (type == TANK_DESTRUCTION){
            return 75;
        }
        else if (type == DUST_SPLASH){
            return 55;
        }
        else if (type == WATER){
            return Entity.ENTITY_NORMAL_DIM;
        }
        else return Entity.ENTITY_NORMAL_DIM;

    }

    public static AnimationInGame createAnimation(int type, IEngine engine, int width, int height){
        AnimationInGame animationInGame;
        GraphicManagerSingleton graphicManagerSingleton = GraphicManagerSingleton.getManager(engine.getProcessing());
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final Image image = graphicManagerSingleton.getImage(path);
        int graphicStep = 32;
        ImageZoneSimpleData imageZoneSimpleData;
        int alongX = 6;
        int alongY = 1;
        int first = 0;  //number in array
        int last = 5;   //number in array
        int imagesPerSecond = NORMAL_ANIMATION_FREQUENCY;
        int direction = 1;
        int actual = 0;
        int repeateability = AnimationInGame.PLAY_ONCE_AND_SWITCH_OFF;
        if (type == BULLET_EXPLOSION || type == MINE_EXPLOSION){
            imageZoneSimpleData = ImageZoneData.BULLET_EXPLOSION;
            alongX = 6;
            alongY = 1;
            first = 0;
            last = 5;
            imagesPerSecond = NORMAL_ANIMATION_FREQUENCY;
            direction = 1;
            actual = 0;
            repeateability = AnimationInGame.PLAY_ONCE_AND_SWITCH_OFF;
            animationInGame = new AnimationInGame(image, width, height, imageZoneSimpleData, alongX, alongY, first, last, imagesPerSecond, direction, actual, repeateability, -1);
        }
        else if (type == TANK_DESTRUCTION){
            //width = 75;
            //height = 75;
            graphicStep = 64;
            imageZoneSimpleData = ImageZoneData.TANK_DESTRUCTION;
            alongX = 4;
            alongY = 4;
            first = 0;
            last = 15;
            imagesPerSecond = NORMAL_ANIMATION_FREQUENCY;
            direction = 1;
            actual = 0;
            repeateability = AnimationInGame.PLAY_ONCE_AND_SWITCH_OFF;
            animationInGame = new AnimationInGame(image, width, height, imageZoneSimpleData, alongX, alongY, first, last, imagesPerSecond, direction, actual, repeateability, -1);
        }
        else if (type == WATER){
           // width = 75;
           // height = 75;
            graphicStep = 64;
            imageZoneSimpleData = new ImageZoneSimpleData(832, 0, 832+graphicStep, 3*graphicStep);
            alongX = 1;
            alongY = 3;
            first = 0;
            last = 2;
            imagesPerSecond = NORMAL_ANIMATION_FREQUENCY;
            direction = 1;
            actual = 0;
            repeateability = AnimationInGame.PLAY_ALWAYS;
            animationInGame = new AnimationInGame(image, width, height, imageZoneSimpleData, alongX, alongY, first, last, imagesPerSecond, direction, actual, repeateability, -1);
        }
        else if (type == DUST_SPLASH){
            //width = 55;
            //height = 55;
            //graphicStep = 64;
            imageZoneSimpleData = ImageZoneData.DUST_SPLASH;
            alongX = 1;
            alongY = 10;
            first = 0;
            last = 9;
            imagesPerSecond = NORMAL_ANIMATION_FREQUENCY;
            direction = 1;
            actual = 0;
            repeateability = AnimationInGame.PLAY_ONCE_AND_SWITCH_OFF;
            animationInGame = new AnimationInGame(image, width, height, imageZoneSimpleData, alongX, alongY, first, last, imagesPerSecond, direction, actual, repeateability, -1);
        }
        else {
            Logger.error("No data about animation type: " + type);
            animationInGame = null;
        }
        return animationInGame;
    }



}
