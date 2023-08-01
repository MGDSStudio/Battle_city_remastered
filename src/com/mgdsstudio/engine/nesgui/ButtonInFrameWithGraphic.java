package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ButtonInFrameWithGraphic extends ButtonInFrameWithText{

    //private final static ImageZoneSimpleData openedImageZoneSimpleDataWithBlackBackground = new ImageZoneSimpleData(98, 254, 131, 289);
    private final static ImageZoneSimpleData selectedWithoutFlagFrameData = new ImageZoneSimpleData(34,32,68,66);

    private final ImageZoneSimpleData insideImageData;
    private Image image;
    private int graphicAngleInRad;
    final int graphicWidth, graphicHeight;
    private final static float GRAPHIC_SCALE = 0.72f;

    public ButtonInFrameWithGraphic(IEngine engine, int left, int upper, int width, int frameHeight, String name, ImageZoneSimpleData imageData, int graphicAngle, PGraphics graphics) {
        super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        this.insideImageData = imageData;
        this.graphicAngleInRad = (int) graphics.parent.radians(graphicAngle);
        float smallestSize = PApplet.min(width, frameHeight);
        graphicWidth = (int) (smallestSize*GRAPHIC_SCALE);
        graphicHeight = (int) (smallestSize*GRAPHIC_SCALE);
    }

    public ButtonInFrameWithGraphic(IEngine engine, int left, int upper, int width, int frameHeight, String name, ImageZoneSimpleData imageData, int graphicAngle, PGraphics graphics, Image externalImage) {
        super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        this.insideImageData = imageData;
        this.image = externalImage;
        this.graphicAngleInRad = (int) graphics.parent.radians(graphicAngle);
        float smallestSize = PApplet.min(width, frameHeight);
        graphicWidth = (int) (smallestSize*GRAPHIC_SCALE);
        graphicHeight = (int) (smallestSize*GRAPHIC_SCALE);
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
            if (image == null) graphic.image(graphicFile.getImage(), 0,0, graphicWidth, graphicHeight, insideImageData.leftX, insideImageData.upperY, insideImageData.rightX, insideImageData.lowerY);
            else graphic.image(image.getImage(), 0,0, graphicWidth, graphicHeight, insideImageData.leftX, insideImageData.upperY, insideImageData.rightX, insideImageData.lowerY);

            graphic.popMatrix();
        }
    }




}
