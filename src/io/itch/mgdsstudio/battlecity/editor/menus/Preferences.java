package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.*;

import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;

public class Preferences extends AbstractEditorMenu {

    private String grid, gridStep, gridShifting, back;


   private interface Statements{
         int setGridStep = 11;
         int setGridShifting = 12;
         //int REALLY_WANT_TO_CLEAR = 21;
         //int CLEARING = 22;
       
   }

    public Preferences (EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        initButtonNames();
        int buttons = 4;
        Rectangle [] zones = getCoordinatesForDefaultButtonsAlignment(buttons);
        
        for (int i = 0; i < buttons; i++){

            GuiElement gui ;
            if (i == 0) gui = new CheckBox(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g);
            
            else gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g, true);
            //GuiElement gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x-zones[i].width/2, zones[i].y-zones[i].height/2, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g);
            //
            //new NoTextButtonWithFrameSelection(engine, x, y, w, h, name, graphics);
            guiElements.add(gui);
        }
    }

    private void initButtonNames(){
      //grid, gridStep, gridShifting, back;
        grid = "GRID VISIBLE";
       gridStep = "GRID STEP";
        gridShifting = "GRID SHIFTING";
         back = "BACK";

    }

    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  grid; break;
            case (1): name =  gridStep; break;
            case (2): name =  gridShifting; break;
            case (3): name =  back; break;
            default:  name = "No name"; break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        //set specific language
        if (element.getName() == grid){
            return "MAKE GRID VISIBLE OR INVISIBLE";
        }
        else if (element.getName() == gridStep){
            return "CHANGE GRID CELLS SIZES";
        }
        else if (element.getName() == gridShifting){
            return "SHIFT GRID START POINT";
        }
        else if (element.getName() == back){
            return "BACK TO EDITOR MAIN PAGE";
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
        else if (element.getName().equals(gridStep)){
            changeStatement(Statements.setGridStep);
            
        }
        else if (element.getName().equals(gridShifting)){
            changeStatement(Statements.setGridShifting);
        }
        else if (element.getName().equals(grid)){
            if (element instanceof CheckBox){
                CheckBox box = (CheckBox)element;
                //if flag set
            }
        }
    }

    @Override
    protected void initDataForStatement(int actualStatement) {
        //Logger.debug("This menu has no statements");
        guiElements.clear();
        String consoleText = "";
        if (actualStatement == Statements.setGridStep){
            consoleText = "ENTER THE GRID STEP YOU WANT";
            createSubmenuWithDigitKeyboard(false);
        }
        if (actualStatement == Statements.setGridShifting){
            consoleText = "ENTER THE SHIFTING FOR THE GRID START POINT";
            createSubmenuWithDigitKeyboard(false);
        }
        editorController.setTextInConcole(getTextForConsoleByPressedGui(consoleText));

    }


    @Override
    protected void onBackPressed(){
          editorController.transferToMenu(MenuType.PREFERENCES, MenuType.MAIN);
    }
    
}
