package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.EightPartsFrameImage;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PVector;

public class InEditorGraphic {
    private EightPartsFrameImage zoneFrame;
    private final static ImageZoneSimpleData zoneForGameWorld = new ImageZoneSimpleData(0,66, 153, 219);



    private void initZoneFrame(IEngine engine, PVector leftUpperCorner) {
        int basicWidth = (int) (0.2f*(engine.getEngine().width * GuiElement.NES_SCREEN_X_RESOLUTION)/engine.getEngine().width);
        final float CURSOR_DIMENSIONS_COEF = 1.3f;
        int frameWidth = (int) (engine.getEngine().width*CURSOR_DIMENSIONS_COEF);
        int frameHeight = (int) (engine.getEngine().height*CURSOR_DIMENSIONS_COEF);
        zoneFrame = new EightPartsFrameImage(GuiElement.getGraphicFile(), zoneForGameWorld, basicWidth, basicWidth, frameWidth, frameHeight, leftUpperCorner);
    }
}
