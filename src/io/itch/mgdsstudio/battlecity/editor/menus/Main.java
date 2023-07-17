package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonInFrameWithGraphic;
import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;

import java.awt.*;

public class Main extends AbstractEditorMenu {
    private enum MainButtonsNames {
        WALL, PLAYER_TANK, ENEMY_TANK, COLLECTABLE,
        GRAPHIC, STAFF, WATER, FOREST,
        FILE, EDIT , PREFERENCES, TEST;
    }

    private interface ImageZones{
        ImageZoneSimpleData WALL = new ImageZoneSimpleData(84,326, 118,360);
        ImageZoneSimpleData PREFERENCES = new ImageZoneSimpleData(119,326, 153,360);
        ImageZoneSimpleData GRAPHIC = new ImageZoneSimpleData(119,291, 153,325);
        ImageZoneSimpleData WATER = new ImageZoneSimpleData(84,291, 118,325);
        ImageZoneSimpleData PLAYER_TANK = new ImageZoneSimpleData(84,361, 118,393);
// not Set up
        ImageZoneSimpleData ENEMY_TANK = new ImageZoneSimpleData(119,429, 153,463);
        ImageZoneSimpleData COLLECTABLE = new ImageZoneSimpleData(84,395, 118,429);
        ImageZoneSimpleData STAFF = new ImageZoneSimpleData(84,463, 118,497);
        ImageZoneSimpleData FOREST = new ImageZoneSimpleData(84,429, 118,463);
        ImageZoneSimpleData FILE = new ImageZoneSimpleData(119,361, 153,393);
        ImageZoneSimpleData EDIT = new ImageZoneSimpleData(119,463, 153,497);
        ImageZoneSimpleData TEST = new ImageZoneSimpleData(119,395, 153,429);
    }

    private boolean userKnowsAboutUnsavedData;

    public Main(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui() {
        Rectangle [] zones = getCoordinatesForSquareButtonsAndColumnAlignment(12, 4);
        for (int i = 0; i < 12; i++){
            GuiElement gui = new ButtonInFrameWithGraphic(editorController.getEngine(), zones[i].x-zones[i].width/2, zones[i].y-zones[i].height/2, zones[i].width, zones[i].height, getNameForPos(i), getImageZoneForPos(i),0,editorController.getEngine().getEngine().g);
            guiElements.add(gui);
        }
    }



    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  MainButtonsNames.FILE.name(); break;
            case (1): name =  MainButtonsNames.EDIT.name(); break;
            case (2): name =  MainButtonsNames.PREFERENCES.name(); break;
            case (3): name =  MainButtonsNames.TEST.name(); break;
            case (4): name =  MainButtonsNames.PLAYER_TANK.name(); break;
            case (5): name =  MainButtonsNames.WALL.name(); break;
            case (6): name =  MainButtonsNames.COLLECTABLE.name(); break;
            case (7): name =  MainButtonsNames.ENEMY_TANK.name(); break;
            case (8): name =  MainButtonsNames.GRAPHIC.name(); break;
            case (9): name =  MainButtonsNames.STAFF.name(); break;
            case (10): name =  MainButtonsNames.WATER.name(); break;
            default:  name = MainButtonsNames.FOREST.name(); break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        //set specific language
        if (element.getName() == MainButtonsNames.FILE.name()){
            return "CATEGORY: FILE";
        }
        else if (element.getName() == MainButtonsNames.EDIT.name()){
            return "CATEGORY: EDIT";
        }
        else if (element.getName() == MainButtonsNames.PREFERENCES.name()){
            return "CATEGORY: PREFERENCES";
        }
        else if (element.getName() == MainButtonsNames.TEST.name()){
            return "TEST THE LEVEL";
        }
        else if (element.getName() == MainButtonsNames.PLAYER_TANK.name()){
            return "CATEGORY: PLAYER TANK";
        }
else if (element.getName() == MainButtonsNames.WALL.name()){
            return "CATEGORY: WALL PART";
        }
else if (element.getName() == MainButtonsNames.COLLECTABLE.name()){
            return "CATEGORY: COLLECTABLE OBJECT";
        }
else if (element.getName() == MainButtonsNames.ENEMY_TANK.name()){
            return "CATEGORY: ENEMY";
        }
else if (element.getName() == MainButtonsNames.GRAPHIC.name()){
            return "CATEGORY: GRAPHIC ELEMENT";
        }
else if (element.getName() == MainButtonsNames.STAFF.name()){
            return "CATEGORY: STAFF";
        }
else if (element.getName() == MainButtonsNames.WATER.name()){
            return "CATEGORY: WATER";
}

else if (element.getName() == MainButtonsNames.FOREST.name()){
            return "CATEGORY: FOREST";
        }





        






        else return "NO DATA";
    }

    private ImageZoneSimpleData getImageZoneForPos(int i) {
       ImageZoneSimpleData name = null;
       switch(i) {
            case (0): name =  ImageZones.FILE; break;
            case (1): name =  ImageZones.EDIT; break;
            case (2): name =  ImageZones.PREFERENCES; break;
            case (3): name =  ImageZones.TEST; break;
            case (4): name =  ImageZones.PLAYER_TANK; break;
            case (5): name =  ImageZones.WALL; break;
            case (6): name =  ImageZones.COLLECTABLE; break;
            case (7): name =  ImageZones.ENEMY_TANK; break;
            case (8): name =  ImageZones.GRAPHIC; break;
            case (9): name =  ImageZones.STAFF; break;
            case (10): name =  ImageZones.WATER; break;
            default:  name = ImageZones.FOREST; break;
        }
        return name;
        //return name;
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
    if (element.getName().equals(MainButtonsNames.TEST))
         onBackPressed();
    }

    @Override
    protected void initDataForStatement(int actualStatement) {
        Logger.debug("This menu has no statements");
    }


    @Override
    protected void onBackPressed(){
        if (!editorController.areThereUnsavedData()){
          editorController.exitFromEditor();
        }
        else if (userKnowsAboutUnsavedData){
            editorController.exitFromEditor();
        }
        else {
            tellAboutUnsavedData();
        }
    }

    private void tellAboutUnsavedData() {
       editorController.setTextInConcole("You have unsaved data that will be lost after transfer in the menu. If you need to save the data - select EDIT button and choose punct - SAVE. If you need to leave the editor without saving - press back button again" );
       userKnowsAboutUnsavedData = true;
    }
}
