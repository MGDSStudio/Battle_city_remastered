package io.itch.mgdsstudio.battlecity.editor;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class UnsavedDataLabel {
    private final static ImageZoneSimpleData data = new ImageZoneSimpleData(0,496, 34,530);
    private boolean active;
    private final Image image;
    private Coordinate pos;
    private int size;
    private final AlphaController alphaController;
    private final IEngine engine;

    public UnsavedDataLabel(IEngine engine, Coordinate pos, int size) {
        this.engine = engine;
        this.image = GuiElement.getGraphicFile();
        this.pos = pos;
        this.size = size;
        alphaController = new AlphaController();
    }

    public void draw(){
        if (active){
            engine.getEngine().image(image.getImage(), pos.x, pos.y, size, size, data.leftX, data.upperY, data.rightX, data.lowerY);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private class AlphaController{


        void update(int millis){

        }
    }
}
