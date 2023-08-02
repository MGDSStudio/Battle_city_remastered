package com.mgdsstudio.engine.nesgui;



import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.GraphicManagerSingleton;
import io.itch.mgdsstudio.engine.graphic.Image;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.Enumeration;
import java.util.Properties;

public abstract class GuiElement {
    public final static int NES_SCREEN_X_RESOLUTION = 254;
    private Object userData;
    protected float effectiveHeightCoef = 1.5f;
    public final static int PRESSED = 1, RELEASED = 2, ACTIVE = 0, BLOCKED = 4, HIDDEN = 3;
    //, LONG_PRESSED =5
    private boolean longPressing;
    public static int NORMAL_HEIGHT = 20;
    private final static int LONG_PRESSING_TIME = 1500;
    private Timer longPressTimer;
    protected int ACTIVE_COLOR_RED = 255, ACTIVE_COLOR_GREEN = 255, ACTIVE_COLOR_BLUE = 255, BLOCKED_COLOR = 100;
    protected int colorRed = ACTIVE_COLOR_RED;
    protected int colorGreen = ACTIVE_COLOR_GREEN;
    protected int colorBlue = ACTIVE_COLOR_BLUE;
    protected static Image graphicFile;
    protected final boolean debug = true;
    protected PFont font;
    protected int textWidth;

    protected float x, y;
    protected float leftX, upperY;
    protected int width, height = NORMAL_HEIGHT;
    protected String name;
    protected int actualStatement;
    protected int prevStatement;
    boolean fontInitialized;
    private static boolean withCoutureRect = false;
    private String anotherTextToBeDrawnAsName;
    protected boolean hidden;
    private boolean visible = true;

    private boolean drawDigitWithAnotherColor;
    protected PApplet engine;
    protected int textYShifting;
    protected boolean shiftingWasSet;
    protected static boolean prevMousePressed;
    private static int lastMousePressedStatementFrame;
    private static DeltaTimeController deltaTimeController;

    protected int maxChars = 12;

    public static boolean usingConsoleOutput = false;

    protected static final int LEFT_ALIGNMENT_OS_SPECIFIC = 31;    //For Android constant LEFT is 21! For desktop 31
    protected static int RIGHT_ALIGNMENT_OS_SPECIFIC = 39;    //For Android constant LEFT is 22! For desktop 39

    private static boolean alignmentConstantsIsInit = false;

    protected static int os;
    protected final static int WINDOWS = 0;
    protected final static int LINUX = 1;   //and android
    protected final static int ANDROID = 2;   //and android
    protected static boolean desktop;

    protected static char actualPressedChar = '@';

    protected int singleCharWidth = -1;
    private final static String LOG_PREFIX =  "GUI: ";
    private IEngine engineInterface;


    GuiElement(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics){
        initOsConstants();
        if (engine != null) {
            this.engine = engine.getEngine();
            engineInterface = engine;
        }
        font = engine.getEngine().loadFont(engine.getPathToObjectInAssets("Font1.vlw"));

        //GraphicManage r.getManager(engine).getFont();

        this.height = height;
        graphics.textFont(font);
        //graphics.textSize(height);
        this.x = centerX;
        this.y = centerY;
        this.leftX = (int) (x-width/2f);
        this.upperY = (int) (y-height/2f);
        if (width>0) {
            this.width = width;
        }
        else setWidthByFontWidth(graphics);
        if (name != null) this.name = name;
        else this.name = "No name";
        initGraphic();
        if (deltaTimeController == null) deltaTimeController = new DeltaTimeController();
    }

    protected final void log(String data){
        System.out.println(LOG_PREFIX + data);
    }

    private void initOsConstants() {
        if (!alignmentConstantsIsInit) {
            alignmentConstantsIsInit = true;
            RIGHT_ALIGNMENT_OS_SPECIFIC = 39;
            String OS = System.getProperty("os.name");
            //String version = System.getProperty("os.version");
            Properties p = System.getProperties();
            Enumeration keys = p.keys();
            //System.out.println("This OS is: " + OS + " " + version);
            boolean isAndroid = false;
            while(keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = (String) p.get(key);
                if (value.contains("Dalvik") || value.contains("dalvik")){
                    isAndroid = true;
                }
                //System.out.println("*** "+ key + " >>>> " + value);
            }
            if (OS.contains("indows")) {
                RIGHT_ALIGNMENT_OS_SPECIFIC = 39;
                os = WINDOWS;
                System.out.println("This is Windows");
                desktop = true;
            } else {
                if (isAndroid) {
                    os = ANDROID;
                    RIGHT_ALIGNMENT_OS_SPECIFIC = 22;
                    System.out.println("This is Android");
                    desktop = false;
                }
                else {
                    RIGHT_ALIGNMENT_OS_SPECIFIC = 39;
                    os = LINUX;
                    System.out.println("This is Linux");
                    desktop = true;
                }
            }
            if (usingConsoleOutput) System.out.println("Constant alignment is: " + RIGHT_ALIGNMENT_OS_SPECIFIC) ;

        }
    }


