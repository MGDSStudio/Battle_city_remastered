package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.editor.Cross;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;
import java.util.ArrayList;


//To complete
public class AddStaff extends AbstractEditorMenu{
    private String placePlayer, removePlayers;  //MAIN MENU
    private String addPlayer, yesIWant;

    // private String select

    private interface Statements{
        int SELECT_IDE = START_STATEMENT;
        int ARE_YOU_SURE_YOU_WANT_TO_DELETE = 111;
        int DELETE_PLAYER = 21;
    }

    public AddStaff(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == Statements.SELECT_TYPE) {
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            initButtonNames();
            String [] names = new String[]{sprite, animation, back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SELECT TYPE OF THE GRAPHIC YOU WANT TO ADD");
        }
        else if (actualStatement == Statements.SELECT_TILESET){
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            createMenuWithGraphicButtons(4,3, 0);
            editorController.setTextInConcole("SELECT SPRITE FOR THE GRAPHIC");
        }
        else if (actualStatement == Statements.SELECT_SIZE){
            guiElements.clear();
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            editorController.setTextInConcole("SELECT SIZE OF THE OBJECT");
            createSubmenuWithDigitKeyboard(true, "TEXT FIELD");
            GuiElement gui = getGuiByName(KEYBOARD_GUI_NAME);
            if (gui != null){
                DigitKeyboard keyboard = (DigitKeyboard) gui;
                TextLabel label =  keyboard.getEmbeddedGui();
                int actualSize = editorController.getGrid().getGridStep();
                String defaultValue = ""+ actualSize;
                label.setAnotherTextToBeDrawnAsName(defaultValue);
                label.setUserData(defaultValue);
            }
        }
        else if (actualStatement == Statements.SELECT_LAYER){
            int value = getDigitValueFromKeyboard();
            if (value<1){
                value = 1;
            }
            else editorController.setTextInConcole("SHIFT THE BATTLEFIELD VIA SWIPES TO SELECT RIGHT POSITION FOR THE OBJECT. PRESS ADD BUTTON TO PLACE THE OBJECT ON THE BATTLEFIELD. THE OBJECT SIZE WILL BE " + value + " WORLD UNITS");
            Logger.debug("Graphic object will have size: " + value);
            spriteDataStruct.setSize(value);
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            String [] names = new String[] {ground,onGround,aboveGround,inAir,back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SELECT FORM OF THE WALL ELEMENT");
        }
        else if (actualStatement == Statements.PLACE_ON_MAP){
            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
            String [] names = new String[] {add, back, cancel};
            createSubmenuWithDefaultAlignedButtons(names);
        }
    }



    private void initButtonNames(){
        sprite =  " SPRITE ";
        animation = " ANIMATION ";
        add = " ADD ON MAP ";
        ground = " GROUND LAYER ";
        onGround = "  ON GROUND  ";
        aboveGround = "ABOVE GROUND";
        inAir = "  IN AIR ";
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == sprite){
            return "SPRITE (SINGLE PICTURE) WILL BE ADDED";
        }
        else if (element.getName() == animation){
            return "ANIMATION (SEQUENCE OF SPRITES) WILL BE ADDED";
        }
        else if (element.getName() == add){
            return "PLACE OBJECT ON THE FIELD";
        }
        else if (element.getName() == ground){
            return "OBJECT CAN BE GRASS, FLOOR, SAND OR SNOW";
        }
        else if (element.getName() == onGround){
            return "OBJECT CAN BE ROAD, BUSH, STONE OR PUDDLE";
        }
        else if (element.getName() == aboveGround){
            return "OBJECT CAN BE TREE, MOUNTAIN WITH A TUNNEL OR SECOND FLOOR";
        }
        else if (element.getName() == inAir){
            return "OBJECT CAN BE SNOW ON A MOUNTAIN PEAK";
        }
        else return " ";
    }

    @Override
    protected void guiPressed(GuiElement element) {

    }

    @Override
    protected void setConsoleTextForFirstButtonPressing(GuiElement element) {
        editorController.setTextInConcole(getTextForConsoleByPressedGui(element));
    }

