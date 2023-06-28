package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.Logger;
import processing.core.PApplet;

public class WayPoint {
    private float x,y;
    private int radius;
    private boolean visited;


    public WayPoint(float x, float y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean isIn(float x, float y){
        if (PApplet.dist(this.x,this.y,x,y)<radius){
            return true;
        }

        else         return false;
    }

    public void markAsVisited(){
        visited = true;
    }

    public boolean isVisited(){
        return visited;
    }

}

