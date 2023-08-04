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
    private ArrayList <ImageZoneSimpleData> imageZoneSimpleDatas;
    private Controller  controller;

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

    private void initImageZones(AnimationZoneFullData animData){
        imageZoneSimpleDatas = new ArrayList <>();
        ImageZoneSimpleData simpleData = animData.getData();
        int alongX = animData.getAlongX();
        int alongY = animData.getAlongY();
        int singleW = (simpleData.rightX-simpleData.leftX)/alongX;
        int singleH = (simpleData.lowerY-simpleData.upperY)/alongY;
        for (int i = 0; i < alongY; i++){
            for (int j = 0 ; j < alongX; j++){
                int l = simpleData.leftX+j*singleW;
                int u = simpleData.upperY+ i*singleH;
                int right = l + singleW;
                int down = u+ singleH;
                imageZoneSimpleDatas.add(new ImageZoneSimpleData(l,u, right, down));
            }
        }
        controller = new Controller(alongX*alongY-1, animData.getSpritesPerSecond());
    }

    @Override
    protected void drawData(PGraphics graphic, int side) {
        controller.update(graphic.parent.millis());
        if (actualStatement != PRESSED && actualStatement != RELEASED){
            graphic.pushMatrix();
            graphic.rotate(graphicAngleInRad);
            graphic.translate(x,y);
            ImageZoneSimpleData data = imageZoneSimpleDatas.get(controller.actual);
            if (image == null) graphic.image(graphicFile.getImage(), 0,0, graphicWidth, graphicHeight, data.leftX, data.upperY, data.rightX, data.lowerY);
            else graphic.image(image.getImage(), 0,0, graphicWidth, graphicHeight, data.leftX, data.upperY, data.rightX, data.lowerY);
            graphic.popMatrix();
        }
        
    }


private class Controller{
    private int actual, last;
    private int timeBetween, nextChangeTime;

    Controller(int last, int spritesPerSec){
    this.last = last;

    this.timeBetween = 1000/spritesPerSec;;

    }

        private void update(int time){
            if (time>nextChangeTime) transfer();
        }

        private void transfer(){
            actual++;
            if (actual > last) actual = 0;
            nextChangeTime+=timeBetween;
        }
    }

}
