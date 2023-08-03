package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class ButtonInFrameWithGraphic extends ButtonInFrameWithText{

    protected Image image;
    protected final static ImageZoneSimpleData selectedWithoutFlagFrameData = new ImageZoneSimpleData(34,32,68,66);

    protected int graphicAngleInRad;
    protected int graphicWidth, graphicHeight;
    protected final static float GRAPHIC_SCALE = 0.72f;


    public ButtonInFrameWithGraphic(IEngine engine, int i, int i1, int width, float v, String name, PGraphics graphics) {
        super(engine, i,i1,width,v,name,graphics);
    }

    protected void initGraphicCommonData(PGraphics graphics, int frameHeight, int graphicAngle){
        this.graphicAngleInRad = (int) graphics.parent.radians(graphicAngle);
        float smallestSize = PApplet.min(width, frameHeight);
        graphicWidth = (int) (smallestSize*GRAPHIC_SCALE);
        graphicHeight = (int) (smallestSize*GRAPHIC_SCALE);
    }

    @Override
    ImageZoneSimpleData getFrameImageZoneSimpleData() {
        return selectedWithoutFlagFrameData;
    }

    @Override
    protected void updateFunction() {

    }
}
