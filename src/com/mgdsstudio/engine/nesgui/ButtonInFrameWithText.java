package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PConstants;
import processing.core.PGraphics;

abstract class ButtonInFrameWithText extends FrameWithText {


    protected ButtonInFrameWithText(IEngine engine, int centerX, int centerY, int width, float textHeight, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, textHeight, name, graphics);
    }


    @Override
    public void draw(PGraphics graphic) {
        if (actualStatement != HIDDEN) {
            if (!fontInitialized) initFont(graphic);
            drawFrame(graphic);
            if (actualStatement != PRESSED && actualStatement != BLOCKED) drawData(graphic, RIGHT_ALIGNMENT_OS_SPECIFIC);
        }
    }
}
