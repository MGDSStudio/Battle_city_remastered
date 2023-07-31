package io.itch.mgdsstudio.battlecity.game.hud;

import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GamePartWithGameWorldAbstractController;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.graphic.Image;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class Hud {

    protected final ArrayList<GlobalListener> globalListeners = new ArrayList<>();
    protected GamePartWithGameWorldAbstractController gameController;
    protected FPS_HUD fps_hud;
    protected IEngine engine;
    protected PGraphics graphics;
    protected Image image;
    protected int fontSize;
    protected PFont font;

    protected float lowerHeight;
    protected float upperHeight;
    protected int graphicUpperPixel, graphicLowerPixel;
    protected int graphicLeftPixel, graphicRightPixel;

    public void draw(){
        graphics.beginDraw();
        graphics.clear();
        graphics.textFont = font;
        drawPanels();
        if (GlobalVariables.debug) fps_hud.showFrameRateWithRenderer(graphics);
        graphics.endDraw();
        engine.getEngine().image(graphics,engine.getEngine().width/2,engine.getEngine().height/2);
    }



    protected abstract void drawPanels();

    public ArrayList<GlobalListener> getListeners() {
        return globalListeners;
    }

    public int getGraphicUpperPixel() {
        return graphicUpperPixel;
    }

    public int getGraphicLowerPixel() {
        return graphicLowerPixel;
    }

    public int getGraphicLeftPixel() {
        return graphicLeftPixel;
    }

    public int getGraphicRightPixel() {
        return graphicRightPixel;
    }

    public void backToMenu(MenuDataStruct menuDataStruct) {
        gameController.backToMenu(menuDataStruct);
    }

    public void appendButtonsListener(GlobalListener gameProcessController) {
        globalListeners.add(gameProcessController);
    }

    public void setLevelEnded(MenuDataStruct dataStruct) {
        Logger.error("Function must not be called from editor");
    }

    public void appendGameRoundData(GameRound gameRound) {
    }

    public abstract Panel getLowerPanel();

    public abstract void update(GameRound gameRound);

    public abstract void initHudStartData(GameRound gameRound);

    public Image getImage() {
        return image;
    }
}
