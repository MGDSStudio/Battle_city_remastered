package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.editor.Cursor;
import io.itch.mgdsstudio.battlecity.editor.changessaving.ChangesController;
import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferences;
import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferencesSingleton;
import io.itch.mgdsstudio.battlecity.editor.data.UnsavedDataList;
import io.itch.mgdsstudio.battlecity.editor.menus.AbstractEditorMenu;
import io.itch.mgdsstudio.battlecity.editor.menus.MenuType;
import io.itch.mgdsstudio.battlecity.game.camera.EditorCamera;
import io.itch.mgdsstudio.battlecity.game.control.GameProcessController;
import io.itch.mgdsstudio.battlecity.game.dataloading.ExternalDataController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Grid;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.hud.InEditorHud;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class EditorController extends GamePartWithGameWorldAbstractController implements EditorActionsListener {
    private LastActionController lastActionController;

    private ArrayList <EditorAction> actions;
    private WorldZoneScrollingController worldZoneScrollingController;
    private Cursor cursor;
    private Grid grid;
    private AbstractEditorMenu menu;
    private MenuType actualMenuType, nextMenuType;

    private UnsavedDataList unsavedDataList;;
    private ChangesController changesController;
    private ArrayList <ISelectable> selectedElements;
    private UnsavedDataLabel unsavedDataLabel;
    private boolean firstUpdated;

    public EditorController(IEngine engine, MainController mainController, int level, int dif, int playersConnected, int playerNumber, int playerNumberInMultiplayerMode) {
        super(engine, mainController, dif, level, playerNumberInMultiplayerMode,playersConnected, true);
        selectedElements = new ArrayList<>();
        changesController = new ChangesController(this);
        EditorPreferencesSingleton editorPreferencesSingleton = EditorPreferencesSingleton.getInstance(engine);
        Logger.editor("Grid step: " + editorPreferencesSingleton.getIntegerValue(EditorPreferences.GRID_STEP.name()));
        drawingGraphicPlaces = new DrawingGraphicPlaces(InEditorGraphicData.graphicCenterX, InEditorGraphicData.graphicCenterY, InEditorGraphicData.fullGraphicWidth, InEditorGraphicData.fullGraphicHeight);
        singleplayer = true;
        hud.appendGameRoundData(gameRound);
        gameProcessController = new GameProcessController(gameRound, hud, singleplayer, playerNumber, playerNumberInMultiplayerMode);
        gameRound.appendButtonsListenerToControllers(gameProcessController);
        hud.appendButtonsListener(gameProcessController);
        initDeltaTime();
        initGraphicZone();
        InEditorHud inEditorHud = (InEditorHud) hud;
        worldZoneScrollingController = new WorldZoneScrollingController(engine, inEditorHud.getInEditorGameWorldFrame());
        actions = new ArrayList<>();
        EditorListenersManagerSingleton.getInstance().addAsListener(this);

        createOnMapZoneGraphic();
        nextMenuType = MenuType.MAIN;
        createMenu();



        lastActionController = new LastActionController(10);

    }



    private void createMenu(){
        menu = AbstractEditorMenu.createMenuForType(nextMenuType, this, (LowerPanelInEditor) hud.getLowerPanel());
        actualMenuType = nextMenuType;
    }

    private void initGraphicZone(){
        Rectangle graphicZone = new Rectangle();
        graphicZone.x = hud.getGraphicLeftPixel();
        graphicZone.y = hud.getGraphicUpperPixel();
        graphicZone.width = hud.getGraphicRightPixel()-graphicZone.x;
        graphicZone.height = hud.getGraphicLowerPixel()-graphicZone.y;
        //IEngine engine, Image image, Coordinate pos, int size
    }

    private void createOnMapZoneGraphic() {

        grid = new Grid(this);
        cursor = new Cursor(this);
        boolean visible = EditorPreferencesSingleton.getInstance().getBooleanValue(EditorPreferences.GRID_VISIBILITY.name());
        if (!visible) grid.setVisible(false);

        gameRound.addEntityToEnd(cursor);
        gameRound.addEntityToEnd(grid);
    }

    protected void initHud(int playerNumberInMultiplayerMode){
        hud = new InEditorHud(this, engine, playerNumberInMultiplayerMode, singleplayer);

    }

    public void update(){
        if (!startDataInit) {
            initStartData();
            int size = (int) ((hud.getGraphicRightPixel()-hud.getGraphicLeftPixel())/12f);
            unsavedDataLabel = new UnsavedDataLabel(engine, new Coordinate(drawingGraphicPlaces.centerX-drawingGraphicPlaces.getWidth()/2+size*0.9f, drawingGraphicPlaces.centerY-drawingGraphicPlaces.getHeight()/2+size*0.9f) , size);
            unsavedDataList = new UnsavedDataList(engine.getPathToObjectInUserFolder(ExternalDataController.LEVEL_PREFIX)+level+ExternalDataController.LEVEL_EXTENSION, unsavedDataLabel);
        }
        if (nextMenuType!= actualMenuType){
            createMenu();
        }
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
        if (startDataInit) unsavedDataLabel.draw();
        menu.update();
        hud.update(gameRound);
    }

    private void updateActions() {
        if (actions.size()>0){
            for (int i = (actions.size()-1); i >= 0; i--){
                if (actions.get(i).getPrefix().equals(EditorCommandPrefix.WORLD_SCROLLING)){
                    EditorCamera editorCamera = (EditorCamera) gameRound.getCamera();
                    editorCamera.appendCommand(actions.get(i));
                }
                else if (actions.get(i).getPrefix().equals(EditorCommandPrefix.OBJECT_CREATED)){
                    unsavedDataList.addNewObjectString(actions.get(i).getStringParameters());

                }
                lastActionController.saveActionForRestoring(actions.get(i));
                actions.remove(i);
            }
        }
        if (actions.size()>20){
            Logger.error("Too many actions " + actions.size());
        }
    }

    @Override
    public void draw(){
        hud.draw();
        menu.draw();

    }

    public Hud getHud() {
        return hud;
    }

    @Override
    public void backToMenu(MenuDataStruct dataStruct) {
        mainController.backToMenu(dataStruct, true);
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

    public void setTextInConcole(String text) {
        InEditorHud editorHud = (InEditorHud)hud;
        editorHud.setTextForConsole(text);
    }

    public void exitFromEditor(boolean testLevel) {
        MenuDataStruct dataStruct = new MenuDataStruct();
        dataStruct.setNextLevel(level);
        if (!testLevel)  dataStruct.setNextMenu(io.itch.mgdsstudio.battlecity.menu.MenuType.EDITOR_PRELOADING_WINDOW);
        else dataStruct.setNextMenu(io.itch.mgdsstudio.battlecity.menu.MenuType.SINGLE_MISSION_LOADING);
        mainController.backToMenu(dataStruct, true);
    }

    public boolean areThereUnsavedData() {
        return changesController.areThereUnsavedData();
    }

    public void addNewUnsavedData(String dataString){
        changesController.addNewUnsavedData(dataString);

    }

    public ArrayList <ISelectable> getSelectedObjects(){
        return selectedElements;
    }

    public void transferToMenu(MenuType from , MenuType to) {
        nextMenuType = to;
        Logger.debug("transfer to menu: " + to.name());
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Grid getGrid() {
        return grid;
    }

    public UnsavedDataList getUnsavedDataList() {
        return unsavedDataList;
    }

    private class LastActionController{
        private final ArrayList <EditorAction> actions = new ArrayList<>();
        private final int maxActions;

        private LastActionController(int maxActions) {
            this.maxActions = maxActions;
        }

        private void saveActionForRestoring(EditorAction action){
            actions.add(0,action);
            if (actions.size()>maxActions) actions.remove(actions.size()-1);
        }
    }
}
