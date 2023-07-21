package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public class DigitKeyboard extends Frame{
    public static final String NO_DATA_STRING = "";
    private GuiElement embeddedGui; //COnsole
    protected final static ImageZoneSimpleData frameImageZoneSimpleDataWithoutBackground = new ImageZoneSimpleData(0, 462, 33, 495);
    protected EightPartsFrameImage frame;
private ArrayList<ButtonWithFrameSelection> buttons;
    private final IEngine iengine;
    private GuiElement pressed, released;

    public DigitKeyboard(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(engine, centerX, centerY, width, (height), name, graphics);
        buttons = new ArrayList<>();
        iengine = engine;
        initButtons();
    }

    void initButtons(){
    /*
    1 2 3 BACKSPACE
    4 6 7 CLEAR
    8 9 0 ENTER
    */

    //WIDTHES
    // x x x 3x

        Rectangle[] zones = getCoordinatesForSquareButtonsAndColumnAlignment(12,5);
        for (int i = 0; i < zones.length; i++){
             ButtonWithFrameSelection button = new ButtonWithFrameSelection(iengine, (int)zones[i].getX(), (int)zones[i].getY(), (int)zones[i].getWidth(), (int)zones[i].getHeight(), getNameForButtonNumber(i), engine.g, true, 1f);
             buttons.add(button);
        }
    }

    private String getNameForButtonNumber(int i) {
        String name = "";
        switch(i){
            case 0: name = "1"; break;
            case 1: name = "2"; break;
            case 2: name = "3"; break;
            case 3: name = "4"; break;
            case 4: name = "5"; break;
            case 5: name = "6"; break;
            case 6: name = "7"; break;
            case 7: name = "8"; break;
            case 8: name = "9"; break;
            case 9: name = "0"; break;
            case 10: name = "CLEAR"; break;
            case 11: name = "ENTER"; break;
            default: name = "NO DATA"; break;
        }

        return name;
    }

    protected void drawButtons(PGraphics graphics) {
        if (actualStatement != HIDDEN) {
            if (graphics != null) {
                graphics.resetMatrix();
                for (ButtonWithFrameSelection button : buttons){
                     button.draw(graphics);
                }
            }
        }
    }

    public void update(int mouseX, int mouseY){
        //updatePrevPressed();
        super.update(mouseX, mouseY);
        if (actualStatement == PRESSED || actualStatement == RELEASED){
            actualStatement = ACTIVE;

        }
        if (actualStatement != HIDDEN && actualStatement != BLOCKED){
            boolean somePressed = false;
            boolean someReleased = false;
           // Logger.debug(" Keyboard updated");
            for (GuiElement guiElement : buttons){
                guiElement.update(mouseX, mouseY);
                if (guiElement.getActualStatement() == PRESSED){
                    pressed = guiElement;
                    somePressed = true;
                }
                if (guiElement.getActualStatement() == RELEASED){
                    released = guiElement;
                    someReleased = true;
                }
            }
            if (!someReleased) {
                //Logger.debug(" Keyboard released");
                released = null;
            }
            if (!somePressed) {
                //Logger.debug(" Keyboard pressed");
                pressed = null;
            }
        }
    }

    public String getPressedName(){
        if (pressed != null){
            return pressed.getName();
        }
        else return NO_DATA_STRING;
    }

    public String getReleasedName(){
        if (pressed != null){
            return pressed.getName();
        }
        else return NO_DATA_STRING;
    }

    @Override
    public int getActualStatement() {
        if (released != null) return RELEASED;
        else if (pressed != null ) return  PRESSED;
        else {
            if (actualStatement != HIDDEN && actualStatement != BLOCKED){
                return ACTIVE;
            }
            else return actualStatement;
        }

    }

    @Override
    public void draw(PGraphics graphic) {
        super.draw(graphic);
        for (GuiElement guiElement : buttons){
            guiElement.draw(graphic);

        }
    }

    @Override
    protected void updateFunction() {

    }


protected Rectangle[] getCoordinatesForSquareButtonsAndColumnAlignment(int fullCount, int alongX){
        int fullWidth = width;
        int fullHeight = height;
        int left = (int) leftX;
        int upper = (int) upperY;
        int alongY = PApplet.ceil(fullCount/alongX);
        float xRelativeGap = 0.05f;
        float xRelativeGapOnBoard = 0.75f;
        float xGapInCenter = fullWidth*relativeGap;
        float xGapOnBoard = fullWidth*xRelativeGapOnBoard;
        
    float fullRelativeGapX = xGapOnBoard*2+(alongX-1)*relativeGap;
        Logger.debug("Full realative x: " + fullRelativeGapX);
        
        float restWidth = fullWidth-
        int guiWidth = alongX
    
           
    
        float fullRelativeGapY = (alongY+1f)*relativeGap;
        float fullGapX =  (float) fullWidth*fullRelativeGapX;
        float fullGapY = (float) fullHeight*fullRelativeGapY;

        float minimalFullGap;
        int guiWidth;
        int guiHeight;
        int theoreticalGuisAlongX = alongX+3;
         
        minimalFullGap = fullGapX;
        int xCentralGap = (int)(minimalFullGap/(alongX+1f));
        guiWidth = (int) ((fullWidth-minimalFullGap)/theoreticalGuisAlongX);
        
        minimalFullGap = fullGapY;
        int yGap = (int)(minimalFullGap/(alongY+1f));
        guiHeight = (int) ((fullHeight-minimalFullGap)/alongY);
        Rectangle [] positions = calculatePositionsForParams(guiWidth, guiHeight, alongX, alongY, left, upper, xGap, yGap, 3);
        return positions;
    }

    private Rectangle [] calculatePositionsForParams(int guiWidth, int guiHeight, int alongX, int alongY, int left, int upper, int gapX, int gapY, int lastColumnCoef){
        Rectangle [] positions = new Rectangle[alongX*alongY];
        int lastColumnGuiWidth = guiWidth*lastColumnCoef;
        Logger.debug("Last button width: " + lastColumnCoef);
        int fullCount = 0;
        for (int i = 0; i < alongY; i++){
            for (int j = 0; j < alongX; j++){
                 Rectangle rect ;
                if (j != (alongX-1)){
                      int centerX = gapX+guiWidth/2+j*(guiWidth+gapX);
                      int centerY = gapY+guiHeight/2+i*(guiHeight+gapY);
                      rect = new Rectangle(centerX+left, centerY+upper, guiWidth, guiHeight);
                }
                else {
                      int leftCorner = gapX+(j*gapX+guiWidth);
                      int centerX = leftCorner+lastColumnGuiWidth/2;
                      int centerY = gapY+guiHeight/2+i*(guiHeight+gapY);
                      rect = new Rectangle(centerX+left, centerY+upper, guiWidth, guiHeight);
   
                }
                int number = j+i*alongX;
                positions[number] = rect;
                fullCount++;
            }
        }
        return positions;

   

    }

    public void setEmbeddedGui(GuiElement embeddedGui) {
        this.embeddedGui = embeddedGui;
    }
}
