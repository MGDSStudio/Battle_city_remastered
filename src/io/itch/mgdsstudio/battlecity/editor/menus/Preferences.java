package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.*;

import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferences;
import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferencesSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;

public class Preferences extends AbstractEditorMenu {

    private String grid, gridStep, gridShifting;
    private String textField;

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
            if (i == 0) {
                gui = new CheckBox(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g, true);
                EditorPreferencesSingleton preferencesSingleton = EditorPreferencesSingleton.getInstance();
                boolean gridVisible = preferencesSingleton.getBooleanValue(EditorPreferences.GRID_VISIBILITY.name());
                CheckBox checkBox = (CheckBox) gui;
                if (gridVisible){
                    editorController.getGrid().setVisible(true);
                    checkBox.setChecked(true);
                    editorController.setTextInConcole("GRID IS VISIBLE NOW");
                }
                else {
                    editorController.getGrid().setVisible(false);
                    checkBox.setChecked(false);
                    editorController.setTextInConcole("GRID IS NOT VISIBLE NOT");
                }
            }
            else gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g, true);
            guiElements.add(gui);
        }
    }

    private void initButtonNames(){
        grid = "GRID VISIBLE";
        gridStep = "GRID STEP";
        gridShifting = "GRID SHIFTING";
        textField = "TEXT FIELD";
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
            if (actualStatement == Statements.setGridStep){
                nextStatement = START_STATEMENT;
            }
            else if (actualStatement == Statements.setGridShifting){
                nextStatement = START_STATEMENT;
            }
            else if (actualStatement == START_STATEMENT) onBackPressed();
            else onBackPressed();
        }
        else if (element.getName().equals(gridStep)){
            nextStatement = Statements.setGridStep;
        }
        else if (element.getName().equals(gridShifting)){
            nextStatement = Statements.setGridShifting;
        }
        else if (element.getName().equals(grid)){
            if (element instanceof CheckBox){
                CheckBox box = (CheckBox)element;
                EditorPreferencesSingleton preferencesSingleton = EditorPreferencesSingleton.getInstance();
                if (box.isChecked()){
                    editorController.getGrid().setVisible(true);
                    preferencesSingleton.setValueForKey(EditorPreferences.GRID_VISIBILITY, 1);
                }
                else {
                    editorController.getGrid().setVisible(false);
                    preferencesSingleton.setValueForKey(EditorPreferences.GRID_VISIBILITY, 0);
                }
                preferencesSingleton.saveOnDisk();
                Logger.debug("Grid statement was changed");
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
            createSubmenuWithDigitKeyboard(false, textField);
        }
        if (actualStatement == Statements.setGridShifting){
            consoleText = "ENTER THE SHIFTING FOR THE GRID START POINT";
            createSubmenuWithDigitKeyboard(false, textField);
        }
        editorController.setTextInConcole(consoleText);

    }


    @Override
    protected void onBackPressed(){
          editorController.transferToMenu(MenuType.PREFERENCES, MenuType.MAIN);
    }
    
}
