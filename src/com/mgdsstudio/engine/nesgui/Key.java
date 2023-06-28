package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PApplet;
import processing.core.PGraphics;

class Key extends ButtonWithFrameSelection {
    private int singleWidth, singleHeight;

    Key(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics);
        init(centerX, centerY);
    }


    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        if (actualStatement == PRESSED || actualStatement == RELEASED) {
            graphics.pushStyle();
            graphics.imageMode(PApplet.CORNER);
            drawCursor(graphics);
            graphics.popStyle();
        }
        drawName(graphics);
    }

    @Override
    protected void updateFunction() {
    }

}
