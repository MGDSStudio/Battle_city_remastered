package io.itch.mgdsstudio.battlecity.game.hud;


import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.RectButton;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

public class UpperPanelInGame extends Panel implements GlobalListener {

    private boolean levelEnded;

    private RectButton backButton;
    private MenuDataStruct menuDataStruct;


    public UpperPanelInGame(IEngine engine, InGameHud inGameHud, int restHeight, PGraphics graphics, Image image, PlayerTank playerTank) {
        super(engine, inGameHud, restHeight, image, playerTank.getId());
        init();
    }

    public void update(PlayerTank playerTank) {
        if (levelEnded){
            updateLevelEnding();
            //Logger.debugLog("Level ended");
            backButton.update(engine, playerTank);
            if (backButton.getReleasedStatement()){
                inGameHud.backToMenu(menuDataStruct);
            }
            //if (backButton.)
        }
    }

    private void updateLevelEnding() {
        if (engine.isRectAreaPressed(center, width, height)){
            Logger.debug("Back to menu");
            inGameHud.backToMenu(menuDataStruct);
        }
    }

    @Override
    protected void init() {
        leftUpper = new Coordinate(0,0);
        center = new Coordinate(width/2, height/2);
    }

    public void setLevelEnded(MenuDataStruct dataStruct) {
        levelEnded = true;
        backButton  = new RectButton(this, engine );
        backButton.addListener(this);
        this.menuDataStruct = dataStruct;
    }

    @Override
    public void appendCommand(GLobalSerialAction action) {
        if (action.getPrefix() == ActionPrefixes.EXIT_FROM_GAME){
            inGameHud.backToMenu(menuDataStruct);
        }
    }
}
