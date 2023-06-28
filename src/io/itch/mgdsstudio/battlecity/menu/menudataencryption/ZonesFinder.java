package io.itch.mgdsstudio.battlecity.menu.menudataencryption;

import io.itch.mgdsstudio.battlecity.game.Logger;
import processing.core.PApplet;
import processing.core.PImage;

public class ZonesFinder {
    private PImage image;
    private PApplet engine;
    private final static int LEFT = 0;
    private final static int UP = 1;
    private final static int RIGHT = 2;
    private final static int DOWN = 3;
    private int imageWidth, imageHeight;
    private int displayWidth, displayHeight;
    //private int upperPixel, lowerPixel;
    private float scale ;

    public ZonesFinder(PImage image, int displayWidth, int displayHeight)
    {
        this.engine = image.parent;
        this.image = image;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        imageWidth = image.width;
        imageHeight = image.height;
        scale = (float)displayWidth/(float)imageWidth;
        //int visibleImageHeight = (int)scale*displayHeight;
        //upperPixel = imageHeight/2-visibleImageHeight/2;
        //lowerPixel = imageHeight/2+visibleImageHeight/2;
    }

    public RectGuiZone getRectZoneForColor(int r, int g, int b){
        image.loadPixels();
        int [] pixels = image.pixels;
        int color;
        RectGuiZone zone = null;
        for (int i = 0; i < pixels.length; i++){
            color = pixels[i];
            if (engine.red(color)==r){
                if (engine.green(color)==g){
                    if (engine.blue(color)==b){
                        zone = createRectZoneFromLeftUpperPixel(r,g,b,i, pixels);
                        return zone;
                    }
                }
            }
        }
        return zone;
    }

    private RectGuiZone createRectZoneFromLeftUpperPixel(int r, int g, int b, int number, int [] pixels){
        RectGuiZone zone = null;
        int left = getCoordinate(LEFT, pixels, r, g, b);
        int up = getCoordinate(UP, pixels, r, g, b);
        int right = getCoordinate(RIGHT, pixels, r, g, b);
        int down = getCoordinate(DOWN, pixels, r, g, b);
        zone = new RectGuiZone(left, up, right, down);
        zone.convertInScreenCoordinates(engine.width, engine.height, imageWidth, imageHeight);
        //Logger.debug("Zone pos: " + left + "x" + up + ";" + right + "x" + down);
        //shiftZoneAccordingToScale(zone);
        return zone;
    }



    private void shiftZoneAccordingToScale(RectGuiZone zone){
        int centerX = imageWidth/2;
        int centerY = imageHeight/2;
        float halfScale = scale/2f; // not half
        float left = zone.getLeft()-centerX;
        float right = zone.getRight()-centerX;
        float upper = zone.getUp()-centerY;
        float lower = zone.getDown()-centerY;
        //Logger.debug("Zone after shifting: " + left + "x" + upper + ";" + right + "x" + lower);
        left*=halfScale;
        right*=halfScale;
        upper*=halfScale;
        lower*=halfScale;
        left+=centerX;
        right+=centerX;
        upper+=centerY;
        lower+=centerY;
        //Logger.debug("Scale: " + halfScale);
        //Logger.debug("Zone pos absolute: " + zone.toString());
        zone.init((int)left, (int)upper, (int)right, (int)lower);
        //Logger.debug("Zone pos on screen: " + zone.toString());
    }


    /*
    private void shiftZoneAccordingToScale(RectGuiZone zone){
        int centerX = imageWidth/2;
        int centerY = imageHeight/2;
        float halfScale = scale/2f;
        float left = zone.getLeft()-centerX;
        float right = zone.getRight()-centerX;
        float upper = zone.getUp()-centerY;
        float lower = zone.getDown()-centerY;
        left*=halfScale;
        right*=halfScale;
        upper*=halfScale;
        lower*=halfScale;
        left-=centerX;
        right-=centerX;
        upper-=centerY;
        lower-=centerY;
        //Logger.debug("Zone pos absolute: " + zone.toString());
        zone.init((int)left, (int)upper, (int)right, (int)lower);
        //Logger.debug("Zone pos on screen: " + zone.toString());
    }
     */

    private int getCoordinate(int which, int [] pixels , int r, int g, int b){
        int color = engine.color(r,g,b);
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i] == color){
                if (which == LEFT) {
                    int leftCoordinate = getXforAbsoluteNumber(i);
                    //Logger.debug("Left is on: " + leftCoordinate);
                    return leftCoordinate;
                }
                else if (which == UP) {
                    int upCoordinate = getYforAbsoluteNumber(i);
                    //Logger.debug("Up is on: " + upCoordinate);
                    return upCoordinate;
                }
                else if (which == RIGHT){
                    for (int j = i+1; j < pixels.length; j++){
                        if (pixels[j] != color){
                            j--;
                            int rightPos = getXforAbsoluteNumber(j);
                            //Logger.debug("Right is on: " + rightPos);
                            return rightPos;
                        }
                    }
                }
                else if (which == DOWN){
                    int startAbsoluteNumber = i+imageWidth;
                    for (int j = startAbsoluteNumber; j < pixels.length; j+=imageWidth){
                        if (pixels[j] != color){
                            j-=imageWidth;
                            int lowerPos = getYforAbsoluteNumber(j);
                            //Logger.debug("Lower is on: " + lowerPos);
                            return lowerPos;
                        }
                    }
                }
            }
        }
        Logger.error("Can not find color on the image");
        return -2;
    }

    private int getXforAbsoluteNumber(int absolute){
        return absolute%imageWidth;
    }

    private int getYforAbsoluteNumber(int absolute){
        return PApplet.floor(absolute/imageWidth);
    }

    private int getAbsoluteForCoordinate(int x, int y){
        return y*imageWidth+x;
    }


}
