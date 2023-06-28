package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PGraphics;
import processing.core.PVector;

abstract class ElementWithFrameSelection extends ElementWithCursor {
    protected final static ImageZoneSimpleData cursorImageZoneSimpleDataForActiveFrame = new ImageZoneSimpleData(0,32,34,66);
    protected final static ImageZoneSimpleData cursorImageZoneSimpleDataForBlockedFrame = new ImageZoneSimpleData(98, 256,132,288);

    protected final static float CURSOR_DIMENSIONS_COEF = 1.3f;
    protected EightPartsFrameImage activeFrame, closedFrame;

    public ElementWithFrameSelection(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, float cursorDimensionCoefficient) {
        super(engine, centerX, centerY, width, height, name, graphics, cursorDimensionCoefficient);
    }

    protected void init(int centerX, int centerY) {
        final int NES_SCREEN_X_RESOLUTION = 254;
        int basicWidth = (int) (0.2f*(engine.width * NES_SCREEN_X_RESOLUTION)/engine.width);
        int frameWidth = (int) (width*CURSOR_DIMENSIONS_COEF);
        int frameHeight = (int) (height*CURSOR_DIMENSIONS_COEF);
        activeFrame = new EightPartsFrameImage(graphicFile, cursorImageZoneSimpleDataForActiveFrame, basicWidth, basicWidth, frameWidth, frameHeight, new PVector(centerX-frameWidth/2, centerY-frameHeight/2));
        closedFrame= new EightPartsFrameImage(graphicFile, cursorImageZoneSimpleDataForActiveFrame, basicWidth, basicWidth, frameWidth, frameHeight, new PVector(centerX-frameWidth/2, centerY-frameHeight/2));
    }

    @Override
    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        super.setAnotherTextToBeDrawnAsName(anotherTextToBeDrawnAsName);
        System.out.println("Dimensions for frame must be recalculated");
        float coef = (float)anotherTextToBeDrawnAsName.length()/(float)name.length();
        activeFrame.setWidth((int)((float) activeFrame.getWidth()*coef));
    }

    @Override
    protected void initGraphic(){
        super.initGraphic();

    }

    @Override
    protected void drawCursor(PGraphics graphics){
        if (actualStatement == PRESSED) {
            if (graphics != null) {
                graphics.resetMatrix();
                activeFrame.draw(graphics);
                //System.out.println("Drawn");
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY){
        if (prevStatement != actualStatement) prevStatement = actualStatement;
        if (actualStatement != BLOCKED && actualStatement != HIDDEN){
            if (GameMechanics.isPointInRect(mouseX, mouseY, leftX, upperY, activeFrame.getWidth(), activeFrame.getHeight())){
                if (engine.mousePressed){
                    if (actualStatement != PRESSED) actualStatement = PRESSED;
                }
                else if (actualStatement == PRESSED){
                    actualStatement = RELEASED;
                }
                else if (actualStatement == RELEASED ){
                    actualStatement = ACTIVE;
                }
            }
            else{
                actualStatement = ACTIVE;
            }
        }
        updateFunction();
    }


    protected EightPartsFrameImage getFrameForActualStatement(){
        if (actualStatement == BLOCKED) return closedFrame;
        else return activeFrame;
    }

}
