package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.DigitKeyboard;
import com.mgdsstudio.engine.nesgui.GuiElement;

import com.mgdsstudio.engine.nesgui.TextLabel;
import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.dataloading.GraphicData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Collectable;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ObjectActivatingController;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class AddCollectable extends AbstractEditorMenu {

    /*
         int LIFE = 0;   //More tanks
        int WEAPON = 1;     //Rocket launcher
        int ENGINE = 2;    //Motor
        int MINE = 3;       //Mines
        int ARMOUR = 4;
        int RADAR = 5;  //Enemies to flag

        int AI_TURRET = 6;  //Enemies to flag

        int RANDOM = 7;  //Enemies to flag

        int CLOCK = 8;  //Enemies to flag
        int MONEY_1 = 21;
        int MONEY_2 = 22;
        int MONEY_3 = 23;
        int MONEY_5 = 24;
        int MONEY_10 = 25;
        int MONEY_15 = 26;
        int MONEY_20 = 27;
        int MONEY_25 = 28;
        int MONEY_30 = 29;
        int MONEY_40 = 30;
        int MONEY_50 = 31;

     */


    private String weapon, armour, extraLife, engine, mine, radar, aiturret, random, money;
    private String money1, money2, money3, money5, money10, money15, money20,money25, money30, money40, money50;
    private String valueAddingField, add;
   // private String apply;
    private final static String DATA_FIELD = "DATA_FIELD";
    private ObjectDataStruct objectData;

    // private String select

   private interface Statements{
         int SELECT_TYPE = 11;
         int SELECT_VALUE = 21; //only for money
         int SELECT_ACTIVATION_TYPE = 31;   //for future
         int SELECT_DELAY = 41;
         int PLACE_ON_MAP = 51;
   }

    public AddCollectable(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == START_STATEMENT) {
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            initButtonNames();
            String [] names = new String[10];
            for (int i = 0; i < names.length; i++){
                names[i] = getNameForPos(i);
            }
            createSubmenuWithColumnAlignedButtons(names,2);
            editorController.setTextInConcole("SELECT TYPE OF THE COLLECTABLE OBJECT TO BE ADDED ON THE BATTLEFIELD");
        }
        else if (actualStatement == Statements.SELECT_DELAY){
            editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
            guiElements.clear();
            createSubmenuWithDigitKeyboard(true, DATA_FIELD);
            editorController.setTextInConcole("ENTER THE DELAY FOR START TIMER. THE OBJECT WILL APPEAR ON THE BATTLE FIELD AFTER THE TIMER CALLS BACK");
        }
        else if (actualStatement == Statements.PLACE_ON_MAP){
            int value = getDigitValueFromKeyboard();
            objectData.addValue(value);
            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
            String [] names = new String[] {add, back};
            createSubmenuWithDefaultAlignedButtons(names);
            editorController.setTextInConcole("SHIFT THE BATTLEFIED VIA SWIPES TO SELECT RIGHT POSITION FOR THE OBJECT. PRESS ADD BUTTON TO PLACE THE OBJECT ON THE BATTLEFIELD");
        }
        else if (actualStatement == Statements.SELECT_VALUE){

            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
            String [] names = new String[] {money1, money2, money3, money5, money10, money15, money20, money25, money30,money40, money50, back};
            createSubmenuWithColumnAlignedButtons(names,3);
            editorController.setTextInConcole("SELECT VALUE - HOW MUCH COSTS THIS COLLECTABLE?");
        }
    }

    private void initButtonNames(){
        weapon =  "WEAPON";
        armour = "ARMOUR";
        extraLife = "EXTRA LIFE";
        engine = "ENGINE";
        mine = "MINE";
        radar = "RADAR";
        aiturret = "TURRET";
        random = "RANDOM";
        money = "MONEY";
        valueAddingField = "valueAddingField";
        apply = "NEXT";
        add = "ADD ON MAP";

        money1 = " 1";
        money2 = " 2";
        money3 = " 3"; 
        money5 = " 5";
        money10 = "10"; 
        money15 = "15";
        money20 = "20";
        money25 = "25";
        money30 = "30";
        money40 = "40"; 
        money50 = "50";

    }

    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  weapon; break;
            case (1): name =  armour; break;
            case (2): name =  extraLife; break;
            case (3): name =  engine; break;
            case (4): name =  mine; break;
            case (5): name =  radar; break;
            case (6): name =  aiturret; break;
            case (7): name =  money; break;
            case (8): name =  random; break;
            default:  name = back; break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == apply){
            return "CONTINUE TO THE NEXT SUBMENU";
        }
        else if (element.getName() == add){
            return "PLACE OBJECT ON THE FIELD";
        }
        else if (element.getName() == weapon){
            return "TURRET UPGRADE";
        }
        else if (element.getName() == armour){
            return "BETTER ARMOUR";
        }
        else if (element.getName() == extraLife){
             return "MORE LIFES FOR THE PLAYER";
        }
        else if (element.getName() == engine){
             return "POWERFUL ENGINE WITH HIGHER MAX VELOCITY";
        }
        else if (element.getName() == mine){
             return "GENERATE MINES ON THE BATTLE FIELD";
        }
        else if (element.getName() == radar){
             return "RADAR SHOWS THE ENEMIES ON THE BATTLEFIELD EVEN BEHIND OBSTACLES";
        }
        else if (element.getName() == aiturret){
             return "GENERATE AI CONTROLLED TURRETS ON THE BATTLEFIELD";
        }
        else if (element.getName() == money){
             return "MONEY WHICH CAN BE USED TO BUY UPGRADES IN THE SHOP";
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
       else if (element.getName().equals(weapon) || element.getName().equals(armour)  ||
               element.getName().equals(extraLife)  || element.getName().equals(engine)  ||
               element.getName().equals(mine)  || element.getName().equals(radar)
               || element.getName().equals(aiturret)
               || element.getName().equals(random)){
         nextStatement =   Statements.SELECT_DELAY;
         initDataStructForGuiName(element.getName());
      }
      else if (element.getName().equals(money)){
             nextStatement =   Statements.SELECT_VALUE;
             objectData = new ObjectDataStruct(io.itch.mgdsstudio.battlecity.game.gameobjects.Collectable.class.getSimpleName());
        }
        else if (element.getName().equals(apply)){
            if (actualStatement == Statements.SELECT_DELAY){
                objectData.addValue(ObjectActivatingController.BY_TIMER_ACTIVATION);
                nextStatement = Statements.PLACE_ON_MAP;
            }
            else Logger.debug("No data for this statement and button");
            Logger.debug("Apply action");
        }
        else if (element.getName().equals(add)){
             Coordinate pos = editorController.getCross().getPos();
             objectData.addValueToStart((int)pos.y);
             objectData.addValueToStart((int)pos.x);
             Collectable object = Collectable.create(editorController.getEngine(), editorController.getGameRound().getPhysicWorld(), objectData.createEntityData());
             editorController.getGameRound().addEntityOnGround(object);
             Logger.debug("Created object: " + object.getDataString());
             EditorListenersManagerSingleton singleton = EditorListenersManagerSingleton.getInstance();
             EditorAction editorAction = new EditorAction(EditorCommandPrefix.OBJECT_CREATED, object.getDataString());
             singleton.notify(editorAction);
             nextStatement = START_STATEMENT;
        }
        else if (element.getName().equals(money1) || element.getName().equals(money2) || element.getName().equals(money3) || element.getName().equals(money5) || element.getName().equals(money10) || element.getName().equals(money15) || element.getName().equals(money20) || element.getName().equals(money25)|| element.getName().equals(money30) || element.getName().equals(money30) || element.getName().equals(money40) || element.getName().equals(money50)){
            int value = getValueFromName(element.getName());
            objectData.addValue(value);
            nextStatement = Statements.SELECT_DELAY;
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

    private void initDataStructForGuiName(String name) {
       int value = -1;
       if (name == extraLife)  value = Collectable.Types.LIFE;
       else if (name == weapon)  value = Collectable.Types.WEAPON;
       else if (name == armour)  value = Collectable.Types.ARMOUR;
       else if (name == engine)  value = Collectable.Types.ENGINE;
       else if (name == mine)  value = Collectable.Types.MINE;
       else if (name == radar)  value = Collectable.Types.RADAR;
       else if (name == aiturret)  value = Collectable.Types.AI_TURRET;
       else if (name == random)  value = Collectable.Types.RANDOM;
       else if (name == money)  value = Collectable.Types.MONEY_1;  //not need
       objectData = new ObjectDataStruct(value, io.itch.mgdsstudio.battlecity.game.gameobjects.Collectable.class.getSimpleName());
    }


   @Override
    protected void initDataForStatement(int actualStatement) {
        initGui();
    }

    @Override
    protected void onBackPressed(){
        if (actualStatement == START_STATEMENT) editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
        else if (actualStatement == Statements.SELECT_TYPE) editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
        else {
            if (actualStatement == Statements.SELECT_DELAY) nextStatement = Statements.SELECT_TYPE;
            else if (actualStatement == Statements.SELECT_VALUE) nextStatement = Statements.SELECT_TYPE;
            else if (actualStatement == Statements.PLACE_ON_MAP) nextStatement = Statements.SELECT_DELAY;
        }
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
            EntityData entityData = new EntityData(valuesArray, new GraphicData[]{}, id);
            Logger.debug("Entity data will content: " + values + "; ID: " + id );
            return entityData;
        }

        public void addValueToStart(int x) {
            values.add(0,x);
        }
    }
    
  }
