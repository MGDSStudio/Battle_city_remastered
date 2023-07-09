package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferences;
import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferencesSingleton;
import io.itch.mgdsstudio.battlecity.game.camera.EditorCamera;
import io.itch.mgdsstudio.battlecity.game.control.GameProcessController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Grid;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.hud.InEditorHud;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class EditorController extends GamePartWithGameWorldAbstractController implements EditorActionsListener {

    //private ConnectingController connectingController;
    private ArrayList <EditorAction> actions;
    private WorldZoneScrollingController worldZoneScrollingController;
    private Cross cross;


    public EditorController(IEngine engine, MainController mainController, int level, int dif, int playersConnected, int playerNumber, int playerNumberInMultiplayerMode) {
        super(engine, mainController, dif, level, playerNumberInMultiplayerMode,playersConnected);
        EditorPreferencesSingleton editorPreferencesSingleton = EditorPreferencesSingleton.getInstance(engine);

        Logger.editor("Grid step: " + editorPreferencesSingleton.getIntegerValue(EditorPreferences.GRID_STEP.name()));
        drawingGraphicPlaces = new DrawingGraphicPlaces(InEditorGraphicData.graphicCenterX, InEditorGraphicData.graphicCenterY, InEditorGraphicData.fullGraphicWidth, InEditorGraphicData.fullGraphicHeight);
        singleplayer = true;
        hud.appendGameRoundData(gameRound);
        gameProcessController = new GameProcessController(gameRound, hud, singleplayer, playerNumber, playerNumberInMultiplayerMode);
        gameRound.appendButtonsListenerToControllers(gameProcessController);
        hud.appendButtonsListener(gameProcessController);

        initDeltaTime();
        Logger.debug("Editor launched");

        Rectangle graphicZone = new Rectangle();
        graphicZone.x = hud.getGraphicLeftPixel();
        graphicZone.y = hud.getGraphicUpperPixel();
        graphicZone.width = hud.getGraphicRightPixel()-graphicZone.x;
        graphicZone.height = hud.getGraphicLowerPixel()-graphicZone.y;
        InEditorHud inEditorHud = (InEditorHud) hud;

        worldZoneScrollingController = new WorldZoneScrollingController(engine, inEditorHud.getInEditorGameWorldFrame());
        actions = new ArrayList<>();
        EditorListenersManagerSingleton.getInstance().addAsListener(this);

        createOnMapZoneGraphic();
    }

    private void createOnMapZoneGraphic() {
        cross = new Cross(this);
        Grid grid = new Grid(this);
        gameRound.addEntityToEnd(cross);
        gameRound.addEntityToEnd(grid);
    }

    protected void initHud(int playerNumberInMultiplayerMode){
        hud = new InEditorHud(this, engine, playerNumberInMultiplayerMode, singleplayer);
    }

    public void update(){
            if (!startDataInit) initStartData();
            worldZoneScrollingController.update( engine.getEngine().millis());
            updateActions();
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

    private void updateActions() {
        if (actions.size()>0){
            for (int i = (actions.size()-1); i >= 0; i--){
                if (actions.get(i).getPrefix().equals(EditorCommandPrefix.WORLD_SCROLLING)){
                    EditorCamera editorCamera = (EditorCamera) gameRound.getCamera();
                    editorCamera.appendCommand(actions.get(i));
                    actions.remove(i);
                }
                else actions.remove(i);
            }
        }
        if (actions.size()>20){
            Logger.error("Too many actions " + actions.size());
        }
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
    public EditorCamera createCamera(GameRound gameRound) {
        EditorCamera gameCamera = new EditorCamera(engine, new Coordinate(0,0));
        return gameCamera;
    }

    @Override
    public void appendCommand(EditorAction action) {
        actions.add(action);
    }
}
