package io.itch.mgdsstudio.battlecity.game;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.connectingcontrol.ConnectingController;

import io.itch.mgdsstudio.battlecity.game.control.GameProcessController;
//import io.itch.mgdsstudio.battlecity.game.net.MainPlayerServer;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.LevelEndConditionController;
import io.itch.mgdsstudio.battlecity.game.hud.InGameHud;
import io.itch.mgdsstudio.battlecity.game.net.Net;
//import io.itch.mgdsstudio.battlecity.game.net.PlayerClient;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;

public class GameController extends GamePartWithGameWorldAbstractController {
    //private GameProcessController gameProcessController;
    private ConnectingController connectingController;

    public GameController(IEngine engine, MainController mainController, int level, int dif, int playersConnected, int playerNumber, int playerNumberInMultiplayerMode) {
        super(engine, mainController, dif, level, playerNumberInMultiplayerMode, playersConnected);
        drawingGraphicPlaces = new DrawingGraphicPlaces(InGameGraphicData.graphicCenterX, InGameGraphicData.graphicCenterY, InGameGraphicData.fullGraphicWidth, InGameGraphicData.fullGraphicHeight);
        //hud = new Hud(this, engine, playerNumberInMultiplayerMode, singleplayer);


        hud.appendGameRoundData(gameRound);
        if (playersConnected >1) {
            singleplayer = false;
            Logger.debug("Multiplayer enabled ");
        }
        gameProcessController = new GameProcessController(gameRound, hud, singleplayer, playerNumber, playerNumberInMultiplayerMode);
        gameRound.appendButtonsListenerToControllers(gameProcessController);
        hud.appendButtonsListener(gameProcessController);
        initDeltaTime();
        initMultiplayerSpecific(playerNumberInMultiplayerMode, playersConnected);
        System.out.println("This game played: " + playersConnected + " players");
    }

    protected void initHud(int playerNumberInMultiplayerMode){
        hud = new InGameHud(this, engine, playerNumberInMultiplayerMode, singleplayer);
    }

    private void initMultiplayerSpecific(int playerNumberInMultiplayerMode, int playersConnected) {
        if (singleplayer){

        }
        else {
            netController = Net.CreateNetController(playerNumberInMultiplayerMode, engine);
            netController.appendListener(gameProcessController);
            Logger.debug("Net controller got listener");
            gameRound.appendButtonsListenerToControllers(netController);
            hud.appendButtonsListener(netController);
        }
        connectingController = ConnectingController.createController(playerNumberInMultiplayerMode, playersConnected, netController, engine, gameRound.getPlayer().getId(), !singleplayer);
        connectingController.appendListener(netController);
        if (playersConnected>=2){
            netController.appendListener(connectingController);
        }
    }

    public void update(){
        if (connectingController.isGameCanBeStarted()) {
            if (!startDataInit) initStartData();
            deltaTime = engine.getEngine().millis() - lastFrameTime;
            gameRound.update(deltaTime);
            gameProcessController.update(deltaTime);
            gameRound.draw();
            lastFrameTime = engine.getEngine().millis();
            engine.getEngine().image(gameRound.getGraphics(), drawingGraphicPlaces.centerX, drawingGraphicPlaces.centerY,
                    drawingGraphicPlaces.getWidth(), drawingGraphicPlaces.getHeight(),
                    hud.getGraphicLeftPixel(), hud.getGraphicUpperPixel(), hud.getGraphicRightPixel(), hud.getGraphicLowerPixel());
            hud.update(gameRound);

        }
        else {
            updateConnecting();
        }
        updateNet();
    }

    @Override
    protected void updateConnecting() {
        connectingController.update();
    }

    private void updateNet() {
        if (!singleplayer){
            netController.update();
        }
    }

    /*private void initStartData() {
        gameProcessController.setLevelStartTimeForUsers(gameRound.getPlayer().getId(), engine.getEngine().millis());
        startDataInit = true;
    }*/



    public void draw(){
        if (connectingController.isGameCanBeStarted()) {
            hud.draw();
            //gameRound.draw();
        }
    }


    public boolean isCampaign(){
        Logger.correct("I must add also campaign missions implementation");
        return false;
    }

    @Override
    public void backPressed(){
        MenuDataStruct menuDataStruct = new MenuDataStruct();
        menuDataStruct.setNextMenu(MenuType.SINGLE_MISSIONS);
        menuDataStruct.setLevelEndCode(LevelEndConditionController.LevelEndType.PLAYER_RETURNED_BACK);
        mainController.backToMenu(menuDataStruct);
    }


    public static int getDifficulty(){
        return difficulty;
    }

    @Override
    public void backToMenu(MenuDataStruct dataStruct) {
        mainController.backToMenu(dataStruct);
    }

    @Override
    public int getGraphicWidth() {
        return (int) InGameGraphicData.fullGraphicWidth;
    }

    @Override
    public int getGraphicHeight() {
        return (int) InGameGraphicData.fullGraphicHeight;
    }

    @Override
    public Camera createCamera(GameRound gameRound) {
        GameCamera gameCamera = new GameCamera(engine, gameRound.getPlayer(), getHud());
        return gameCamera;
    }
}
