package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class NoTextCursorButton extends ButtonWithCursor{

    private int blinkTimes = 3;
    private boolean actualShownByBlinking = true;

    private final static float CURSOR_DIMENSIONS_COEF = 1.25f;
    //private final static float CURSOR_DIMENSIONS_COEF = 1.2f/(23f/36f);

    private String hiddenName;

    public NoTextCursorButton(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, createClearString(name), graphics);
        hiddenName = name;
        fontsDimensionRelationship = 0;
    }



    @Override
    public String getName(){
        return hiddenName;
    }

    @Override
    public void draw(PGraphics graphics){
        if (!hidden) {
            super.draw(graphics);
            if (!shiftingWasSet) {
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

    int getCursorPosX(){
        return (int)(leftX-height*1.0f);
    }

    @Override
    int getCursorPosY(){
        return (int) (upperY+(height/2f));
    }

    public void setActualShownByBlinking(boolean actualShownByBlinking) {
        this.actualShownByBlinking = actualShownByBlinking;
    }

    @Override
    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        Logger.error("This function must not be used: " + "setAnotherTextToBeDrawnAsName");
        /*super.setAnotherTextToBeDrawnAsName(anotherTextToBeDrawnAsName);
        //System.out.println("Dimensions for button must be recalculated");
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
        setWidth((int) ((float) width * coef));*/
    }
}
