package io.itch.mgdsstudio.battlecity.game.control.onscreencontrols;


import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
//import io.itch.mgdsstudio.battlecity.datatransfer.data.SerialAction;
//import io.itch.mgdsstudio.battlecity.datatransfer.listeners.Listener;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.hud.Panel;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.GameMechanics;
import processing.core.PApplet;

import java.util.ArrayList;

public abstract class Stick extends OnScreenControl{
    protected final float DEAD_ZONE_FORWARD_MOVEMENT_HALF_ANGLE = 5;
    protected final static float GRAPHIC_ADDITIONAL_ANGLE_IN_RAD = PApplet.HALF_PI;
    public final static int DOES_NOT_TOUCHED = ActionPrefixes.DOES_NOT_TOUCHED;
    public final static Integer NEUTRAL_ZONE = 0;
    public final static Integer CCW = 1;
    public final static Integer CW = 2;
    protected Integer actualGraphic = NEUTRAL_ZONE;
    private int deadZoneRadius, rotateZoneRadius;
    private final ArrayList<Coordinate> touchScreenPos = new ArrayList<>();

    protected int touchedStatementForCenterZone, touchedStatementForRingZone;
    protected int prevTouchedStatementForCenterZone, prevTouchedStatementForRingZone;


    public Stick(Panel panel , IEngine engine, Coordinate pos, int diameter, float angle) {
        super(panel, engine, pos, ROUND, diameter, diameter, angle);
        initDims(engine);
        touchScreenPos.add(new Coordinate(-1,-1));
        graphicAdditionalAngleInRad = PApplet.HALF_PI;
    }
    
    private void initDims(IEngine engine){
        deadZoneRadius = engine.getProcessing().width/100;
        rotateZoneRadius = (int)(width *0.65f/2f);
    }
    
    
    protected void updatePlayerInteraction(IEngine engine){
        engine.fillTouchesArray(touchScreenPos);
        resetPrevStatements();
        updateCenterZoneTap();
        updateRingZoneTap();
        writeDataToListeners();
    }

    protected abstract void writeDataToListeners();
        
    private void updateRingZoneTap(){
        for (Coordinate touch : touchScreenPos){
            if (PApplet.dist(touch.x, touch.y, pos.x, pos.y)<(width /2)){
                if (PApplet.dist(touch.x, touch.y, pos.x, pos.y)>rotateZoneRadius){
                    if (engine.getProcessing().mousePressed) {
                        updateRingZoneTouchStatement(GameMechanics.getAngleToPointInDegrees(pos.x, pos.y, touch.x, touch.y));
                        return;
                    }
                }
            }
        }
    }

    protected void updateActualGraphic() {
        if (touchedStatementForCenterZone == CCW) actualGraphic = CCW;
        else if (touchedStatementForCenterZone == CW) actualGraphic = CW;
        else actualGraphic = NEUTRAL_ZONE;
    }

    protected abstract void updateRingZoneTouchStatement(float angleToTouchPlace);

    private void updateCenterZoneTap(){
        for (Coordinate touch : touchScreenPos){
            if (PApplet.dist(touch.x, touch.y, pos.x, pos.y)<rotateZoneRadius){
                if (PApplet.dist(touch.x, touch.y, pos.x, pos.y)>deadZoneRadius){
                    if (engine.getProcessing().mousePressed || GlobalVariables.getOs() == GlobalConstants.ANDROID) {
                        updateCenterZoneTouchStatement(GameMechanics.getAngleToPointInDegrees(pos.x, pos.y, touch.x, touch.y));
                        return;
                    }
                }
            }
        }
    }

    protected abstract void updateCenterZoneTouchStatement(float angleToTouchPlace);

    public int getCenterZoneTouchStatement(){
        return touchedStatementForCenterZone;
    }
    
    public int getRingZoneTouchStatement(){
        return touchedStatementForRingZone;
    }
    
    
    private void resetPrevStatements(){
        prevTouchedStatementForRingZone = touchedStatementForRingZone;
        prevTouchedStatementForCenterZone = touchedStatementForCenterZone;
        touchedStatementForRingZone = DOES_NOT_TOUCHED;
        touchedStatementForCenterZone = DOES_NOT_TOUCHED;
    }

    public void update(IEngine engine, PlayerTank playerTank){
        super.update(engine, playerTank);
    }

    public void setAngle(int angle) {
        if (angle<0) {
            while (angle<0){
                angle+=360;
            }
        }
        else if (angle>360){
            while (angle > 360){
                angle-=360;
            }
        }
        this.angle = angle;
    }
    
    /*
    public boolean hasStartedToPressCenter(){
        if (prevCenterZoneTouched)
    }
    */

    protected int getSideForTouchPlace(float angleToTouchPlace, float lineBackAngle, float lineFrontAngle){
        if (lineBackAngle<360){
            if (angleToTouchPlace > lineFrontAngle && angleToTouchPlace < lineBackAngle){
                return CCW;
            }
            else return CW;
        }
        else {
            lineBackAngle = lineBackAngle-360;
            if (angleToTouchPlace > lineBackAngle && angleToTouchPlace < lineFrontAngle) return CW;
            else return CCW;
        }
    }

    protected int getCommandNumber(){
        return -1;
    }


}
