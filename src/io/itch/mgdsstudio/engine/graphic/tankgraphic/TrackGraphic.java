package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.*;
import processing.core.PApplet;
import processing.core.PGraphics;

class TrackGraphic extends GraphicPart{

    private interface AnimationStatements{
        int PAUSE_ALL = 0;
        int LEFT_PAUSE_RIGHT_ROTATE_FORWARD = 1;
        int RIGHT_PAUSE_LEFT_ROTATE_FORWARD = 2;
        int LEFT_ROTATE_HALF_RIGHT_ROTATE_FULL_FORWARD = 3;
        int RIGHT_ROTATE_HALF_LEFT_ROTATE_FULL_FORWARD = 4;
        int ALL_FORWARD = 5;
        int ALL_BACKWARD = 6;
    }

    private int animationStatement = AnimationStatements.PAUSE_ALL;
    private boolean side;
    private float maxForward, maxBackward;
    private float maxUpdatingFrequency;
    private float actualVelocity;

    //private AnimationInGame leftTrack, rightTrack;


    private int maxAnimationFramerateForForward = 12;
    private int maxAnimationFramerateForBackward = 7;
    private int minAnimationFramerate = 2;
    private final static float STOP_ANIMATION_FRAMERATE = 4;


    private float prevFreq, freq;
    private final static boolean LEFT = true;
    private final static boolean RIGHT = false;

    private SimpleSpriteAnimationController leftTrackGraphicController, rightTrackGraphicController;
    private ImageZoneSimpleData [] singleSpriteZones;

    TrackGraphic(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, boolean side) {
        super(parent, tank, engine, relativeGraphicScale);
        this.side = side;
        if (side == true){
            imageZoneSimpleData = PLAYER_BASIC_LEFT_TRACK;
        }
        else imageZoneSimpleData = PLAYER_BASIC_RIGHT_TRACK;
        maxForward = tank.getForwardMaxVelocityInWorldUnits();
        maxBackward = tank.getBackwardMaxVelocityInWorldUnits();
        leftTrackGraphicController = new SimpleSpriteAnimationController(3, 1, engine.getEngine().millis());
        rightTrackGraphicController = new SimpleSpriteAnimationController(3, 1, engine.getEngine().millis());
        singleSpriteZones = new ImageZoneSimpleData[3];
        singleSpriteZones[0] = PLAYER_LEFT_TRACK_0;
        singleSpriteZones[1] = PLAYER_LEFT_TRACK_1;
        singleSpriteZones[2] = PLAYER_LEFT_TRACK_2;
        Logger.correct(" I need to change animation frequency in according to real tank velocities. ");
    }

    @Override
    void draw(PGraphics graphics, GameCamera gameCamera){
        update(graphics.parent.millis());
        imageZoneSimpleData  = singleSpriteZones[leftTrackGraphicController.getActualNumber()];
        super.draw(graphics, gameCamera);
        graphics.scale(1,-1);
        imageZoneSimpleData  = singleSpriteZones[rightTrackGraphicController.getActualNumber()];
        super.draw(graphics, gameCamera);
        graphics.scale(1,-1);
    }

    private void update(long time) {
        float angular = tank.getBody().getAngularVelocity();
        float linear = tank.getBody().getLinearVelocity().length();
        boolean directionForward = tank.getTankController().isLastMovementDirectionForward();
        setAnimationStatement(angular, linear, directionForward);
        prevFreq = freq;
        changeAnimation(time);
        //Logger.debug("Angular: " + angular + "; Linear: " + linear + "; Forward: " + directionForward + "; Statement: " + animationStatement);
        leftTrackGraphicController.update(time);
        rightTrackGraphicController.update(time);
    }

    private void setGraphicAllForward(long time){
        int framerate = getFramerateForForwardAnimation();
        if (leftTrackGraphicController.isStatementForward()) {
            leftTrackGraphicController.setFramesPerSec(framerate, time);
        }
        else leftTrackGraphicController.startForwardAnimation(framerate, time);
        if (rightTrackGraphicController.isStatementForward()) {
            rightTrackGraphicController.setFramesPerSec(framerate, time);
        }
        else rightTrackGraphicController.startForwardAnimation(framerate, time);
    }

    private void setGraphicAllBackward(long time){
        int framerate = getFramerateForBackwardAnimation();
        if (leftTrackGraphicController.isStatementBackward()) {
            leftTrackGraphicController.setFramesPerSec(framerate, time);
        }
        else leftTrackGraphicController.startReverseAnimation(framerate, time);
        if (rightTrackGraphicController.isStatementBackward()) {
            rightTrackGraphicController.setFramesPerSec(framerate, time);
        }
        else rightTrackGraphicController.startReverseAnimation(framerate, time);
    }

