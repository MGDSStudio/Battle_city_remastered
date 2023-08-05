package io.itch.mgdsstudio.battlecity.game.textes;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DissolvingAndUpwardsMovingText extends AbstractText{
    public final static float NORMAL_Y_VELOCITY = -0.03f;
    public final static int NORMAL_ALPHA_CHANGING_STEP = 40;
    public final static int NORMAL_DISSOLVING_TIME = 2500;
    public final static int NORMAL_TEXT_HEIGHT = 10;
    public final static int NORMAL_STAGES_NUMBER = 5;
    public final static int ONLY_TEXT = 0;
    public final static int MONEY = 1;
    public final static int MEDICAL_KIT = 2;
    public final static int AMMO = 10;
    // another values more than 10 are weapons


    //private static boolean fontUploaded;


    private int timeToNextAlphaChanging;
    private int alphaChangingStep;
    private int actualAlpha = 255;
    private boolean fullDissolve;
    private float yVelocity;
    private int valueType;


/*
    public DissolvingAndUpwardsMovingText(float actualX, float actualY, float yVelocity, String text, int timeToShow, int stagesNumber, int  valueType) {
        initBasicValues(actualX, actualY, yVelocity, text, valueType);
        init(timeToShow, stagesNumber);
        if (!fontUploaded){
            createFont();
        }
    }*/

    public DissolvingAndUpwardsMovingText(IEngine engine, Coordinate pos, String text) {
        super(engine, pos);
        valueType = 0;
        initBasicValues(pos.x, pos.y, yVelocity, text, valueType);
        init(NORMAL_DISSOLVING_TIME, NORMAL_STAGES_NUMBER);
        if (!fontUploaded){
            createFont();
        }
    }

    public DissolvingAndUpwardsMovingText(IEngine engine, Coordinate pos, String text, int color, float yVelocity) {
        super(engine, pos);
        valueType = 0;
        initBasicValues(pos.x, pos.y, yVelocity, text, valueType);
        init(NORMAL_DISSOLVING_TIME, NORMAL_STAGES_NUMBER);
        this.color = color;
        if (!fontUploaded){
            createFont();
        }
        Logger.debug("TEXT " + text + " MUST BE SHOWN with size: " + font.getSize());
    }

    public DissolvingAndUpwardsMovingText(IEngine engine, Coordinate pos, String text, int color) {
        super(engine, pos);
        valueType = 0;
        initBasicValues(pos.x, pos.y, yVelocity, text, valueType);
        init(NORMAL_DISSOLVING_TIME, NORMAL_STAGES_NUMBER);
        if (!fontUploaded){
            createFont();
        }
    }

/*
    public DissolvingAndUpwardsMovingText(float actualX, float actualY, float yVelocity, String text, int timeToShow, int stagesNumber, int  valueType, int color) {
        initBasicValues(actualX, actualY, yVelocity, text, valueType);
        init(timeToShow, stagesNumber);
        if (!fontUploaded){
            createFont();
        }
        this.color = color;
    }*/

    private void initBasicValues(float actualX, float actualY, float yVelocity, String text, int valueType){
        fontHeight = NORMAL_TEXT_HEIGHT;
        pos.x = actualX;
        pos.y = actualY;
        this.yVelocity = yVelocity;
        this.text = text;
        this.valueType = valueType;
    }

    private void init(int timeToShow, int stagesNumber) {
        timeToNextAlphaChanging = timeToShow/stagesNumber;
        timer = new Timer(timeToNextAlphaChanging, engine.getProcessing());
        alphaChangingStep = 255/stagesNumber;
    }







    private void updateActualPos(float deltaTime) {
        pos.y += (yVelocity * deltaTime);
        //Logger.debugLog("Pos cahgned:  " + pos.y);
        //System.out.println("New pos: " + actualY + " by delta: " +deltaTime + " and speed " + yVelocity);
    }

    private void updateActualTint(GameRound gameRound) {
        if (actualAlpha > 0){
            if (timer.isTime()){
                timer.setNewTimer(timeToNextAlphaChanging);
                actualAlpha-=alphaChangingStep;
            }
            if (actualAlpha<=0) {
                fullDissolve = true;
                gameRound.deleteObjectAfterActualLoop(this);
            }
        }
        else if (!fullDissolve) {
            fullDissolve = true;
            gameRound.deleteObjectAfterActualLoop(this);
        }
    }

    public boolean canBeDeleted(){
        return fullDissolve;
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
        if (!fullDissolve) {
            updateActualTint(gameRound);
            updateActualPos(deltaTime);
        }
    }

    public void draw(PGraphics objectsFrame, Camera gameCamera){

        if (!fullDissolve){
            objectsFrame.pushStyle();
            if (font == null) createFont();
            objectsFrame.textFont(font, fontHeight);
            objectsFrame.fill(color, actualAlpha);
            objectsFrame.textAlign(PApplet.CENTER, PApplet.BOTTOM);
            objectsFrame.text(text, gameCamera.getDrawPosX(pos.x), gameCamera.getDrawPosY(pos.y));
            objectsFrame.popStyle();
        }

    }



    public void draw(PGraphics graphics){
        if (!fullDissolve){
            graphics.pushStyle();
            if (font == null) createFont();
            graphics.textFont(font, fontHeight);
            graphics.fill(engine.getProcessing().color(255), actualAlpha);
            graphics.textAlign(PApplet.CENTER, PApplet.BOTTOM);
            graphics.text(text, pos.x,pos.y);
            graphics.popStyle();
        }
    }

    public boolean isMoneyText(){
        if (valueType == MONEY) return true;
        else return false;
    }

    public boolean isMedicalKitText(){
        if (valueType == MONEY) return true;
        else return false;
    }

    public boolean isAmmoText(){
        if (valueType >= AMMO) return true;
        else return false;
    }
}