    @Override
    protected void guiReleased(GuiElement element) {
        if (element.getName().equals(back)) {
            onBackPressed();
        }
        else if (element.getName().equals(cancel)){
            cancelPressed();
        }
        else if (actualStatement == Statements.SELECT_TYPE) {
            if (element.getName().equals(sprite) || element.getName().equals(animation)) {
                nextStatement = Statements.SELECT_TILESET;
                initDataStructForGuiName(element.getName());
            }
            else {
                Logger.error("Can not be !");
            }
        }
        else if (actualStatement == Statements.PLACE_ON_MAP){
            if (element.getName().equals(add)){
                Coordinate pos = editorController.getCross().getPos();
                spriteDataStruct.setPosX((int)pos.x);
                spriteDataStruct.setPosY((int)pos.y);
                createGraphic();
            }
        }
        else if (actualStatement == Statements.SELECT_TILESET){
            if (!element.getName().equals(prev) && !element.getName().equals(next)){
                int tilesetNumber = getSelectedTilesetButton(element);
                spriteDataStruct.setImageZoneKeyCode(tilesetNumber);
                nextStatement = Statements.SELECT_SIZE;
                addInfoAboutLastUsedTilesets(tilesetNumber);
            }
        }
        else if (actualStatement == Statements.SELECT_SIZE){
            if (element.getName().equals(apply)){
                nextStatement = Statements.SELECT_LAYER;
            }
        }
        else if (actualStatement == Statements.SELECT_LAYER){
            int layer = 0;
            if (element.getName().equals(ground)) {
                layer = GraphicObject.GraphicLayers.GROUND_LAYER;
            }
            else if (element.getName().equals(onGround)) {
                layer = GraphicObject.GraphicLayers.ON_GROUND_LAYER;
            }
            else if (element.getName().equals(aboveGround)) {
                layer = GraphicObject.GraphicLayers.OBJECT_ABOVE_GROUND_LAYER;
            }
            else if (element.getName().equals(inAir)) {
                layer = GraphicObject.GraphicLayers.OVER_SKY_LEVEL;
            }
            else Logger.error("No data for this button");
            if (layer>=0) spriteDataStruct.setLayer(layer);

            nextStatement = Statements.PLACE_ON_MAP;
        }
    }
    private void createGraphic() {
        //SpriteInGame
        GraphicObject object;
        String name = spriteDataStruct.getName();
        if (name.equals(SpriteInGame.class.getSimpleName())){
            object = SpriteInGame.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), spriteDataStruct.createEntityData());
        }
        else object = SpriteAnimationInGame.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), spriteDataStruct.createEntityData());
        editorController.getGameRound().addEntityOnGround(object);
        Logger.debug("Created object: " + object.getDataString());
        EditorListenersManagerSingleton singleton = EditorListenersManagerSingleton.getInstance();
        EditorAction editorAction = new EditorAction(EditorCommandPrefix.OBJECT_CREATED, object.getDataString());
        singleton.notify(editorAction);
    }

    private void initDataStructForGuiName(String name) {
        if (name.equals(sprite)) spriteDataStruct = new ObjectDataStruct(SpriteInGame.class.getSimpleName());
        else if (name.equals(animation)) spriteDataStruct = new ObjectDataStruct(AnimationInGame.class.getSimpleName());
        else {
            Logger.error("No data for this name: " + name);
        }
    }
    @Override
    protected void initDataForStatement(int actualStatement) {
        initGui();
    }

    @Override
    protected void onBackPressed(){
        if (actualStatement == START_STATEMENT) editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
        else if (actualStatement == Statements.SELECT_TILESET) nextStatement = Statements.SELECT_TYPE;
        else if (actualStatement == Statements.SELECT_SIZE) nextStatement = Statements.SELECT_TILESET;
        else if (actualStatement == Statements.SELECT_LAYER) nextStatement = Statements.SELECT_SIZE;
        else if (actualStatement == Statements.PLACE_ON_MAP) nextStatement = Statements.SELECT_LAYER;
    }

    protected void cancelPressed() {
        nextStatement = START_STATEMENT;
    }

    
   
    
}
