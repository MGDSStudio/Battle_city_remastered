package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonInFrameWithGraphic;
import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;

import java.awt.*;

public class File extends AbstractEditorMenu {

    private String save, clear, back, exit;
   private interface Statements{

   }

    public File(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui() 
    initButtonNames();
        int buttons = 4;
        Rectangle [] zones = getCoordinatesForFrameButtons(buttons, AlignmentType.ONE_COLUMN);
        for (int i = 0; i < buttons; i++){
            GuiElement gui = new ButtonInFrameWithGraphic(editorController.getEngine(), zones[i].x-zones[i].width/2, zones[i].y-zones[i].height/2, zones[i].width, zones[i].height, getNameForPos(i), getImageZoneForPos(i),0,editorController.getEngine().getEngine().g);
            guiElements.add(gui);
        }
    }

private void initButtonNames(){
    save = "SAVE";
   clear = "CLEAR"; 
    back = "BACK";
 exit = "EXIT";

}

    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  save; break;
            case (1): name =  clear; break;
            case (2): name =  back; break;
            case (2): name =  exit; break;
            default:  name = "No name"; break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        //set specific language
        if (element.getName() == save){
            return "SAVE CHANGES IN THE ACTUAL LEVEL";
        }
        else if (element.getName() == clear){
            return "CLEAR ALL THE LEVEL DATA";
        }
        else if (element.getName() == back){
            return "BACK IN PREVIOUS MENU";
        }
        else if (element.getName() == MainButtonsNames.EXIT.name()){
            return "CATEGORY: EXIT";
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
    if (element.getName().equals(MainButtonsNames.EXIT)){
         onBackPressed();
    
    }

    @Override
    protected void initDataForStatement(int actualStatement) {
        Logger.debug("This menu has no statements");
    }


    @Override
    protected void onBackPressed(){
          editorController.transferToPrevMenu();
    }

    
}
