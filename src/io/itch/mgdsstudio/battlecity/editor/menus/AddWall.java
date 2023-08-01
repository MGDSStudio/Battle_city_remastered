package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.dataloading.GraphicData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ObjectActivatingController;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.util.ArrayList;

public class AddWall extends AbstractEditorMenu {

    private String crushable, armored, immortal;
    private String square, triangle, circle;
    private String add;
    private ObjectDataStruct objectData;

   private interface Statements{

         int SELECT_FORM = 11;
         int SELECT_TRIANGLE_FORM = 15;
         int SELECT_TILESET = 21;
         int PLACE_SIMPLE_OBJECT_ON_MAP = 51;
         int PLACE_TRIANGLE_ON_MAP = 61;
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
            //createSubmenuWithColumnAlignedButtons(names,2);
            editorController.setTextInConcole("SELECT TYPE OF THE WALL ELEMENT");
        }
        else if (actualStatement == Statements.PLACE_SIMPLE_OBJECT_ON_MAP){
            int value = getDigitValueFromKeyboard();
            objectData.addValue(value);
            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
            String [] names = new String[] {add, back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SHIFT THE BATTLEFIELD VIA SWIPES TO SELECT RIGHT POSITION FOR THE OBJECT. PRESS ADD BUTTON TO PLACE THE OBJECT ON THE BATTLEFIELD");
        }
        else if (actualStatement == Statements.SELECT_FORM){
            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
            String [] names = new String[] {square,triangle,circle,back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SELECT FORM OF THE WALL ELEMENT");
        }
    }

    private void initButtonNames(){
        crushable =  "DESTROYABLE";
        immortal = "IMMORTAL";
        add = "ADD ON MAP";
        square = "SQUARE;"
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
        else return "NO DATA";
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
            nextStatement =   Statements.SELECT_TILESET;
            initDataStructForGuiName(element.getName());
      }
      else if (actualStatement == Statements.SELECT_FORM){

      
      }
    }

    private void initDataStructForGuiName(String name) {
       if (name.equals(crushable)) objectData = new ObjectDataStruct(BrickWall.class.getSimpleName());
       else if (name.equals(immortal)) objectData = new ObjectDataStruct(WorldBoard.class.getSimpleName());
       else if (name.equals(armored)) objectData = new ObjectDataStruct(ArmoredWall.class.getSimpleName());
       else {
           Logger.error("No data for this name: " + name);
       }
    }

    private int getValueFromName(String name) {
       int value = 0;
       for (int i = 0; i < name.length(); i++) {
            if (Character.isDigit(name.charAt(i))) {
                String valueString = ""+ name.charAt(i);
                for (int j = i; j < name.length(); j++) {
                    if (Character.isDigit(name.charAt(j))) {
                        valueString+=name.charAt(j);
                    }
                }
                try {
                    value = Integer.parseInt(valueString);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
           }
       }
       return value;

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
        else if (actualStatement == Statements.PLACE_SIMPLE_OBJECT_ON_MAP) nextStatement = Statements.SELECT_TILESET;
        else if (actualStatement == Statements.PLACE_TRIANGLE_ON_MAP) nextStatement = Statements.SELECT_TILESET;

        /*
        int SELECT_FORM = 11;
         int SELECT_TILESET = 21;
         int PLACE_SIMPLE_OBJECT_ON_MAP = 51;
         int PLACE_TRIANGLE_ON_MAP = 61;
         */
    }

    class ObjectDataStruct{
        private ArrayList <Integer> values ;
        private int id;
        private String name;

        public ObjectDataStruct(ArrayList<Integer> values, int id, String name) {
            this.values = values;
            this.id = id;
            this.name = name;
        }

        public ObjectDataStruct(int value) {
            this.values = new ArrayList<>();
            values.add(value);
            this.id = Entity.NO_ID;
            this.name = "no name";
        }

        public ObjectDataStruct(int value, String name) {
            this.values = new ArrayList<>();
            values.add(value);
            this.id = Entity.NO_ID;
            this.name = name;
        }

        public ObjectDataStruct(String name) {
            this.values = new ArrayList<>();
            this.id = Entity.NO_ID;
            this.name = name;
        }

        public void addValue(int value){
            values.add(value);
        }

        public EntityData createEntityData(){
            int [] valuesArray = new int[values.size()];
            for (int i  = 0 ; i < valuesArray.length ; i++){
                valuesArray[i] = values.get(i);
            }
            EntityData entityData = new EntityData(valuesArray, new int[]{}, id);
            Logger.debug("Entity data will content: " + values + "; ID: " + id );
            return entityData;
        }

        public void addValueToStart(int x) {
            values.add(0,x);
        }
    }
    
  }
