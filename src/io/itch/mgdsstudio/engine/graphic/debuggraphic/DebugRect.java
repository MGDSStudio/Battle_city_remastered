package io.itch.mgdsstudio.engine.graphic.debuggraphic;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DebugRect extends DebugGraphic
{
    
    
public DebugRect(Entity entity, PApplet engine)
    {
        super(entity,engine);
    }
    
    
    
    
    protected void drawElement(PGraphics graphics, Camera gameCamera){
		graphics.rect(0,0,entity.getWidth(), entity.getHeight());
		
	}
}