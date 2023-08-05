package io.itch.mgdsstudio.battlecity.editor;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;
import processing.core.PConstants;

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
        alphaController = new AlphaController(0, 128,1500);
    }

    public void draw(){
        if (active){
            alphaController.update(engine.getProcessing().millis());
            engine.getProcessing().pushStyle();
            engine.getProcessing().tint(255,255,255,alphaController.getAlpha());
            engine.getProcessing().image(image.getImage(), pos.x, pos.y, size, size, data.leftX, data.upperY, data.rightX, data.lowerY);
            engine.getProcessing().popStyle();
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
        private int alpha;

        private AlphaController(int min, int max, int period) {
            this.min = min;
            this.max = max;
            coef = PConstants.TWO_PI / period;
        }



        void update(int millis){
            alpha = (int) (min+((max-min)* PApplet.sin(millis*coef)));
            if (alpha < min) alpha = min;
        }

        int getAlpha(){
            return alpha;
        }
    }
}
