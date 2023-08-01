package io.itch.mgdsstudio.battlecity.game.control.onscreencontrols;


import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.hud.HudConstants;
import io.itch.mgdsstudio.battlecity.game.hud.Panel;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;
import processing.core.PGraphics;


import java.util.Map;

public class AimingStick extends Stick{
    public final static Integer BULLET = -1;
    public final static Integer NO_BULLET = -2;

    private boolean readyToShot = true;

    private interface TouchingStatementsForShotZone{
        int PRESSED = 1;
    }

    private interface TouchingStatementsForRotateZone{
        int DOES_NOT_PRESSED = ActionPrefixes.DOES_NOT_TOUCHED;
        int PRESSED_LEFT_FROM_AXIS = ActionPrefixes.CCW;
        int PRESSED_RIGHT_FROM_AXIS = ActionPrefixes.CW;
        int PRESSED_IN_FRONT = ActionPrefixes.FORWARD;
    }


    public AimingStick(Panel panel, IEngine engine, Coordinate pos, int diameter, float angle) {
        super(panel, engine, pos, diameter, angle);
        initGraphic();
    }

    protected void initGraphic() {
        imagesData.put(NEUTRAL_ZONE, HudConstants.RIGHT_STICK_NEUTRAL_ZONE);
        imagesData.put(CCW, HudConstants.RIGHT_STICK_CCW);
        imagesData.put(CW, HudConstants.RIGHT_STICK_CW);
        imagesData.put(BULLET, HudConstants.BULLET);
        imagesData.put(NO_BULLET, HudConstants.NO_BULLET);
    }


    @Override
    protected void rotateAndDrawSubgraphic(PGraphics graphics) {
        graphics.rotate(PApplet.radians(angle)+graphicAdditionalAngleInRad);
        for (Map.Entry singleImage : imagesData.entrySet()) {
            if (singleImage.getKey() == actualGraphic) {
                ImageZoneSimpleData data = (ImageZoneSimpleData) singleImage.getValue();
                graphics.image(image.getImage(), 0 ,0, width, width, data.leftX, data.upperY, data.rightX, data.lowerY);
                break;
            }
        }
        for (Map.Entry singleImage : imagesData.entrySet()) {
            if (singleImage.getKey() == BULLET && readyToShot) {
                ImageZoneSimpleData data = (ImageZoneSimpleData) singleImage.getValue();
                graphics.image(image.getImage(),0, 0, width, width, data.leftX, data.upperY, data.rightX, data.lowerY);
                return;
            }
            if (singleImage.getKey() == NO_BULLET && !readyToShot) {
                ImageZoneSimpleData data = (ImageZoneSimpleData) singleImage.getValue();
                graphics.image(image.getImage(), 0, 0, width, width, data.leftX, data.upperY, data.rightX, data.lowerY);
                return;
            }
        }
    }


    protected void writeDataToListeners(){
        if (getListeners().size()>0) {
            if (prevTouchedStatementForRingZone != touchedStatementForRingZone){
                GLobalSerialAction action = new GLobalSerialAction(ActionPrefixes.AIMING_STICK_TURRET_ROTATION, touchedStatementForRingZone, panel.getControlableObjectId(), engine.getEngine().millis(), getCommandNumber());
                //SerialAction action = new SerialAction(DataPrefixes.AIMING_STICK_TURRET_ROTATION, touchedStatementForRingZone, panel.getControlableObjectId(), engine.getEngine().millis());
                for (GlobalListener globalListener : getListeners()){
                    globalListener.appendCommand(action);
                }
            }
            if (prevTouchedStatementForCenterZone != touchedStatementForCenterZone) {
                GLobalSerialAction action = new GLobalSerialAction(ActionPrefixes.AIMING_STICK_SHOOTING, touchedStatementForCenterZone, panel.getControlableObjectId(), engine.getEngine().millis(), getCommandNumber());
                for (GlobalListener globalListener : getListeners()) {
                    globalListener.appendCommand(action);
                }
            }
        }
    }





    protected int calculateStatementForZone(float angleToTouchPlace){
        int touchedStatement;
        if (PApplet.abs(angle-angleToTouchPlace)<DEAD_ZONE_FORWARD_MOVEMENT_HALF_ANGLE) {
            touchedStatement = TouchingStatementsForRotateZone.PRESSED_IN_FRONT;
        }
        else {
            float backLineAngle = angle+180;
            touchedStatement = getSideForTouchPlace(angleToTouchPlace, backLineAngle, angle);
            //2 == CCW
            //Logger.debugLog("Side: " + touchedStatement + " back line angle: " + backLineAngle + " to touch place: " + angleToTouchPlace + " stick angle: " + angle);
        }
        return touchedStatement;
    }

    private int calculateStatementForCenterZone(float angleToTouchPlace){
        int touchedStatement = TouchingStatementsForShotZone.PRESSED;
        /*if (PApplet.abs(angle-angleToTouchPlace)<DEAD_ZONE_FORWARD_MOVEMENT_HALF_ANGLE) {
            touchedStatement = TouchingStatementsForRotateZone.PRESSED_IN_FRONT;
        }
        else {
            float backLineAngle = angle+180;
            touchedStatement = getSideForTouchPlace(angleToTouchPlace, backLineAngle, angle);
            //2 == CCW
            //Logger.debugLog("Side: " + touchedStatement + " back line angle: " + backLineAngle + " to touch place: " + angleToTouchPlace + " stick angle: " + angle);
        }*/
        return touchedStatement;
    }

    /*
    protected int calculateStatementForZone(float angleToTouchPlace){
        int touchedStatement;
        if (PApplet.abs(angle-angleToTouchPlace)<DEAD_ZONE_FORWARD_MOVEMENT_HALF_ANGLE) {
            touchedStatement = MovementStick.TouchingStatementsForRunAndRotateZone.PRESSED_IN_FRONT;!
        }
        else {
            float backLineAngle = angle+180;
            touchedStatement = getSideForTouchPlace(angleToTouchPlace, backLineAngle, angle);
            //2 == CCW
            //Logger.debugLog("Side: " + touchedStatement + " back line angle: " + backLineAngle + " to touch place: " + angleToTouchPlace + " stick angle: " + angle);
        }
        return touchedStatement;
    }
     */

    @Override
    protected void updateRingZoneTouchStatement(float angleToTouchPlace) {
        touchedStatementForRingZone = calculateStatementForZone(angleToTouchPlace);
    }

    @Override
    protected void updateCenterZoneTouchStatement(float angleToTouchPlace) {
        touchedStatementForCenterZone = calculateStatementForCenterZone(angleToTouchPlace);
    }
}
