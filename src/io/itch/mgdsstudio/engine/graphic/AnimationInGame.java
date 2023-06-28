package io.itch.mgdsstudio.engine.graphic;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;
import processing.core.PGraphics;
import sun.rmi.runtime.Log;

public class AnimationInGame extends GraphicElementInGame{

    //Playing dirs
    public final static boolean FORWARD = false;
    public final static boolean REVERSE = true;

    public final static int PLAY_ALWAYS = 0;
    public final static int PLAY_ONCE_AND_SWITCH_OFF = 1;
    public final static int PLAY_AND_STOP_AT_LAST = 2;
    public final static int PLAY_AND_STOP_AT_FIRST = 3;

    //Start constants
    private int alongX, alongY;
    private int startSprite, lastSprite; //0 = first
    private int timeToNextSprite;
    //private boolean dir = FORWARD;

    //Not important
    private int keySprite = -1; //Sprite which will make an interruption
    //private boolean playOnce;
    private boolean played;
    //private int firstInTheTable, lastInTheTable;

    //Variables
    //private int actual;
    private Timer timer;
    //private int timeToNextSprite;
    private boolean playingDirection;
    //private boolean firstUpdated;
    private SpriteChangingController spriteChangingController;
    private int repeatability;
    protected boolean loopedOnce = false;
    private boolean activeUpdating = true;
    private final PApplet engine;

    public AnimationInGame(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData, int alongX, int alongY, int firstInTheTable, int lastInTheTable, int imagesPerSecond, int direction, int actual, int repeatability, int keySprite) {
        super(image, getCorrectedWidth(width, imageZoneSimpleData, alongX), getCorrectedHeight(height, imageZoneSimpleData, alongY), imageZoneSimpleData);
        engine = image.getEngine();
        this.alongX = alongX;
        this.alongY = alongY;
        this.startSprite = firstInTheTable;
        this.lastSprite = lastInTheTable;
        int actualSprite = 0;
        if (actual >= firstInTheTable && actual <= lastInTheTable){
            actualSprite = actual;
        }
        else actualSprite = firstInTheTable;
        this.repeatability = repeatability;
        //Logger.errorLog("First sprite: " + actualSprite);
        this.playingDirection = parseIntDirectionToBoolean(direction);
        calculateAnimationChangingVelocity(imagesPerSecond, actualSprite, imageZoneSimpleData);
        this.keySprite = keySprite;
    }

    private static int getCorrectedWidth(int width, ImageZoneSimpleData data, int alongX){
        if (width > 0) return width;
        else {
            int correctedWidth = (data.rightX-data.leftX)/alongX;
            Logger.debug("Corrected width for graphic: " + correctedWidth);
            if (correctedWidth <= 0){
                Logger.error("False width for animation!");
            }
            return correctedWidth;
        }
    }

    private static int getCorrectedHeight(int height, ImageZoneSimpleData data, int alongY){
        if (height > 0) return height;
        else {
            int correctedHeight = (data.lowerY-data.upperY)/alongY;
            Logger.debug("Corrected height for graphic: " + correctedHeight);
            if (correctedHeight <= 0){
                Logger.error("False height for animation!");
            }
            return correctedHeight;
        }
    }

    private void calculateAnimationChangingVelocity(float imagesPerSecond, int actualSprite, ImageZoneSimpleData imageZoneSimpleData) {
        if (imagesPerSecond == 0) imagesPerSecond = 0.001f;
        float timeBetween = 1000f/imagesPerSecond;
        timeToNextSprite = (int) timeBetween;
        if (spriteChangingController == null) spriteChangingController = new SpriteChangingController(actualSprite, timeToNextSprite, imageZoneSimpleData);
        else {
            //Logger.debug("Time: " + timeToNextSprite);

            spriteChangingController.setTimeToNextTransfer(timeToNextSprite);
        }
    }


    private static boolean parseIntDirectionToBoolean(int dir){
        if (dir == 1) return FORWARD;
        else return !FORWARD;
    }

    //Next function launched from draw
    private void update() {

        if (firstUpdatingEnded) {
            if (active) {
                //Logger.debugLog("Actual sprite number: " + spriteChangingController.actualSprite + " but last: " + lastSprite);
                if (spriteChangingController.actualSprite == lastSprite) {
                    if (!loopedOnce) {
                        //Logger.debugLog("Loop endes: " + spriteChangingController.actualSprite);
                        firstLoopEnded();
                    }
                }

                if (activeUpdating) {
                    spriteChangingController.update();
                }
                updateRepeateabilitySpecific();
            }
        }
        else firstUpdatingEnded = true;
    }

