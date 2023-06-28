package io.itch.mgdsstudio.engine.graphic.debuggraphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import processing.core.*;

public abstract class DebugGraphic {
	public static final int RECT = 0;
	public static final int CIRCLE = 1;
	public static final int POLYGON = 2;
	public static final int RECT_WITHOUT_ANGLES = 3;
	
	//private int dim;
	
	private int type;
	//private boolean withFilling;
	private int strokeColor, fillingColor;
    private PApplet engine;
	protected Entity entity;
	private int lineThickness;
    
   public DebugGraphic(Entity entity, PApplet engine){
       this.entity = entity;
       this.engine = engine;
       init(engine);
       
   }
   
   
       
   
	
	protected void init(PApplet engine){
		
		int red = 0;
		int green = 0;
		int blue = 0;
		if (entity.getClass() == Forest.class){
			green = 255;
		}
		else if (entity.getClass() == Wall.class){
			red = 125;
			blue = 40;
		}
		else if (entity instanceof Tank) green = 220;
		else red = 255;
		
		fillingColor = engine.color(red,green,blue, 200);
		initStrokeColor(engine, red, green, blue);
			
		lineThickness = 5;
	}
	
	private void initStrokeColor(PApplet engine, int r, int g, int b){
		int delta = 45;
		int r1 = r-delta;
		if (r1<0) r1 = 0;
		int g1 = g-delta;
		if (g1<0) g1 = 0;
		int b1 = b-delta;
		if (b1<0) b1 = 0;
		
		strokeColor = engine.color(r1,g1,b1);
		
	}
	
	private void startRender(PGraphics graphics, GameCamera gameCamera){
		graphics.pushMatrix();
		graphics.pushStyle();
		graphics.translate(gameCamera.getDrawPosX(entity.getPos().x), gameCamera.getDrawPosY(entity.getPos().y));
		//System.out.println("Must be drawn at: " + (gameCamera.getDrawPosX(entity.getPos().x) + " x " + gameCamera.getDrawPosY(entity.getPos().y)));
		//graphics.translate(entity.getPos().x-gameCamera.getPos().x+ GlobalVariables.getScreenCenterX(), entity.getPos().y-gameCamera.getPos().y+GlobalVariables.getScreenCenterY());
		graphics.rotate(PApplet.radians(entity.getAngle()));
		graphics.fill(fillingColor);
		graphics.stroke(strokeColor);
		graphics.strokeWeight(lineThickness);
	}
	
	
	
	protected abstract void drawElement(PGraphics graphics, GameCamera gameCamera);
	    
	    
	
	
	

    public void draw(PGraphics graphics, GameCamera gameCamera){
         startRender(graphics, gameCamera);
         drawElement(graphics, gameCamera);
         endRender(graphics);
    }
	
	private void endRender(PGraphics graphics){
		graphics.popMatrix();
		graphics.popStyle();
	}
}
