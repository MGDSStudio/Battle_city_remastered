package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PGraphics;
import processing.core.PVector;

public class Frame extends GuiElement{
    protected final static ImageZoneSimpleData frameImageZoneSimpleDataWithoutBackground = new ImageZoneSimpleData(0, 462, 33, 495);
    protected EightPartsFrameImage frame;
    //protected float distanceBetweenTextAndFrameAlongY;
    public Frame(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, (height), name, graphics);
        //float frameHeight = height;
        init(centerX, centerY, height);
    }

    protected void drawFrame(PGraphics graphics) {
        if (actualStatement != HIDDEN) {
            if (graphics != null) {
                graphics.resetMatrix();
                frame.draw(graphics);
            }
        }
    }
    private void init(int centerX, int centerY, int frameHeight) {
        final int NES_SCREEN_X_RESOLUTION = 254;
        int basicWidth = (int) (0.2f * (engine.width * NES_SCREEN_X_RESOLUTION) / engine.width);
        int frameWidth = (width * 1);
        //distanceBetweenTextAndFrameAlongY = (frameHeight-height)/2;
        frame = new EightPartsFrameImage(graphicFile, frameImageZoneSimpleDataWithoutBackground, basicWidth, basicWidth, frameWidth, frameHeight, new PVector(centerX - frameWidth / 2, centerY - frameHeight / 2));
    }



    @Override
    public void draw(PGraphics graphic) {
        if (actualStatement != HIDDEN) {
            drawFrame(graphic);
        }
    }

    @Override
    protected void updateFunction() {

    }


}
