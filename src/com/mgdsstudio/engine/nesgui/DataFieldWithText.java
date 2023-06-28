package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class DataFieldWithText extends FrameWithText {

    private ArrayList<IKeyboardOpeningListener> listeners;

    protected final static float CURSOR_DIMENSIONS_COEF = 1.3f;

   // private int cursorPos = 0;
    protected boolean charWasAdded;
    protected boolean cursorShown;
    protected Cursor cursor;

    private IVirtualKeyboardController virtualKeyboardController;

    private boolean keyboardMustBeOpenedOnThisFrame;

    protected boolean manualScrollable = false;
    private ScrollingController scrollingController;


    DataFieldWithText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, String defaultData) {
        super(engine, centerX, centerY, width, (int) (height ), name, graphics);
        this.height = height;
        this.data = defaultData;
        cursor = new Cursor();
        //init(centerX, centerY, (int) (height*CURSOR_DIMENSIONS_COEF / heightRelativeCoef));
        //float charWidth = graphics.textWidth('W');
        //maxChars = PApplet.floor(frame.getWidth() / charWidth) - 2;

        maxChars = getMaxCharsForGui(graphics, frame.getWidth(), defaultData);

        setAnotherTextToBeDrawnAsName(data);
        scrollingController = new ScrollingController(cursor);
        listeners = new ArrayList<>();
    }


    public void appendListener(IKeyboardOpeningListener listener){
        listeners.add(listener);
    }

    public void clearListener(IKeyboardOpeningListener listener){
        if (listener == null){
            listeners.clear();
            System.out.println("GUI: keyboard listeners are clear");
        }
        else {
            if (listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }
    }


    @Override
    protected final ImageZoneSimpleData getFrameImageZoneSimpleData(){
        return frameImageZoneSimpleDataWithoutBackground;
    }

    @Override
    public void update(int x, int y) {
        super.update(x, y);
        if (keyboardMustBeOpenedOnThisFrame == true){
            for (IKeyboardOpeningListener listener : listeners){
                listener.keyboardMustBeOpened();
            }
        }
        keyboardMustBeOpenedOnThisFrame = false;
        if (prevStatement == ACTIVE && actualStatement == PRESSED) {
            cursorShown = true;
            setKeyboardMustBeOpened(true);
            cursor.setCursorPosRelativeToClickPlace(x,y, singleCharWidth);
        }
        else {
            if (engine.mousePressed && !isMouseOnEffectiveArea(x, y)) {
                if (actualStatement == RELEASED || actualStatement == ACTIVE) {
                    fieldIsNotMoreActive();
                    setKeyboardMustBeOpened(false);
                }
            }
        }
        if (cursorShown) updateDataFilling();
    }



    private void setKeyboardMustBeOpened(boolean flag) {
        if (flag){
            keyboardMustBeOpenedOnThisFrame = true;
        }
        if (virtualKeyboardController != null){
            if (flag) virtualKeyboardController.virtualKeyboardMustBeShown();
            else virtualKeyboardController.virtualKeyboardMustBeClosed();
        }
        if (flag){
            if (os == ANDROID) System.out.println("GUI: keyboard must be shown");
            for (IKeyboardOpeningListener listener : listeners){
                listener.keyboardMustBeOpened();
            }
        }
        else {
            if (os == ANDROID) System.out.println("GUI: keyboard must be hidden");
            for (IKeyboardOpeningListener listener : listeners){
                listener.keyboardMustBeClosed();
            }
        }
    }

    public boolean isKeyboardMustBeOpenedOnThisFrame() {
        return keyboardMustBeOpenedOnThisFrame;
    }

    protected void fieldIsNotMoreActive() {
        cursorShown = false;
        cursor.setCursorVisibleNow(false);
        actualStatement = RELEASED;
    }

    protected abstract void updateDataFilling();


    public void setVirtualKeyboardController(IVirtualKeyboardController virtualKeyboardController) {
        this.virtualKeyboardController = virtualKeyboardController;
    }




    @Override
    protected final void drawData(PGraphics graphic, int side) {
        drawName(graphic, RIGHT_ALIGNMENT_OS_SPECIFIC);
    }


    protected String getTextToBeDrawn() {
        return cursor.getActualDataToBeDrawn();
    }


    protected void addCharOnCursorPlace(char newChar) {
        System.out.println("Try to add char at the cursor place: " + cursor.getCursorPos() + ". The string is between: " + scrollingController.startChar + " to " + scrollingController.endChar);
        if (data.length() < maxChars) {
            if (cursor.getCursorPos() < 0) {
                data = newChar + data;
                scrollingController.charWasAdded(ScrollingController.ADDED_FROM_LEFT);
            }
            else if (cursor.getCursorPos() >= maxChars || cursor.getCursorPos() >= data.length()) {
                data = data + newChar;
                cursor.charWasAdded();
                cursor.translateCursorRight();
                scrollingController.charWasAdded(ScrollingController.ADDED_FROM_RIGHT);
                cursor.translateCursorRight();
                if (cursor.actualDrawnString.length() > maxChars) cursor.translateCursorRight();
            } else {
                cursor.changeCharOnCursorPlace(newChar);
            }
        }
        else
            {
             if (manualScrollable) {
                 /*
                 if (cursor.getCursorPos() < 0) {
                     data = newChar + data;
                     scrollingController.charWasAdded(ScrollingController.ADDED_FROM_LEFT);
                 }
                 else if (cursor.getCursorPos() >= maxChars || cursor.getCursorPos() >= data.length()) {
                     data = data + newChar;
                     cursor.charWasAdded();
                     cursor.translateCursorRight();
                     scrollingController.charWasAdded(ScrollingController.ADDED_FROM_RIGHT);
                     cursor.translateCursorRight();
                     if (cursor.actualDrawnString.length() > maxChars) cursor.translateCursorRight();
                 }
                 else {
                     cursor.changeCharOnCursorPlace(newChar);
                 }
                 */
             }



            if (cursor.getCursorPos() >= 0 && cursor.getCursorPos() < data.length()) {
                data = data.substring(0, cursor.getCursorPos()) + newChar + data.substring(cursor.getCursorPos() + 1);
            }
        }
        cursor.updateVisibleData();
        System.out.println("New data: " + data + " visible: " + cursor.getActualDataToBeDrawn() + " it is scrollable: " + manualScrollable);
    }



    protected void deleteChar() {
        if (cursor.getCursorPos() < 0) cursor.translateCursorRight();
        else if (cursor.getCursorPos() >= data.length()) {

        } else {
            data = data.substring(0, cursor.getCursorPos()) + data.substring(cursor.getCursorPos() + 1);
        }
    }

    protected void deletePrevChar() {
        System.out.print("Try to delete previous char: ");
        if (cursor.getCursorPos() < 0) {
            cursor.setCursorToVisibleStringEnd();
            System.out.println(" jump to string end");
        }
        else if (cursor.getCursorPos() >= cursor.visibleData.length()) {
            cursor.translateCursorLeft();
            System.out.println(" shift cursor left without deleting");
        }
        else if (cursor.getCursorPos() == 0) {
            String originalVisibleString = cursor.visibleData;
            int startPos = data.indexOf(originalVisibleString);
            int length = originalVisibleString.length();
            cursor.visibleData = cursor.visibleData.substring(1);
            scrollingController.setStart(scrollingController.startChar-1);
            data = data.substring(0,startPos) + cursor.visibleData + data.substring(startPos+length);
            //System.out.println("First char must be deleted");
            cursor.translateCursorLeft();

        } else {
            boolean shiftRightToRight = false;
            if (cursor.getCursorPos()>cursor.visibleData.length()/2){
                shiftRightToRight = true;
            }
            cursor.visibleData = cursor.visibleData.substring(0, cursor.getCursorPos()) + cursor.visibleData.substring(cursor.getCursorPos()+1);

            if (shiftRightToRight) scrollingController.setEnd(scrollingController.endChar++);
            else scrollingController.setStart(scrollingController.startChar--);
        }
    }

    /*
    protected void deletePrevChar() {
        if (cursor.getCursorPos() < 0) {
            cursor.setCursorToPos(data.length());
            }
        else if (cursor.getCursorPos() >= data.length()) {
            cursor.translateCursorLeft();
        } else if (cursor.getCursorPos() == 0) {
            data = data.substring(cursor.getCursorPos() + 1);
            cursor.translateCursorLeft();
        } else {
            data = data.substring(0, cursor.getCursorPos() - 1) + data.substring(cursor.getCursorPos());
        }
    }
     */

    protected boolean updateControlCharsAdding(char newChar) {
        if (newChar == PConstants.DELETE) {
            System.out.println("Delete pressed");
            deleteChar();
            return true;
        } else if (newChar == PConstants.ENTER) {
            System.out.println("Enter pressed");
            fieldIsNotMoreActive();
            return true;
        } else if (newChar == PConstants.BACKSPACE || (os == ANDROID && newChar == 'C')) {    //For android version of the Processing c - is code for backspace
            System.out.println("Backspace pressed");
            deletePrevChar();
            return true;
        } else if (engine.keyCode == LEFT_ALIGNMENT_OS_SPECIFIC) {
            System.out.println("Arrow left pressed");
            cursor.translateCursorLeft();
            return true;
        } else if (engine.keyCode == RIGHT_ALIGNMENT_OS_SPECIFIC) {
            System.out.println("Arrow right pressed");
            cursor.translateCursorRight();
            return true;
        } else {
            //System.out.println("Button " + newChar + " was pressed. It is not a control char");
            return false;
        }
    }

    class ScrollingController{
        private int startChar = 0;
        private int endChar;
        private int actualStartChar;
        protected static final int ADDED_FROM_LEFT = -1;
        protected static final int ADDED_FROM_RIGHT = 1;

        protected static final int ADDED_IN_CENTER = 0;
        protected String visibleString;
        private Cursor cursor;
        public ScrollingController(Cursor cursor) {
            this.cursor = cursor;
        }

        public void charWasAdded(int dir){

            if (data.length()< maxChars){
                startChar = 0;
                endChar = data.length();
            }
            else {
                if (dir == ADDED_FROM_LEFT){
                    endChar = startChar+maxChars-1;
                    System.out.print("New char was added from left");
                }
                else if (dir == ADDED_FROM_RIGHT){
                    startChar++;
                    endChar = startChar+maxChars-1;
                    System.out.print("New char was added from right");
                    cursor.setCursorRightsFromData();
                    cursor.translateCursorRight();
                }
                else{
                    System.out.print("New char was added in the center");
                }
            }
            cursor.setVisibleData(data.substring(startChar, endChar));
            System.out.println(" End string is between: " + startChar + " and " + endChar + " Data is " + data);
            //System.out.println(" End string is between: " + startChar + " and " + endChar + " and must be " + data.substring(startChar,endChar));
        }


        protected void setStart(int newStart) {
            this.startChar=newStart;
        }

        protected void setEnd(int newEnd) {
            this.endChar=newEnd;
        }

    }

    class Cursor{
        public static final boolean LEFT = false ;
        public static final boolean RIGHT = true ;
        //int pos;
        //String data;
        private Timer blinkTimer;
        private final int BLINK_DELAY = 400;
        private boolean cursorVisibleNow = true;
        private int cursorPos;
        private String actualDrawnString;
        private final char cursorChar = '_';
        private String visibleData;


        Cursor() {
            if (data.length()>maxChars) {
                visibleData = data.substring(0,maxChars);
                System.out.println("Start data must not be longer as max!");
            }
            else visibleData = ""+data;

            cursorPos = visibleData.length()+1;
            actualDrawnString = visibleData;
        }

        public String getVisibleData() {
            return visibleData;
        }

        public void setVisibleData(String visibleData) {
            this.visibleData = visibleData;
        }

        public int getCursorPos() {
            return cursorPos;
        }

        void setCursorVisibleNow(boolean flag){
            cursorVisibleNow = flag;
            cursorShown = flag;
            if (!cursorShown){
                actualDrawnString = visibleData;
            }
        }

        protected void translateCursorRight(){
            cursorPos++;
            //if (cursorPos > actualDrawnString.length()) cursorPos = actualDrawnString.length();
            if (cursorPos >= visibleData.length()) cursorPos = visibleData.length()+1;
        }

        protected void translateCursorLeft(){
            if (visibleData.length()<=maxChars) {
                cursorPos--;
                if (cursorPos < 0) cursorPos = -1;
            }
            else System.out.println("This string is too long");
        }

        protected String getActualDataToBeDrawn() {
            if (cursorShown){
                if (blinkTimer == null) blinkTimer = new Timer(BLINK_DELAY, engine);
                else{
                    if (blinkTimer.isTime()){
                        blinkTimer.setNewTimer(BLINK_DELAY);
                        cursorVisibleNow = !cursorVisibleNow;
                        if (cursorVisibleNow){

                            if (cursorPos>=actualDrawnString.length()){
                                actualDrawnString = visibleData+ cursorChar;
                                //System.out.println("Char added from right");
                            }
                            else if (cursorPos < 0){
                                actualDrawnString = cursorChar+visibleData + ' ';
                                //System.out.println("Char added from left");
                            }
                            else {
                                //System.out.println("Char added in the string");
                                int numberInString = cursorPos;
                                actualDrawnString = visibleData +' ';
                                actualDrawnString = actualDrawnString.substring(0,numberInString) + cursorChar + actualDrawnString.substring(numberInString+1);
                            }
                        }
                        else {
                            actualDrawnString = visibleData+' ';
                        }
                    }
                }
            }
            return actualDrawnString;
        }

        protected void charWasAdded(){

        }

        protected void setCursorToPos(int i) {
            cursorPos = i;
            if (i < (0)) cursorPos = -1;
            else if (cursorPos > visibleData.length()) cursorPos = visibleData.length();
        }

        public void setCursorPosRelativeToClickPlace(int x, int y, int singleCharWidth) {
            if (singleCharWidth>0){
                float rightStartPos = leftX+width-distanceBetweenTextAndFrameAlongY;
                int charNumberFromLeft = (int) ((rightStartPos-x)/singleCharWidth)+1;
                int charNumberFromRight = actualDrawnString.length()-charNumberFromLeft;
                setCursorToPos(charNumberFromRight);
            }
            //os.arch >>>> aarch64
        }

        public void updateVisibleData() {

        }

        public void setCursorRightsFromData() {
            cursorPos = visibleData.length();
        }

        public void changeCharOnCursorPlace(char newChar) {
            //System.out.println("Data before: " + visibleData.substring(0,cursorPos));

            //System.out.println("Data with char and forward: " + visibleData.substring(cursorPos));
            int nextChar = cursorPos+1;
            String newVisible;
            if (visibleData.length()<nextChar){
                newVisible = visibleData.substring(0,cursorPos) + newChar;
            }
            else newVisible = visibleData.substring(0,cursorPos) + newChar + visibleData.substring(cursorPos+1);
            visibleData = newVisible;
            // maybe I need to change the data in the data variable
        }

        public void setCursorToVisibleStringEnd() {
            cursorPos = visibleData.length();
        }
    }



    protected boolean wasKeyPressed() {
        //System.out.println("Desktop: " + desktop + " OS: " + os);
        if (desktop == true){
            //System.out.println("Try to test");
            if (engine.keyPressed) {
                System.out.println("Key pressed");
                return true;
            }
        }
        else if (os == ANDROID){
            if (actualPressedChar != '@') return true;
        }
        return false;
    }

}
