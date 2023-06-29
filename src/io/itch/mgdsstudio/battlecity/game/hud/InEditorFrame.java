package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.EightPartsFrameImage;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

public abstract class InEditorFrame {
    protected EightPartsFrameImage zoneFrame;
    //protected Rectangle gameWorldZone;
    protected IEngine engine;

    public InEditorFrame(IEngine engine) {
        this.engine = engine;
    }

    protected abstract void initZoneFrame(PVector leftUpperCorner, float additionalData) ;


    public void draw(PGraphics graphics) {
        zoneFrame.draw(graphics);
    }

}
