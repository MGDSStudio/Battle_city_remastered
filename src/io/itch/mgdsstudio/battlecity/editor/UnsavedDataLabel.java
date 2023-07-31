package io.itch.mgdsstudio.battlecity.editor;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class UnsavedDataLabel {
    private final static ImageZoneSimpleData data = new ImageZoneSimpleData(0,496, 34,530);
    private boolean active;
    private final Image image;
    private Coordinate pos;
    private int size;

    private final IEngine engine;

    public UnsavedDataLabel(IEngine engine, Image image, Coordinate pos, int size) {
        this.engine = engine;
        this.image = image;
        this.pos = pos;
        this.size = size;
    }

    public void draw(){
        if (active){
            engine.getEngine().image(image.getImage(), pos.x, pos.y, size, size, data.leftX, data.upperY, data.rightX, data.lowerY);
            //Logger.debug("Drawn ");
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
