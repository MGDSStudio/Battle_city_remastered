package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;

public class TextureSelectingZone extends GuiElement{



    TextureSelectingZone(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics);
    }

    @Override
    protected void updateFunction() {

    }
}
