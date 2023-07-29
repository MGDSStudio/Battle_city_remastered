package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonInFrameWithGraphic;
import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.GuiElement;

import com.mgdsstudio.engine.nesgui.NoTextButtonWithFrameSelection;
import io.itch.mgdsstudio.battlecity.editor.ISelectable;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;
import java.util.ArrayList;

public class Collectable extends AbstractEditorMenu {

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

   // private String select

   private interface Statements{
         int SELECT_TYPE = 11;
         int SELECT_VALUE = 21;
         int SELECT_DELAY = 31;
         int PLACE_ON_MAP = 41;
   }

    public Collectable(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
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

        money1 = " 1;
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
//      //select, copy, move, clearSelection, delete, back;
        String name;
        switch(i) {
            case (0): name =  select; break;
            case (1): name =  copy; break;
            case (2): name =  move; break;
            case (3): name =  clearSelection; break;
            case (4): name =  remove; break;
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
    private String weapon, armour, extraLife, engine;
    private String valueAddingField, add;
    private String apply;
        */
       if (element.getName().equals(back)) {
            onBackPressed();
        }
       else if (element.getName().equals(weapon)){
         nextStatement =   Statements.SELECT;

      }
         else if (element.getName().equals(copy)){
         nextStatement =   Statements.COPY;
          }
            else if (element.getName().equals(move)){
         nextStatement =   Statements.MOVE;
    }
         else if (element.getName().equals(remove)){
          nextStatement = Statements.REMOVE;
      }
      else if (element.getName().equals(clearSelection)){
            clearSelection();
      }
    }

    private void removeSelectedObjects() {
       ArrayList < ISelectable> selected = editorController.getSelectedObjects();
       for (int i = selected.size()-1; i>= 0; i--){
           String sourceString = selected.get(i).getDataString();

       }
    }



    protected void clearSelection(){

  }

    @Override
    protected void initDataForStatement(int actualStatement) {
        Logger.debug("This menu has no statements");
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
    }
    
  }
