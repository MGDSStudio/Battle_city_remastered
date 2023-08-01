package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

public class SliderButtonWithText extends ButtonInFrameWithText {
    protected interface Constants{
        int FULL_CLOSED = 0;
        int OPENING = 1;
        int FULL_OPENED = 2;
        int CLOSING = 3;
    }
    private final static ImageZoneSimpleData openedImageZoneSimpleDataWithBlackBackground = new ImageZoneSimpleData(98, 254, 131, 289);


    //private final static float heightRelativeCoef = 0.65f;
    private final int startLeft;
    protected int maxOpenedDistance;

    private MovementController movementController;

    private SubButtonInFrameWithText[] subButtons;
    private IEngine engineInterface;


    public SliderButtonWithText(IEngine engine, int left, int upper, int width, int frameHeight, String name, PGraphics graphics) {
        super(engine, left+width/2, (int) (upper+frameHeight/2), width, frameHeight/ListButton.CURSOR_DIMENSIONS_COEF*heightRelativeCoef, name, graphics);
        this.engineInterface = engine;
        this.startLeft = left;
        maxOpenedDistance = left-(int) (width*0.75f);
        movementController = new MovementController();
    }

    /*
    public SliderButtonWithText(int left, int upper, int width, int textHeight, String name, PGraphics graphics) {
        super(left+width/2, upper+textHeight/2, width, textHeight, name, graphics);
        this.startLeft = left;
        maxOpenedDistance = left-(int) (width*0.75f);
        movementController = new MovementController();
    }
     */
    
    public void addSubButtonsList(String [] names, PGraphics graphics){
        subButtons = new SubButtonInFrameWithText[names.length];
        for (int i = 0; i < names.length; i++){
            subButtons[i] = new SubButtonInFrameWithText(engineInterface, this, graphics, names[i],  i, names.length);

        }
        //System.out.println("Added: " + subButtons.length + " buttons");
    }

    @Override
    protected void updateFunction() {

    }

    public void update(int mouseX, int mouseY){
        super.update(mouseX, mouseY);
        movementController.update();
            if (subButtons != null) {
                if (subButtons.length>0) {
                    if (!movementController.isFullClosed()){
                        for (int i = 0; i < subButtons.length; i++) {
                            subButtons[i].update(mouseX, mouseY);
                        }
                        if (isSubbutonsFullOpened()){
                            for (SubButtonInFrameWithText sub : subButtons){
                                if (sub.getActualStatement() == RELEASED){
                                    startToClose();
                                    break;
                                }
                            }
                        }

                    }
                    /*if (movementController.isFullOpened() || !subButtons[0].isClosed()) {
                        for (int i = 0; i < subButtons.length; i++) {
                            subButtons[i].update(mouseX, mouseY);
                        }
                        for (NES_SubButtonInFrameWithText sub : subButtons){
                            if (sub.getActualStatement() == RELEASED){
                                //movementController.setMovingStatement(MovementController.CLOSING);
                                startToClose();
                                break;
                            }
                        }
                }*/
            }
        }
    }

    private boolean isSubbutonsFullOpened() {
        if (!subButtons[0].isClosed()){
            return subButtons[0].isOpened();
        }
        return false;
    }

    @Override
    public void draw(PGraphics graphic) {
        if (subButtons != null) {
            if (subButtons.length>0) {
                for (int i = 0; i < subButtons.length; i++) {
                    subButtons[i].draw(graphic);
                }
            }
        }
        if (actualStatement == PRESSED){

        }
        super.draw(graphic);
    }

    public boolean isSomeSubbuttonReleased(){
        if (subButtons != null){
            for (int i = 0; i < subButtons.length; i++){
                if (subButtons[i].getActualStatement() == RELEASED) return true;
            }
        }
        return false;
    }

