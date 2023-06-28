package io.itch.mgdsstudio.engine.graphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class GraphicElementInGame {
    protected Image image;
    protected int width, height;
    protected boolean active = true;
    protected ImageZoneSimpleData imageZoneSimpleData;
    protected boolean firstUpdatingEnded;
    protected int alpha;
    private boolean withSpecificFeatures;

    public GraphicElementInGame(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.imageZoneSimpleData = imageZoneSimpleData;
    }

    public Image getImage() {
        return image;
    }

    public ImageZoneSimpleData getImageZoneSimpleData() {
        return imageZoneSimpleData;
    }

    /*public void draw(PGraphics graphics, GameCamera gameCamera, float x, float y) {
        graphics.image(image.getImage(), gameCamera.getDrawPosX(x),gameCamera.getDrawPosY(y) ,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
    }*/

    public void drawWithTransformations(PGraphics graphics, GameCamera gameCamera, Entity entity) {
        if (active) {
            startRender(graphics, gameCamera, entity);
            if (withSpecificFeatures) renderSpecific(graphics);
            else render(graphics);
            endRender(graphics);
        }
    }

    public void drawWithoutTransformations(PGraphics graphics, GameCamera gameCamera) {
        if (active) {
            //startRender(graphics, gameCamera);
            if (withSpecificFeatures) renderSpecific(graphics);
            else render(graphics);
            //endRender(graphics);
        }
    }

    protected void renderSpecific(PGraphics graphics) {
        graphics.pushStyle();
        graphics.tint(255,255,255, alpha);
        render(graphics);
        graphics.popStyle();
    }

    protected abstract void render(PGraphics graphics);

    private void endRender(PGraphics graphics){
        graphics.popMatrix();
    }

    private void startRender(PGraphics graphics, GameCamera gameCamera, Entity entity){
        graphics.pushMatrix();
        graphics.translate(gameCamera.getDrawPosX(entity.getPos().x), gameCamera.getDrawPosY(entity.getPos().y));
        graphics.rotate(PApplet.radians(entity.getAngle()));
    }

    public final boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        if (alpha != 255){
            withSpecificFeatures = true;
        }
        else withSpecificFeatures = false;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