    private void updateRepeateabilitySpecific() {
        if (repeatability == PLAY_ALWAYS) {
            //Nothing to change
        }
        else if (repeatability == PLAY_AND_STOP_AT_LAST){
            if (spriteChangingController.actualSprite == lastSprite){
                activeUpdating = false;
            }
        }
        else if (repeatability == PLAY_AND_STOP_AT_FIRST || repeatability == PLAY_ONCE_AND_SWITCH_OFF){
            if (spriteChangingController.actualSprite == startSprite && loopedOnce){
                activeUpdating = false;
                if (repeatability == PLAY_ONCE_AND_SWITCH_OFF){
                    Logger.debug("Animation is not more active at: " + image.getEngine().frameCount + " for animation: ");
                    active  = false;
                }
            }
        }
    }

    private void firstLoopEnded() {
        if (repeatability == PLAY_AND_STOP_AT_LAST) activeUpdating = false;
        loopedOnce = true;
    }


    @Override
    protected void render(PGraphics graphics) {
        update();
        if (active) {
            ImageZoneSimpleData imageZoneSimpleData = spriteChangingController.getActualImageData();
            graphics.image(image.getImage(), 0, 0, width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
        }
    }

    public void setFreq(float freq) {
        calculateAnimationChangingVelocity((int)freq, spriteChangingController.actualSprite, imageZoneSimpleData);
    }

    private class SpriteChangingController{
        //private Timer timer;
        private ImageZoneSimpleData  [] imageData;
        private int timeToNextTransfer;
        private int nextTransferTime, prevTransferTime;
        private int actualSprite;


        public SpriteChangingController(int actualSprite, int timeToNextTransfer, ImageZoneSimpleData imageFullZoneData) {
            this.timeToNextTransfer = timeToNextTransfer;
            this.actualSprite = actualSprite;
            initImageZones(imageFullZoneData);

            //Logger.debugLog("Start sprite for this animation: " + actualSprite);
        }



        private void initImageZones(ImageZoneSimpleData imageFullZoneData) {
            int imagesInAnimation = lastSprite-startSprite;
            imageData = new ImageZoneSimpleData[imagesInAnimation+1];
            int zoneWidth = imageFullZoneData.rightX-imageFullZoneData.leftX;
            int zoneHeight = imageFullZoneData.lowerY-imageFullZoneData.upperY;
            int cellWidth = zoneWidth/alongX;
            int cellHeight = zoneHeight/alongY;
            int fullNumber = 0;
            for (int j = 0; j < alongY; j++){
                for (int i = 0; i < alongX; i++) {
                    if (fullNumber >= startSprite && fullNumber <= lastSprite) { //Maybe <=
                        int xLeft = i*cellWidth;
                        int xRight = (i+1)*(cellWidth);
                        int yTop = j*cellHeight;
                        int yBottom = (j+1)*cellHeight;
                        ImageZoneSimpleData data = new ImageZoneSimpleData(xLeft+imageFullZoneData.leftX, yTop+imageFullZoneData.upperY, xRight+imageFullZoneData.leftX, yBottom+imageFullZoneData.upperY);
                        imageData[fullNumber] = data;
                    }
                    else break;
                    fullNumber++;
                }
            }

            for (int i = 0; i < imageData.length; i++){
                //Logger.debugLog("Single sprites №" + i + " in animation has coordinates: " + imageData[i].toString());
            }
        }



        private void setTimeToNextTransfer(int timeToNextTransfer) {
            this.timeToNextTransfer = timeToNextTransfer;
            if (engine.millis()-prevTransferTime<timeToNextTransfer){
                nextTransferTime = engine.millis();
            }
            else {
                nextTransferTime = (engine.millis()-prevTransferTime)+engine.millis();
            }
        }

        private void update(){
            if (engine.millis()>nextTransferTime){
                prevTransferTime = nextTransferTime;
                int delta = (engine.millis()-nextTransferTime);
                if (delta>timeToNextTransfer){
                    transferToAnotherSprite(delta);
                }
                else transferToNextSprite();
                setNewTimer(prevTransferTime, engine.millis());
            }
        }

        private void setNewTimer(int prevTime, int millis) {
            boolean timeSet = false;
            nextTransferTime = prevTime;
            while (!timeSet){
                nextTransferTime+=timeToNextTransfer;
                if (nextTransferTime > (millis+timeToNextSprite)){
                    timeSet = true;
                }
            }
        }

        private void transferToNextSprite() {
            actualSprite++;
            resetSpriteIfNeeded();
        }

        private void transferToAnotherSprite(int delta) {
            int step = PApplet.floor(delta/timeToNextTransfer);
            actualSprite+=step;
            resetSpriteIfNeeded();
        }

        private void resetSpriteIfNeeded() {
            if (actualSprite>(lastSprite)) {
                int deltaSprite = actualSprite-(lastSprite+1);
                actualSprite = startSprite+deltaSprite;
                resetSpriteIfNeeded();
            }
        }

        public ImageZoneSimpleData getActualImageData() {
            //Logger.errorLog("Start sprite: " + startSprite + " but actual : " + actualSprite);
            return imageData[actualSprite+startSprite];
        }
    }

}
