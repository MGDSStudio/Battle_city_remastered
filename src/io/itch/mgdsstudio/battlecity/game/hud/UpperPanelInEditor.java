package io.itch.mgdsstudio.battlecity.game.hud;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.RectButton;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

public class UpperPanelInEditor extends Panel implements GlobalListener {

    public UpperPanelInEditor(IEngine engine, Hud inGameHud, int restHeight, PGraphics graphics, Image image) {
        super(engine, inGameHud, restHeight, image, Entity.NO_ID);
        init();
    }

    public void update(PlayerTank playerTank) {

    }

    private void updateLevelEnding() {

    }

    @Override
    protected void init() {
        leftUpper = new Coordinate(0, 0);
        center = new Coordinate(width / 2, height / 2);
    }

    public void setLevelEnded(MenuDataStruct dataStruct) {

    }

    @Override
    public void appendCommand(GLobalSerialAction action) {
        if (action.getPrefix() == ActionPrefixes.EXIT_FROM_GAME) {

        }
    }
}