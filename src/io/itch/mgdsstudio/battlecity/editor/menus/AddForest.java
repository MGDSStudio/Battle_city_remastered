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
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Forest;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.util.ArrayList;

//To complete
public class AddForest extends AbstractEditorMenu{

    private String add;
    private ObjectDataStruct dataStruct;

    private interface Statements{        
        int SELECT_TILESET = START_STATEMENT;
        int SELECT_SIZE = 21;
        int PLACE_ON_MAP = 31;
    }

    public AddForest(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
         if (actualStatement == Statements.SELECT_TILESET){
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            createMenuWithGraphicButtons(4,3, 0);
            editorController.setTextInConcole("SELECT SPRITE FOR THE FOREST");
        }
        else if (actualStatement == Statements.SELECT_SIZE){
            guiElements.clear();
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            editorController.setTextInConcole("SELECT SIZE OF THE FOREST");
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
        
        else if (actualStatement == Statements.PLACE_ON_MAP){
            editorController.getCursor().setStatement(Cursor.Statement.CELL_CENTER);
            String [] names = new String[] {add, back, cancel};
            createSubmenuWithDefaultAlignedButtons(names);
        }
    }



    private void initButtonNames(){
        add = " ADD ON MAP "; 
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == add){
            return "PLACE OBJECT ON THE FIELD";
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
        
        else if (actualStatement == Statements.PLACE_ON_MAP){
            if (element.getName().equals(add)){
                Coordinate pos = editorController.getCursor().getPos();
                dataStruct.setPosX((int)pos.x);
                dataStruct.setPosY((int)pos.y);
                createGraphic();
            }
        }
        else if (actualStatement == Statements.SELECT_TILESET){
            if (!element.getName().equals(prev) && !element.getName().equals(next)){
                int tilesetNumber = getSelectedTilesetButton(element);
                dataStruct = new ObjectDataStruct(Forest.class.getSimpleName());
                dataStruct.setImageZoneKeyCode(tilesetNumber);
                nextStatement = Statements.SELECT_SIZE;
                addInfoAboutLastUsedTilesets(tilesetNumber);
            }
        }
        else if (actualStatement == Statements.SELECT_SIZE){
            if (element.getName().equals(apply)){
                nextStatement = Statements.PLACE_ON_MAP;
            }
        }
    }
    
    private void createGraphic() {
        Forest object;
        object = Forest.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), dataStruct.createEntityData());
        editorController.getGameRound().addEntityToEnd(object);
        Logger.correct("This forest must be added on right layer");
        Logger.debug("Created object: " + object.getDataString());
        EditorListenersManagerSingleton singleton = EditorListenersManagerSingleton.getInstance();
        EditorAction editorAction = new EditorAction(EditorCommandPrefix.OBJECT_CREATED, object.getDataString());
        singleton.notify(editorAction);
    }

    @Override
    protected void initDataForStatement(int actualStatement) {
        initGui();
    }

    @Override
    protected void onBackPressed(){
        if (actualStatement == Statements.SELECT_TILESET) editorController.transferToMenu(MenuType.FOREST, MenuType.MAIN);
        else if (actualStatement == Statements.SELECT_SIZE) nextStatement = Statements.SELECT_TILESET;
        else if (actualStatement == Statements.PLACE_ON_MAP) nextStatement = Statements.SELECT_SIZE;
    }

    protected void cancelPressed() {
        nextStatement = START_STATEMENT;
    }

    private class ObjectDataStruct {
        private ArrayList<Integer> values ;
        private ArrayList <Integer> graphicValues;
        private int id;
        private String name;
        private int size, angle, posX, posY;
        private int imageZoneKeyCode = 1;   //now only 1 - bricks

        public ObjectDataStruct(String name) {
            this.values = new ArrayList<>();
            this.graphicValues = new ArrayList<>();
            this.id = Entity.NO_ID;
            this.name = name;
        }
        public EntityData createEntityData(){
            int [] values = new int[5];
            values[0] = posX;
            values[1] = posY;
            values[2] = angle;  //always 0
            values[3] = size;
            values[4] = size;
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

        public void setImageZoneKeyCode(int imageZoneKeyCode) {
            this.imageZoneKeyCode = imageZoneKeyCode;
        }

    }
    
}
