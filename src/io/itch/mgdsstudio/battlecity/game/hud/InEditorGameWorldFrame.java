package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.EightPartsFrameImage;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.InEditorGraphicData;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PVector;

import java.awt.*;

public class InEditorGameWorldFrame extends InEditorFrame{

    private final static ImageZoneSimpleData zoneForGameWorld = new ImageZoneSimpleData(0,66, 153, 219);
    private final Rectangle gameWorldZone;


    public InEditorGameWorldFrame(IEngine engine, Rectangle gameWorldZone) {
        super(engine);
        this.gameWorldZone = gameWorldZone;
        initZoneFrame(new PVector(gameWorldZone.x, gameWorldZone.y), 0.06f);
    }

    protected void initZoneFrame(PVector leftUpperCorner, float additionalData) {
        int basicWidth = InEditorGraphicData.theoreticalWidthOfFramesWithNoZomming;
        final float CURSOR_DIMENSIONS_COEF = 1.3f;
        float additionalWidth = additionalData*basicWidth;
        float additionalHeight = additionalData*basicWidth;
        leftUpperCorner.x-=additionalWidth/2f;
        leftUpperCorner.y-=additionalWidth/2f;
        int frameWidth = (int) (gameWorldZone.width+additionalWidth);
        int frameHeight = (int) (gameWorldZone.height+additionalHeight);
        zoneFrame = new EightPartsFrameImage(GuiElement.getGraphicFile(), zoneForGameWorld, basicWidth, basicWidth, frameWidth, frameHeight, leftUpperCorner);
    }

    public Rectangle getGameWorldZone() {
        return gameWorldZone;
    }
}
