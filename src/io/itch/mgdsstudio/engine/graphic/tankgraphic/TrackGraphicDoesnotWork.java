package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.TankController;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.graphic.SimpleSpriteAnimationController;
import processing.core.PGraphics;

class TrackGraphicDoesnotWork extends GraphicPart{
    private boolean side;
    private float maxForward, maxBackward;
    private float maxUpdatingFrequency;
    private float actualVelocity;

    //private AnimationInGame leftTrack, rightTrack;


    private float maxAnimationFramerateForForward = 12;
    private float maxAnimationFramerateForBackward = 7;
    private float minAnimationFramerate = 2;
    private final static float STOP_ANIMATION_FRAMERATE = 4;


    private float prevFreq, freq;
    private final static boolean LEFT = true;


    private SimpleSpriteAnimationController leftTrackGraphicController, rightTrackGraphicController;
    private ImageZoneSimpleData [] singleSpriteZones;

    TrackGraphicDoesnotWork(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale, boolean side) {
        super(parent, tank, engine, relativeGraphicScale);

        this.side = side;
        if (side == true){
            imageZoneSimpleData = PLAYER_BASIC_LEFT_TRACK;
        }
        else imageZoneSimpleData = PLAYER_BASIC_RIGHT_TRACK;
        maxForward = tank.getForwardMaxVelocityInWorldUnits();
        maxBackward = tank.getBackwardMaxVelocityInWorldUnits();
        //Image image = GraphicManager.getManager(engine.getEngine()).getImage(engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE));
       // leftTrack = new AnimationInGame(image, width, height, PLAYER_LEFT_TRACK_FULL, 1,3,0,2,10, 1, 0, AnimationInGame.PLAY_ALWAYS,0);
        //rightTrack = new AnimationInGame(image, width, height, PLAYER_LEFT_TRACK_FULL, 1,3,0,2,10, 1, 0, AnimationInGame.PLAY_ALWAYS,0);
        leftTrackGraphicController = new SimpleSpriteAnimationController(3, 1, engine.getEngine().millis());
        rightTrackGraphicController = new SimpleSpriteAnimationController(3, 1, engine.getEngine().millis());
        singleSpriteZones = new ImageZoneSimpleData[3];
        //ImageZoneSimpleData PLAYER_LEFT_TRACK_FULL = new ImageZoneSimpleData(0,685-685+81, 80,685+80-685+81+80*2);
        singleSpriteZones[0] = PLAYER_LEFT_TRACK_0;
        singleSpriteZones[1] = PLAYER_LEFT_TRACK_1;
        singleSpriteZones[2] = PLAYER_LEFT_TRACK_2;

        //leftTrackGraphicController.startForwardAnimation(1);
    }

    @Override
    void draw(PGraphics graphics, GameCamera gameCamera){
        update(graphics.parent.millis());
        imageZoneSimpleData  = singleSpriteZones[leftTrackGraphicController.getActualNumber()];
        //Logger.debug("Sprite: " +leftTrackGraphicController.getActualNumber() + " with zone: " + imageZoneSimpleData );
        super.draw(graphics, gameCamera);
        graphics.scale(1,-1);
        imageZoneSimpleData  = singleSpriteZones[rightTrackGraphicController.getActualNumber()];
        super.draw(graphics, gameCamera);
        graphics.scale(1,-1);
    }

    private void update(long time) {
        prevFreq = freq;
        int movementDirection = tank.getTankController().getLastMovingAction();
        boolean rotatingOnPlace = updateOnPlaceRotating(movementDirection, time);
        if (!rotatingOnPlace){
            float relativeVelocity;
            float maxAnimationFramerate;
            if (tank.getTankController().isLastMovementDirectionForward()) {
                relativeVelocity = (actualVelocity/tank.getForwardMaxVelocityInWorldUnits());
                maxAnimationFramerate  = maxAnimationFramerateForForward;
            }
            else {
                relativeVelocity = (actualVelocity/tank.getBackwardMaxVelocityInWorldUnits());
                maxAnimationFramerate  = maxAnimationFramerateForBackward;
            }
            if (relativeVelocity>1) relativeVelocity = 1f;
            freq = (relativeVelocity)*(maxAnimationFramerate - minAnimationFramerate)+ minAnimationFramerate;
            actualVelocity = tank.getBody().getLinearVelocity().length();
            if (relativeVelocity > 0.05f && relativeVelocity <= 1f) {
                if (freq <= STOP_ANIMATION_FRAMERATE) {
                    pauseAnimation(LEFT);
                    pauseAnimation(!LEFT);
                } else if ((int) prevFreq != (int) freq) {
                    updateAnimationForMovement(movementDirection, time);
                }
            } else {
                pauseAnimation(LEFT);
                pauseAnimation(!LEFT);
            }
        }
        leftTrackGraphicController.update(time);
        rightTrackGraphicController.update(time);
    }

