package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class HaloController
{
    //private
    private final static ImageZoneSimpleData greenZone = new ImageZoneSimpleData(925, 941, 975,991 );
    private final static ImageZoneSimpleData blueZone = new ImageZoneSimpleData(926-50, 941, 926,991 );
    private final static ImageZoneSimpleData purpleZone = new ImageZoneSimpleData(974, 941, 1024,991 );
    private AlphaController green, blue, purple;
    private PApplet engine;
    private AngleController angleController;
    private final PImage texture;
    private float width, height;
    private Entity entity;
    //private float angl

    public HaloController(PApplet engine, Image image, Entity entity)
    {
        this.entity = entity;
        this.engine = engine;
        this.texture = image.getImage();
        int randomShift = (int) engine.random(2000);
        green = new AlphaController(0,3,2000, randomShift);
        blue = new AlphaController(1,3,2000, randomShift);
        purple = new AlphaController(2,3,2000, randomShift);
        angleController = new AngleController(2000);
        width = greenZone.rightX-greenZone.leftX;
        height = greenZone.lowerY-greenZone.upperY;
    }

    public void update(){
        angleController.update(engine.millis());
        green.update(engine.millis());
        blue.update(engine.millis());
        purple.update(engine.millis());
        //alpha = MIN_ALPHA+(MAX_ALPHA-MIN_ALPHA)*PApplet.sin((time+shift)*coef);
    }


    public void draw (PGraphics graphic, Camera gameCamera){
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);

        graphic.translate(gameCamera.getDrawPosX(entity.getPos().x), gameCamera.getDrawPosY(entity.getPos().y));

        graphic.rotate(angleController.getAngle());
        graphic.tint(255,255,255,blue.getAlpha());
        graphic.image(texture,0,0,width, height, blueZone.leftX, blueZone.upperY, blueZone.rightX, blueZone.lowerY);
        graphic.tint(255,255,255,green.getAlpha());
        graphic.image(texture,0,0,width, height, greenZone.leftX, greenZone.upperY, greenZone.rightX, greenZone.lowerY);
        graphic.tint(255,255,255, purple.getAlpha());
       // Logger.debug("purple " + purple.alpha);
        graphic.image(texture,0,0,width, height, purpleZone.leftX, purpleZone.upperY, purpleZone.rightX, purpleZone.lowerY);
        graphic.popStyle();
        graphic.popMatrix();
    }

    private class AngleController{
        private float angle;
        private final float coef;

        private AngleController (float loopTime){
            coef = PConstants.PI/loopTime;
        }

        private void update(long time){
            angle = time*coef;
        }

        private float getAngle(){
            return angle;
        }

    }

    private class AlphaController{
        private float alpha;
        private final static float MAX_ALPHA = 20;
        private final static float MIN_ALPHA = 5;
        private final float shift;
        private final float coef;
        //private final int LOOP_TIME = 2000;

        private AlphaController(int number, int numbers, int loopTime, int randomShift){
            //this.LOOP_
            shift = number*loopTime/numbers+randomShift;
            coef = PConstants.PI/loopTime;
        }

        private void update(long time){
            alpha = MIN_ALPHA+(MAX_ALPHA-MIN_ALPHA)*(PApplet.sin((time+shift)*coef)+0.5f);

        }

        private int getAlpha(){
            return (int)alpha;
        }
    }
}
