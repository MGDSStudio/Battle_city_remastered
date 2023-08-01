package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.DigitKeyboard;
import com.mgdsstudio.engine.nesgui.GuiElement;

import com.mgdsstudio.engine.nesgui.TextLabel;
import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.util.ArrayList;

public class AddWall extends AbstractEditorMenu {

    private String crushable, armored, immortal;

    private String square, triangle, circle;
    private String NE, NW, SE,SW;
    private String add;
    private ObjectDataStruct objectData;

   private interface Statements{

         int SELECT_FORM = 11;
         int SELECT_TRIANGLE_FORM = 15;
         int SELECT_SIZE = 17;
         int SELECT_TILESET = 21;
         int PLACE_ON_MAP = 51;
         //int PLACE_TRIANGLE_ON_MAP = 61;
   }

    public AddWall(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == START_STATEMENT) {
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            initButtonNames();
            String [] names = new String[]{crushable, armored, immortal};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SELECT TYPE OF THE WALL ELEMENT");
        }
        else if (actualStatement == Statements.PLACE_ON_MAP){
            int value = getDigitValueFromKeyboard();
            objectData.setSize(value); //width
            //editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
            String [] names = new String[] {add, back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SHIFT THE BATTLEFIELD VIA SWIPES TO SELECT RIGHT POSITION FOR THE OBJECT. PRESS ADD BUTTON TO PLACE THE OBJECT ON THE BATTLEFIELD");
        }
        else if (actualStatement == Statements.SELECT_FORM){
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            String [] names = new String[] {square,circle,triangle,back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SELECT FORM OF THE WALL ELEMENT");
        }
        else if (actualStatement == Statements.SELECT_TRIANGLE_FORM){
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            String [] names = new String []{ SE, SW, NW, NE, back };
            editorController.setTextInConcole("SELECT ORIENTATION OF THE TRIANGLE");
            createSubmenuWithDefaultAlignedButtons(names);
        }
        else if (actualStatement == Statements.SELECT_SIZE){
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            editorController.setTextInConcole("SELECT SIZE OF THE OBJECT");
            createSubmenuWithDigitKeyboard(true, "TEXT FIELD");
            GuiElement gui = getGuiByName(KEYBOARD_GUI_NAME);
            if (gui != null){
               DigitKeyboard keyboard = (DigitKeyboard) gui;
               TextLabel label =  keyboard.getEmbeddedGui();
               int actualSize = editorController.getGrid().getWidth();
               String defaultValue = ""+ actualSize;
               label.setAnotherTextToBeDrawnAsName(defaultValue);
               label.setUserData(defaultValue);
            }
        }
        else if (actualStatement == Statements.SELECT_TILESET){
            createMenuWithGraphicButtons(5,3, 0);
        }
    }



    private void initButtonNames(){
        crushable =  "DESTROYABLE";
        immortal = "IMMORTAL";
        add = "ADD ON MAP";
        square = "SQUARE;";
        circle = "CIRCLE";
        triangle = "TRIANGLE";
        NW = "NW";
        NE = "NE";
        SE = "SE";
        SW = "SW";
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0; 
        int language = ENGLISH;
        if (element.getName() == crushable){
            return "THE NEW OBJECT CAN BE DESTROYED BY A SIMPLE SHOT";
        }
        else if (element.getName() == armored){
            return "THE NEW OBJECT CAN BE DESTROYED ONLY BY A MISSILE";
        }
        else if (element.getName() == immortal){
            return "THE NEW OBJECT CAN NOT BE DESTROYED";
        }
        else if (element.getName() == add){
            return "PLACE OBJECT ON THE FIELD";
        }
        else if (element.getName() == triangle){
            return "THE WALL WILL HAVE TRIANGLE FORM";
        }
        else if (element.getName() == square){
            return "THE WALL WILL HAVE SQUARE FORM";
        }
        else if (element.getName() == circle){
            return "THE WALL WILL HAVE ROUND FORM";
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
       else if (element.getName().equals(crushable) || element.getName().equals(armored) || element.getName().equals(immortal)){
            nextStatement = Statements.SELECT_TILESET;
            initDataStructForGuiName(element.getName());
      }
      else if (actualStatement == Statements.SELECT_FORM){
            int form = -1;
            if (element.getName().equals(square)) {
                nextStatement = Statements.SELECT_SIZE;
                form = SolidObject.BodyForms.RECT;
            }
            else if (element.getName().equals(circle)) {
                nextStatement = Statements.SELECT_SIZE;
                form = SolidObject.BodyForms.CIRCLE;
            }
            else if (element .getName().equals(triangle)) nextStatement = Statements.SELECT_TRIANGLE_FORM;
            else Logger.error("No data for this button");
            if (form>=0) objectData.setForm(form);
      }
      else if (actualStatement == Statements.SELECT_TRIANGLE_FORM){
          int form = 0;
          if (element.getName().equals(NE)) {
              form = SolidObject.BodyForms.NE_TRIANGLE;
              editorController.getCross().setStatement(Cross.Statement.TRIANGLE_RIGHT_UP);
          }
          else if (element.getName().equals(NW)) {
              form = SolidObject.BodyForms.NW_TRIANGLE;
              editorController.getCross().setStatement(Cross.Statement.TRIANGLE_LEFT_UP);
          }
          else if (element.getName().equals(SE)) {
              form = SolidObject.BodyForms.SE_TRIANGLE;
              editorController.getCross().setStatement(Cross.Statement.TRIANGLE_RIGHT_DOWN);
          }
          else if (element.getName().equals(SW)) {
              form = SolidObject.BodyForms.SW_TRIANGLE;
              editorController.getCross().setStatement(Cross.Statement.TRIANGLE_LEFT_DOWN);
          }
          objectData.setForm(form);
          nextStatement = Statements.SELECT_SIZE;
       }
       else if (actualStatement == Statements.PLACE_ON_MAP){
            if (element.getName().equals(add)){
               Coordinate pos = editorController.getCross().getPos();
               objectData.setPosX((int)pos.y);
               objectData.setPosY((int)pos.x);
               createWall();
           }
       }
    }

    private void createWall() {
        Wall object;
        String name = objectData.getName();
        if (name.equals(BrickWall.class.getSimpleName())){
            object = BrickWall.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), objectData.createEntityData());
        }
        else if (name.equals(ArmoredWall.class.getSimpleName())){
            object = ArmoredWall.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), objectData.createEntityData());
        }
        else object = WorldBoard.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), objectData.createEntityData());
        editorController.getGameRound().addEntityOnGround(object);
        Logger.debug("Created object: " + object.getDataString());
        EditorListenersManagerSingleton singleton = EditorListenersManagerSingleton.getInstance();
        EditorAction editorAction = new EditorAction(EditorCommandPrefix.OBJECT_CREATED, object.getDataString());
        singleton.notify(editorAction);
    }

    private void initDataStructForGuiName(String name) {
       if (name.equals(crushable)) objectData = new ObjectDataStruct(BrickWall.class.getSimpleName());
       else if (name.equals(immortal)) objectData = new ObjectDataStruct(WorldBoard.class.getSimpleName());
       else if (name.equals(armored)) objectData = new ObjectDataStruct(ArmoredWall.class.getSimpleName());
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
        else if (actualStatement == Statements.SELECT_FORM) nextStatement = START_STATEMENT;
        else if (actualStatement == Statements.SELECT_TILESET) nextStatement = Statements.SELECT_FORM;
        else if (actualStatement == Statements.PLACE_ON_MAP) nextStatement = Statements.SELECT_TILESET;
        else if (actualStatement == Statements.SELECT_SIZE) nextStatement = Statements.SELECT_FORM;
    }

    class ObjectDataStruct{
        private ArrayList <Integer> values ;
        private ArrayList <Integer> graphicValues;
        private int id;
        private String name;
        private int size, angle, posX, posY, form;
        private int imageZoneKeyCode = 1;   //now only 1 - bricks

        public ObjectDataStruct(String name) {
            this.values = new ArrayList<>();
            this.graphicValues = new ArrayList<>();
            this.id = Entity.NO_ID;
            this.name = name;
        }

        public void addGraphicValue(int graphicValue){
            graphicValues.add(graphicValue);
        }

        public void addValue(int value){
            values.add(value);
        }

        public EntityData createEntityData(){
            int [] values = new int[6];
            values[0] = posX;
            values[1] = posY;
            values[2] = angle;  //always 0
            values[3] = size;
            values[4] = size;
            values[5] = form;
            int [] graphicValues = new int[1];
            graphicValues[0] = imageZoneKeyCode;
            EntityData entityData = new EntityData(values, graphicValues, id);
            Logger.debug("Entity data will content: " + values + "; ID: " + id );
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

        public void setForm(int form) {
            this.form = form;
        }

        public void setImageZoneKeyCode(int imageZoneKeyCode) {
            this.imageZoneKeyCode = imageZoneKeyCode;
        }

        public void addValueToStart(int x) {
            values.add(0,x);
        }
    }
    
  }
