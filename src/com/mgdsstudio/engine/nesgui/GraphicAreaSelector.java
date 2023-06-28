package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;

public class GraphicAreaSelector extends GuiElement{
    private interface GuiNames{
        String MOVE_LEFT = "ML";
        String SHIFT_LEFT_SIDE_LEFT = "LL";
        String SHIFT_LEFT_SIDE_RIGHT = "LR";

        String MOVE_RIGHT = "MR";
        String SHIFT_RIGHT_SIDE_LEFT = "RL";
        String SHIFT_RIGHT_SIDE_RIGHT = "RR";

        String MOVE_UP = "MU";
        String SHIFT_UPPER_SIDE_UP = "UU";
        String SHIFT_UPPER_SIDE_DOWN = "UD";

        String MOVE_DOWN = "MD";
        String SHIFT_BOTTOM_SIDE_UP = "DU";
        String SHIFT_BOTTOM_SIDE_DOWN = "DD";

        String ADD_ZOOM = "ZOOM +";
        String SUB_ZOOM = "ZOOM -";
    }

    GraphicAreaSelector(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics);
    }

    @Override
    protected void updateFunction() {

    }


}