    private void updateAnimationForMovement(int lastMovementDirection, long time){
        if (lastMovementDirection == TankController.MovementConstants.MOVE_FORWARD){
            if (leftTrackGraphicController.isStatementForward()) {
                leftTrackGraphicController.setFramesPerSec((int) freq, time);
            } else leftTrackGraphicController.startForwardAnimation((int) freq, time);
            if (rightTrackGraphicController.isStatementForward()) {
                rightTrackGraphicController.setFramesPerSec((int) freq, time);
            } else rightTrackGraphicController.startForwardAnimation((int) freq, time);
        }
        else if (lastMovementDirection == TankController.MovementConstants.MOVE_BACKWARD){
            if (leftTrackGraphicController.isStatementBackward()) {
                leftTrackGraphicController.setFramesPerSec((int) freq, time);
            } else leftTrackGraphicController.startReverseAnimation((int) freq, time);
            if (rightTrackGraphicController.isStatementBackward()) {
                rightTrackGraphicController.setFramesPerSec((int) freq, time);
            } else rightTrackGraphicController.startReverseAnimation((int) freq, time);
        }
        if (lastMovementDirection == TankController.MovementConstants.MOVE_FORWARD_ROTATE_CW){
            if (leftTrackGraphicController.isStatementForward()) {
                leftTrackGraphicController.setFramesPerSec((int) freq, time);
            } else leftTrackGraphicController.startForwardAnimation((int) freq, time);
            if (rightTrackGraphicController.isStatementForward()) {
                rightTrackGraphicController.setFramesPerSec((int) freq/2, time);
            } else rightTrackGraphicController.startForwardAnimation((int) freq/2, time);
        }
        if (lastMovementDirection == TankController.MovementConstants.MOVE_FORWARD_ROTATE_CCW){
            if (leftTrackGraphicController.isStatementForward()) {
                leftTrackGraphicController.setFramesPerSec((int) freq/2, time);
            } else leftTrackGraphicController.startForwardAnimation((int) freq/2, time);
            if (rightTrackGraphicController.isStatementForward()) {
                rightTrackGraphicController.setFramesPerSec((int) freq, time);
            } else rightTrackGraphicController.startForwardAnimation((int) freq, time);
        }
        else {
            pauseAnimation(LEFT);
            pauseAnimation(!LEFT);
        }

    }

    private boolean updateOnPlaceRotating(int movementDirection, long time) {
        if (movementDirection == TankController.MovementConstants.ROTATE_CW){
            freq = maxAnimationFramerateForForward;
            if ((int) prevFreq != (int) freq) {
                if (rightTrackGraphicController.isStatementForward()) {
                    rightTrackGraphicController.setFramesPerSec((int) freq, time);
                } else rightTrackGraphicController.startForwardAnimation((int) freq, time);
                leftTrackGraphicController.pauseAnimation();
            }
            return true;
        }
        else if (movementDirection == TankController.MovementConstants.ROTATE_CCW){
            freq = maxAnimationFramerateForForward;
            if ((int) prevFreq != (int) freq) {
                if (leftTrackGraphicController.isStatementForward()) {
                    leftTrackGraphicController.setFramesPerSec((int) freq, time);
                } else leftTrackGraphicController.startForwardAnimation((int) freq, time);
                rightTrackGraphicController.pauseAnimation();
            }
            return true;
        }
        return false;
    }

    private void updateOnlyForSymetrical(long time) {
        prevFreq = freq;
        float relativeVelocity;
        float maxAnimationFramerate;
        if (tank.getTankController().isLastMovementDirectionForward()) {
            relativeVelocity = (actualVelocity/tank.getForwardMaxVelocityInWorldUnits());
            maxAnimationFramerate  = maxAnimationFramerateForForward;
        }
        else {
            relativeVelocity = (actualVelocity/tank.getBackwardMaxVelocityInWorldUnits());
            maxAnimationFramerate  = maxAnimationFramerateForBackward;
        }
        if (relativeVelocity>1) relativeVelocity = 1f;

        freq = (relativeVelocity)*(maxAnimationFramerate - minAnimationFramerate)+ minAnimationFramerate;
        actualVelocity = tank.getBody().getLinearVelocity().length();
        if (relativeVelocity > 0.05f && relativeVelocity <= 1f){
            if (freq <= STOP_ANIMATION_FRAMERATE){
                pauseAnimation(LEFT);
                pauseAnimation(!LEFT);
            }
            else if ((int)prevFreq != (int)freq) {
                if (leftTrackGraphicController.isStatementForward()) {
                    leftTrackGraphicController.setFramesPerSec((int) freq, time);
                }
                else leftTrackGraphicController.startForwardAnimation((int) freq, time);

                if (rightTrackGraphicController.isStatementForward()) {
                    rightTrackGraphicController.setFramesPerSec((int) freq, time);
                }
                else rightTrackGraphicController.startForwardAnimation((int) freq, time);

            }
        }
        else {
            pauseAnimation(LEFT);
            pauseAnimation(!LEFT);
        }

        leftTrackGraphicController.update(time);
        rightTrackGraphicController.update(time);
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
