package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.EightPartsFrameImage;
import io.itch.mgdsstudio.battlecity.game.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.engine.graphic.Image;
import processing.core.PConstants;
import processing.core.PGraphics;

public class InGameHud extends Hud{

    private UpperPanel upperPanel;
    private LowerPanel lowerPanel;



    public InGameHud(GamePartWithGameWorldAbstractController gameController, IEngine engine, int playerNumberInMultiplayerMode, boolean singleplayer ) {
        this.engine = engine;
        this.gameController = gameController;
        initGraphic();
        lowerHeight = InGameGraphicData.lowerHeight;
        upperHeight = InGameGraphicData.upperHeight;
        initFont();
        boolean server = true;
        if (playerNumberInMultiplayerMode>GlobalConstants.PLAYER_AS_SERVER) server = false;
        fps_hud = new FPS_HUD(engine, GlobalVariables.getRenderer(), GlobalVariables.is3D(), upperHeight*0.1f, upperHeight*0.1f+fontSize, 2, server, singleplayer);

    }

    private void appendPlayerToHud(PlayerTank playerTank){
        upperPanel = new UpperPanel(engine, this, (int)upperHeight, graphics, image, playerTank);
        lowerPanel = new LowerPanel(engine, this, (int)lowerHeight, image, playerTank);
    }

    private void initFont() {
        fontSize = (int) (upperHeight/4f);
        font = engine.getEngine().loadFont(engine.getPathToObjectInAssets(GlobalConstants.SECONDARY_FONT));
        graphics.textFont = font;
    }

    private void initGraphic() {
        graphics = engine.getEngine().createGraphics(engine.getEngine().width, engine.getEngine().height, engine.getEngine().sketchRenderer());
        image = new Image(engine.getEngine(), engine.getPathToObjectInAssets(HudConstants.RELATIVE_PATH));
        graphics.rectMode(PConstants.CENTER);
    }

    protected void drawPanels(){
        upperPanel.draw(graphics);
        lowerPanel.draw(graphics);
    }

    public void update(GameRound gameRound) {
        PlayerTank playerTank = gameRound.getPlayer();
        upperPanel.update(playerTank);
        lowerPanel.update(playerTank);
    }

    @Override
    public void initHudStartData(GameRound gameRound) {
        PlayerTank playerTank = gameRound.getPlayer();
        int angle = (int) playerTank.getAngle();
        int turretAngle = (int) playerTank.getTurretAbsoluteAngle();
        getLowerPanel().setAngleForMovementStick(angle);
        getLowerPanel().setAngleForAimStick(turretAngle);
    }

    public LowerPanel getLowerPanel() {
        return lowerPanel;
    }

    private void appendMainGraphic(PGraphics graphics) {
        float restHeightForScreen = (engine.getEngine().height-upperHeight-lowerHeight);
        float restRelationshipForScreen = restHeightForScreen/engine.getEngine().width;
        graphicUpperPixel = 0;
        graphicLowerPixel = graphics.height;
        graphicLeftPixel = 0;
        graphicRightPixel = graphics.width;
        Logger.debug("Upper: " + graphicUpperPixel + " lower: " + graphicLowerPixel + " left: " + graphicLeftPixel + " right: " + graphicRightPixel + " rest relationship: " + restRelationshipForScreen );
    }

    public void appendGameRoundData(GameRound gameRound) {
        appendPlayerToHud(gameRound.getPlayer());
        appendMainGraphic(gameRound.getGraphics());
    }

    public void setLevelEnded(MenuDataStruct dataStruct) {
        upperPanel.setLevelEnded(dataStruct);
    }

    public GamePartWithGameWorldAbstractController getGameController() {
        return gameController;
    }


}
