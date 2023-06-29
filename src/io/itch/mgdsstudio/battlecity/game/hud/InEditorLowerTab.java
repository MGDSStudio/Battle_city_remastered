package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.EightPartsFrameImage;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.InEditorGraphicData;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PVector;

import java.awt.*;

public class InEditorLowerTab extends InEditorFrame{
    //private int yFreeZone;
    private final static ImageZoneSimpleData zoneForGameWorld = new ImageZoneSimpleData(0,32,34,66);
    // private final static ImageZoneSimpleData zoneForGameWorld = new ImageZoneSimpleData(0,66, 153, 219);
    //
    private int width, height;

    public InEditorLowerTab(IEngine engine, EightPartsFrameImage inEditorGameWorldFrame, int yFreeZone) {
        super(engine);
        //this.yFreeZone = yFreeZone;
        float left = inEditorGameWorldFrame.getLeftUpperCorner().x;
        float upper = inEditorGameWorldFrame.getLeftUpperCorner().y+inEditorGameWorldFrame.getHeight()+yFreeZone;
        this.width = inEditorGameWorldFrame.getWidth();

        float lowerPoint = engine.getEngine().height-yFreeZone;
        this.height = (int) (lowerPoint-upper);
        PVector leftUpper = new PVector(left, upper);
        //this.yFreeZone = yFreeZone;
        initZoneFrame(leftUpper, yFreeZone);
    }

    @Override
    protected void initZoneFrame(PVector leftUpperCorner, float yFreeZone) {
        int basicWidth = InEditorGraphicData.theoreticalWidthOfFramesWithNoZomming/2;

        zoneFrame = new EightPartsFrameImage(GuiElement.getGraphicFile(), zoneForGameWorld, basicWidth, basicWidth, width, height, leftUpperCorner);
    }
}
