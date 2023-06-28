package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

abstract class AbstractCheckableElement extends ElementWithCursor {
    protected boolean flagSet;
    protected final static float CURSOR_DIMENSIONS_COEF = 1.2f/(23f/36f);
    protected int framePosX, framePosY, frameHeight;

    AbstractCheckableElement(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, float cursorDimensionsCoef) {
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF );
    }

    protected final boolean isMouseOnEffectiveArea(int mouseX, int mouseY){
        return GameMechanics.isPointInRect(mouseX, mouseY, getCursorPosX()-frameHeight/2, getCursorPosY()-frameHeight/2, frameHeight, frameHeight);

    }

    @Override
    public final void draw(PGraphics graphic) {
        if (actualStatement != BLOCKED) {
            super.draw(graphic);
            if (!shiftingWasSet) {
                setYShiftingForFont(graphic.textFont);
                shiftingWasSet = true;
            }
            drawName(graphic, LEFT_ALIGNMENT_OS_SPECIFIC);
            drawCursor(graphic);
        }
        //System.out.println("Frame effective hight: " + height + " text height: " + graphic.textFont.getSize());

    }





    @Override
    protected void drawCursor(PGraphics graphics) {
        if (actualStatement != BLOCKED) {
            if (graphics != null) {
                graphics.pushStyle();
                graphics.imageMode(PApplet.CENTER);
                graphics.image(graphicFile.getImage(), getCursorPosX(), getCursorPosY(), frameHeight, frameHeight, getActualImageZOneData().leftX, getActualImageZOneData().upperY, getActualImageZOneData().rightX, getActualImageZOneData().lowerY);
                graphics.popStyle();
            }
        }
    }

    protected abstract ImageZoneSimpleData getActualImageZOneData();

    public void setChecked(boolean flagSet) {
        this.flagSet = flagSet;
    }

    public boolean isChecked(){
        return flagSet;
    }
}
