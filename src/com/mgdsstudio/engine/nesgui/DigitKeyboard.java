package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;
import java.util.ArrayList;

public class DigitKeyboard extends Frame{
    public static final String NO_DATA_STRING = "";
    private static final String CLEAR = "CLEAR";
    private static final String BACK = "BACK";
    //private FrameWithText embeddedGui; //COnsole
    private TextLabel embeddedGui; //COnsole
    private int minValue, maxValue;
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
            try {
                ButtonWithFrameSelection button = new ButtonWithFrameSelection(iengine, (int) zones[i].getX(), (int) zones[i].getY(), (int) zones[i].getWidth(), (int) zones[i].getHeight(), getNameForButtonNumber(i), engine.g, true, 1f);
                buttons.add(button);
            }
            catch (Exception e){

            }
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
            case 10: name = CLEAR; break;
            case 11: name = BACK; break;
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
            for (GuiElement guiElement : buttons){
                guiElement.update(mouseX, mouseY);
                if (guiElement.getActualStatement() == PRESSED){
                    pressed = guiElement;
                    actualStatement = PRESSED;
                    somePressed = true;
                }
                if (guiElement.getActualStatement() == RELEASED){
                    released = guiElement;
                    actualStatement = RELEASED;
                    someReleased = true;
                    updateTextEntering();
                }
            }
            if (!someReleased) {
                released = null;
            }
            if (!somePressed) {
                pressed = null;
            }
        }
    }

    private void updateTextEntering() {
        if (embeddedGui != null){
            if (embeddedGui instanceof TextLabel){
                TextLabel frameWithText = (TextLabel) embeddedGui;
                String origData = ""+frameWithText.getTextToBeDrawn();
                if (released.getName().equals(CLEAR)){
                    frameWithText.setAnotherTextToBeDrawnAsName(NO_DATA_STRING);
                }
                else if (released.getName().equals(BACK)){

                    if (origData.length()==0){
                        //NOTHING
                    }
                    else  if (origData.length()==1){
                        origData = "";
                    }
                    else {
                        origData = origData.substring(0, origData.length()-1);
                    }
                    try{
                        int value = Integer.parseInt(origData);
                        if (value< minValue) value = minValue;
                        origData = ""+value;
                    }
                    catch (Exception e){
                        Logger.error("Not a digit string " + origData);
                    }
                    frameWithText.setAnotherTextToBeDrawnAsName(origData);
                }
                else {
                    try {
                        int value = Integer.parseInt(released.getName());
                        origData+=value;
                        if (maxValue != minValue){
                            try{
                                int intValue = Integer.parseInt(origData);
                                if (intValue> maxValue) intValue = maxValue;
                                origData = ""+intValue;
                            }
                            catch (Exception e){
                                Logger.error("Not a digit string " + origData);
                            }
                        }
                        frameWithText.setAnotherTextToBeDrawnAsName(origData);
                    }
                    catch (Exception e){
                        Logger.debug("Not digit data was entered!");
                    }
                }
                frameWithText.setUserData(origData);
            }

        }
    }

    public String getPressedKeyName(){
        if (pressed != null){
            return pressed.getName();
        }
        else return NO_DATA_STRING;
    }

    public String getReleasedKeyName(){
        if (released != null){
            return released.getName();
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

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    protected Rectangle[] getCoordinatesForSquareButtonsAndColumnAlignment(int fullCount, int alongX){
        int fullWidth = width;
        int fullHeight = height;
        int left = (int) leftX;
        int upper = (int) upperY;
        float relativeGap = 0.05f;
        float relativeGapOnBoard = 0.075f;
        float xGapInCenter = fullWidth*relativeGap;
        float xGapOnBoard = fullWidth*relativeGapOnBoard;
        float allGapsAlongX = xGapOnBoard*2+(alongX-1)*xGapInCenter;
        float widthForGui = fullWidth-allGapsAlongX;
        int guiWidth = (int)(widthForGui/alongX);
        int doubleGuiWidth = (int) (guiWidth*2+xGapInCenter);

        int alongY = PApplet.ceil((float)fullCount/alongX);
        float yGapInCenter = fullWidth*relativeGap;
        float yGapOnBoard = fullWidth*relativeGapOnBoard;
        float allGapsAlongY = yGapOnBoard*2+(alongY-1)*yGapInCenter;
        float heightForGui = fullHeight-allGapsAlongY;
        int guiHeight = (int) (heightForGui/alongY);

        Rectangle [] positions = calculatePositionsForParams(guiWidth, guiHeight, alongX, alongY, left, upper, (int)xGapOnBoard, (int)xGapInCenter, (int)yGapOnBoard, (int)yGapInCenter, fullCount, doubleGuiWidth);
        return positions;
    }

    private Rectangle [] calculatePositionsForParams(int guiWidth, int guiHeight, int alongX, int alongY, int left, int upper, int xGapOnBoard, int xGapInCenter, int yGapOnBoard, int yGapInCenter, int max, int doubleGuiWidth){
        Rectangle [] positions = new Rectangle[alongX*alongY];

        int fullCount = 0;
        for (int i = 0; i < alongY; i++){
            for (int j = 0; j < alongX; j++){
                Rectangle rect ;
                int centerX;
                int centerY = yGapOnBoard+guiHeight/2+((yGapInCenter+guiHeight)*i)+upper;
                if (i == (alongY-1)){
                      if (j == 0){
                          centerX = xGapOnBoard+xGapInCenter+doubleGuiWidth/2+left;
                      }
                      else centerX = (left+width)-xGapOnBoard-xGapInCenter-doubleGuiWidth/2;
                      rect = new Rectangle(centerX, centerY, doubleGuiWidth, guiHeight);
                }
                else {
                    centerX = xGapOnBoard+guiWidth/2+((xGapInCenter+guiWidth)*j)+left;
                    rect = new Rectangle(centerX, centerY, guiWidth, guiHeight);
                }
                int number = j+i*alongX;
                positions[number] = rect;
                fullCount++;
                //Logger.debug(i + "x" + j + "; " + rect + "; Along Y: " + alongY + "; ");
                if (fullCount == max){
                    return positions;
                }
            }
        }
        return positions;

   

    }

    public void setEmbeddedGui(TextLabel embeddedGui) {
        this.embeddedGui = embeddedGui;
    }
}
