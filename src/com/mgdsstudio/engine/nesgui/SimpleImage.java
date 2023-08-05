package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import processing.core.PGraphics;

public class SimpleImage extends GuiElement {
    private final Image image;

    public SimpleImage(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, String path) {
        super(engine, centerX, centerY, width, height, name, graphics);
        image = new Image(engine.getProcessing(), path);
    }

    @Override
    public void update(int mouseX, int mouseY){

    }

    @Override
    protected void updateFunction() {

    }

    @Override
    public void draw(PGraphics graphics) {
        graphics.image(image.getImage(), x, y, width, height);
    }
}
