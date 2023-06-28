package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

public class FrameWithMoveableText extends FrameWithText {
    private final static ImageZoneSimpleData openedImageZoneSimpleDataWithBlackBackground = new ImageZoneSimpleData(98, 254, 131, 289);
    private String basicString;
    private final PGraphics graphics;

    private  MovementController movementController;
    public FrameWithMoveableText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, String text) {
        super(engine, centerX, centerY, width, height, name, graphics);
        this.graphics = graphics;
        basicString = text;
        maxChars = getMaxCharsForGui(graphics, frame.getWidth(), text);
        setAnotherTextToBeDrawnAsName(text);
        //System.out.println("This console can have only " + maxChars + " chars ");
        movementController = new MovementController();
    }


    public void changeConsoleText(String anotherTextToBeDrawnAsName){
        basicString = anotherTextToBeDrawnAsName;
        maxChars = getMaxCharsForGui(graphics, frame.getWidth(), anotherTextToBeDrawnAsName);
        System.out.println("Text: " + anotherTextToBeDrawnAsName);
        movementController = new MovementController();
    }

    @Override
    protected void updateFunction() {

    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);
        movementController.update();

    }

    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        //System.out.println("Chars: " + getMaxCharsForGui(graphics, frame.getWidth(), getTextToBeDrawn()) + " length: " + graphics.textWidth(getTextToBeDrawn()));
    }
    public String getData(){
        return data;
    }



    /*protected void updateDataFilling() {
        if (engine.keyPressed) {
            if (!charWasAdded) {
                char newChar = engine.key;
                Character character = newChar;

                charWasAdded = updateControlCharsAdding(newChar);
                if (!charWasAdded) {
                    if (newChar != '-' && newChar != '%' && newChar != '/' && newChar != '^') {
                        PFont.Glyph glyph = font.getGlyph(character);
                        if (glyph!=null) {
                            addCharOnCursorPlace(newChar);
                            charWasAdded = true;
                        }
                        else System.out.println("This char can not be added to the string");
                    }
                }
                if (!charWasAdded)
                {
                    System.out.println("New char: " + newChar + " with code " + engine.keyCode +  " can not be added" );
                    charWasAdded = true;
                }
            }
        }
        else{
            charWasAdded = false;
        }

    }*/

    @Override
    ImageZoneSimpleData getFrameImageZoneSimpleData() {
        //final ImageZoneSimpleData openedImageZoneSimpleDataWithBlackBackground = new ImageZoneSimpleData(98, 254, 131, 289);
        return openedImageZoneSimpleDataWithBlackBackground;
    }

    @Override
    protected void drawData(PGraphics graphic, int side) {
        //getMaxCharsForGui(graphic, frame.getWidth(), basicString);
        drawName(graphic, side);

    }

    private class MovementController {
        private final boolean MOVING = true;
        private final boolean HOLDING = false;
        private boolean movingStatement;
        private float velocity;

        private Timer timer;
        private final int NEXT_CHAR_TIME = 150;
        private String actualDrawnString;
        private int actualStartChar, actualEndChar;
        private boolean moveable = true;
        private float transfering = 0;
        private final boolean FORWARD = true;


        public MovementController() {
            movingStatement = MOVING;
            timer = new Timer(NEXT_CHAR_TIME, engine);
            actualStartChar = 0;
            actualEndChar = 1;
            if (maxChars > basicString.length()-1) {
                moveable = false;
                actualDrawnString = basicString;
                setAnotherTextToBeDrawnAsName(actualDrawnString);
            }
            else {
                actualDrawnString = basicString.substring(actualStartChar, actualEndChar);
            }
           // System.out.println("In this console frame with text " + maxChars + " chars in this string");
            //System.out.println("Substring is: " + actualDrawnString);
        }

        protected void update(){

            if (moveable) {
                updateStatementChanging();
                updateMovement();
            }
        }

        private void updateMovement() {
            if (movingStatement == MOVING){
                if (timer.isTime()){
                    timer.setNewTimer(NEXT_CHAR_TIME);
                    shiftStringToBeDrawn(FORWARD);
                    setAnotherTextToBeDrawnAsName(actualDrawnString);

                }
            }
        }

        private void shiftWhenStringFilledWithChars(){
            if (actualStartChar>=0) {
                actualDrawnString = basicString.substring(actualStartChar, actualEndChar);
            }
            else{
                actualDrawnString = "";
                for (int i = 0; i < PApplet.abs(actualStartChar); i++){
                    actualDrawnString+=' ';
                }
                actualDrawnString+=basicString.substring(0, actualEndChar);
            }
        }

        private void shiftWhenStringHasWhitespaces(){
            if (actualStartChar <= basicString.length()){
                String restString = "";
                for (int i = 0; i < maxChars-(basicString.length()-actualStartChar); i++){
                    restString+=' ';
                }
                if (actualStartChar >= 0) actualDrawnString = basicString.substring(actualStartChar)+ restString;
                else {
                    //actualDrawnString = basicString.substring(actualStartChar)+ restString;

                    actualDrawnString = "";
                    actualDrawnString+=restString;
                    int freeSpacesCount = maxChars-restString.length();
                    for (int i = 0; i < freeSpacesCount; i++) {
                        actualDrawnString+=' ';
                    }
                    System.out.println("Actual char: " + actualStartChar + " end: " + actualEndChar + " full text width: " );
                }
            }
            else {
                actualStartChar = -maxChars;
                actualEndChar = 0;
                actualDrawnString = " ";
            }
        }

        private void shiftStringToBeDrawn(boolean dir) {
            if (dir){
                actualStartChar++;
            }
            else{
                actualStartChar--;
            }
            actualEndChar=actualStartChar+maxChars;
            if (actualEndChar < basicString.length()){
                shiftWhenStringFilledWithChars();
            }
            else if (actualEndChar >= basicString.length()){
                shiftWhenStringHasWhitespaces();
            }
        }



        private void updateStatementChanging(){
            if (actualStatement == RELEASED || actualStatement == PRESSED){
                if (movingStatement == MOVING){
                    movingStatement = HOLDING;
                }
                if (actualStatement == PRESSED){
                    updateBraking();
                }
            }
            else {
                transfering = 0;
                if (actualStatement == ACTIVE){
                    if (movingStatement == HOLDING){
                        movingStatement = MOVING;
                    }
                }
            }

        }

        private void updateBraking() {
            if (engine.pmouseX!=engine.mouseX){
                float stepTransfering = engine.mouseX-engine.pmouseX;
                transfering+=stepTransfering;
                if (PApplet.abs(transfering) > getCharWidth()){
                    if (transfering > 0){
                        shiftStringToBeDrawn(!FORWARD);
                    }
                    else shiftStringToBeDrawn(FORWARD);
                    timer.setNewTimer(NEXT_CHAR_TIME);
                    setAnotherTextToBeDrawnAsName(actualDrawnString);
                    System.out.println("String was shifted");
                    transfering = 0;
                }

            }
        }

        private int getCharWidth(){
            return (int) ((float)frame.getWidth()/(maxChars+1f));
        }
    }
}
