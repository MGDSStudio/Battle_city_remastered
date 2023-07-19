package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonInFrameWithGraphic;
import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.GuiElement;

import com.mgdsstudio.engine.nesgui.NoTextButtonWithFrameSelection;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;

public class Edit extends AbstractEditorMenu {

    private String select, copy, move, clearSelection, remove, back;


   private interface Statements{
         int SELECT = 11;
         int COPY = 21;
         int MOVE = 31;
         //int CLEARING = 22;  NO CLEARING STATEMENT
         int REMOVE_1 = 41;
    // int BACK = NO_BACK_ACTION
   }

    public Edit(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
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
      //select, copy, move, clearSelection, delete, back;
        select = "SELECT";
        copy = "COPY";
move = "MOVE";
       clearSelection = "CLEAR SELECTION";
        remove = "REMOVE";
         back = "BACK";

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
        //set specific language
        if (element.getName() == select){
            return "SELECT ONE OR MORE OBJECTS TO COPY OR DELETE THEM";
        }
        else if (element.getName() == copy){
            return "COPY SELECTED OBJECTS";
        }
        else if (element.getName() == move){
            return "MOVE SELECTED OBJECTS";
        }
        else if (element.getName() == clearSelection){
            return "CLEAR SELECTION";
        }
else if (element.getName() == remove){
            return "REMOVE SELECTED OBJECTS FROM GAME WORLD";
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
        if (element.getName().equals(back)) {
            onBackPressed();
        }
       else if (element.getName().equals(select)){
initSubmenu(Statements.SELECT);
      }
else if (element.getName().equals(copy)){
initSubmenu(Statements.COPY);
      }
else if (element.getName().equals(move)){
initSubmenu(Statements.MOVE);
}
else if (element.getName().equals(remove)){
removeSelectedObjects(element);
      }
      else if (element.getName().equals(clearSelection)){
clearSelection();
      }
    }

  protected void clearSelection(){
      Logger.debug("Not implemented");
       //! continue
  }

    @Override
    protected void initDataForStatement(int actualStatement) {
        Logger.debug("This menu has no statements");
    }


    @Override
    protected void onBackPressed(){
          editorController.transferToMenu(MenuType.EDIT, MenuType.MAIN);
    }
    
  }
