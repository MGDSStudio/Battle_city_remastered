package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.GraphicElementInGame;
import io.itch.mgdsstudio.engine.graphic.debuggraphic.DebugGraphic;
import io.itch.mgdsstudio.engine.graphic.debuggraphic.DebugRect;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

public abstract class Entity extends GameElement{

    protected int [] graphicData;
    public final static boolean KILLED = true;
    public final static int ENTITY_NORMAL_DIM = 32;
    public static final int NO_ID = -9999;

    protected final static int IMMORTAL_LIFE = 9999;
    protected int life = IMMORTAL_LIFE;
    protected int maxLife = IMMORTAL_LIFE;
    protected Coordinate pos;
    protected int width, height;
    protected float angle;
    protected GraphicElementInGame graphicElementInGame;
    protected DebugGraphic debugGraphic;
    protected boolean firstUpdatingCompleted;
    protected int graphicLayer = GraphicObject.GraphicLayers.OBJECT_ABOVE_GROUND_LAYER;
    protected final IEngine engine;

    protected Entity(IEngine engine, Coordinate pos, int angle, int life, int width, int height){
        super();
        this.engine = engine;
        this.width = width;
        this.height = height;
        this.pos = pos;
        this.angle = angle;
        this.life = life;
        createDebugGraphic(engine);
    }



    protected void createDebugGraphic(IEngine engine) {
        debugGraphic = new DebugRect(this, engine.getProcessing());
    }

    public void update(GameRound gameRound, long deltaTime) {
        if (!firstUpdatingCompleted){
            firstUpdating(gameRound, deltaTime);
            firstUpdatingCompleted = true;
        }
    }

    public void draw(PGraphics graphics, Camera gameCamera) {
        if (GlobalVariables.debug) {
            if (graphicElementInGame == null) debugGraphic.draw(graphics, gameCamera);
            else {
                graphicElementInGame.drawWithTransformations(graphics, gameCamera, this);
            }
        }
    }

    protected void drawWithAlpha(PGraphics graphics, Camera gameCamera, int alpha ) {
        if (GlobalVariables.debug) {
            if (graphicElementInGame == null) debugGraphic.draw(graphics, gameCamera);
            else {
                graphics.pushStyle();
                graphics.tint(255,255,255, alpha);
                graphicElementInGame.drawWithTransformations(graphics, gameCamera, this);
                graphics.popStyle();
            }
        }
    }

    public Coordinate getPos() {
        return pos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAngle() {
        return angle;
    }



    public boolean executeAction(GLobalSerialAction action) {
        return true;
    }



    protected void firstUpdating(GameRound gameRound, long deltaTime){

    }

    public int getGraphicLayer() {
        return graphicLayer;
    }

    public int getLife() {
        return life;
    }

    public boolean mustBeAlwaysAbove() {
        return false;
    }

    public boolean inZone(Rectangle testArea) {
        Logger.correct("This InZone function test only simple tests");
        if ( PApplet.dist(pos.x, pos.y, (testArea.x+testArea.width/2), (testArea.x+testArea.height/2)) <= (PApplet.min(width, height)+PApplet.min(testArea.width, testArea.height))){
            return true;
        }
        else return  false;

    }
}
