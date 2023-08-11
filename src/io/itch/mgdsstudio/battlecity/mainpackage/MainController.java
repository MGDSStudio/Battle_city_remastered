package io.itch.mgdsstudio.battlecity.mainpackage;

import io.itch.mgdsstudio.battlecity.game.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.LevelEndConditionController;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;
import processing.core.PConstants;

public class MainController implements GameStatements{
    private IEngine engine;
    private static int globalStatement;
    private GamePart gamePart;
    private boolean mustBeReloaded = true;
    private int levelOrMenuNumber = 1;
    private Integer additionalData = GlobalConstants.EASY_DIFFICULTY;
    private int playersConnected = 2;
    private int playerNumberInMultiplayerMode;

    public MainController(IEngine engine, int playerNumberInMultiplayerMode) {
        this.engine = engine;
        this.playerNumberInMultiplayerMode = playerNumberInMultiplayerMode;
        engine.getProcessing().imageMode(PConstants.CENTER);
        engine.getProcessing().rectMode(PApplet.CENTER);
        globalStatement = MENU;
        levelOrMenuNumber = 1;
        init();
    }

    private void init(){
        globalStatement = MENU;

        if (playerNumberInMultiplayerMode == GlobalConstants.PLAYER_IN_SINGLEPLAYER_MODE) {
            playersConnected = 1;
        }
        if (globalStatement == GAME || globalStatement == EDITOR){
            if (globalStatement == GAME) gamePart = new GameController(engine, this, levelOrMenuNumber, additionalData, playersConnected, 0, playerNumberInMultiplayerMode);
            else gamePart = new EditorController(engine, this, levelOrMenuNumber, additionalData, playersConnected, 0, playerNumberInMultiplayerMode);
        }
        else {
            MenuDataStruct menuDataStruct = new MenuDataStruct();
            menuDataStruct.setNextLevel(1);
            menuDataStruct.setNextMenu(MenuType.MAIN);
            gamePart = new MenuController(engine, this, menuDataStruct);
        }
        mustBeReloaded = false;
    }


    public void changeStatement(int newStatement, int levelOrMenuNumber, Integer additionalData){
        globalStatement = newStatement;
        this.levelOrMenuNumber = levelOrMenuNumber;
        this.additionalData = additionalData;
        mustBeReloaded = true;
    }

    public void update(){
        if (mustBeReloaded) init();
        else {
            if (globalStatement == GAME || globalStatement == EDITOR) {
                gamePart.update();
            }
            else gamePart.update();
        }
    }

    public void draw(){
        if (!mustBeReloaded) {
            if (globalStatement == GAME || globalStatement == EDITOR) {
                gamePart.draw();
            }
            else gamePart.draw();
        }
    }

    public void beginContact(Contact contact) {
        if (globalStatement == GAME) gamePart.beginContact(contact);
    }


    public void endContact(Contact contact) {

    }


    public void preSolve(Contact contact, Manifold manifold) {

    }


    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }


    public void startGame(int nextLevel) {
        globalStatement = GAME;
        this.levelOrMenuNumber = nextLevel;
        gamePart = new GameController(engine, this, levelOrMenuNumber, additionalData, playersConnected, 0, playerNumberInMultiplayerMode);
        Logger.debug("Start level with " + levelOrMenuNumber + " for players: " + playersConnected + ". Number in multiplayer: " + playerNumberInMultiplayerMode);
        Logger.debug("It is better if I save this as a command in a queue");
    }

    public void backToMenu(MenuDataStruct dataStruct, boolean fromEditor) {
        globalStatement = MENU;
        if (fromEditor){
            Logger.debug("Next menu after editor must be: " + dataStruct.getNextMenuType().name());
        }
        else {
            int levelEndCode = dataStruct.getLevelEndCode();
            if (levelEndCode == LevelEndConditionController.LevelEndType.PLAYER_CAMP_KILLED)
                dataStruct.setNextMenu(MenuType.SINGLE_MISSION_LOOSED);
            else if (levelEndCode == LevelEndConditionController.LevelEndType.PLAYER_RETURNED_BACK) {
            } else dataStruct.setNextMenu(MenuType.SINGLE_MISSION_COMPLETED);
        }
        gamePart = new MenuController(engine, this, dataStruct);
        Logger.debug("It is better if I save this as a command in a queue");
        Logger.correct("I need to implement campaign in this function to select the rightest menu!");
    }

    public void startEditor(int nextLevel) {
        globalStatement = EDITOR;
        this.levelOrMenuNumber = nextLevel;
        gamePart = new EditorController(engine, this, levelOrMenuNumber, additionalData, playersConnected, 0, playerNumberInMultiplayerMode);
        playersConnected = 1;
        //playerNumberInMultiplayerMode
        Logger.debug("Start editor from level " + levelOrMenuNumber + " for players: " + playersConnected + ". Number in multiplayer: " + playerNumberInMultiplayerMode);
        Logger.debug("It is better if I save this as a command in a queue");
    }

    public static boolean isEditor(){
        if (globalStatement == EDITOR) return true;
        else if (globalStatement == GAME) return false;
        else {
            Logger.error("No data about global statement");
            return false;
        }
    }

    public void backPressed() {
        if (gamePart != null){
            gamePart.backPressed();
        }
    }
}
