package io.itch.mgdsstudio.battlecity.game.hud;

import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class Panel {
    protected Coordinate leftUpper, center;

    protected IEngine engine;
    protected final int width, height;
    protected final Image image;
    protected Hud inGameHud;
    protected int controlableObjectId;

    protected Panel(IEngine engine, Hud inGameHud, int height, Image image, int controlableObjectId) {
        this.engine = engine;
        this.height = height;
        this.width = engine.getEngine().width;
        this.image = image;
        this.inGameHud = inGameHud;
        this.controlableObjectId = controlableObjectId;
    }

    protected abstract void init();


    public int getHeight() {
        return height;
    }

    public void draw(PGraphics graphics){
        drawCleared(graphics);
    }

    protected void drawCleared(PGraphics graphics){
        graphics.imageMode(PConstants.CORNER);
        graphics.image(image.getImage(), leftUpper.x, leftUpper.y, width, height, HudConstants.BLACK_RECT.leftX, HudConstants.BLACK_RECT.upperY, HudConstants.BLACK_RECT.rightX, HudConstants.BLACK_RECT.lowerY );
        //System.out.println("Drawn at: " + leftUpper + " WxH: " + width + "x"+height);
        graphics.imageMode(PConstants.CENTER);
    }

    public void update(PlayerTank playerTank) {

    }

    public int getControlableObjectId() {
        return controlableObjectId;
    }

    public ArrayList <GlobalListener> getListeners(){
        return inGameHud.getListeners();
    }

    public int getWidth() {
        return width;
    }

    public Coordinate getCenter() {
        return center;
    }
}
