package io.itch.mgdsstudio.engine.graphic.debuggraphic;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class TankDebugGraphic extends DebugGraphic
{
    protected ArrayList<Coordinate> vertecies;
    //private float turretAngle;
    
    
public TankDebugGraphic(Entity entity, PApplet engine)
    {
        super(entity,engine);
    }
    
    @Override
    protected void init(PApplet engine){
        super.init(engine);
        initVertecies();
    }
    
protected void initVertecies(){
     vertecies = new ArrayList<>();
     Tank tank = (Tank)entity;
     int width = tank.getWidth();
     int height = tank.getHeight();
     int angleCircleDiam = tank.getAngleCircleDiam();
     if (angleCircleDiam >= width){
         System.out.println("This debug graphic can not be created");
         vertecies.add(new Coordinate(-width/2, 0));
         vertecies.add(new Coordinate(0, height/2));
         vertecies.add(new Coordinate(width/2, 0));
         vertecies.add(new Coordinate(0, -height/2));
         
     }
     else{
         int rad = angleCircleDiam/2;
         vertecies.add(new Coordinate(-width/2, -height/2+rad));
         vertecies.add(new Coordinate(-width/2, height/2-rad));
        vertecies.add(new Coordinate(-width/2+rad, height/2));
        vertecies.add(new Coordinate(width/2-rad, height/2));
        vertecies.add(new Coordinate(width/2, -height/2+rad));
        vertecies.add(new Coordinate(width/2, height/2-rad));
        vertecies.add(new Coordinate(-width/2+rad, -height/2));
        vertecies.add(new Coordinate(width/2-rad, -height/2));
     }
     
   }
   
   
   
protected void drawElement(PGraphics graphics, Camera gameCamera){
    for (int i = 0; i < vertecies.size(); i+=2){
        if (i!=0) graphics.stroke(25,100,100);
			graphics.line(vertecies.get(i).x, vertecies.get(i).y, vertecies.get(i+1).x, vertecies.get(i+1).y);

		}
	graphics.ellipse(0,0,entity.getWidth()/2, entity.getHeight()/2);
    drawCannon(graphics);
}
   
   
	private void drawCannon(PGraphics graphics){
	    Tank tank = (Tank)entity;
	    //float angle = tank.getRelativeTurretAngle();
        float angle = tank.getRelativeTurretAngle();
        //Logger.debugLog("Turret: " + tank.getRelativeTurretAngle());

        graphics.rotate(PApplet.radians(angle));
	    graphics.line(0,0,entity.getWidth()/1,0);
        //System.out.println("Drawn");
	}
}