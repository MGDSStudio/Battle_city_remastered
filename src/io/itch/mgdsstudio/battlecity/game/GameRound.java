package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.datatransfer.GlobalListenersManagerSingleton;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.dataloading.LevelLoadingMaster;
import io.itch.mgdsstudio.battlecity.game.dataloading.PlayerProgressControllerSingleton;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameRound {

    private ArrayList <Entity> gameObjects;
    private final ArrayList <Entity> objectToBeDeleted  = new ArrayList<>();
    private PGraphics graphics;
    private GameCamera gameCamera;
    private GamePartWithGameWorldAbstractController  gameController;
    private PhysicWorld physicWorld;
    private ArrayList <Controller> controllers;
    //private EnemiesSpawnController spawnController;
    private boolean singleplayer;
    private int playersConnected, playerNumberInMultiplayerMode;

    private static int difficulty = GlobalConstants.EASY_DIFFICULTY;
    private int startTime = -1;

    private boolean editor;

    private PlayerProgressControllerSingleton playerProgressControllerSingleton;

    public GameRound(GamePartWithGameWorldAbstractController  gameController, int number, int difficulty, int playersConnected, int playerNumberInMUltiplayerMOde) {

        if (gameController instanceof GameController ) editor = false;
        else editor = true;
        this.gameController = gameController;
        this.playerProgressControllerSingleton = PlayerProgressControllerSingleton.getInstance();
        this.difficulty = difficulty;
        this.controllers = new ArrayList<>();
        this.playersConnected = playersConnected;
        if (playersConnected<=1) singleplayer = true;
        this.playerNumberInMultiplayerMode = playerNumberInMUltiplayerMOde;
        loadGraphic();
        loadLevel(number, playersConnected, playerNumberInMUltiplayerMOde, playerProgressControllerSingleton);
        gameCamera = new GameCamera(getPlayer(), gameController.getHud());
        deleteBodiesWithoutEntities();
    }

    private void deleteBodiesWithoutEntities() {
        Body body = physicWorld.getController().world.getBodyList();
        Body prev = null;
        int bodiesHaveNoEntities = 0;
        if (body != null){
            while (body != null){
                prev = body;
                body = body.getNext();
            }
            if (prev != null){
                for (Body testBody = prev; testBody != null; testBody = testBody.m_prev){
                    Entity entity = getObjectByBody(testBody);
                    if (entity == null){
                        bodiesHaveNoEntities++;
                        testBody.setActive(false);
                        physicWorld.getController().world.destroyBody(testBody);
                    }
                }
            }
        }
        Logger.debug("This world has " + bodiesHaveNoEntities + " bodies that have no entities");
    }

    private void loadGraphic(){
        if (gameController == null) Logger.error("Game controller is null");
        if (gameController.getEngine() == null)  Logger.error("Engine is null");
        if (gameController.getEngine().getEngine() == null)  Logger.error("PApplet is null");
        Logger.debug("Resolution: " + InGameGraphicData.RESOLUTION_X + "x" + InGameGraphicData.RESOLUTION_Y);
        int graphicWidth = gameController.getGraphicWidth();
        int graphicHeight = gameController.getGraphicHeight();
        graphics = gameController.getEngine().getEngine().createGraphics(graphicWidth, graphicHeight, InGameGraphicData.renderer);
        graphics.beginDraw();
        graphics.rectMode(PConstants.CENTER);
        graphics.imageMode(PConstants.CENTER);
        graphics.endDraw();
    }

    private void loadLevel(int level, int playersConnected, int playerNumber, PlayerProgressControllerSingleton playerProgressControllerSingleton){
        physicWorld = new PhysicWorld(this);
        gameObjects = new ArrayList<>();
        LevelLoadingMaster levelLoadingMaster = new LevelLoadingMaster(this, level, gameController.getEngine(), playersConnected, playerNumber, singleplayer, playerProgressControllerSingleton, editor);
        levelLoadingMaster.loadLevelData(gameObjects, controllers, physicWorld);
        loadNotSaveableData(playersConnected, playerNumber);
    }

    private void loadNotSaveableData(int playersConnected, int playerNumber) {
        controllers.add(new CollisionsController(this));
        if (playersConnected>1){

        }
    }

    public void update(long deltaTime){
        gameCamera.update();
        if (startTime <0) startTime = gameController.getEngine().getEngine().millis();
        physicWorld.update(deltaTime);
        for (int i = (gameObjects.size())-1; i >= 0; i--) gameObjects.get(i).update(this, deltaTime);
        for (int i = (controllers.size()-1); i >= 0; i--) controllers.get(i).update(this, deltaTime);
        clearObjects();
    }

    private void clearObjects() {
        if (objectToBeDeleted.size()>0){
            for (int i = (objectToBeDeleted.size()-1); i >= 0; i--){
                for (int j = (gameObjects.size()-1); j >= 0; j--){
                    if (gameObjects.get(j).equals(objectToBeDeleted.get(i))){
                        gameObjects.get(j).dispose(this);
                        Logger.debug("Object " + objectToBeDeleted.get(i).getId() + " of type: " + objectToBeDeleted.get(i).getClass() + " was deleted at " + getEngine().getEngine().millis());
                        gameObjects.remove(j);
                        objectToBeDeleted.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public long getTimeFromLevelBegan(){
        return gameController.getEngine().getEngine().millis()-startTime;
    }

    public void draw(){
        graphics.beginDraw();
        graphics.background(50);
        for (Entity entity : gameObjects) {
            entity.draw(graphics, gameCamera);
        }
        graphics.endDraw();
    }

    public PGraphics getGraphics() {
        return graphics;
    }

    public IEngine getEngine(){
        return gameController.getEngine();
    }
/*
    public CollisionsController getCollisionsController() {
        return collisionsController;
    }*/

    public void removeEntity(Entity entityMustBeDeleted) {
        if (gameObjects.contains(entityMustBeDeleted)) {
            gameObjects.remove(entityMustBeDeleted);
            if (entityMustBeDeleted instanceof SolidObject) {
                SolidObject solidObject = (SolidObject) entityMustBeDeleted;
                solidObject.getBody().setActive(false);
                physicWorld.getController().world.destroyBody(solidObject.getBody());
            }
        }
        else {
            //Logger.errorLog("This object doesnot exists and can noy be deleted " + entityMustBeDeleted.getClass());
            //physicGameWorld.remo
        }
    }

    public Entity getObjectByBody(Body bodyToBeFounded) {
        for (Entity entity : gameObjects){
            if (entity instanceof SolidObject){
                SolidObject solidObject = (SolidObject) entity;
                if (solidObject.getBody().equals(bodyToBeFounded)){
                    return entity;
                }
            }
        }
        return null;
    }

    public static int getDifficulty(){
        return difficulty;
    }

	public void campKilled(boolean side) {
		System.out.println("Level ended");
	}

    public int getEntitiesCount(Class name){
        int count = 0;
        for (Entity entity : gameObjects){
            if (entity.getClass() == name) count++;
        }
        return count;
    }


    public ArrayList<Entity> getEntitiesForType(Class name, ArrayList <Entity> results) {
        if (results.size()>0) results.clear();
        for (Entity entity : gameObjects){
            if (entity.getClass() == name) results.add(entity);
        }
        return results;
    }

    public ArrayList<Entity> getEntities() {
        return gameObjects;
    }

    public void addEntityToStart(Entity entity){
        gameObjects.add(0,entity);
        //gameObjects.add(entity);
    }

    public void addEntityToEnd(Entity entity){
        gameObjects.add(gameObjects.size()-1,entity);
        //gameObjects.add(entity);
    }

    public void addEntityAndSort(Entity entity){
        gameObjects.add(entity);
        Collections.sort(gameObjects, Comparator.comparing(Entity::getGraphicLayer));
        //gameObjects.add(entity);
    }

    public PhysicWorld getPhysicWorld() {
        return physicWorld;
    }

    public PlayerTank getPlayer() {
        for (Entity entity : gameObjects){
            if (entity.getClass() == PlayerTank.class){
                //System.out.println("This tank is player tank: " + ((PlayerTank) entity).getRole());
                if (((PlayerTank) entity).getRole() == playerNumberInMultiplayerMode || isSinglePlayer()){
                    return ((PlayerTank) entity);
                }
            }
        }
        return null;
    }

    public Entity getObjectById(int id){
        for (Entity entity : gameObjects){
            if (entity.getId() == id) return entity;
        }
        return null;
    }

/*
    public Controller getControllerById(int id){
        for (Controller entity : controllers){
            if (entity.getId() == id) return entity;
        }
        return null;
    }*/

    public void deleteObjectAfterActualLoop(Entity mine) {
        objectToBeDeleted.add(mine);
    }

    public void addNewBeginContact(Contact contact) {
        for (Controller controller : controllers){
            controller.appendDataTypeA(contact);
        }
    }

    public boolean isSinglePlayer() {
        return singleplayer;
        /*if (playersConnected <= 1) return true;
        else return false;*/
    }

    public boolean isActualPlayerMaster() {
        if (playersConnected > 1){
            if (playerNumberInMultiplayerMode == 0) return true;
            else return false;
        }
        else return true;
    }

    public void appendButtonsListenerToControllers(GlobalListener gameProcessController) {
        for (Controller controller : controllers){
            controller.appendButtonsListener(gameProcessController);
        }
    }

    public ArrayList<Controller> getControllers() {
        return controllers;
    }


    public void addExplosion(Coordinate pos, float angle, int type) {
        Animation animation = new Animation(getEngine(), pos, (int) angle, -1, -1, type);
        addEntityToEnd(animation);
    }

    /*public GameController getGameController() {
        return gameController;
    }*/

    public void setLevelEnd(int levelEndCode) {
        Logger.debug("Level ends with code: " + levelEndCode);
        MenuDataStruct dataStruct = new MenuDataStruct();
        dataStruct.setLevelEndCode(levelEndCode);

        gameController.setGameEnded(dataStruct);
    }

    public PlayerProgressControllerSingleton getPlayerProgressController() {
        return playerProgressControllerSingleton;
    }

    public void addEntityOnGround(SpriteInGame rest) {
        int lastEntityOnLayer = 0;
        if (gameObjects.size()>0){
            if (gameObjects.get(0).getGraphicLayer() != GraphicObject.GraphicLayers.GROUND_LAYER){
                gameObjects.add(0, rest);
                Logger.debug("Sprite added as first");

            }
            else {
                for (int i = 1; i < gameObjects.size(); i++) {
                    if (gameObjects.get(i).getGraphicLayer() != GraphicObject.GraphicLayers.GROUND_LAYER) {
                        gameObjects.add(i, rest);
                        Logger.debug("Sprite added as " + i + "th");
                        return;
                    }
                }
            }
        }
        else gameObjects.add(0, rest);

    }


}
