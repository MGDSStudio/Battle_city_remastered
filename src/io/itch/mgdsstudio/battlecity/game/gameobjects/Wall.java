package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.dynamics.BodyType;
import processing.core.PGraphics;

public abstract class Wall extends SolidObject {
    //protected GraphicElementInGame image;
    //public ImageInGame(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData) {
    protected Wall(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int thirdDim) {
        super(engine, physicWorld, pos, angle, 1, width, height, BodyForms.RECT, BodyType.STATIC, -1);

    }

/*
    protected final void loadImage(IEngine engine, String path, int graphicWidth, int graphicHeight, ImageZoneSimpleData data){
        GraphicManager manager = GraphicManager.getManager(engine.getEngine());
        Image graphicImage = manager.getImage(path);
        graphicElementInGame = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
        //image = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
        //graphicElementInGame
    }*/

    public int getDyingAnimationType() {
        return IAnimations.DUST_SPLASH;
    }

    @Override
    public void draw(PGraphics graphics, Camera gameCamera) {
        if (graphicElementInGame != null) graphicElementInGame.drawWithTransformations(graphics, gameCamera, this);
        //if (graphicElementInGame != null) graphicElementInGame.draw(graphics, gameCamera, pos.x, pos.y);
    }


}
