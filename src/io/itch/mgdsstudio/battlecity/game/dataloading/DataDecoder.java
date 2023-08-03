package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.EnemiesSpawnController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.LevelEndConditionController;
import io.itch.mgdsstudio.engine.graphic.AnimationInGame;

public class DataDecoder {
    public static final char END_ROW_SYMBOL = '!';
    static final public char MAIN_DATA_START_CHAR = ':';
    static final public char GRAPHIC_NAME_START_CHAR = '#';
    static final public char GRAPHIC_DATA_END_CHAR = ';';
    static final public char DIVIDER_BETWEEN_VALUES = ',';
    static final public char DIVIDER_BETWEEN_GRAPHIC_DATA = 'x';
    static final protected char VERTICES_START_CHAR = '%';
    static final protected char DIVIDER_BETWEEN_VERTICES = 'v';
    public final static String DIVIDER_NAME_ID = " ";
    private DataStringDecoder levelDataStringDecoder;

    private GameRound gameRound;
    private PhysicWorld physicWorld;
    private boolean playerIsServerOrSingleplayer;
    private PlayerProgressControllerSingleton playerProgressControllerSingleton;

    public DataDecoder(PhysicWorld physicWorld, GameRound gameRound, int playerNumber, boolean singleplayer, PlayerProgressControllerSingleton playerProgressControllerSingleton) {
        this.gameRound = gameRound;
        this.playerProgressControllerSingleton = playerProgressControllerSingleton;
        this.physicWorld = physicWorld;
        if (playerNumber==0 || singleplayer) playerIsServerOrSingleplayer = true;
    }
    public Object getObjectFromString(String dataString) {
        Entity entity = null;
        Controller controller = null;
        if (!isDataComment(dataString)) {
            String type = getObjectNameFromString(dataString);
            if (type != null) {
                if (levelDataStringDecoder == null) levelDataStringDecoder = new DataStringDecoder(dataString);
                else levelDataStringDecoder.setNewStringData(dataString);
                EntityData entityData = levelDataStringDecoder.getDecodedData(type);
                if (entityData != null){
                    if (type.contains("Controller") || type.contains("controller")){
                        controller = createControllerByData(entityData, type, playerIsServerOrSingleplayer);
                    }
                    else entity = createEntityByData(entityData, type);

                }
            } else {
                //Logger.errorLog("this entity from string " + dataString + " is not knew");
            }
        }
        if (entity != null) return  entity;
        else if (controller != null) return  controller;
        else {
            //This is a comment
            return null;
        }

    }

    private Controller createControllerByData(EntityData entityData, String type, boolean playerIsServerOrSingleplayer) {
        Controller c = null;
        if (type == getClassNameForController(EnemiesSpawnController.class) || type.contains(getClassNameForController(EnemiesSpawnController.class))){
            c = EnemiesSpawnController.create(gameRound, entityData, playerIsServerOrSingleplayer);
            Logger.debug("Enemies spawn controller was created as " + c.getClass());
        }
        else if (type == getClassNameForController(LevelEndConditionController.class) || type.contains(getClassNameForController(LevelEndConditionController.class))){
            c = LevelEndConditionController.create(gameRound, entityData);
            Logger.debug("Enemies spawn controller was created as " + c.getClass());
        }
        else if (type == getClassNameForEntity(EnemyTank.class) || type.contains(getClassNameForEntity(EnemyTank.class))){

        }
        else Logger.error(" this class is not knew: "  );
        return c;
    }

