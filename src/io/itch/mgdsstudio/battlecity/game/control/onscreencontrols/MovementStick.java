package io.itch.mgdsstudio.battlecity.game.control.onscreencontrols;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.hud.HudConstants;
import io.itch.mgdsstudio.battlecity.game.hud.Panel;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Map;

public class MovementStick extends Stick{

    private final float DEAD_ZONE_BACKWARD_MOVEMENT_HALF_ANGLE = 90;

    public interface TouchingStatementsForRotateZone{
        //int DOES_NOT_PRESSED = 0;
        int PRESSED_LEFT_FROM_AXIS = 1;
        int PRESSED_RIGHT_FROM_AXIS = 2;
        int PRESSED_IN_FRONT = 3;
    }

    public interface TouchingStatementsForRunAndRotateZone{
        int DOES_NOT_PRESSED = ActionPrefixes.DOES_NOT_TOUCHED;
        int PRESSED_LEFT_FROM_AXIS = ActionPrefixes.CCW;
        int PRESSED_RIGHT_FROM_AXIS = ActionPrefixes.CW;
        int PRESSED_IN_FRONT = ActionPrefixes.FORWARD;
        int PRESSED_IN_BACK = ActionPrefixes.BACKWARD;
    }


    public MovementStick(Panel panel, IEngine engine, Coordinate pos, int diameter, float angle) {
        super(panel, engine, pos, diameter, angle);
        initGraphic();
    }

    protected void initGraphic() {
        imagesData.put(NEUTRAL_ZONE, HudConstants.LEFT_STICK_NEUTRAL_ZONE);
        imagesData.put(CCW, HudConstants.LEFT_STICK_CCW);
        imagesData.put(CW, HudConstants.LEFT_STICK_CW);
    }



    @Override
    protected void rotateAndDrawSubgraphic(PGraphics graphics) {

        graphics.rotate(PApplet.radians(angle)+graphicAdditionalAngleInRad);
        for (Map.Entry singleImage : imagesData.entrySet()) {
            if (singleImage.getKey() == actualGraphic) {
                ImageZoneSimpleData data = (ImageZoneSimpleData) singleImage.getValue();
                graphics.image(image.getImage(), 0, 0, width, width, data.leftX, data.upperY, data.rightX, data.lowerY);
            }
        }
    }

    protected void writeDataToListeners(){
        if (getListeners().size()>0) {
            if (prevTouchedStatementForRingZone != touchedStatementForRingZone){
                GLobalSerialAction action = new GLobalSerialAction(ActionPrefixes.MOVEMENT_STICK_RUN_AND_ROTATION, touchedStatementForRingZone, panel.getControlableObjectId(), engine.getEngine().millis(), getCommandNumber());
                for (GlobalListener globalListener : getListeners()){
                    globalListener.appendCommand(action);
                }
            }
            if (prevTouchedStatementForCenterZone != touchedStatementForCenterZone) {
                GLobalSerialAction action = new GLobalSerialAction(ActionPrefixes.MOVEMENT_STICK_BODY_ROTATION, touchedStatementForCenterZone, panel.getControlableObjectId(), engine.getEngine().millis(), getCommandNumber());
                for (GlobalListener globalListener : getListeners()) {
                    globalListener.appendCommand(action);
                }
            }
        }
    }

    private boolean isTouchPlaceInSomeAngleZone(float angleToTouchPlace, float zoneWidthInDegrees, float zoneCenterLineAngle){
        float effectedStickAngle = zoneCenterLineAngle;
        if (zoneCenterLineAngle < 0){
            effectedStickAngle=360+effectedStickAngle;
        }
        else if (effectedStickAngle > 360) effectedStickAngle = effectedStickAngle-360;
        if  (PApplet.abs(effectedStickAngle-angleToTouchPlace)<zoneWidthInDegrees) {
            //Logger.debugLog("");
            return true;
        }
        else return false;
    }



    protected int calculateStatementForRingZone(float angleToTouchPlace){
        int touchedStatement;
        if (PApplet.abs(angle-angleToTouchPlace)<DEAD_ZONE_FORWARD_MOVEMENT_HALF_ANGLE) {
            touchedStatement = TouchingStatementsForRunAndRotateZone.PRESSED_IN_FRONT;
        }
        else {
            float backLineAngle = angle+180;
            if (isTouchPlaceInSomeAngleZone(angleToTouchPlace, DEAD_ZONE_BACKWARD_MOVEMENT_HALF_ANGLE, backLineAngle)) touchedStatement = TouchingStatementsForRunAndRotateZone.PRESSED_IN_BACK;
            else {
                touchedStatement = getSideForTouchPlace(angleToTouchPlace, backLineAngle, angle);
            }
        }
        return touchedStatement;
    }

    protected int calculateStatementForCenterZone(float angleToTouchPlace){
        int touchedStatement;
        float backLineAngle = angle+180;

            touchedStatement = getSideForTouchPlace(angleToTouchPlace, backLineAngle, angle);

        return touchedStatement;
    }

    @Override
    protected void updateRingZoneTouchStatement(float angleToTouchPlace) {
        touchedStatementForRingZone = calculateStatementForRingZone(angleToTouchPlace);
    }

    @Override
    protected void updateCenterZoneTouchStatement(float angleToTouchPlace) {
        touchedStatementForCenterZone = calculateStatementForCenterZone(angleToTouchPlace);
        //touchedStatementForCenterZone = calculateStatementForZone(angleToTouchPlace);
    }
}
