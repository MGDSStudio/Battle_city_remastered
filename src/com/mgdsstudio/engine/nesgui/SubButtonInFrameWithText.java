package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

class SubButtonInFrameWithText extends ButtonInFrameWithText {
    protected interface Constants{
        int FULL_CLOSED = 0;
        int OPENING = 1;
        int FULL_OPENED = 2;
        int CLOSING = 3;
    }
    private final static ImageZoneSimpleData upperClosedImageZoneSimpleData = new ImageZoneSimpleData(65, 254, 99, 289);
    private OpeningController openingController;
    //private final float startLeft;
    private final float startUpper;
    private float maxOpenedDistance;
    private int maxNumberInList;
    private int actualNumberInList;


    SubButtonInFrameWithText(IEngine engine, SliderButtonWithText sliderButton, PGraphics graphic, String name, int elementNumber, int numbersInList) {
        super(engine, (int) (sliderButton.leftX+(sliderButton.maxOpenedDistance-(graphic.width-sliderButton.leftX))/2), (int) (sliderButton.upperY+sliderButton.frame.getHeight()/2), (int) (sliderButton.maxOpenedDistance-(graphic.width-sliderButton.leftX)), sliderButton.theoreticFontSize, name, graphic);

        //System.out.println("Center pos for button: " + ((int) (sliderButton.upperY+sliderButton.frame.getHeight()/2)) + " with number: " + elementNumber);
        //System.out.println("Width for gui: " + (sliderButton.maxOpenedDistance-sliderButton.leftX)/2);
        //this.startLeft = sliderButton.leftX;
        this.startUpper = sliderButton.getUpperY();
        //System.out.println("Start upper:  " + this.startUpper + " with number: " + elementNumber);

        maxOpenedDistance = (elementNumber+1)*sliderButton.frame.getHeight()+sliderButton.getUpperY();
        //maxOpenedDistance = (elementNumber+1)*sliderButton.frame.getHeight()+sliderButton.getUpperY();
        //
        this.maxNumberInList = numbersInList;
        this.actualNumberInList = elementNumber;
        openingController = new OpeningController();
        frame.setHeight(sliderButton.frame.getHeight());

    }

    @Override
    protected void updateFunction() {

    }

    protected void drawData(PGraphics graphic, int left) {
        if (actualStatement != PRESSED && actualStatement != RELEASED) drawName(graphic, LEFT_ALIGNMENT_OS_SPECIFIC);
    }

    @Override
    public void draw(PGraphics graphic) {
        if (!openingController.isFullClosed()) super.draw(graphic);
    }


    @Override
    protected void drawName(PGraphics graphics){
        if (actualStatement == BLOCKED){

        }
        else super.drawName(graphics);
    }

    public void update(int mouseX, int mouseY){
        super.update(mouseX, mouseY);
        openingController.update();
    }

    void startToOpen(boolean flag){
        openingController.startToOpen(flag);
    }

    boolean isClosed() {
        return openingController.isFullClosed();
    }

    @Override
    protected final ImageZoneSimpleData getFrameImageZoneSimpleData(){
        return upperClosedImageZoneSimpleData;
    }

    public void backToStart() {
        openingController.backToStart();
    }

    public boolean isOpened() {
        return openingController.isFullOpened();
    }


    private class OpeningController implements Constants{
        private int movingStatement;
        private final int openingTime = 500;
        private float velocity;

        private final boolean DOWNWARDS = true;

        protected OpeningController() {
            movingStatement = FULL_CLOSED;
            initVelocity();
        }
        protected void update(){
            updateStatementChanging();
            updateMovement();
        }


        private void updateMovement() {
            if (actualStatement != HIDDEN){
                if (movingStatement == OPENING){
                    move(DOWNWARDS);
                }
                else if (movingStatement == CLOSING){
                    move(!DOWNWARDS);
                }
            }
        }

        private void move(boolean dir) {
            if (dir == DOWNWARDS) {
                translateGui(getDeltaTime()*velocity, !ALONG_X);
                if (upperY > maxOpenedDistance){
                    translateGui(-(upperY-maxOpenedDistance), !ALONG_X);
                    movingStatement = FULL_OPENED;
                }
            }
            else {
                translateGui(-getDeltaTime()*velocity, !ALONG_X);
                if (upperY < startUpper){
                    translateGui((upperY-startUpper), !ALONG_X);
                    movingStatement = FULL_CLOSED;
                }
            }
        }

        private void updateStatementChanging(){
            if (actualStatement == RELEASED){
                if (movingStatement == FULL_CLOSED){
                    movingStatement = OPENING;
                }
                else if (movingStatement == FULL_OPENED){

                }
            }

        }

        private void initVelocity() {
            velocity = (PApplet.abs((maxOpenedDistance-upperY) /(float)openingTime));
            //System.out.println("Element: " + actualNumberInList + " has velocity: " + velocity + " and max distance: " + maxOpenedDistance);
        }

        public void startToOpen(boolean flag) {
            if (movingStatement == FULL_CLOSED){
                if (flag){
                    movingStatement = OPENING;

                    //System.out.println("List started to open");
                }
            }
            else if (movingStatement == FULL_OPENED){
                if (!flag){
                    movingStatement = CLOSING;
                    //System.out.println("List started to close");
                }
            }
        }

        public boolean isFullClosed() {
            if (movingStatement == FULL_CLOSED) return true;
            else return false;
        }

        public void backToStart() {
            translateGui((startUpper), !ALONG_X);
            movingStatement = FULL_CLOSED;
        }

        public boolean isFullOpened() {
            if (movingStatement == FULL_OPENED) return true;
            else return false;
        }
    }
}
