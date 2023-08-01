package io.itch.mgdsstudio.battlecity.game.control.onscreencontrols;

import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;

import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.hud.HudConstants;
import io.itch.mgdsstudio.battlecity.game.hud.Panel;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.GraphicManager;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class OnScreenControl {

    protected float graphicAdditionalAngleInRad = 0;
    protected Panel panel;
    protected HashMap<Integer, ImageZoneSimpleData> imagesData;

    protected static Image image;
    protected Coordinate pos;
    protected int width, height;
    protected float angle;

    protected final static boolean ROUND = true;
    protected final static boolean RECTANGULAR = false;
    protected boolean form = RECTANGULAR;
    public final static boolean VISIBLE = true;
    protected boolean active = true;
    protected boolean pressedStatement, previousPressedStatement, previousVisibilityStatement = !VISIBLE, actualVisibilityStatement = VISIBLE;
    protected boolean mustBeRedrawnOnThisFrame;


    //protected Launcher launcher;
    //protected ArrayList<Listener> listeners;
    protected final IEngine engine;
    protected int controllableObjectId;

    public OnScreenControl(Panel panel, IEngine engine, Coordinate pos, boolean form, int w, int h, float angle) {
        //listeners = new ArrayList <>();
        imagesData = new HashMap<>();
        this.engine = engine;
        this.panel = panel;
        this.pos = pos;
        this.form = form;
        this.width = w;
        this.height = h;
        this.angle = angle;
        init(engine);
    }

    private void init(IEngine engine) {
        String path = engine.getPathToObjectInAssets(HudConstants.RELATIVE_PATH);
        if (image == null) image = GraphicManager.getManager(engine.getEngine()).getImage(path);
    }


    public void update(IEngine engine, PlayerTank playerTank){
        if (active){
            mustBeRedrawnOnThisFrame = false;
            previousPressedStatement = pressedStatement;
            if (active){
                if (form == RECTANGULAR){
                    if (engine.isRectAreaPressed(pos, width, height)) {
                        mustBeRedrawnOnThisFrame = true;
                        pressedStatement = true;
                    }
                }
                else {
                    if (engine.isCircleAreaPressed(pos, width)) {
                        mustBeRedrawnOnThisFrame = true;
                        pressedStatement = true;
                    }
                }
            }
            updatePlayerInteraction(engine);
        }
    }
    
    protected abstract void updatePlayerInteraction(IEngine engine);
    
    protected abstract void writeDataToListeners();
        
    
    /*
    protected void sendDataToListeners(TransferActionData data){
        for (Listener listener : getListeners()){
            listener.appendData(data);
        }
    }*/
    
    public void addListener(GlobalListener globalListener){
        getListeners().add(globalListener);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVisible(boolean actualVisibilityStatement) {
        this.actualVisibilityStatement = actualVisibilityStatement;
    }

    public void draw(PGraphics graphics){
        if (actualVisibilityStatement){
            graphics.pushMatrix();
            graphics.translate(pos.x, pos.y);
            updateActualGraphic();
            rotateAndDrawSubgraphic(graphics);
            graphics.popMatrix();
        }
    }

    protected abstract void updateActualGraphic();

    protected abstract void rotateAndDrawSubgraphic(PGraphics graphics);


    protected ArrayList <GlobalListener> getListeners(){
        return panel.getListeners();
    }

    public boolean getReleasedStatement() {
        if (pressedStatement == false && previousPressedStatement == true) return true;
        else return false;
    }
}
