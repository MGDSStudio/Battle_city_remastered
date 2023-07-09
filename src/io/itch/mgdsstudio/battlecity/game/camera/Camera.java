package io.itch.mgdsstudio.battlecity.game.camera;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.hud.InGameHud;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

public abstract class Camera {
    protected Coordinate pos;
    private int graphicCenterX;
    private int graphicCenterY;
    protected boolean firstInit;
    protected Hud inGameHud;
    protected IEngine engine;

    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;

    }

    public interface Magneting{
        int STATIC = 0;
        int TO_ENTITY_WITHOUT_SPRING = 1;
        int TO_ENTITY_WITH_SPRING = 2;
        int TO_COORDINATE = 3;
    }

    protected final void initGraphicCenter(PGraphics graphics){
        graphicCenterX = graphics.width/2;
        graphicCenterY = graphics.height/2;
    }

    protected final void initGraphicCenter(Hud inGameHud){
        graphicCenterX = inGameHud.getGraphicLeftPixel()+(inGameHud.getGraphicRightPixel()- inGameHud.getGraphicLeftPixel())/2;
        graphicCenterY = inGameHud.getGraphicUpperPixel()+(inGameHud.getGraphicLowerPixel()- inGameHud.getGraphicUpperPixel())/2;
        Logger.debug("Center is: " + graphicCenterX + " x " + graphicCenterY);

        //Logger.debugLog("Center is: " + graphicCenterX + " x " + graphicCenterY);
    }

    public final float getDrawPosX(float x){
        return (x-pos.x+graphicCenterX);
    }

    public final float getDrawPosY(float y){
        return (y-pos.y+graphicCenterY);
    }




    public final float getObjectXPosForDrawPlace(float xOnScreen){
        return xOnScreen+pos.x-graphicCenterX;
        //return (x-pos.x+graphicCenterX) = xOnScreen;
    }

    public final float getObjectYPosForDrawPlace(float yOnScreen){
        return yOnScreen+pos.y-graphicCenterY;


    }


    public final Coordinate getPos() {
        return pos;
    }

    public abstract void update();

    public abstract void shiftCameraPos(Vec2 distance);
}
