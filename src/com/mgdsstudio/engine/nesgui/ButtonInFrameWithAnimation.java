package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.AnimationZoneFullData;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class ButtonInFrameWithAnimation  extends ButtonInFrameWithGraphic{
    protected ImageZoneSimpleData insideImageData;
    private ArrayList <ImageZoneSimpleData> imageZoneSimpleData;

    public ButtonInFrameWithAnimation(IEngine engine, int left, int upper, int width, int frameHeight, String name, AnimationZoneFullData imageData, int graphicAngle, PGraphics graphics) {
        super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        initImageZones(imageData);
        //this.insideImageData = imageData;
        initGraphicCommonData(graphics, frameHeight, graphicAngle);
    }

    public ButtonInFrameWithAnimation(IEngine engine, int left, int upper, int width, int frameHeight, String name, AnimationZoneFullData imageData, int graphicAngle, PGraphics graphics, Image externalImage) {
        super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        initImageZones(imageData);
        this.image = externalImage;
        initGraphicCommonData(graphics, frameHeight, graphicAngle);
    }

    private void initImageZones(AnimationZoneFullData imageData){


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
