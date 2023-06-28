package io.itch.mgdsstudio.engine.libs;

import processing.core.PApplet;

public class Geometrie {
    public final static boolean CW = true;

    private enum Quarter{
        QUARTER_RIGHT_DOWN, QUARTER_LEFT_DOWN , QUARTER_LEFT_UP, QUARTER_RIGHT_UP;
    }
    /*private final static int QUARTER_RIGHT_DOWN = 0;
    private final static int QUARTER_LEFT_DOWN = 1;
    private final static int QUARTER_LEFT_UP = 2;
    private final static int QUARTER_RIGHT_UP = 3;*/



    public static float getPositiveAngleCCWforYAxisDownOriented(float baseX, float baseY, float x, float y){
        float dX = Math.abs(x-baseX);
        float dY = Math.abs(y-baseY);
        float angle = (float)Math.atan(dY/dX);
        Quarter quarter = getQuarterIfYAxisDownOriented(baseX, baseY, x,y);
        angle = PApplet.degrees(angle);
        if (quarter == Quarter.QUARTER_RIGHT_UP){
            return 270+(90-angle);
        }
        else if (quarter == Quarter.QUARTER_RIGHT_DOWN){
            return angle;
        }
        else if (quarter == Quarter.QUARTER_LEFT_UP){
            return 180+angle;
        }
        else return 180-angle;  //LEFT DOWN
    }

    private static Quarter getQuarterIfYAxisDownOriented(float baseX, float baseY, float x, float y){   //For processing engine
        if (x>=baseX){
            if (y>=baseY) return Quarter.QUARTER_RIGHT_DOWN;
            else return Quarter.QUARTER_RIGHT_UP;
        }
        else {
            if (y >= baseY) return Quarter.QUARTER_LEFT_DOWN;
            else return Quarter.QUARTER_LEFT_UP;
        }
    }

    public static float getShortestDeltaAngleInDegrees(float a1, float a2){
        float delta ;
        if (a1 < 0 || a2 < 0){
            a1 = convertAngleToPositive(a1);
            a2 = convertAngleToPositive(a2);
        }
        if (a1 > 360) {
            while (a1 > 360){
                a1-=360;
            }
        }
        if (a2 > 360){
            while (a2 > 360){
                a2-=360;
            }
        }
        delta = Math.abs(a1 - a2);
        if (delta > 180) delta = 360 - delta;
        return delta;
    }



    public static float dist(float x1, float y1, float x2, float y2){
        float dx = x2- x1;
        float dy = y2 - y1;
        return (float)Math.sqrt(dy*dy+dx*dx);
    }

    public static float convertAngleToPositive(float angleFromMinus180){
        if (angleFromMinus180<0) {
            return 360+angleFromMinus180;
        }
        else {
            return angleFromMinus180;
        }
    }

    public static boolean isPointInRect(float x, float y, float centerX, float centerY, float width, float height){
        if (x>(centerX-width/2)){
            if (x < (centerX+width/2)) {
                if (y > (centerY-height/2)){
                    if (y < (centerY+height/2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
