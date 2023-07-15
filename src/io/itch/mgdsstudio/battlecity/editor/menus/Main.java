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

    }


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

    private ImageZoneSimpleData getImageZoneForPos(int i) {

       /* switch(i) {
            case (0): name =  ImageZones.FILE.name(); break;
            case (1): name =  ImageZones.EDIT.name(); break;
            case (2): name =  ImageZones.PREFERENCES.name(); break;
            case (3): name =  ImageZones.EXIT.name(); break;
            case (4): name =  ImageZones.PLAYER_TANK.name(); break;
            case (5): name =  ImageZones.WALL.name(); break;
            case (6): name =  ImageZones.COLLECTABLE.name(); break;
            case (7): name =  ImageZones.ENEMY_TANK.name(); break;
            case (8): name =  ImageZones.GRAPHIC.name(); break;
            case (9): name =  ImageZones.STAFF.name(); break;
            case (10): name =  ImageZones.WATER.name(); break;
            default:  name = ImageZones.FOREST.name(); break;
        }*/
        return ImageZones.WALL;
        //return name;
    }

    @Override
    protected void guiPressed(GuiElement element) {

    }

    @Override
    protected void guiReleased(GuiElement element) {

    }
}
