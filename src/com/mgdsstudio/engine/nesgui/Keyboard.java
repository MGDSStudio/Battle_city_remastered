package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

abstract class Keyboard extends ButtonWithFrameSelection {
    private DataFieldWithText dataField;
    protected static ImageZoneSimpleData cursorImageZoneSimpleData = new ImageZoneSimpleData(0,32,34,66);
    protected EightPartsFrameImage frame;
    private ArrayList<Key> keys;


    Keyboard(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, DataFieldWithText dataField) {
        super(engine, centerX, centerY, width, height, name, graphics);
        this.dataField = dataField;
        init(centerX, centerY);
    }

    protected void init(int centerX, int centerY) {
        final int NES_SCREEN_X_RESOLUTION = 254;
        int basicWidth = (int) (0.2f*(engine.width * NES_SCREEN_X_RESOLUTION)/engine.width);
        int frameWidth = (int) (width);
        int frameHeight = (int) (height);
        frame = new EightPartsFrameImage(graphicFile, cursorImageZoneSimpleData, basicWidth, basicWidth, frameWidth, frameHeight, new PVector(centerX-frameWidth/2, centerY-frameHeight/2));

    }

    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);

            graphics.pushStyle();
            graphics.imageMode(PApplet.CORNER);
            drawCursor(graphics);
            graphics.popStyle();

        drawName(graphics);
    }

}
