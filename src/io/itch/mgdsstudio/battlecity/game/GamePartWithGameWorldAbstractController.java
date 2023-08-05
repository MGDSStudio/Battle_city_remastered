package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.control.GameProcessController;
import io.itch.mgdsstudio.battlecity.game.dataloading.PlayerProgressControllerSingleton;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.net.Net;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public abstract class GamePartWithGameWorldAbstractController extends GamePart{


    protected GameRound gameRound;
    protected Hud hud;
    protected boolean singleplayer = true;
    protected int levelNumber;
    protected long deltaTime;
    protected boolean startDataInit = false;
    protected GameProcessController gameProcessController;
    protected long lastFrameTime;
    protected Net netController;
    protected DrawingGraphicPlaces drawingGraphicPlaces;
    public GamePartWithGameWorldAbstractController(IEngine engine, MainController mainController, int dif, int level, int playerNumberInMultiplayerMode, int playersConnected, boolean isEditor) {
        super(engine, mainController);
        this.levelNumber = level;
        deltaTime = engine.getProcessing().millis();
        difficulty = dif;
        PlayerProgressControllerSingleton.init(engine, PlayerProgressControllerSingleton.SINGLE_MISSIONS);
        initHud(playerNumberInMultiplayerMode);
        gameRound = new GameRound(this, level, difficulty, playersConnected, playerNumberInMultiplayerMode, isEditor);
    }

    protected abstract void initHud(int playerNumberInMultiplayerMode);

    public Hud getHud() {
        return hud;
    }

    public void setGameEnded(MenuDataStruct dataStruct) {
        hud.setLevelEnded(dataStruct);
    }

    protected void initDeltaTime(){
        lastFrameTime = engine.getProcessing().millis();
    }

    protected void updateConnecting() {

    }

    public GameRound getGameRound(){
        return gameRound;
    }

    public void beginContact(Contact contact) {
        gameRound.addNewBeginContact(contact);
    }


    public void endContact(Contact contact) {
        //gameRound.getCollisionsController().endContact(contact);
    }


    public void preSolve(Contact contact, Manifold manifold) {
        //gameRound.getCollisionsController().preSolve(contact, manifold);
    }


    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        //gameRound.getCollisionsController().postSolve(contact, contactImpulse);
    }

    public abstract void backToMenu(MenuDataStruct dataStruct);

    protected void initStartData() {
        gameProcessController.setLevelStartTimeForUsers(gameRound.getPlayer().getId(), engine.getProcessing().millis());
        startDataInit = true;

    }

    public abstract int getGraphicWidth();

    public abstract int getGraphicHeight();

    public abstract Camera createCamera(GameRound gameRound);

    public int getLevelNumber() {
        return levelNumber;
    }

    protected class DrawingGraphicPlaces{
        float centerX, centerY, width, height;

        protected DrawingGraphicPlaces(float centerX, float centerY, float width, float height) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.width = width;
            this.height = height;
        }

        protected  float getCenterX() {
            return centerX;
        }

        protected float getCenterY() {
            return centerY;
        }

        protected float getWidth() {
            return width;
        }

        protected  float getHeight() {
            return height;
        }
    }
}
