package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PGraphics;

public class CheckBox extends AbstractCheckableElement {

    private final static ImageZoneSimpleData clearFrameData = new ImageZoneSimpleData(0,32,34,66);
    private final static ImageZoneSimpleData selectedWithoutFlagFrameData = new ImageZoneSimpleData(34,32,68,66);
    private final static ImageZoneSimpleData flagSetFrameData = new ImageZoneSimpleData(68,32,102,66);
    private final static ImageZoneSimpleData selectedWithFlagFrameData = new ImageZoneSimpleData(85,221,119,255);



    //private int prevStatement;

    public CheckBox(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, boolean centerAlignment) {
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF, centerAlignment);
        framePosY = centerY;
        frameHeight = (int) (height*effectiveHeightCoef);
        framePosX = centerX;
        actualStatement = ACTIVE;
        prevStatement = ACTIVE;
    }


    @Override
    protected void updateFunction() {

    }





    @Override
    public void update(int x, int y){
        super.update(x,y);
        if (prevStatement == ACTIVE && actualStatement == PRESSED) {
            flagSet = !flagSet;
        }
    }



    protected ImageZoneSimpleData getActualImageZOneData(){
        if (actualStatement == PRESSED) {
            if (flagSet) return selectedWithFlagFrameData;
            else return selectedWithoutFlagFrameData;
        }
        else {
            if (flagSet) return flagSetFrameData;
            else return clearFrameData;
        }
    }
}
