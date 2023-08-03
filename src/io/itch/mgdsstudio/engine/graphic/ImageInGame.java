package io.itch.mgdsstudio.engine.graphic;

import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ImageInGame extends GraphicElementInGame{

    public ImageInGame(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData) {
        super(image, width, height, imageZoneSimpleData);
    }

    @Override
    protected void render(PGraphics graphics) {
        graphics.image(image.getImage(), 0,0,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
    }
}
