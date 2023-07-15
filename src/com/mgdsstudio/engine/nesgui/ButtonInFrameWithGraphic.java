package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ButtonInFrameWithGraphic extends ButtonInFrameWithText{

    //private final static ImageZoneSimpleData openedImageZoneSimpleDataWithBlackBackground = new ImageZoneSimpleData(98, 254, 131, 289);
    private final static ImageZoneSimpleData selectedWithoutFlagFrameData = new ImageZoneSimpleData(34,32,68,66);

    private final ImageZoneSimpleData insideImage;
    private int graphicAngleInRad;
    final int graphicWidth, graphicHeight;
    private final static float GRAPHIC_SCALE = 0.72f;

    public ButtonInFrameWithGraphic(IEngine engine, int left, int upper, int width, int frameHeight, String name, ImageZoneSimpleData imageData, int graphicAngle, PGraphics graphics) {
        super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        //super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        this.insideImage = imageData;
        this.graphicAngleInRad = (int) graphics.parent.radians(graphicAngle);

        float smallestSize = PApplet.min(width, frameHeight);
        graphicWidth = (int) (smallestSize*GRAPHIC_SCALE);
        graphicHeight = (int) (smallestSize*GRAPHIC_SCALE);
        //Logger.debug("Button sizes: " + this.width + "x" + height);
    }

    @Override
    protected void updateFunction() {

    }

    @Override
    ImageZoneSimpleData getFrameImageZoneSimpleData() {
        return selectedWithoutFlagFrameData;
    }

    @Override
    protected void drawData(PGraphics graphic, int side) {
        if (actualStatement != PRESSED && actualStatement != RELEASED){
            graphic.pushMatrix();
            graphic.rotate(graphicAngleInRad);
            graphic.translate(x,y);
            graphic.image(graphicFile.getImage(), 0,0, graphicWidth, graphicHeight, insideImage.leftX, insideImage.upperY, insideImage.rightX, insideImage.lowerY);
            graphic.popMatrix();
        }
    }




}