    protected void setWidthByFontWidth(PGraphics graphics) {
        if (font!=null){
            if (name == null) System.out.println("This GUI " + this.getClass() + " has not name");
            else width = (int) graphics.textWidth(name);
        }

    }

    protected void initGraphic(){
        if (graphicFile == null) {
            graphicFile = GraphicManagerSingleton.getManager(engine).getImage(engineInterface.getPathToObjectInAssets("Gui.gif"));
        }
    }

    protected long getDeltaTime(){
        return deltaTimeController.getDeltaTime();
    }

    protected final void initFont(PGraphics graphics){
        if (font == null){
            font = engine.loadFont("Font1.vlw");
            graphics.textFont = font;
            if (height <= 0 ) {
                Logger.error("Can not set text height! It is " + height);
            }
            else graphics.textSize(height);
            if (width<=0){
                width = (int) graphics.textWidth(name);
            }
            System.out.println("Font height was set on " + height + " for gui: " + this.getClass());
        }
        if (name != null) textWidth = (int) graphics.textWidth(name);
        fontInitialized = true;
        if (anotherTextToBeDrawnAsName != null) maxChars = getMaxCharsForGui(graphics, width, anotherTextToBeDrawnAsName);
        else maxChars = getMaxCharsForGui(graphics, width, name);
    }

    protected void drawName(PGraphics graphic){
            drawName(graphic, LEFT_ALIGNMENT_OS_SPECIFIC);
        /*}
        else drawAnotherName(graphic, LEFT_ALIGNMENT_OS_SPECIFIC);*/
    }


    protected void drawName(PGraphics graphic, int xAlignment){
        graphic.pushStyle();
        graphic.textFont(font);
        graphic.textAlign(xAlignment, PConstants.CENTER);
        if (height < 0) {
            Logger.debug("Text height can not be applied by name drawing: " + height);
        }
        else graphic.textSize(height);
        if (!drawDigitWithAnotherColor) {
            if (actualStatement == BLOCKED) {
                graphic.tint(colorRed, colorGreen, colorBlue);
            }
            else {
                graphic.fill(colorRed, colorGreen, colorBlue);
            }
            if (xAlignment == LEFT_ALIGNMENT_OS_SPECIFIC) {
                graphic.text(getTextToBeDrawn(), leftX, y+textYShifting);
            }
            else if (xAlignment == RIGHT_ALIGNMENT_OS_SPECIFIC) {
                graphic.text(getTextToBeDrawn(), leftX+width, y+textYShifting);

            }
            else {
                graphic.textAlign(PConstants.CENTER, PConstants.CENTER);
                graphic.text(getTextToBeDrawn(), x, y+textYShifting);
            }
        }
        else {
            if (actualStatement == BLOCKED) {
                graphic.tint(colorRed, colorGreen, colorBlue);
            }
            else {
                graphic.fill(colorRed, colorGreen, colorBlue);
            }
            if (height < 0) {
                Logger.debug("Text height can not be applied by name drawing: " + height);
            }
            else graphic.textSize(height);
            if (xAlignment == LEFT_ALIGNMENT_OS_SPECIFIC) {
                graphic.text(getTextToBeDrawn(), leftX, y+textYShifting);
            } else {
                graphic.text(getTextToBeDrawn(), x, y+textYShifting);
            }
        }
        graphic.popStyle();
    }

    protected String getTextToBeDrawn(){
        if (anotherTextToBeDrawnAsName != null){
            return anotherTextToBeDrawnAsName;
        }
        else if (name != null) return  name;
        else {
            return "No name to be drawn";
        }
    }

    protected void drawDebugRect(PGraphics graphic){
        graphic.pushStyle();
        graphic.rectMode(PConstants.CORNER);
        graphic.noFill();
        graphic.strokeWeight(graphic.width/150);
        graphic.stroke(255);
        graphic.rect(leftX,upperY, width, height*effectiveHeightCoef);
        graphic.popStyle();
    }

