package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PGraphics;

public abstract class ElementWithCursor extends GuiElement {
    protected float cursorPosX;
    protected float cursorPosY;
    private float cursorDimensionCoefficient;
    protected static ImageZoneSimpleData lockImageZoneSimpleData;
    protected static ImageZoneSimpleData cursorImageZoneSimpleData;
    protected float fontsDimensionRelationship = 23f/36f;

    public ElementWithCursor(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, float cursorDimensionCoefficient) {
        super(engine, centerX, centerY, width, height, name, graphics);
        cursorPosX = (int)(leftX-height*cursorDimensionCoefficient*2);
        cursorPosY = upperY+height/2;
        this.cursorDimensionCoefficient = cursorDimensionCoefficient;
    }

    @Override
    protected void initGraphic(){
        if (graphicFile == null) {
            super.initGraphic();
        }
    }

    protected abstract void drawCursor(PGraphics graphics);

    int getCursorPosX(){
        //return cursorPosX;
        return (int)(leftX-height*cursorDimensionCoefficient);
        //return leftX-height;
    }

    int getCursorPosY(){


        //return (int) (upperY+(height/2f));
        //return (int) (upperY);
        return (int) (upperY+(height/2f)/ fontsDimensionRelationship);
    }
}
