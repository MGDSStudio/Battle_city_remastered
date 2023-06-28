package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.SolidObject;

import java.util.ArrayList;
import io.itch.mgdsstudio.engine.libs.*;
import io.itch.mgdsstudio.battlecity.game.*;
import io.itch.mgdsstudio.battlecity.mainpackage.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import processing.core.PApplet;

public class MinesOnFieldGenerator {

	public MinesOnFieldGenerator() {
	}

	public ArrayList<Coordinate> generateMinesPositions(int count, GameRound gr){
        ArrayList <Coordinate> positions = new ArrayList<>();
        
		float left =  getLeft(gr);
		float right = getRight(gr);
		float upper = getUpper(gr);
		float lower = getLower(gr);
		Coordinate leftUpper = new Coordinate(left, upper);
		Coordinate rightLower = new Coordinate(right, lower);
		float  theoreticalCellDim = getCelDim(leftUpper, rightLower, gr);
		int alongX = (int)((rightLower.x-leftUpper.x)/theoreticalCellDim);
		int alongY = (PApplet.abs((int)((rightLower.y-leftUpper.y)/theoreticalCellDim)));
		ArrayList <Coordinate> freeCells = generateCellsArray(gr, leftUpper, alongX, alongY, theoreticalCellDim);
		int generated = 0;
		//ArrayList <Vec2> pos = new ArrayList<>();
		while(generated <= count)
		{
			int randomCellNumber = (int)gr.getEngine().getEngine().random(alongX*alongY);
			positions.add(freeCells.get(randomCellNumber).clone());
			generated++;
			freeCells.remove(freeCells.get(randomCellNumber));
		}

        return positions;
    }

	private ArrayList<Coordinate> generateCellsArray(GameRound gr, Coordinate leftUpper, int alongX, int alongY, float  theoreticalCellDim) {
		ArrayList<Coordinate> arrayList = new ArrayList<>();
		float x;
		float y;
		int count = alongX*alongY;
		for (int i = 0; i < alongX; i++){
			for (int j = 0; j < alongY; j++){
				x = leftUpper.x+theoreticalCellDim/2+theoreticalCellDim*i;
				y = leftUpper.y+theoreticalCellDim/2+theoreticalCellDim*j;
				if (gr.getPhysicWorld().getBodyAtPoint(x,y) == null){
					Coordinate coordinate = new Coordinate(x,y);
					arrayList.add(coordinate);
				}
			}
		}
		Logger.debug("The map has " + count + " theoretical cells and " + arrayList.size() + " free cells. Along sizes: " + alongX + "x" + alongY);
		return arrayList;
	}

	private float getCelDim(Coordinate leftUpper, Coordinate rightLower, GameRound gr){
		return gr.getPlayer().getWidth();
		/*for (Entity gameObject : gr.getEntities()){
			//if (gameObject
		}*/
	}
	
	private float getLower(GameRound gr){
		float lower = -99999;
		if (!GlobalConstants.Y_AXIS_DOWN) lower = -lower;

        for (Entity gameObject : gr.getEntities()){
            if (gameObject instanceof Wall){

				SolidObject obj = (SolidObject)gameObject;
				AxisAlignedBoundingBox aabb = obj.getAabb();
				float upper = aabb.getVertexPos(AxisAlignedBoundingBox.RIGHT_UPPER).y;
				if (!GlobalConstants.Y_AXIS_DOWN) {
					if (upper > lower) lower = upper;
				}
				else {
					Logger.debug("Not implemented for revetce axis");
				}

            }
        }
		if (lower == -99999) Logger.error("Mines can not be generated. World is too large");
        return lower;
    }
	
	
	private float getUpper(GameRound gr){
		float upper = 99999;
		if (!GlobalConstants.Y_AXIS_DOWN) upper = -upper;
        
        for (Entity gameObject : gr.getEntities()){
            if (gameObject instanceof Wall){

				SolidObject obj = (SolidObject)gameObject;
				AxisAlignedBoundingBox aabb = obj.getAabb();
				float lower = aabb.getVertexPos(AxisAlignedBoundingBox.RIGHT_LOWER).y;
				if (!GlobalConstants.Y_AXIS_DOWN) {
					if (lower<upper) upper = lower;
				}
				else {
					Logger.debug("Not implemented for revetce axis");
				}
				
            }
        }
		if (upper == 99999) Logger.error("Mines can not be generated. World is too large");
        return upper;
    }
	

    private float getLeft(GameRound gr){
        float left = 99999;
        for (Entity gameObject : gr.getEntities()){
            if (gameObject instanceof Wall){
               
				SolidObject obj = (SolidObject)gameObject;
				AxisAlignedBoundingBox aabb = obj.getAabb();
				float right = aabb.getVertexPos(AxisAlignedBoundingBox.RIGHT_LOWER).x;
				if (right<left) left = right;
            }
        }
		if (left == 99999) Logger.error("Mines can not be generated. World is too large");
        return left;
    }

	private float getRight(GameRound gr){
        float right = -99999;
        for (Entity gameObject : gr.getEntities()){
            if (gameObject instanceof Wall){

				SolidObject obj = (SolidObject)gameObject;
				AxisAlignedBoundingBox aabb = obj.getAabb();
				float left = aabb.getVertexPos(AxisAlignedBoundingBox.LEFT_LOWER).x;
				if (right<left) right = left;
            }
        }
		if (right == 99999) Logger.error("Mines can not be generated. World is too large");
 
        return right;
    }
	
}
