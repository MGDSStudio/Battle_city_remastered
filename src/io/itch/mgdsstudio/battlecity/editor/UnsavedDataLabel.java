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
        alphaController = new AlphaController(0, 128);
    }

    public void draw(){
        if (active){
            engine.getEngine().tint(255,255,255,alphaController.getAlpha(engine.getEngine().millis()));
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
private int max, min;
//private float period;
private float coef;

private AlphaController(int min, int max, int period){
this.min = min;
this.max = max;
    coef = PConstants.TWO_PI/period;
}

        void update(int millis){

        }

        int getAlpha(int millis){
            int alpha = min+(max-min)PApplet.sin(millis*coef);
            if (alpha < 0) alphaChangingStep = 0;
            return alpha;
        }
    }
}
