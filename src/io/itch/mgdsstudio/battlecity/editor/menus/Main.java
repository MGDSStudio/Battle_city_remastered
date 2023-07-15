package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonInFrameWithGraphic;
import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;

import java.awt.*;

public class Main extends AbstractEditorMenu {
    private enum MainButtonsNames {
        WALL, PLAYER_TANK, ENEMY_TANK, COLLECTABLE,
        GRAPHIC, STAFF, WATER, FOREST,
        FILE, EDIT , PREFERENCES, EXIT;
    }

    private interface ImageZones{
        ImageZoneSimpleData WALL = new ImageZoneSimpleData(84,326, 118,360);
        ImageZoneSimpleData PREFERENCES = new ImageZoneSimpleData(119,326, 153,360);
        ImageZoneSimpleData GRAPHIC = new ImageZoneSimpleData(119,291, 153,325);
        ImageZoneSimpleData WATER = new ImageZoneSimpleData(84,291, 118,325);
        ImageZoneSimpleData PLAYER_TANK = new ImageZoneSimpleData(84,361, 118,393);
// not Set up
        ImageZoneSimpleData ENEMY_TANK = new ImageZoneSimpleData(119,429, 153,363);
        ImageZoneSimpleData COLLECTABLE = new ImageZoneSimpleData(84,395, 118,29);
        ImageZoneSimpleData STAFF = new ImageZoneSimpleData(84,463, 118,497);
        ImageZoneSimpleData FOREST = new ImageZoneSimpleData(84,429, 118,363);
        ImageZoneSimpleData FILE = new ImageZoneSimpleData(119,361, 153,393);
        ImageZoneSimpleData EDIT = new ImageZoneSimpleData(119,463, 153,497);
        ImageZoneSimpleData EXIT = new ImageZoneSimpleData(119,395, 153,29);
        

    }

    private GuiElement lastPressed = null;

    public Main(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui() {
        Rectangle [] zones = getCoordinatesForFrameButtons(12, AlignmentType.FOUR_COLUMNS);
        for (int i = 0; i < 5; i++){
            GuiElement gui = new ButtonInFrameWithGraphic(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), getImageZoneForPos(i),0,editorController.getEngine().getEngine().g);
            guiElements.add(gui);
        }
    }

    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  MainButtonsNames.FILE.name(); break;
            case (1): name =  MainButtonsNames.EDIT.name(); break;
            case (2): name =  MainButtonsNames.PREFERENCES.name(); break;
            case (3): name =  MainButtonsNames.EXIT.name(); break;
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
        else if (element.getName() == MainButtonsNames.EDIT.name()){
            return "CATEGORY: EDIT";
        }
        else if (element.getName() == MainButtonsNames.EDIT.name()){
            return "CATEGORY: EDIT";
        }
        else if (element.getName() == MainButtonsNames.EDIT.name()){
            return "CATEGORY: EDIT";
        }
        else return "NO DATA";
    }

    private ImageZoneSimpleData getImageZoneForPos(int i) {
       ImageZoneSimpleData name = null;
       switch(i) {
            case (0): name =  ImageZones.FILE; break;
            case (1): name =  ImageZones.EDIT; break;
            case (2): name =  ImageZones.PREFERENCES; break;
            case (3): name =  ImageZones.EXIT; break;
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
        if (!wasGuiPressedAlsoOnPrevFrame(element)){
             setConsoleText(getTextForConsoleByPressedGui(element));
        }
    }

    //transfer in parent
    protected boolean wasGuiPressedAlsoOnPrevFrame(GuiElement element){
        boolean wasLastButtonChanged = false;
        if (lastPressed == null){
             wasLastButtonChanged = true;
             lastPressed = element;
        }
        else {
             if (!lastPressed.equals(element)){
                 wasLastButtonChanged = true;
                 lastPressed = element;
             }
        }
        return !wasLastButtonChanged;
    }

    protected void setConsoleText(String text){
        editorController.setTextInConcole(text);
    }



    @Override
    protected void guiReleased(GuiElement element) {

    }
}
