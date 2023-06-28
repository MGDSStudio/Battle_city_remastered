package io.itch.mgdsstudio.engine.graphic.debuggraphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DebugCircle extends DebugGraphic
{
    
    public DebugCircle(Entity entity, PApplet engine)
    {
        super(entity,engine);
    }
    
    
    protected void drawElement(PGraphics graphics, GameCamera gameCamera){
		graphics.ellipse(0,0,entity.getWidth(), entity.getWidth());

	}
    
}