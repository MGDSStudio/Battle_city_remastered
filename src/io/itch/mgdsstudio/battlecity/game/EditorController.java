package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.control.GameProcessController;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.hud.InEditorHud;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class EditorController extends GamePartWithGameWorldAbstractController {

    //private ConnectingController connectingController;



    public EditorController(IEngine engine, MainController mainController, int level, int dif, int playersConnected, int playerNumber, int playerNumberInMultiplayerMode) {
        super(engine, mainController, dif, level, playerNumberInMultiplayerMode,playersConnected);
        drawingGraphicPlaces = new DrawingGraphicPlaces(InEditorGraphicData.graphicCenterX, InEditorGraphicData.graphicCenterY, InEditorGraphicData.fullGraphicWidth, InEditorGraphicData.fullGraphicHeight);
        singleplayer = true;
        hud.appendGameRoundData(gameRound);
        gameProcessController = new GameProcessController(gameRound, hud, singleplayer, playerNumber, playerNumberInMultiplayerMode);
        gameRound.appendButtonsListenerToControllers(gameProcessController);
        hud.appendButtonsListener(gameProcessController);

        initDeltaTime();
        Logger.debug("Editor launched");
    }

    protected void initHud(int playerNumberInMultiplayerMode){
        hud = new InEditorHud(this, engine, playerNumberInMultiplayerMode, singleplayer);
    }


    public void update(){
            if (!startDataInit) initStartData();
            deltaTime = engine.getEngine().millis() - lastFrameTime;
            gameRound.update(deltaTime);
            gameProcessController.update(deltaTime);
            gameRound.draw();
            lastFrameTime = engine.getEngine().millis();
            engine.getEngine().image(gameRound.getGraphics(), drawingGraphicPlaces.centerX, drawingGraphicPlaces.centerY,
                drawingGraphicPlaces.getWidth(), drawingGraphicPlaces.getHeight(),
                hud.getGraphicLeftPixel(), hud.getGraphicUpperPixel(), hud.getGraphicRightPixel(), hud.getGraphicLowerPixel());
    }

    @Override
    public void draw(){
        hud.draw();
    }

    public Hud getHud() {
        return hud;
    }




    @Override
    public void backToMenu(MenuDataStruct dataStruct) {
        mainController.backToMenu(dataStruct);
    }

    @Override
    public int getGraphicWidth() {
        return (int) InEditorGraphicData.fullGraphicWidth;
    }

    @Override
    public int getGraphicHeight() {
        return (int) InEditorGraphicData.fullGraphicHeight;
    }

    @Override
    public GameCamera createCamera(GameRound gameRound) {
        GameCamera gameCamera = new GameCamera(new Coordinate(0,0));
        return gameCamera;
    }
}
