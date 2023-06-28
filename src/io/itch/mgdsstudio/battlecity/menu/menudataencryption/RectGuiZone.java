package io.itch.mgdsstudio.battlecity.menu.menudataencryption;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import processing.core.PApplet;

public class RectGuiZone {
    private int left, up, right, down;
    private int w, h;

    public RectGuiZone(int left, int up, int right, int down)
    {
        this.left = left;
        this.up = up;
        this.right = right;
        this.down = down;
        this.w = right-left;
        this.h  =  PApplet.abs(down-up);
    }

    public RectGuiZone(GuiElement guiElement)
    {
        this.left = (int) guiElement.getLeftX();
        this.up = (int)guiElement.getUpperY();
        this.right = left+(int)guiElement.getWidth();
        this.down = up+(int)guiElement.getHeight();
        this.w = right-left;
        this.h  =  PApplet.abs(down-up);
    }

    public void init(int left, int up, int right, int down)
    {
        this.left = left;
        this.up = up;
        this.right = right;
        this.down = down;
        this.w = right-left;
        this.h  = PApplet.abs(down-up);
    }

    public void convertInScreenCoordinates(int screenWidth, int screenHeight, int sourceFileWidth, int sourceFileHeight){
        //318 pixel
        //Logger.debug("Button start pos relative angle: " + left + "x" + up + ";" + right + "x" + down);
        float scale = (float)screenWidth/sourceFileWidth;
        //Logger.debug("screen relative to graphic scale = " + scale);
        float displaySidesRelationship = (float)screenWidth/(float)screenHeight;
        float sourceHeightWithoutUnusedZones = (float)sourceFileWidth/displaySidesRelationship;
        float restHeight = sourceFileHeight-sourceHeightWithoutUnusedZones;
        float halfUnusedHeight = restHeight/2f;
        //Logger.debug("Not used lines with height: " + halfUnusedHeight + " and scale: "  + scale + " ");

        float relativeCenterLeft = left-sourceFileWidth/2f;
        float relativeCenterUp = up-sourceFileHeight/2f;
        float relativeCenterRight = right-sourceFileWidth/2f;
        float relativeCenterDown = down-sourceFileHeight/2f;

        //Смещаем координаты в центр

        /*
        float relativeCenterLeft = left-sourceFileWidth/2f;
        float relativeCenterUp = up-sourceFileHeight/2f;
        float relativeCenterRight = right-sourceFileWidth/2f;
        float relativeCenterDown = down-sourceFileHeight/2f;
        */

        float sourceHeightInAccordingToScreenHeight = sourceFileHeight*displaySidesRelationship;

        relativeCenterLeft*=scale;
        relativeCenterRight*=scale;
        relativeCenterUp*=scale;
        relativeCenterDown*=scale;

        float sourceSidesScale = (float)sourceFileHeight/(float)sourceFileWidth;
        float theoreticalScreenHeight = screenWidth*sourceSidesScale;

        /*
        left = (int) (relativeCenterLeft+0);
        up = (int) (relativeCenterUp+0);
        right = (int) (relativeCenterRight+0);
        down = (int) (relativeCenterDown+0);
        */

        left = (int) (relativeCenterLeft+screenWidth/2f);
        up = (int) (relativeCenterUp+screenHeight/2f);
        right = (int) (relativeCenterRight+screenWidth/2f);
        down = (int) (relativeCenterDown+screenHeight/2f);

/*
        left = (int) (relativeCenterLeft+screenWidth/2f);
        up = (int) (relativeCenterUp+theoreticalScreenHeight/2f);
        right = (int) (relativeCenterRight+screenWidth/2f);
        down = (int) (relativeCenterDown+theoreticalScreenHeight/2f);
         */

        /*
        left = (int) (relativeCenterLeft+screenWidth/2);
        up = (int) (relativeCenterUp+theoreticalScreenHeight/2);
        right = (int) (relativeCenterRight+screenWidth/2);
        down = (int) (relativeCenterDown+theoreticalScreenHeight/2);
         */

        //Logger.debug("Button end pos relative angle: " + left + "x" + up + ";" + right + "x" + down);

        w = right-left;
        h = PApplet.abs(down-up);
    }

    public void setLeft(int left)
    {
        this.left = left;
    }

    public int getLeft()
    {
        return left;
    }

    public void setUp(int up)
    {
        this.up = up;
    }

    public int getUp()
    {
        return up;
    }

    public void setRight(int right)
    {
        this.right = right;
    }

    public int getRight()
    {
        return right;
    }

    public void setDown(int down)
    {
        this.down = down;
    }

    public int getDown()
    {
        return down;
    }



    public int getW()
    {
        return w;
    }



    public int getH()
    {
        return h;
    }

    @Override
    public String toString(){
        return "" + left + "x" + up + "; WxH: " + w + "x" + h;
    }

    public int getCenterX(){
        return left+w/2;
    }

    public int getCenterY(){
        if (GlobalConstants.Y_AXIS_DOWN)  return up+h/2;
        else  return up-h/2;
    }

}
