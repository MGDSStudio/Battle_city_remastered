package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ButtonWithFrameSelection extends ElementWithFrameSelection {
    private boolean centerAlignment;
    public ButtonWithFrameSelection(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF);
        init(centerX, centerY);
    }

    public ButtonWithFrameSelection(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, boolean centerAlignment) {
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF);
        init(centerX, centerY);
        this.centerAlignment = centerAlignment;

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
        if (centerAlignment) drawName(graphics, -1);
        else drawName(graphics);
    }

    @Override
    protected void updateFunction() {
    }
}
