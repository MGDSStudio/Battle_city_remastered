package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class ListButton extends ElementWithCursor {
    //public final static int NORMAL_WIDTH = (int) (engine.width*0.75f);
    private ArrayList<String> actions;
    private ArrayList<String> anotherTextToBeDrawnAsActions;
    private int selectedString;
    protected static ImageZoneSimpleData cursorImageZoneSimpleData;
    protected final static float CURSOR_DIMENSIONS_COEF = 1.2f;


    public ListButton(IEngine engine, int centerX, int centerY, int width, int height, String name, ArrayList <String> actions, int selectedString, PGraphics graphics){
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF);
        
        this.actions = actions;
        /*System.out.println("This button has functions:");
        for (String string : actions){
            System.out.println(" " + string + ";");
        }
        System.out.println("");*/
        this.selectedString = selectedString;
    }

    public void draw(PGraphics graphic){
        super.draw(graphic);
        if (actualStatement != HIDDEN) {
            drawName(graphic);
            drawFunctionsText(graphic);
            drawCursor(graphic);
        }
    }



    private void drawFunctionsText(PGraphics graphic){
        graphic.pushStyle();
        if (actualStatement == BLOCKED) graphic.tint(BLOCKED_COLOR);
        else graphic.tint(colorRed, colorGreen, colorBlue);
        graphic.textAlign(LEFT_ALIGNMENT_OS_SPECIFIC, PConstants.CENTER);
        graphic.textSize(height);
        if (anotherTextToBeDrawnAsActions == null) {
            graphic.text(actions.get(selectedString), graphic.width - leftX - graphic.textWidth(actions.get(selectedString)), y);
        }
        else {
            graphic.text(anotherTextToBeDrawnAsActions.get(selectedString), graphic.width - leftX - graphic.textWidth(anotherTextToBeDrawnAsActions.get(selectedString)), y);
        }
        graphic.popStyle();

    }

    @Override
    protected void initGraphic(){
        super.initGraphic();
        if (cursorImageZoneSimpleData == null){
            cursorImageZoneSimpleData = new ImageZoneSimpleData(0,0,32,32);
        }
    }



    @Override
    protected void updateFunction() {
        if (actualStatement == RELEASED){
            selectedString++;
            if (selectedString > (actions.size()-1)){
                selectedString = 0;
            }
        }
    }

    public String getSelectedString(){
        return actions.get(selectedString);
    }

    public void setSelectedString(int selectedString) {
        this.selectedString = selectedString;
    }

    public void setSelectedString(String string) {
        boolean founded = false;
        for (int i = 0; i < actions.size(); i++){
            if (actions.get(i) == string || actions.get(i).equals(string)){
                founded = true;
                this.selectedString = i;
            }
        }
        if (!founded){
            System.out.println("Can not find the string with name " + string);
        }
    }

    protected void drawCursor(PGraphics graphics){
        if (actualStatement == PRESSED || actualStatement == BLOCKED) {
            if (graphics != null) {
                graphics.pushStyle();
                graphics.imageMode(PApplet.CENTER);
                if (actualStatement == BLOCKED){
                    graphics.image(graphicFile.getImage(),  getCursorPosX(), getCursorPosY(), CURSOR_DIMENSIONS_COEF*height, CURSOR_DIMENSIONS_COEF*height, lockImageZoneSimpleData.leftX, lockImageZoneSimpleData.upperY, lockImageZoneSimpleData.rightX, lockImageZoneSimpleData.lowerY);
                }
                else graphics.image(graphicFile.getImage(),  getCursorPosX(), getCursorPosY(), CURSOR_DIMENSIONS_COEF*height, CURSOR_DIMENSIONS_COEF*height, cursorImageZoneSimpleData.leftX, cursorImageZoneSimpleData.upperY, cursorImageZoneSimpleData.rightX, cursorImageZoneSimpleData.lowerY);
                graphics.popStyle();
            }
        }
        else {
            //System.out.println("It is not pressed");
        }
    }

    public void setAnotherTextToBeDrawnAsActions(ArrayList<String> anotherTextToBeDrawnAsActions) {
        this.anotherTextToBeDrawnAsActions = anotherTextToBeDrawnAsActions;
    }

    //protected void drawHeadCursor();
}
