package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.FrameWithMoveableText;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.RectButton;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.WorldBoard;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;
import sun.rmi.runtime.Log;

import java.awt.*;

public class UpperPanelInEditor extends Panel implements GlobalListener {
    private FrameWithMoveableText console;
    private LowerPanelInEditor lowerPanelInEditor;
    private final Rectangle worldZone;
    public UpperPanelInEditor(IEngine engine, Hud inGameHud, int restHeight, PGraphics graphics, Image image, LowerPanelInEditor lowerPanelInEditor, Rectangle worldZone) {
        super(engine, inGameHud, restHeight, image, Entity.NO_ID);
        this.lowerPanelInEditor  = lowerPanelInEditor;
        this.worldZone = worldZone;
        init();
    }

    public void update(PlayerTank playerTank) {
        console.update(engine.getEngine().mouseX, engine.getEngine().mouseY);
    }

    private void updateLevelEnding() {

    }

    @Override
    protected void init() {
        leftUpper = new Coordinate(0, 0);
        center = new Coordinate(width / 2, height / 2);
        float gapBetweenPanels = lowerPanelInEditor.leftUpper.y-(worldZone.y+worldZone.height);
        float consoleHeight = gapBetweenPanels/2;

        float gapToRightSide = engine.getEngine().width-(worldZone.x+worldZone.width);
        float width = engine.getEngine().width-2*gapToRightSide;
        Logger.debug("Upper height: " + height);
        //public FrameWithMoveableText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, String text) {
        console = new FrameWithMoveableText(engine, (int) center.x, (int) worldZone.y/2, (int) width, (int) consoleHeight, "Console", engine.getEngine().g, "WELCOME TO THE EDITOR");
    }

    public void setLevelEnded(MenuDataStruct dataStruct) {

    }

    @Override
    public void appendCommand(GLobalSerialAction action) {
        if (action.getPrefix() == ActionPrefixes.EXIT_FROM_GAME) {

        }
    }

    public void draw(PGraphics graphics){
        super.draw(graphics);
        console.draw(graphics);
    }

    public void setConsoleText(String newText) {
        console.setAnotherTextToBeDrawnAsName(newText);
    }
}