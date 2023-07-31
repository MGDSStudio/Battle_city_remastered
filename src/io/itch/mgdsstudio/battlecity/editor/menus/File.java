package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonInFrameWithGraphic;
import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.GuiElement;

import com.mgdsstudio.engine.nesgui.NoTextButtonWithFrameSelection;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;

public class File extends AbstractEditorMenu {

    private String save, clear, exit;


   private interface Statements{
         int REALLY_WANT_TO_SAVE = 11;
         int SAVED = 12;
         int REALLY_WANT_TO_CLEAR = 21;
         int CLEARING = 22;
       
   }

    public File(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == START_STATEMENT) {
            initButtonNames();
            String [] names = new String[4];
            for (int i = 0; i < names.length; i++){
                names[i] = getNameForPos(i);
            }
            createSubmenuWithDefaultAlignedButtons(names);
        }
        /*else if (actualStatement == Statements.REALLY_WANT_TO_SAVE){
            String [] names = new String[2];
            names[0] = save;
            names[1] = back;
            createSubmenuWithDefaultAlignedButtons(names);
        }*/
        else if (actualStatement == Statements.SAVED){
            String [] names = new String[1];
            names[0] = back;
            createSubmenuWithDefaultAlignedButtons(names);
            saveData();

        }
    }

    private void saveData() {

        boolean success =  editorController.getUnsavedDataList().save();

        if (success) {
            editorController.setTextInConcole("DATA WAS SUCCESSFULLY SAVED!");
        }
        else editorController.setTextInConcole("NOT ALL THE DATA WAS SUCCESSFULLY SAVED");
    }


    private void initButtonNames(){
        save = "SAVE";
        clear = "CLEAR";
        exit = "EXIT";
    }

    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  save; break;
            case (1): name =  clear; break;
            case (2): name =  back; break;
            case (3): name =  exit; break;
            default:  name = "No name"; break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == save){
            return "SAVE CHANGES IN THE ACTUAL LEVEL";
        }
        else if (element.getName() == clear){
            return "CLEAR ALL THE LEVEL DATA";
        }
        else if (element.getName() == back){
            return "BACK IN PREVIOUS MENU";
        }
        else if (element.getName() == exit){
            return "LEAVE THE EDITOR";
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
        else if (element.getName().equals(save)) {
            nextStatement = Statements.SAVED;
        }
        //else if (element.getName().equals())
    }

    @Override
    protected void initDataForStatement(int actualStatement) {
        initGui();
    }


    @Override
    protected void onBackPressed(){
       if (actualStatement == START_STATEMENT || actualStatement == Statements.CLEARING || actualStatement == Statements.SAVED) editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
       else if (actualStatement == Statements.REALLY_WANT_TO_SAVE || actualStatement == Statements.REALLY_WANT_TO_CLEAR){
           nextStatement = START_STATEMENT;
       }
    }
    
}