    public boolean isSubbuttonReleased(String name){
        if (subButtons != null){
            for (int i = 0; i < subButtons.length; i++){
                if (subButtons[i].getActualStatement() == RELEASED) {
                    if (name == subButtons[i].name || name.equals(subButtons[i].name)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getReleasedSubbuttonName(){
        if (subButtons != null){
            for (int i = 0; i < subButtons.length; i++){
                if (subButtons[i].getActualStatement() == RELEASED) {
                    return subButtons[i].name;
                }
            }
        }
        return null;
    }

    public SubButtonInFrameWithText getSubbuton(String name){
        if (subButtons != null){
            for (int i = 0; i < subButtons.length; i++){
                if (subButtons[i].getActualStatement() == RELEASED) {
                    if (name == subButtons[i].name || name.equals(subButtons[i].name)){
                        return subButtons[i];
                    }
                }
            }
        }
        return null;
    }

    protected void drawData(PGraphics graphic, int left) {
        if (actualStatement != PRESSED && actualStatement != RELEASED) drawName(graphic, LEFT_ALIGNMENT_OS_SPECIFIC);
    }

    private void openInsideList() {
        for (int i = 0; i < subButtons.length; i++){
            subButtons[i].startToOpen(true);
        }
    }



    private void closeInsideList() {
        for (int i = 0; i < subButtons.length; i++){
            subButtons[i].startToOpen(false);
        }
    }
    @Override
    protected void translateGui(float dist, boolean axis){
        super.translateGui(dist,axis);
        if (subButtons != null) {
            if (subButtons.length>0) {
                for (int i = 0; i < subButtons.length; i++) {
                    subButtons[i].translateGui(dist,axis);
                }
            }
        }
    }

    @Override
    protected final ImageZoneSimpleData getFrameImageZoneSimpleData(){
        return openedImageZoneSimpleDataWithBlackBackground;
    }

    public void startToClose() {
        closeInsideList();
        movementController.setMovingStatement(Constants.CLOSING);
    }

    public void blockSubbutons(Object toBeBlocked) {
        if (toBeBlocked == null){
            for (SubButtonInFrameWithText sub : subButtons){
                sub.setActualStatement(BLOCKED);
            }
        }
        else{
            if (toBeBlocked instanceof SubButtonInFrameWithText) {
                for (SubButtonInFrameWithText sub : subButtons) {
                    if (toBeBlocked.equals(sub)) sub.setActualStatement(BLOCKED);
                }
            }
            else if (toBeBlocked instanceof String){
                String name = (String) toBeBlocked;
                for (SubButtonInFrameWithText sub : subButtons){
                    if (sub.getName().equals(name) || sub.getName().contains(name) || sub.getName() == name){
                        sub.setActualStatement(BLOCKED);
                    }
                }
            }
        }
    }

    private class MovementController implements Constants{
        private int movingStatement;
        private final int openingTime = 1000;
        private float velocity;

        private final boolean TO_RIGHT = true;
        public MovementController() {
            movingStatement = FULL_CLOSED;
            initVelocity();
        }

        private void initVelocity() {
            velocity = (PApplet.abs((maxOpenedDistance-leftX) /(float)openingTime));
        }

        private void update(){
            updateStatementChanging();
            updateMovement();
        }

        private void updateMovement() {

            if (actualStatement != BLOCKED){
                if (movingStatement == OPENING){
                    move(!TO_RIGHT);
                }
                else {
                    boolean wasNotClosed = false;
                    if (movingStatement == CLOSING){
                        move(TO_RIGHT);
                        wasNotClosed = true;
                    }
                    if (movingStatement == FULL_CLOSED && wasNotClosed) closeInsideButtons();
                }
            }
        }

        private void closeInsideButtons() {
            //System.out.println("must be closed");
            for (SubButtonInFrameWithText sub: subButtons){
                sub.backToStart();
            }
        }

        private void move(boolean dir) {
            if (dir == TO_RIGHT) {
                translateGui(getDeltaTime()*velocity, ALONG_X);
                //System.out.println("Moving to right");
                if (leftX > startLeft){
                    translateGui(-(leftX-startLeft), ALONG_X);
                    movingStatement = FULL_CLOSED;
                }
            }
            else {
                translateGui(-getDeltaTime()*velocity, ALONG_X);
                if (leftX < maxOpenedDistance){
                    translateGui((maxOpenedDistance-leftX), ALONG_X);
                    movingStatement = FULL_OPENED;
                }
            }
        }

        public void setMovingStatement(int movingStatement) {
            this.movingStatement = movingStatement;
        }

        private void updateStatementChanging(){

            if (actualStatement == RELEASED){
                if (movingStatement == FULL_CLOSED){
                    movingStatement = OPENING;
                }
                else if (movingStatement == FULL_OPENED){
                    if (subButtons != null){
                        if (subButtons.length > 0){
                            if (subButtons[0].isClosed()){
                                openInsideList();
                            }
                        }
                    }
                }
                else if (movingStatement == OPENING){
                    if (subButtons != null){
                        if (subButtons.length > 0){
                            if (!subButtons[0].isClosed()){
                                System.out.println("It is opening but the buttons are not closed yet");
                                closeInsideButtons();
                            }
                        }
                    }
                }
            }

        }

        public boolean isFullOpened() {
            if (movingStatement == FULL_OPENED) return true;
            else return false;
        }

        public boolean isFullClosed() {
            if (movingStatement == FULL_CLOSED) return true;
            else return false;
        }
    }



}
