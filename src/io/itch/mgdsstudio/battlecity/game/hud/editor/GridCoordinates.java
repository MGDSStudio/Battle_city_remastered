package io.itch.mgdsstudio.battlecity.game.hud.editor;

import com.mgdsstudio.engine.nesgui.GuiElement;
import com.mgdsstudio.engine.nesgui.TextLabel;
import io.itch.mgdsstudio.battlecity.editor.EditorAction;
import io.itch.mgdsstudio.battlecity.editor.EditorActionsListener;
import io.itch.mgdsstudio.battlecity.editor.EditorCommandPrefix;
import io.itch.mgdsstudio.battlecity.editor.EditorListenersManagerSingleton;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Grid;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.hud.InEditorGameWorldFrame;
import io.itch.mgdsstudio.battlecity.game.hud.InEditorHud;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class GridCoordinates implements EditorActionsListener {
    private TextLabel upperX, upperY, lowerY, rightX;
    private InEditorHud hud;
    private final static String clearName = "_____";
    private final static int MAX_VALUE = 99999;
    private final static int MIN_VALUE = -9999;
    private Grid grid;
    private boolean firstInit;
    private InEditorGameWorldFrame frame;
    private Image black;
    private final static ImageZoneSimpleData blackRect = new ImageZoneSimpleData(77,271,78,272);
    private boolean updatingMustBeStarted;
    public GridCoordinates(InEditorHud hud, IEngine engine, InEditorGameWorldFrame frame){
        this.frame = frame;
        float maxTextWidth = frame.getGameWorldZone().x;
        float gap = ((maxTextWidth*0.075f)/2f);
        maxTextWidth-=(2f*gap);
        float textHeight = maxTextWidth/4f;
        black = GuiElement.getGraphicFile();

        int centerX = frame.getGameWorldZone().x-frame.getGameWorldZone().x/2;
        int centerY = frame.getGameWorldZone().y;
        Logger.debug("Text sizes: " + textHeight + ";" + maxTextWidth + " drawn at: " + centerX + "x" + centerY + " but start for frame: " + frame.getGameWorldZone().x);
        upperX = new TextLabel(engine, centerX, centerY, (int)maxTextWidth, (int)textHeight, clearName);

        centerY = frame.getGameWorldZone().y+frame.getGameWorldZone().height;
        lowerY = new TextLabel(engine, centerX, centerY, (int)maxTextWidth, (int)textHeight, clearName);

        centerX = frame.getGameWorldZone().x;
        centerY = (int) (frame.getGameWorldZone().y-gap-upperX.getHeight()/2);
        upperY = new TextLabel(engine, centerX, centerY, (int)maxTextWidth, (int)textHeight, clearName);

        centerX = frame.getGameWorldZone().x+frame.getGameWorldZone().width;
        rightX = new TextLabel(engine, centerX, centerY, (int)maxTextWidth, (int)textHeight, clearName);

        EditorListenersManagerSingleton.getInstance().addAsListener(this);
    }

    public void draw(PGraphics graphics){
        drawBlackBackgrounds(graphics, upperX);
        drawBlackBackgrounds(graphics, upperY);
        drawBlackBackgrounds(graphics, lowerY);
        drawBlackBackgrounds(graphics, rightX);
        upperX.draw(graphics);
        lowerY.draw(graphics);
        rightX.draw(graphics);
        upperY.draw(graphics);
    }

    private void drawBlackBackgrounds(PGraphics graphics, GuiElement guiElement) {
        graphics.pushStyle();
        graphics.imageMode(PConstants.CORNER);
        graphics.image(black.getImage(),  guiElement.getLeftX(),  guiElement.getUpperY(), guiElement.getWidth(),  guiElement.getHeight(), blackRect.leftX, blackRect.upperY, blackRect.rightX, blackRect.lowerY);
        graphics.popStyle();
    }

    public void update(GameRound gameRound){
        if (!firstInit){
            initFirst(gameRound);
        }
        if (updatingMustBeStarted){
            int left = (int) (gameRound.getCamera().getPos().x-frame.getGameWorldZone().width/2);
            int right = (left+frame.getGameWorldZone().width/2);
            int upper = (int) (gameRound.getCamera().getPos().y-frame.getGameWorldZone().height/2);
            int lower = upper+frame.getGameWorldZone().height;
            changeValues(upper, this.upperX);
            changeValues(right, this.rightX);
            changeValues(lower, this.lowerY);
            changeValues(left, this.upperY);
            //Logger.editor("Grid coordinates were updated! ");
        }
    }

    private void changeValues(int text, TextLabel textLabel) {
        String name = null;
        if (text > MAX_VALUE) {
            name = ">" + MAX_VALUE;
           /* while (text > MAX_VALUE){
                text/=10;
            }*/
        }
        else if (text <(MIN_VALUE)) {
            name = "<" + MIN_VALUE;
            /*
            while (text < MIN_VALUE) {
                text /= 10;
            }*/
        }
        //textLabel.setAnotherTextToBeDrawnAsName(""+text);
        if (name != null) textLabel.setName(name);
        else textLabel.setName(""+text);
    }

    private void initFirst(GameRound gameRound) {
        ArrayList <Entity> gameObjects = gameRound.getEntities();
        for (Entity entity: gameObjects){
            if (entity instanceof Grid){
                this.grid = (Grid) entity;

            }
        }
        if (this.grid == null) {
            Logger.error("Can not save instance for grid");
        }
        firstInit = true;
    }

    @Override
    public void appendCommand(EditorAction action) {
        if (action.getPrefix() == EditorCommandPrefix.WORLD_SCROLLING_STARTED){
            updatingMustBeStarted = true;
            //Logger.editor("Action got by the " + this.getClass().getName() + " and must start updating at: " + black.getEngine().frameCount);
        }
        else if (action.getPrefix() == EditorCommandPrefix.WORLD_SCROLLING_ENDED){
            updatingMustBeStarted = false;
            //Logger.editor("Action got by the " + this.getClass().getName() + " and must end updating at: " + black.getEngine().frameCount);
        }

    }
}
