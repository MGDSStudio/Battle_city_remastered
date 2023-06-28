package io.itch.mgdsstudio.engine.libs;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import processing.core.PApplet;
import processing.core.PConstants;

public class AxisAlignedBoundingBox {
    public final static int RIGHT_LOWER = 0;
    public final static int RIGHT_UPPER = 3;
    public final static int LEFT_LOWER = 1;
    public final static int LEFT_UPPER = 2;
    //public final static int RECT = 0;
    //public final static int CIRCLE = 1;
    private float w, h, sourceW, sourceH;
    private Coordinate center;
    private boolean circle;
    private boolean posMustBeUpdated = true;


    public AxisAlignedBoundingBox(int w, int h, float x, float y, float angleInDegrees) {
        //rect
        this.w = w;
        this.h = h;
        sourceW = w;
        sourceH = h;
        center = new Coordinate(x,y);
        updateDims(angleInDegrees);
    }

    public AxisAlignedBoundingBox(int w, int h, Coordinate pos, float angleInDegrees) {
        //rect
        this.w = w;
        this.h = h;
        sourceW = w;
        sourceH = h;
        center = pos;
        posMustBeUpdated = false;
        updateDims(angleInDegrees);
    }

    public AxisAlignedBoundingBox(int w, float x, float y) {
        //circle
        this.w = w;
        this.h = w;
        sourceW = w;
        sourceH = sourceW;
        center = new Coordinate(x,y);
        circle = true;
    }

    public AxisAlignedBoundingBox(float w, Coordinate pos) {
        //circle
        this.w = w;
        this.h = w;
        sourceW = w;
        sourceH = sourceW;
        center = pos;
        circle = true;
        posMustBeUpdated = false;
    }

    private void updateDims(float angleInDegrees) {
        w = sourceW/(PApplet.cos(PApplet.radians(angleInDegrees)));
        h = sourceH/(PApplet.cos(PApplet.radians(angleInDegrees)));
    }



    public void update(float x, float y, float angleInDegrees){
        if (posMustBeUpdated) {
            center.x = x;
            center.y = y;
        }
        if (!circle){
            updateDims(angleInDegrees);
        }
    }

    public void update(float angleInDegrees){
        if (posMustBeUpdated) {
            Logger.error("Position of the AABB can not be updated");
        }
        if (!circle){
            updateDims(angleInDegrees);
        }
    }

    public Coordinate getVertexPos(int vertex){
        float yHalfHeight = -h/2f;
        if (GlobalConstants.Y_AXIS_DOWN){
            yHalfHeight = h/2f;
        }
        final Coordinate mutableVertexPos = new Coordinate(0,0);
        if (vertex == RIGHT_LOWER){
            mutableVertexPos.x = center.x+w/2;
            mutableVertexPos.y = center.y+yHalfHeight;
        }
        else if (vertex == LEFT_LOWER){
            mutableVertexPos.x = center.x-w/2;
            mutableVertexPos.y = center.y+yHalfHeight;
        }
        else if (vertex == LEFT_UPPER){
            mutableVertexPos.x = center.x-w/2;
            mutableVertexPos.y = center.y-yHalfHeight;
        }
        else if (vertex == RIGHT_UPPER){
            mutableVertexPos.x = center.x+w/2;
            mutableVertexPos.y = center.y-yHalfHeight;
        }
        return mutableVertexPos;
    }
}
