package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PGraphics;

public class ShotDustEffect extends GraphicObject{
    private TintController tintController;
    private boolean active = true;
    private final static int START_HEIGHT_FOR_SMALL_TANK = 10;
    private final static int START_HEIGHT_FOR_LARGE_TANK = 17;

    public ShotDustEffect(IEngine engine, Coordinate pos, int angle, Tank tank){
        super(engine, pos, angle, getSizeForBullet(tank), getSizeForBullet(tank), 0, GraphicLayers.OVER_SKY_LEVEL);
        loadGraphicDefaultData(engine);
        tintController = new TintController(engine);
    }

    private static int getSizeForBullet(Tank tank){
        int life = tank.getLife();
        if (life > 2){
            return START_HEIGHT_FOR_SMALL_TANK;
        }
        else return START_HEIGHT_FOR_LARGE_TANK;
    }



    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(310,0, 361, 51);
        loadImage(engine, path, width, height, data);
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
       if (active) {
           super.update(gameRound, deltaTime);
           tintController.update();
       }
    }

    @Override
    public void draw(PGraphics graphics, GameCamera gameCamera) {
        if (active) {

            graphicObject.setWidth(tintController.actualSize);
            graphicObject.setHeight(tintController.actualSize);
            graphicObject.setAlpha(tintController.actualAlpha);
            graphicObject.drawWithTransformations(graphics, gameCamera, this);
        }
    }

    private class TintController{
        private final static int MAX_ANIMATION_TIME = 2000;
        private final static int MAX_ALPHA = 255;
        private int actualAlpha = MAX_ALPHA;
        private final float DIM_SCALING_COEF = 2f;
        private int actualSize;
        private final int startSize;
        private Timer timer;
        private int endSize;

        TintController(IEngine engine){
            timer = new Timer(MAX_ANIMATION_TIME, engine.getEngine());
            startSize = width;
            endSize = (int) (DIM_SCALING_COEF*startSize);
        }

        private void update(){
            if (active) {
                float value = timer.getRelativeRestTime();
                updateAlpha(value);

            }
        }

        private void updateAlpha(float value) {
            actualAlpha = (int) (value * MAX_ALPHA);
            if (actualAlpha <= 0){
                active = false;
                timer = null;
                return;
            }
            else {
                updateDim(value);
            }
        }

        private void updateDim(float value) {
            int deltaDim = (int) ((1f-value)*((endSize-startSize)));
            actualSize = startSize+deltaDim;
            //Logger.debugLog("Actual width: " + tintController.actualSize + " but delta: " + deltaDim);
            //actualSize;

        }
    }
}
