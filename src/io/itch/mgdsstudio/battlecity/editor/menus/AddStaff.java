package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.editor.Cursor;
import io.itch.mgdsstudio.battlecity.editor.EditorAction;
import io.itch.mgdsstudio.battlecity.editor.EditorCommandPrefix;
import io.itch.mgdsstudio.battlecity.editor.EditorListenersManagerSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Camp;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.util.ArrayList;


//To complete
public class AddStaff extends AbstractEditorMenu{
    private String forPlayer, forEnemy;  //MAIN MENU
    private String add;

    private ObjectDataStruct spriteDataStruct;

    // private String select

    private interface Statements{
        int SELECT_SIDE = START_STATEMENT;
        int PLACE_ON_MAP = 21;

    }

    public AddStaff(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == Statements.SELECT_SIDE) {
            editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
            initButtonNames();
            String [] names = new String[]{forPlayer, forEnemy, back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setConsoleText("SELECT TYPE OF THE GRAPHIC YOU WANT TO ADD");
        }
        else if (actualStatement == Statements.PLACE_ON_MAP){
            editorController.getCursor().setStatement(Cursor.Statement.CELL_CENTER);
            String [] names = new String[] {add, back, cancel};
            createSubmenuWithDefaultAlignedButtons(names);
        }
    }



    private void initButtonNames(){
        forPlayer =  " PLAYER'S BASE ";
        forEnemy = "   ENEMY BASE   ";
        add = " ADD ON MAP ";
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == forPlayer){
            return "PLAYER MUST PROTECT THIS BASE. ENEMIES WILL ATTACK IT";
        }
        else if (element.getName() == forEnemy){
            return "PLAYER MUST ATTACK THIS BASE. ENEMIES WILL PROTECT IT";
        }
        else if (element.getName() == add){
            return "PLACE OBJECT ON THE FIELD";
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
        else if (actualStatement == Statements.PLACE_ON_MAP){
            if (element.getName().equals(add)){
                Coordinate pos = editorController.getCursor().getPos();
                spriteDataStruct.setPosX((int)pos.x);
                spriteDataStruct.setPosY((int)pos.y);
                createGraphic();
            }
        }
        else if (actualStatement == Statements.SELECT_SIDE){
            int side = 0;
            if (element.getName().equals(forPlayer)) {
                side = Camp.PLAYERS_CAMP;
            }
            else side = Camp.ENEMY_CAMP;
            spriteDataStruct = new ObjectDataStruct(Camp.class.getSimpleName());
            spriteDataStruct.setSide(side);
            nextStatement = Statements.PLACE_ON_MAP;
        }
    }
    private void createGraphic() {
        Camp object;
        object = Camp.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), spriteDataStruct.createEntityData());
        editorController.getGameRound().addEntityOnGround(object);
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
        if (actualStatement == START_STATEMENT) editorController.transferToMenu(MenuType.STAFF, MenuType.MAIN);
        else if (actualStatement == Statements.PLACE_ON_MAP) nextStatement = START_STATEMENT;
    }

    protected void cancelPressed() {
        nextStatement = START_STATEMENT;
    }

    private class ObjectDataStruct {
        private ArrayList<Integer> values ;
        private ArrayList <Integer> graphicValues;
        private int id;
        private String name;
        private int side;
        private int size, angle, posX, posY;
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
            values[5] = side;
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

        public void setSide(int side) {
            this.side = side;
        }
    }
   
    
}