    private void changeAnimation(long time) {
        if (animationStatement == AnimationStatements.PAUSE_ALL){
            pauseAnimation(LEFT);
            pauseAnimation(RIGHT);
        }
        else if (animationStatement == AnimationStatements.ALL_FORWARD){
            setGraphicAllForward(time);
        }
        else if (animationStatement == AnimationStatements.ALL_BACKWARD){
            setGraphicAllBackward(time);
        }
        else if (animationStatement == AnimationStatements.LEFT_PAUSE_RIGHT_ROTATE_FORWARD){
            int framerate = getFramerateForForwardAnimation();
            leftTrackGraphicController.pauseAnimation();
            if (rightTrackGraphicController.isStatementForward()) {
                rightTrackGraphicController.setFramesPerSec(framerate, time);
            }
            else rightTrackGraphicController.startForwardAnimation(framerate, time);
        }
        else if (animationStatement == AnimationStatements.RIGHT_PAUSE_LEFT_ROTATE_FORWARD){
            int framerate = getFramerateForForwardAnimation();
            rightTrackGraphicController.pauseAnimation();
            if (leftTrackGraphicController.isStatementForward()) {
                leftTrackGraphicController.setFramesPerSec(framerate, time);
            }
            else leftTrackGraphicController.startForwardAnimation(framerate, time);
        }
        else if (animationStatement == AnimationStatements.LEFT_ROTATE_HALF_RIGHT_ROTATE_FULL_FORWARD){
            int framerateRight = getFramerateForForwardAnimation();
            int framerateLeft = framerateRight/2;
            if (leftTrackGraphicController.isStatementForward()) {
                leftTrackGraphicController.setFramesPerSec(framerateLeft, time);
            }
            else leftTrackGraphicController.startForwardAnimation(framerateLeft, time);
            if (rightTrackGraphicController.isStatementForward()) {
                rightTrackGraphicController.setFramesPerSec(framerateRight, time);
            }
            else rightTrackGraphicController.startForwardAnimation(framerateRight, time);
        }
        else if (animationStatement == AnimationStatements.RIGHT_ROTATE_HALF_LEFT_ROTATE_FULL_FORWARD){
            int framerateLeft = getFramerateForForwardAnimation();
            int framerateRight = framerateLeft/2;
            if (leftTrackGraphicController.isStatementForward()) {
                leftTrackGraphicController.setFramesPerSec(framerateLeft, time);
            }
            else leftTrackGraphicController.startForwardAnimation(framerateLeft, time);
            if (rightTrackGraphicController.isStatementForward()) {
                rightTrackGraphicController.setFramesPerSec(framerateRight, time);
            }
            else rightTrackGraphicController.startForwardAnimation(framerateRight, time);
        }
        else {
            Logger.error("No animation for this statement");
        }
    }

    private int getFramerateForBackwardAnimation() {
        return maxAnimationFramerateForBackward;
    }

    private int getFramerateForForwardAnimation() {
        return maxAnimationFramerateForForward;
    }

    private void setAnimationStatement(float angular, float linear, boolean directionForward) {
        if (!isRotating(angular)){
            if (!isMoving(linear)) {
                animationStatement = AnimationStatements.PAUSE_ALL;
            }
            else {
                if (directionForward){
                    animationStatement = AnimationStatements.ALL_FORWARD;
                }
                else animationStatement = AnimationStatements.ALL_BACKWARD;
            }
        }
        else {
            if (!isMoving(linear)){
                if (angular > 0){
                    animationStatement = AnimationStatements.RIGHT_PAUSE_LEFT_ROTATE_FORWARD;
                }
                else animationStatement = AnimationStatements.LEFT_PAUSE_RIGHT_ROTATE_FORWARD;
            }
            else {
                if (directionForward) {
                    if (angular > 0) {
                        animationStatement = AnimationStatements.RIGHT_ROTATE_HALF_LEFT_ROTATE_FULL_FORWARD;
                    } else animationStatement = AnimationStatements.LEFT_ROTATE_HALF_RIGHT_ROTATE_FULL_FORWARD;
                }
            }
        }
    }

    private boolean isMoving(float linear) {
        final float movingGraphicDeadZone = 0.05f;
        if (PApplet.abs(linear) > movingGraphicDeadZone){
            return true;
        }
        else return false;
    }

    private boolean isRotating(float angular) {
        final float rotatingGraphicDeadZone = 0.3f;
        if (PApplet.abs(angular) > rotatingGraphicDeadZone){
            return true;
        }
        else return false;
    }

    private void pauseAnimation(boolean side){
        if (side == LEFT){
            if (leftTrackGraphicController.isActive()){

                leftTrackGraphicController.pauseAnimation();
            }
        }
        else {
            if (rightTrackGraphicController.isActive()){
                rightTrackGraphicController.pauseAnimation();
            }
        }

    }


}
