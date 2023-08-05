package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.DigitKeyboard;
import com.mgdsstudio.engine.nesgui.GuiElement;
import com.mgdsstudio.engine.nesgui.TextLabel;
import io.itch.mgdsstudio.battlecity.editor.Cursor;
import io.itch.mgdsstudio.battlecity.editor.EditorAction;
import io.itch.mgdsstudio.battlecity.editor.EditorCommandPrefix;
import io.itch.mgdsstudio.battlecity.editor.EditorListenersManagerSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.graphic.AnimationInGame;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.util.ArrayList;

public class AddGraphic extends AbstractEditorMenu{
    private String sprite, animation;
    /*
    int GROUND_LAYER = 0;
        int ON_GROUND_LAYER = 1;
        int OBJECT_ABOVE_GROUND_LAYER = 2;
        int OVER_SKY_LEVEL = 3;
     */
    private String ground, onGround, aboveGround, inAir;
    private String add;
    private ObjectDataStruct spriteDataStruct;

    private interface Statements{
        int SELECT_TYPE = START_STATEMENT;
        int SELECT_TILESET = 11;
        int SELECT_SIZE = 21;
        int SELECT_LAYER = 31;
        int PLACE_ON_MAP = 51;
    }

    public AddGraphic(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == Statements.SELECT_TYPE) {
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            initButtonNames();
            String [] names = new String[]{sprite, animation, back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setConsoleText("SELECT TYPE OF THE GRAPHIC YOU WANT TO ADD");
        }
        else if (actualStatement == Statements.SELECT_TILESET){
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            createMenuWithGraphicButtons(4,3, 0);
            editorController.setConsoleText("SELECT SPRITE FOR THE GRAPHIC");
        }
        else if (actualStatement == Statements.SELECT_SIZE){
            guiElements.clear();
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            editorController.setConsoleText("SELECT SIZE OF THE OBJECT");
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
            else editorController.setConsoleText("SHIFT THE BATTLEFIELD VIA SWIPES TO SELECT RIGHT POSITION FOR THE OBJECT. PRESS ADD BUTTON TO PLACE THE OBJECT ON THE BATTLEFIELD. THE OBJECT SIZE WILL BE " + value + " WORLD UNITS");
            Logger.debug("Graphic object will have size: " + value);
            spriteDataStruct.setSize(value);
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            String [] names = new String[] {ground,onGround,aboveGround,inAir,back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setConsoleText("SELECT FORM OF THE WALL ELEMENT");
        }
        else if (actualStatement == Statements.PLACE_ON_MAP){
            editorController.getCursor().setStatement(Cursor.Statement.CELL_CENTER);
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
        editorController.setConsoleText(getTextForConsoleByPressedGui(element));
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
                Coordinate pos = editorController.getCursor().getPos();
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

    class ObjectDataStruct {
        private ArrayList<Integer> values ;
        private ArrayList <Integer> graphicValues;
        private int id;
        private String name;
        private int size, angle, posX, posY, layer;
        private int imageZoneKeyCode = 1;   //now only 1 - bricks

        public ObjectDataStruct(String name) {
            this.values = new ArrayList<>();
            this.graphicValues = new ArrayList<>();
            this.id = Entity.NO_ID;
            this.name = name;
        }
        public EntityData createEntityData(){
            int [] values = new int[6];
            values[0] = posX;
            values[1] = posY;
            values[2] = angle;  //always 0
            values[3] = size;
            values[4] = size;
            values[5] = layer;
            int [] graphicValues = new int[1];
            graphicValues[0] = imageZoneKeyCode;
            EntityData entityData = new EntityData(values, graphicValues, id);
            String content = "";
            for (int i = 0; i < values.length; i++) {
                content+=values[i];
                content+=",";
            }
            Logger.debug("Entity data will content: " + content + "; ID: " + id );
            return entityData;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }

        public String getName() {
            return name;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }

        public void setImageZoneKeyCode(int imageZoneKeyCode) {
            this.imageZoneKeyCode = imageZoneKeyCode;
        }

        public void addValueToStart(int x) {
            values.add(0,x);
        }
    }
}
