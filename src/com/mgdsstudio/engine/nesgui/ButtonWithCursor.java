package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class ButtonWithCursor extends ElementWithCursor {
    //public static int BUTTON_NORMAL_WIDTH = (int) (Program.engine.width/1.5f);
    private int blinkTimes = 3;
    private boolean actualShownByBlinking = true;

    private final static float CURSOR_DIMENSIONS_COEF = 1.2f/(23f/36f);



    public ButtonWithCursor(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF);
        //blinkTimer = new Timer();
    }

    public static void setAnotherCursor(ImageZoneSimpleData imageZoneSimpleData, PImage source){
        if (source != null){
            if (graphicFile != null && cursorImageZoneSimpleData != null){
                graphicFile.getImage().loadPixels();
                int [] sourcePixels = graphicFile.getImage().pixels;
                clearAreaInGraphic(sourcePixels, graphicFile.getImage().width, graphicFile.getImage().height, cursorImageZoneSimpleData);
                graphicFile.getImage().updatePixels();
                source.loadPixels();
                int [] newCursorSource = source.pixels;
                fillAreaWithGraphic(newCursorSource, imageZoneSimpleData, graphicFile.getImage(), cursorImageZoneSimpleData );

                System.out.println("this func is not ready right now");
            }
            else System.out.println("First of all you need to create some button with cursor. After that you can change the cursor");
        }
        else System.out.println("Cursor can not be changed. Image is null");
    }

    private static void fillAreaWithGraphic(int[] newCursorSource, ImageZoneSimpleData imageZoneSimpleData, PImage image, ImageZoneSimpleData cursorImageZoneSimpleData) {
    }

    private static void clearAreaInGraphic (int [] pixels, int width, int height, ImageZoneSimpleData data){

    }

    @Override
    public void draw(PGraphics graphics){
        if (!hidden) {
            super.draw(graphics);
            if (!shiftingWasSet) {
                setYShiftingForFont(graphics.textFont);
                shiftingWasSet = true;
            }

            if (isVisible()) {
                drawName(graphics);
            }
            drawCursor(graphics);
        }

    }

    public int getBlinkTimes(){
        return blinkTimes;
    }

    @Override
    protected void initGraphic(){
        super.initGraphic();
        if (cursorImageZoneSimpleData == null){
            cursorImageZoneSimpleData =  new ImageZoneSimpleData(0,0,32,32);
            lockImageZoneSimpleData = new ImageZoneSimpleData(45-16,0,62,32);
        }
    }

    @Override
    protected void updateFunction() {

    }

    @Override
    protected void drawCursor(PGraphics graphics){
        if (actualStatement == PRESSED || (actualShownByBlinking && actualStatement == RELEASED) || actualStatement == BLOCKED) {
            if (graphics != null) {
                graphics.pushStyle();
                graphics.imageMode(PApplet.CENTER);
                if (actualStatement == BLOCKED){
                    graphics.image(graphicFile.getImage(),  getCursorPosX(), getCursorPosY(), CURSOR_DIMENSIONS_COEF*height, CURSOR_DIMENSIONS_COEF*height, lockImageZoneSimpleData.leftX, lockImageZoneSimpleData.upperY, lockImageZoneSimpleData.rightX, lockImageZoneSimpleData.lowerY);
                }
                else {
                    graphics.image(graphicFile.getImage(),  getCursorPosX(), getCursorPosY(), CURSOR_DIMENSIONS_COEF*height, CURSOR_DIMENSIONS_COEF*height, cursorImageZoneSimpleData.leftX, cursorImageZoneSimpleData.upperY, cursorImageZoneSimpleData.rightX, cursorImageZoneSimpleData.lowerY);
                }
                graphics.popStyle();
            }
        }
    }

    public void setActualShownByBlinking(boolean actualShownByBlinking) {
        this.actualShownByBlinking = actualShownByBlinking;
    }

    @Override
    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        super.setAnotherTextToBeDrawnAsName(anotherTextToBeDrawnAsName);
        System.out.println("Dimensions for button must be recalculated");
        float coef = 1f;
        if (anotherTextToBeDrawnAsName.contains("\n")){
            int deviderPlace = anotherTextToBeDrawnAsName.indexOf("\n");
            if (deviderPlace >0) {
                int lengthBefore = deviderPlace-1;
                int lengthAfter = anotherTextToBeDrawnAsName.length()-1-(deviderPlace+1);
                int maxLength = lengthBefore;
                if (lengthAfter > lengthBefore) maxLength = lengthAfter;
                coef = (float) maxLength / (float) name.length();
            }
        }
        else {
            coef = (float) anotherTextToBeDrawnAsName.length() / (float) name.length();
            if (coef > 1) {

            }

        }
        setWidth((int) ((float) width * coef));
    }

}