    private Entity createEntityByData(EntityData entityData, String type) {
        Entity entity = null;
        if (type == getClassNameForEntity(PlayerTank.class) || type.contains(getClassNameForEntity(PlayerTank.class))){
            PlayerTank playerTank = PlayerTank.create(gameRound.getEngine(), physicWorld, entityData);
            entity = playerTank;
            Logger.debug("Player tank created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(EnemyTank.class) || type.contains(getClassNameForEntity(EnemyTank.class))){
            EnemyTank enemyTank = EnemyTank.create(gameRound.getEngine(), physicWorld, entityData);
            entity = enemyTank;
            Logger.debug("Enemy tank created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(WorldBoard.class) || type.contains(getClassNameForEntity(WorldBoard.class))){
            WorldBoard wallBoard = WorldBoard.create(gameRound.getEngine(), physicWorld, entityData);
            entity = wallBoard;
            Logger.debug("World board created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(BrickWall.class) || type.contains(getClassNameForEntity(BrickWall.class))){
            BrickWall brickWall = BrickWall.create(gameRound.getEngine(), physicWorld, entityData);
            entity = brickWall;
            Logger.debug("Brick wall created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(ArmoredWall.class) || type.contains(getClassNameForEntity(ArmoredWall.class))){
            ArmoredWall armoredWall = ArmoredWall.create(gameRound.getEngine(), physicWorld, entityData);
            entity = armoredWall;
            Logger.debug("Armored wall created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(Water.class) || type.contains(getClassNameForEntity(Water.class))){
            Water armoredWall = Water.create(gameRound.getEngine(), physicWorld, entityData);
            entity = armoredWall;
            Logger.debug("Water created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(Forest.class) || type.contains(getClassNameForEntity(Forest.class))){
            Forest forest = Forest.create(gameRound.getEngine(), physicWorld, entityData);
            entity = forest;
            Logger.debug("Forest created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(Ice.class) || type.contains(getClassNameForEntity(Ice.class))){
            Ice forest = Ice.create(gameRound.getEngine(), physicWorld, entityData);
            entity = forest;
            Logger.debug("Ice created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(Camp.class) || type.contains(getClassNameForEntity(Camp.class))){
            entity = Camp.create(gameRound.getEngine(), physicWorld, entityData);
            Logger.debug("Camp created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(Collectable.class) || type.contains(getClassNameForEntity(Collectable.class))){
            entity = Collectable.create(gameRound.getEngine(), physicWorld, entityData);
            Logger.debug("Collectable created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(Mine.class) || type.contains(getClassNameForEntity(Mine.class))){
            entity = Mine.create(gameRound.getEngine(), physicWorld, entityData);
            Logger.debug("Mine created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(SpriteInGame.class) || type.contains(getClassNameForEntity(SpriteInGame.class))){
            entity = SpriteInGame.create(gameRound.getEngine(), physicWorld, entityData);
            Logger.debug("Sprite in game created with id: " + entity.getId());
        }
        else if (type == getClassNameForEntity(SpriteAnimationInGame.class) || type.contains(getClassNameForEntity(SpriteAnimationInGame.class))){
            entity = SpriteAnimationInGame.create(gameRound.getEngine(), physicWorld, entityData);
            Logger.debug("Sprite animation in game created with id: " + entity.getId());
        }
        else Logger.error(" This class is not knew: " + type.toString());

        return entity;
    }

    private String getClassNameForEntity(Class className){
        if (className == PlayerTank.class) return className.toString().substring(PlayerTank.class.toString().lastIndexOf('.')+1);
        else if (className == BrickWall.class) return className.toString().substring(BrickWall.class.toString().lastIndexOf('.')+1);
        else if (className == ArmoredWall.class) return className.toString().substring(ArmoredWall.class.toString().lastIndexOf('.')+1);
        else if (className == Water.class) return className.toString().substring(Water.class.toString().lastIndexOf('.')+1);
        else if (className == Collectable.class) return className.toString().substring(Collectable.class.toString().lastIndexOf('.')+1);
        else if (className == Camp.class) return className.toString().substring(Camp.class.toString().lastIndexOf('.')+1);
        else if (className == EnemyTank.class) return className.toString().substring(EnemyTank.class.toString().lastIndexOf('.')+1);
        else if (className == Forest.class) return className.toString().substring(Forest.class.toString().lastIndexOf('.')+1);
        else if (className == Ice.class) return className.toString().substring(Ice.class.toString().lastIndexOf('.')+1);
        else if (className == Camp.class) return className.toString().substring(Camp.class.toString().lastIndexOf('.')+1);
        else if (className == Collectable.class) return className.toString().substring(Collectable.class.toString().lastIndexOf('.')+1);


        else return className.toString().substring(WorldBoard.class.toString().lastIndexOf('.')+1);

    }

    private String getClassNameForController(Class className){
        if (className == EnemiesSpawnController.class) return className.toString().substring(EnemiesSpawnController.class.toString().lastIndexOf('.')+1);
        else if (className == LevelEndConditionController.class) return className.toString().substring(LevelEndConditionController.class.toString().lastIndexOf('.')+1);
        else {
            Logger.error("Don't know about this class " + className);
            return null;
        }
    }


    private boolean isDataComment(String dataString) {
        if (dataString.startsWith("/") || dataString.charAt(1) == '/'){
            return true;
        }
        else {
            return false;
        }
    }

    private String getObjectNameFromString(String dataString) {
        if (dataString.contains(DIVIDER_NAME_ID)){
            return dataString.substring(0, dataString.indexOf(DIVIDER_NAME_ID));
        }
        return null;
    }


}