    protected boolean isMouseOnEffectiveArea(int mouseX, int mouseY){
        return GameMechanics.isPointInRect(mouseX, mouseY, leftX, upperY, width, height * effectiveHeightCoef);

        }



    public void update(int mouseX, int mouseY){
        //updatePrevPressed();
        if (!hidden) {
            if (prevStatement != actualStatement) prevStatement = actualStatement;
            if (actualStatement != BLOCKED && actualStatement != HIDDEN) {
                if (isMouseOnEffectiveArea(mouseX, mouseY)) {
                    if (engine.mousePressed) {
                        if (actualStatement != PRESSED) actualStatement = PRESSED;
                    } else if (actualStatement == PRESSED) {
                        actualStatement = RELEASED;
                    } else if (actualStatement == RELEASED) {
                        actualStatement = ACTIVE;
                    }
                } else {
                    actualStatement = ACTIVE;
                }
            }
            updateLongPressingTimer();
            updateFunction();
        }
        deltaTimeController.update();

    }



    private void updatePrevPressed() {
        if (lastMousePressedStatementFrame < engine.frameCount){
            lastMousePressedStatementFrame = engine.frameCount;
            if (engine.mousePressed) prevMousePressed = true;
            else prevMousePressed = false;
        }
    }

    protected void updateLongPressingTimer(){
        if (prevStatement == ACTIVE && actualStatement == PRESSED){
            if (longPressTimer == null){
                longPressTimer = new Timer(LONG_PRESSING_TIME, engine);
            }
            else longPressTimer.setNewTimer(LONG_PRESSING_TIME);
        }
        if (actualStatement == PRESSED){
            if (longPressTimer.isTime()){
                if (longPressing != true){
                    longPressing = true;
                }
            }
            else if (longPressing != false) longPressing = false;
        }
        else if (longPressing != false) longPressing = false;
    }


    public void draw(PGraphics graphics) {
        //graphics.beginDraw();
        if (!hidden) {
            if (!fontInitialized) initFont(graphics);
            if (withCoutureRect) drawDebugRect(graphics);
        }
    }


    protected abstract void updateFunction();

    public int getActualStatement() {
        return actualStatement;
    }

    public boolean wasStatementChanged(){
        if (actualStatement != prevStatement) return true;
        else return false;

    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }


    public float getUpperY() {
        return upperY;
    }

    public float getLeftX() {
        return leftX;
    }

    public void setLeftX(float leftX) {
        float delta = this.leftX - leftX;
        this.leftX = leftX;
        x-=delta;
    }

    public void setUpperY(float upperY) {
        float delta = this.upperY - upperY;
        this.upperY = upperY;
        y-=delta;
    }


    public int getTextWidth() {
        return textWidth;
    }

    public void setWidth(int width) {
        int delta = this.width-width;
        this.width = width;
        leftX+=delta/2;
        x=leftX+width/2;
    }

    public boolean isLongPressing() {
        return longPressing;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public void block(boolean flag){
        if (flag == true) {
            actualStatement = BLOCKED;
        }
        else {
            actualStatement = ACTIVE;
        }
    }

    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        this.anotherTextToBeDrawnAsName = anotherTextToBeDrawnAsName;
    }

    public void setActualStatement(int actualStatement) {
        this.actualStatement = actualStatement;
    }

    public void setCenterX(float x){
        float deltaX = this.x-x;
        this.x = x;
        this.leftX = (leftX-deltaX);
    }

    public void setCenterY(float y){
        float deltaY = this.y-y;
        this.y = y;
        this.upperY =  (upperY-deltaY);
    }


    public void alignAlongY(PGraphics graphics) {
        if (anotherTextToBeDrawnAsName != null){

            float coef = (float)anotherTextToBeDrawnAsName.length()/(float)name.length();
            int shifting = (int) ((float)width*coef/2f);
            if (coef>1f){
                shifting*=(-1f) ;
            }
            //System.out.println("Gui was shifted to "+ shifting + " and coef: " + coef + "; Name was: " + name + " and now: " + anotherTextToBeDrawnAsName);
            leftX+=shifting;
            leftX = (graphics.width/2)-(width/2);
            //setWidth((int)((float)width*coef));
        }
    }

    public void setName(String name) {
        this.name = name;

    }

