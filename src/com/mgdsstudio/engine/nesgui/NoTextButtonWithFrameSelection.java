package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PApplet;
import processing.core.PGraphics;

public class NoTextButtonWithFrameSelection extends ElementWithFrameSelection {

    private String hiddenName;
    public NoTextButtonWithFrameSelection(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, createClearString(name), graphics, CURSOR_DIMENSIONS_COEF);
        init(centerX, centerY);
        hiddenName = name;
    }

    @Override
    public String getName(){
        return hiddenName;
    }


    @Override
    public void draw(PGraphics graphics){
        //super.draw(graphics);

        if (actualStatement != PRESSED && actualStatement != RELEASED) {
            graphics.pushStyle();
            graphics.imageMode(PApplet.CORNER);
            getFrameForActualStatement().draw(graphics);
            graphics.popStyle();
        }
        /*
        if (actualStatement == BLOCKED){
            graphics.pushStyle();
            graphics.tint(255,255,255,65);
            graphics.imageMode(PApplet.CORNER);
            activeFrame.draw(graphics);
            graphics.popStyle();
        }
        else if (actualStatement != PRESSED && actualStatement != RELEASED) {
            graphics.pushStyle();
            graphics.imageMode(PApplet.CORNER);
            activeFrame.draw(graphics);
            graphics.popStyle();
        }*/

    }

    @Override
    protected void updateFunction() {
    }


}
