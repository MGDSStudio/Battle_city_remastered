package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.graphic.VfxsPool;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

public abstract class GraphicObject extends Entity{

    public interface GraphicLayers {
        int GROUND_LAYER = 0;
        int ON_GROUND_LAYER = 1;
        int OBJECT_ABOVE_GROUND_LAYER = 2;
        int OVER_SKY_LEVEL = 3;
    }

    protected GraphicElementInGame graphicObject;
    protected GraphicObject(IEngine engine, Coordinate pos, int angle, int width, int height, int thirdDim, int layer) {
        super(engine, pos, angle, IMMORTAL_LIFE, width, height);
        this.graphicLayer = layer;

    }

    protected final void loadImage(IEngine engine, String path, int graphicWidth, int graphicHeight, ImageZoneSimpleData data){
        GraphicManager manager = GraphicManager.getManager(engine.getEngine());
        Image graphicImage = manager.getImage(path);
        if (width<0) width = data.rightX-data.leftX;
        if (height<0) height = data.lowerY-data.upperY;
        graphicObject = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
    }

    protected void loadAnimation(IEngine engine, int type) {
        graphicObject = VfxsPool.createAnimation(type, engine);
    }

    protected void loadAnimation(IEngine engine, int type, int width, int height) {
        graphicObject = VfxsPool.createAnimation(type, engine, width, height);
    }

    @Override
    public void draw(PGraphics graphics, Camera gameCamera) {
        if (graphicObject != null) graphicObject.drawWithTransformations(graphics, gameCamera, this);

    }


}