    public void hide(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public void setColor(ColorWithName colorWithName){
        colorRed = colorWithName.getRed();
        colorGreen = colorWithName.getGreen();
        colorBlue = colorWithName.getBlue();
        System.out.println("New color was set for this gui " + colorRed + ", " + colorGreen + ", " + colorBlue );
    }

    public void setDrawDigitWithAnotherColor(boolean drawDigitWithAnotherColor) {
        this.drawDigitWithAnotherColor = drawDigitWithAnotherColor;
    }

    protected int setYShiftingForFont(PFont textFont) {
        PGraphics graphics = createClearGraphicWithChar(textFont);
        graphics.loadPixels();
        //System.out.println("Text size: " + textFont.getSize());
        int [] pixels = graphics.pixels;
        int graphicCenter = graphics.height/2;
        int realCenter = getFontCenter(pixels, graphics.width, graphics.height);
        int shifting = graphicCenter-realCenter;
        textYShifting = shifting;
        //textYShifting+=(height*effectiveHeightCoef-font.getSize())/2;
        textYShifting+=(height*effectiveHeightCoef-height)/2;
        //System.out.println("Shifting: " + textYShifting);
        return shifting;
    }

    private int getFontCenter(int[] pixels, int width,  int height) {
        int [][] array = new int[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                array[i][j] = pixels[i*height+j];
            }
        }
        int upper = height;
        int lower = 0;
        int red = 0;

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                red = (int) engine.red(array[i][j]);
                if (red < 25) {
                    if (j< upper){
                        upper = j;
                    }
                    if (j > lower) lower = j;
                }
            }
        }
        //System.out.println("Upper pixel: " + upper + " lower: " + lower);
        return ((upper+lower)/2);
    }

    private PGraphics createClearGraphicWithChar(PFont font){
        PGraphics graphics = engine.createGraphics(150,150);
        graphics.beginDraw();
        graphics.background(255);
        graphics.fill(0);
        graphics.textFont(font);
        if (height < 0) {
            Logger.debug("Text height can not be applied by creation of clear graphic: " + height);
        }
        else graphics.textSize(height);
        graphics.textAlign(PConstants.CENTER, PConstants.CENTER);
        graphics.text('R', graphics.width/2, graphics.height/2);
        graphics.loadPixels();
        graphics.endDraw();
        return graphics;

    }

    protected int getRealTextHeight(PFont font){
        PGraphics graphics =createClearGraphicWithChar(font);
        graphics.loadPixels();
        int [] pixels = graphics.pixels;
        return getHeightInPixels(pixels, graphics.width, graphics.height);
    }

    private int getHeightInPixels(int [] pixels, int width, int height){
        int [][] array = new int[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                array[i][j] = pixels[i*height+j];
            }
        }
        int blackPoints = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                red = (int) engine.red(array[i][j]);
                green = (int) engine.green(array[i][j]);
                blue = (int) engine.blue(array[i][j]);
                if (red < 25) blackPoints++;
            }
        }
        //System.out.println("Black pixels: " + blackPoints + " from: " + width*height);
        int maxLower = 0;
        int minUpper=height;
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                red = (int) engine.red(array[i][j]);
                if (red < 25){
                    if (j<minUpper) minUpper = j;
                    else if (j>maxLower) maxLower = j;
                }
            }
        }
        //System.out.println("Upper point: " + minUpper + " lower: " + maxLower);
        return maxLower-minUpper;
    }


    public static int getNormalButtonHeightRelativeToScreenSize(int width){
        if (NORMAL_HEIGHT != width/24) NORMAL_HEIGHT = width/24;
        return NORMAL_HEIGHT;
    }

    protected int getMaxCharsForGui(PGraphics graphics, int maxWidth, String testText){
        graphics.textFont = font;
        float stringWidth = graphics.textWidth(testText);
        StringWidthCalculator stringWidthCalculator = new StringWidthCalculator();
        float charWidth = stringWidthCalculator.getCharWidth(graphics, testText, this.getClass());
        //float charWidth = (PApplet.ceil(stringWidth/testText.length()));
        //float whitespaceWidth
        //float charWidth = font.width('W');

        float underLineValue = PApplet.ceil(charWidth);
        if (underLineValue == 0){
            Logger.error("can not calculate char width for this gui");
            charWidth++;
        }
        int maxChars = PApplet.floor(maxWidth/ underLineValue) - 4;
        if (maxChars <= 0) maxChars = 1;
        //System.out.println("Single char width for " + this.getClass() + " is " + charWidth + " by frame width: " + maxWidth + " and max chars: " + maxChars) ;
        return maxChars;
    }



    private class StringWidthCalculator{
        public StringWidthCalculator() {
        }

        protected float getCharWidth(PGraphics graphics, String testText, Class className){
            if (singleCharWidth<0) {
                PGraphics testGraphic = createClearGraphicWithString(graphics.textFont, testText, getWidthForTestGraphic(graphics, testText));
                int endCharForSave = 8;
                if (testText.length() < 9) endCharForSave = testText.length() - 1;
                //String dataForSave = testText.substring(0,endCharForSave);


                //testGraphic.save("Frame:"+dataForSave+".png");
                testGraphic.loadPixels();
                //int[] pixels = testGraphic.pixels;
                int fullTextWidth = getTextWidth(testGraphic.pixels, testGraphic.width, testGraphic.height);
                testGraphic.updatePixels();
                if (testText.length()== 0) {
                    Logger.error("Test text length is null! ");
                    testText = " ";
                }
                singleCharWidth = ((fullTextWidth / testText.length()));
            }
            //String saveName = "Frame "+className+".png";
            //testGraphic.save(saveName);
            //System.out.println("Test graphic has width: " + width + " and must have text: " + testText);
            //System.out.println("Text width = " + fullTextWidth + " char width: " + charWidth);
            return singleCharWidth;
        }


        private int getTextWidth(int[] pixels, int width,  int height){

                int [][] array = new int[width][height];
                for (int i = 0; i < width; i++){
                    for (int j = 0; j < height; j++){
                        array[i][j] = pixels[j*width+i];
                    }
                }
                int left = width;
                int right = 0;

                int red;
                for (int i = 0; i < width; i++){
                    for (int j = 0; j < height; j++) {
                        red = (int) engine.red(array[i][j]);
                        if (red < 25) {
                            if (i < left) {
                                left = i;
                                drawPixelAsRed(i,j,pixels, width, height);
                            }
                            else if (i > right) {
                                right = i;
                                drawPixelAsGreen(i,j,pixels, width, height);
                            }
                        }
                    }
                }
                //System.out.println("Left pixel: " + left + " right: " + right);
                return (right-left);

        }

        private void drawPixelAsGreen(int i, int j, int[] pixels, int width, int height) {
            pixels[j*width+i] = engine.color(0,255,0);
        }

        private void drawPixelAsRed(int i, int j, int[] pixels, int width, int height) {
            pixels[j*width+i] = engine.color(255,0,0);
        }



        private int getWidthForTestGraphic(PGraphics graphics, String testText){
            //graphics.textFont = font;
            float stringWidth = graphics.textWidth(testText);
            float charWidth = (PApplet.ceil(stringWidth/testText.length()));

            //System.out.println("Theoretical char width: " + charWidth);
            //int graphicWidth = 4000;
            int graphicWidth;
            if (testText.length()>25) graphicWidth = (int) (charWidth*testText.length()*2);
            else graphicWidth = (1000);
            return graphicWidth;
        }

        private PGraphics createClearGraphicWithString(PFont font, String testText, int graphicWidth){
            PGraphics graphics = engine.createGraphics(graphicWidth,150);
            graphics.noSmooth();

            graphics.beginDraw();
            graphics.background(255);
            graphics.fill(0);
            graphics.textFont(font);
            graphics.textSize(height);
            graphics.textAlign(PConstants.CENTER, PConstants.CENTER);
            graphics.text(testText, graphics.width/2, graphics.height/2);
            graphics.loadPixels();
            graphics.endDraw();
            return graphics;

        }
    }

    private class DeltaTimeController{
        private int lastUpdatedFrame;
        private long deltaTime;
        private long lastFrameTime;

        public DeltaTimeController() {
            lastUpdatedFrame = engine.frameCount;
            lastFrameTime = engine.millis();
        }

        void update(){
            if (engine.frameCount > lastUpdatedFrame){
                deltaTime = engine.millis()-lastFrameTime;
                lastFrameTime = engine.millis();
                lastUpdatedFrame = engine.frameCount;
                //System.out.println("Updated. Delta time: ");
            }
        }

        protected long getDeltaTime() {
            update();
            return deltaTime;
        }
    }

    public void fullDispose(){
        deltaTimeController = null;
        graphicFile = null;
    }



    public static Image getGraphicFile() {
        return graphicFile;
    }

    public int getWidth() {
        return width;
    }

    public static void setActualPressedChar(char ch) {
        actualPressedChar = ch;
    }



    protected static String createClearString(String name) {
        String clearString = "";
        for (int i = 0; i < name.length(); i++){
            clearString+=" ";
        }
        return clearString;
    }
}
