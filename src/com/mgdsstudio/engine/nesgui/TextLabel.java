package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PConstants;
import processing.core.PGraphics;

public class TextLabel extends GuiElement {
    private String prefix;

    public TextLabel(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics);
        resetStatement();
    }

    public TextLabel(IEngine engine, int centerX, int centerY, int width, int height, String name) {
        super(engine, centerX, centerY, width, height, name, engine.getProcessing().g);
        resetStatement();
    }

    protected void resetStatement(){
        actualStatement = ACTIVE;
        prevStatement = ACTIVE;
    }

    @Override
    public void draw(PGraphics graphic) {
        if (actualStatement != BLOCKED) {
            super.draw(graphic);
            drawName(graphic, PConstants.CENTER);
        }
    }

    @Override

    public void update(int mouseX, int mouseY){
        //super.update(mouseX, mouseY);
        //Nothing to update
    }


    @Override
    protected void updateFunction() {
        // Npthing to update
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
