package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PGraphics;

import java.util.ArrayList;

public class RadioButton extends AbstractCheckableElement {
    private final static ImageZoneSimpleData clearFrameData = new ImageZoneSimpleData(68,0,102,33);
    private final static ImageZoneSimpleData selectedWithoutFlagFrameData = new ImageZoneSimpleData(68+34,0,102+34,33);
    private final static ImageZoneSimpleData flagSetFrameData = new ImageZoneSimpleData(68+34,32,102+34,66);
    private final static ImageZoneSimpleData selectedWithFlagFrameData = new ImageZoneSimpleData(85+33,221,119+33,255);

    private ArrayList<RadioButton> anotherMarkers;

    //private int prevStatement;

    public RadioButton(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF );
        framePosY = centerY;
        frameHeight = (int) (height*effectiveHeightCoef);
        framePosX = centerX;
        actualStatement = ACTIVE;
        prevStatement = ACTIVE;
        anotherMarkers = new ArrayList<>();
    }

    static void addButtonToList(ArrayList<GuiElement> elements, RadioButton radioButton) {
        for (GuiElement guiElement : elements){
            if (guiElement instanceof RadioButton){
                RadioButton existed = (RadioButton) guiElement;
                existed.addToGroup(radioButton);
                radioButton.addToGroup(existed);
            }
        }
    }

    public void addToGroup(RadioButton marker){
        if (!this.equals(marker)) {
            if (!anotherMarkers.contains(marker)){
                if (usingConsoleOutput) System.out.println("Element " + marker.name + " was added to group");
                anotherMarkers.add(marker);
            }
        }
    }

    public void addToGroup(ArrayList <GuiElement> guiElements){
        for (GuiElement guiElement : guiElements) {
            if (guiElement.getClass() == RadioButton.class) {
                RadioButton marker = (RadioButton) guiElement;
                /*if (!marker.equals(marker)) {
                    if (!anotherMarkers.contains(marker)) {
                        System.out.println("Element " + marker.name + " was added to group");
                        anotherMarkers.add(marker);
                    }
                    else {
                        System.out.println("Element " + marker.name + " was added to group");
                        anotherMarkers.add(marker);
                    }
                }
                else {*/
                    System.out.println("Element " + marker.name + " was added to group");
                    anotherMarkers.add(marker);
                //}
            }
        }
    }

    @Override
    protected void updateFunction() {

    }





    @Override
    public void update(int x, int y){

        super.update(x,y);
        if (prevStatement == ACTIVE && actualStatement == PRESSED) {
            if (!flagSet){
                flagSet = true;
                clearAnotherFlags();
            }
        }
        /*if (prevStatement != actualStatement){

        }*/
    }

    private void clearAnotherFlags() {
        //System.out.println("in group " + anotherMarkers.size() );
        for (RadioButton marker : anotherMarkers){
            if (!marker.equals(this)){
                marker.flagSet = false;
            }
        }
    }


    protected ImageZoneSimpleData getActualImageZOneData(){
        if (actualStatement == PRESSED) {
            if (flagSet) return selectedWithFlagFrameData;
            else return selectedWithoutFlagFrameData;
        }
        else {
            if (flagSet) return flagSetFrameData;
            else return clearFrameData;
        }
    }
}
