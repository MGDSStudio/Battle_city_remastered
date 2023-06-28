package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ScrollArrow extends GuiElement {
    /*
    protected int upLeftX, upUpperY, downLeftX, downUpperY;
        protected int arrowWidth, arrowHeight;
     */
    private float velocity;

    public final static boolean DIRECTION_UP = true;
    public final static boolean DIRECTION_DOWN = false;
    protected final static ImageZoneSimpleData arrowUpData = new ImageZoneSimpleData(0,221,59,267);
    protected final static ImageZoneSimpleData arrowDownData = new ImageZoneSimpleData(0,267,59,313);
    protected final static ImageZoneSimpleData arrowUpDataCleared = new ImageZoneSimpleData(0,221+149,59,267+149);
    protected final static ImageZoneSimpleData arrowDownDataCleared = new ImageZoneSimpleData(0,267+149,59,313+149);
    private final Tab tab;
    private boolean dir;
    private boolean withArrows = true;
   // private boolean isible = true;


    public ScrollArrow(IEngine engine, int left, int upper, int width, PGraphics graphics, boolean dir, Tab tab) {
        super(engine, left+width/2, upper+calculateHeight(width)/2, width, calculateHeight(width), generateName(dir), graphics);
        this.dir = dir;
        this.tab = tab;
        if (usingConsoleOutput) System.out.println("Arrow place: " + leftX + "x" + upperY + " and dims: " + width + "x" + height + " by screen width: " + graphics.width);
        velocity = tab.visibleHeight*2f/1000f;
    }
    private static String generateName(boolean dir){
        if (dir == DIRECTION_UP) return "up";
        else return "down";

    }

    private static int calculateHeight(int width){
        float imageBasicWidth = arrowUpData.rightX-arrowUpData.leftX;
        float imageBasicHeight = arrowUpData.lowerY-arrowUpData.upperY;
        float relativeHeight = imageBasicHeight/imageBasicWidth;

        return (int)(relativeHeight*width);
    }

    @Override
    public void update(int mouseX, int mouseY){
        if (withArrows){
            super.update(mouseX, mouseY);
            if (actualStatement == PRESSED){
                if (dir == DIRECTION_UP) tab.scrollUp(velocity*getDeltaTime());
                else tab.scrollDown(velocity*getDeltaTime());
            }
        }
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    @Override
    protected void updateFunction() {

    }

    protected void setWithArrows(boolean with){
        withArrows = with;
    }

    @Override
    public void draw(PGraphics graphics) {

            if (withArrows){
                graphics.pushStyle();
                graphics.imageMode(PConstants.CORNER);

                if (actualStatement != HIDDEN) {
                    if (actualStatement != PRESSED && actualStatement != RELEASED) {
                        if (dir == DIRECTION_UP) {
                            //System.out.println("Drawn as up and full visible at: " + leftX + "x" + upperY );
                            graphics.image(graphicFile.getImage(), leftX, upperY, width, height, arrowUpData.leftX, arrowUpData.upperY, arrowUpData.rightX, arrowUpData.lowerY);
                        }
                        else graphics.image(graphicFile.getImage(), leftX, upperY, width, height, arrowDownData.leftX, arrowDownData.upperY, arrowDownData.rightX, arrowDownData.lowerY);
                    }
                    else {
                        if (dir == DIRECTION_UP) {
                            //System.out.println("Drawn as up at: " + leftX + "x" + upperY );
                            graphics.image(graphicFile.getImage(), leftX, upperY, width, height, arrowUpDataCleared.leftX, arrowUpDataCleared.upperY, arrowUpDataCleared.rightX, arrowUpDataCleared.lowerY);
                        }
                        else graphics.image(graphicFile.getImage(), leftX, upperY, width, height, arrowDownDataCleared.leftX, arrowDownDataCleared.upperY, arrowDownDataCleared.rightX, arrowDownDataCleared.lowerY);

                    }
                }
                //else System.out.println("It is invisible");

                //graphics.image(graphicFile.getImage(), downLeftX, downUpperY, arrowWidth, arrowHeight, arrowDownData.leftX, arrowDownData.upperY, arrowDownData.rightX, arrowDownData.lowerY);

                graphics.popStyle();
            }

    }


}
