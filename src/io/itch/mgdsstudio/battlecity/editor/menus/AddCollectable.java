package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.editor.ISelectable;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Collectable;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;
import java.util.ArrayList;

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
    private String apply;
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
        initButtonNames();
        int buttons = 6;
        Rectangle [] zones = getCoordinatesForDefaultButtonsAlignment(buttons);
        for (int i = 0; i < buttons; i++){
            GuiElement gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g, true);
            guiElements.add(gui);
        }
    }

    private void initButtonNames(){
        weapon =  "WEAPON";
        armour = "ARMOUR";
        extraLife = "EXTRA LIFE";
        armour = "ENGINE";
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
             return "POWERFULL ENGINE WITH HIGHER MAX VELOCITY";
        }

        else return "NO DATA";
    }

    

    @Override
    protected void guiPressed(GuiElement element) {

    }

    //transfer in parent
    @Override
    protected void setConsoleTextForFirstButtonPressing(GuiElement element) {
        editorController.setTextInConcole(getTextForConsoleByPressedGui(element));
    }

    @Override
    protected void guiReleased(GuiElement element) {
         /*
        private String weapon, armour, extraLife, engine, mine, radar, aiturret, random, money;
    private String money1, money2, money3, money5, money10, money15, money20,money25, money30, money40, money50;
    private String valueAddingField, add;

         */
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
                GuiElement gui = getGuiByName("DATA_FIELD");
                TextLabel
                objectData = new ObjectDataStruct(io.itch.mgdsstudio.battlecity.game.gameobjects.Collectable.class.getSimpleName());

                nextStatement = Statements.PLACE_ON_MAP;
            }
            else Logger.debug("No data for this statement and button");
        }
    }

    private void initDataStructForGuiName(String name) {
       /*
       private String weapon, armour, extraLife, engine, mine, radar, aiturret, random, money;
        */
       int value = -1;
       if (name == extraLife)  value = Collectable.Types.LIFE;
       elsselect_value= weapon)  value = Collectable.Types.WEAPON;
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
        if (actualStatement == Statements.SELECT_DELAY){
            guiElements.clear();
            createSubmenuWithDigitKeyboard(true, DATA_FIELD);
        }
    }


    @Override
    protected void onBackPressed(){
          editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
    }

    class ObjectDataStruct{
        public ArrayList <Integer> values ;
        public int id;
        public String name;

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

    }
    
  }
