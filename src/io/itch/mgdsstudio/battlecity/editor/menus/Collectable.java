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

    private String weapon, armour, extraLife, engine;
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
        extraLife = "EXTRA LIFE"
        armour = "ENGINE";
        valueAddingField = "valueAddingField";
        apply = "NEXT";
        add = "ADD ON MAP";
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
//case (5): name =  clearSelection; break;

            
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

    }
    
  }
