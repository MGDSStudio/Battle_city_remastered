package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class Tab extends GuiElement {
    private int guiHeight = 0;
    protected final static ImageZoneSimpleData arrowUpData = new ImageZoneSimpleData(0,221,59,267);
    protected final static ImageZoneSimpleData arrowDownData = new ImageZoneSimpleData(0,267,59,313);
    protected final static ImageZoneSimpleData shifter = new ImageZoneSimpleData(0,313,59,370);
    protected EightPartsFrameImage frame;
    protected ArrayList <GuiElement> elements = new ArrayList<>();
    protected int relativeY;
    protected int maxRelativeY = 100;
    protected PGraphics tabCanvas;
    protected int visibleHeight;
    protected boolean withArrows;

    private ArrowsController arrowsController;
    private ScrollArrow up, down;
    private final ElementsAddingController addingController;
    private final PGraphics basicGraphic;
    //private float leftUpper
    private final IEngine engineInteface;
    public Tab(IEngine engine, int leftX, int upperY, int width, int height, int visibleHeight, String name, PGraphics graphics) {
        super(engine, leftX+width/2, upperY+height/2, width, height, name, graphics);
        this.engineInteface = engine;
        this.visibleHeight = visibleHeight;
        addingController = new ElementsAddingController();
        maxRelativeY = height-visibleHeight;
        basicGraphic = graphics;
        init();
    }


    public ArrayList<GuiElement> getElements() {
        return elements;
    }

    public GuiElement getElementByName(String name){
        for (GuiElement guiElement : elements){
            if (guiElement.getName().equals(name) || guiElement.getName() == name){
                return guiElement;
            }
        }
        System.out.print("Can not find the gui element with name: " + name + ". Elements are: " );
        for (GuiElement guiElement : elements) {
            if (guiElement.getName().equals(name) || guiElement.getName() == name) {
                System.out.print(guiElement.getName() + ", ");
            }
        }
        System.out.println();
        return null;
    }

    public void addArrows(int upX, int upY, int downX, int downY, int width, int height){
        withArrows = true;
        arrowsController = new ArrowsController(upX, upY, downX, downY,  width,  height);
    }

    public void addGui(GuiElement element){
        elements.add(element);
        //alignElements();
       // resetFrameHeight();
    }

    public void clearGUI(GuiElement toBeRemoved){
        if (toBeRemoved == null) {
       /* */
            elements.clear();
        }
        else{
            for (int i = elements.size()-1; i >=0; i--){
                if (elements.get(i).equals(toBeRemoved)) {
                    elements.remove(i);
                    return;
                }
            }
        }
    }

    public void setUp(ScrollArrow up) {
        this.up = up;
        if (this.up != null && this.down != null) withArrows = true;
    }

    public void setDown(ScrollArrow down) {
        this.down = down;
        if (this.up != null && this.down != null) withArrows = true;
    }

    public void setGuiHeight(int guiHeight) {
        this.guiHeight = guiHeight;
    }

    public void createGui(String name, Class type, Object additionalData){
        if (guiHeight == 0) guiHeight = (GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width));
        GuiElement element = null;
        int normalWidth = width- ButtonWithCursor.NORMAL_HEIGHT*6;
        int x = width/2;
        int y = getYForNextGui();
        int xShifting = ButtonWithCursor.NORMAL_HEIGHT;
        if (type == ButtonWithCursor.class){
            element = new ButtonWithCursor(engineInteface, x+xShifting,y, normalWidth, guiHeight, name, basicGraphic);
            if (additionalData!= null) element.setUserData(additionalData);
        }
        else if (type == RadioButton.class){
            element = new RadioButton(engineInteface, x+xShifting,y, normalWidth, guiHeight, name, basicGraphic, false);
            RadioButton radioButton = (RadioButton) element;
            RadioButton.addButtonToList(elements, radioButton);
            if (additionalData!= null) element.setUserData(additionalData);
        }
        else if (type == CheckBox.class){
            element = new CheckBox(engineInteface, x+xShifting,y, normalWidth, guiHeight, name, basicGraphic, false);
            if (additionalData!= null) element.setUserData(additionalData);
        }
        else if (type == TextDataFieldWithText.class){
            normalWidth+= GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*3;
            String defaultData = "";
            if (additionalData == null) additionalData = name;
            if (additionalData.getClass() == String.class) defaultData = (String)additionalData;
            element = new TextDataFieldWithText(engineInteface, x,y, normalWidth, guiHeight, name, basicGraphic, defaultData);
        }
        else if (type == DigitDataFieldWithText.class){
            normalWidth+= GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*3;
            Integer defaultData = 0;
            if (additionalData == null) additionalData = defaultData;

            if (additionalData.getClass() == Integer.class) defaultData = (Integer) additionalData;
            element = new DigitDataFieldWithText(engineInteface, x,y, normalWidth, guiHeight, name, basicGraphic,defaultData);

        }
        else {
            System.out.println("This gui " + type.getName() +" can not be created on the tab. Create and add it to the tab from outside");
        }
        if (element!= null) {
            elements.add(element);
            updateFrameDimensionChanging();
        }
    }

    private void updateFrameDimensionChanging() {
        int lastLowerSide = (int) (elements.get(elements.size()-1).getUpperY()+elements.get(elements.size()-1).getHeight());
        int halfStep = (int) ((int) (GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*2.25f));
        int newHeight = (lastLowerSide + halfStep);
        if (newHeight<= height){
            //System.out.println("Tab must not be recreated. It is: " + height + " but the lower side after the last gui must be at: " + lastLowerSide);
        }
        else{
            //System.out.println("Frame must be larger. New height: " + newHeight + " but was: " + tabCanvas.height);
            int width = tabCanvas.width;
            int oldHeight = height;
            int delta = newHeight-oldHeight;
            relativeY = 0;
            maxRelativeY+=delta;
            if (engine.sketchRenderer() == PConstants.JAVA2D) {
                //System.out.println("Canvas recreated as JAVA2D");
                tabCanvas = engine.createGraphics(width, newHeight);
                tabCanvas.noSmooth();
            }
            else {
                //System.out.println("Canvas recreated as OPEN GL");
                //tabCanvas = engine.createGraphics(width, newHeight, PConstants.P2D);
                tabCanvas = engine.createGraphics(width, newHeight, engine.sketchRenderer());
                tabCanvas.noSmooth();
            }
            frame.setHeight(newHeight);
        }
    }

    public void clear(){
        for (int i = elements.size()-1; i >= 0; i--){
            elements.remove(i);
        }
    }

    private int getYForNextGui() {
        if (elements.size() == 0){
            return (int) (GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*2.25f);
        }
        else {
            final int normalButtonsStep = (int) (GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*1.5f);
            float lastElementPos = elements.get(elements.size()-1).getUpperY();
            int newPos = (int) (elements.get(elements.size()-1).getHeight()/2+lastElementPos+normalButtonsStep*2);
            return newPos;
        }
    }

    public void createGuiWithoutFrame(String name, Class type, PGraphics graphics){
        GuiElement element = null;
        int normalWidth = width- ButtonWithCursor.NORMAL_HEIGHT*3;
        float x = width/2+leftX+ ButtonWithCursor.NORMAL_HEIGHT*3;
        float y = (ButtonWithCursor.NORMAL_HEIGHT*1.5f)+upperY+relativeY;
        if (type == ButtonWithCursor.class){
            element = new ButtonWithCursor(engineInteface, (int) x, (int) y, normalWidth, ButtonWithCursor.NORMAL_HEIGHT, name, graphics);
            elements.add(element);
        }
    }

    private void init() {
        final ImageZoneSimpleData frameData = new ImageZoneSimpleData(0,66,154,221);
        final int basicWidth = frameData.rightX-frameData.leftX;
        frame = new EightPartsFrameImage(graphicFile, frameData, basicWidth, basicWidth, width, height, new PVector(0,0));
        if (engine.sketchRenderer() == PConstants.JAVA2D) {
            tabCanvas = engine.createGraphics(width, height+maxRelativeY);
            tabCanvas.noSmooth();
            //System.out.println("Created as JAVA 2D");
        }
        else {
            //tabCanvas = engine.createGraphics(width, height+maxRelativeY, PConstants.P2D);
            tabCanvas = engine.createGraphics(width, height+maxRelativeY, engine.sketchRenderer());
            tabCanvas.noSmooth();
            //tabCanvas = engine.createGraphics(width, height+maxRelativeY, PConstants.P2D);
            //System.out.println("Created as OPEN GL");
        }
       // tabCanvas = engine.createGraphics(width, height+maxRelativeY, engine.sketchRenderer());
    }

    @Override
    protected void updateFunction() {

    }

    @Override
    public void update(int mouseX, int mouseY){
        super.update(mouseX, mouseY);
        boolean translatingNow = updateTranslation(mouseY);
        if (!translatingNow) {
            for (GuiElement element : elements) {
                if (isElementVisible(element)) {
                    element.update((int) (mouseX - leftX), (int) (mouseY - upperY + relativeY));
                }
            }
        }
        if (up!= null) up.update(mouseX, mouseY);
        if (down != null) down.update(mouseX, mouseY);

    }

    private boolean isElementVisible(GuiElement element) {
        int upperSide = relativeY;
        int lowerSide = relativeY+visibleHeight;
        float elementCenterLine = element.getUpperY()+element.getHeight()/2;

        if (elementCenterLine>upperSide && elementCenterLine < lowerSide){
            return true;
        }
        else return false;

    }

    private boolean scroll(float delta){
        int prevRelativeY = relativeY;
        relativeY -= delta;
        if (relativeY < 0) {
            relativeY = 0;
        }
        if (relativeY > maxRelativeY) {
            relativeY = maxRelativeY;
            delta = maxRelativeY - prevRelativeY;
        }
        updateArrows();
        return true;
    }

    private boolean updateTranslation(int mouseY) {
        if (engine.mousePressed && actualStatement == PRESSED) {
            int delta = (int) (mouseY - engine.pmouseY);
            if (delta != 0) {
                return scroll(delta);
            }
        }
        return false;
    }

    private void updatePosForElements(int delta) {
        System.out.println("Frame was translated");
        for (GuiElement element : elements){
            element.setUpperY(element.getUpperY()+delta);
        }
    }

    public void addGui(Class newObject, String name){
        addingController.add(newObject, name);
    }


    /*
    @Override
    public final void draw(PGraphics graphic) {
        if (actualStatement != BLOCKED) {
            super.draw(graphic);

            frame.draw(graphic);
            for (GuiElement element : elements) element.draw(graphic);
            //graphic.image(tabCanvas, leftX, upperY, width, visibleHeight,0, relativeY, tabCanvas.width, (visibleHeight)+relativeY);

            if (withArrows) {
                up.draw(graphic);
                down.draw(graphic);
                //arrowsController.draw(graphic);
            }
            if (!shiftingWasSet) {
                setYShiftingForFont(graphic.textFont);
                shiftingWasSet = true;
            }
            //drawName(graphic, LEFT_ALIGNMENT_OS_SPECIFIC);
            ///drawCursor(graphic);
        }
        //System.out.println("Frame effective hight: " + height + " text height: " + graphic.textFont.getSize());

    }*/

     //Works only for Java2D
    public final void draw(PGraphics graphic) {
        if (actualStatement != BLOCKED) {

            tabCanvas.beginDraw();
            tabCanvas.clear();
            frame.draw(tabCanvas);
            for (GuiElement element : elements) element.draw(tabCanvas);
            tabCanvas.endDraw();
            super.draw(graphic);
            graphic.image(tabCanvas, leftX, upperY, width, visibleHeight,0, relativeY, tabCanvas.width, (visibleHeight)+relativeY);
            if (withArrows) {
                up.draw(graphic);
                down.draw(graphic);
            }
            if (!shiftingWasSet) {
                setYShiftingForFont(graphic.textFont);
                shiftingWasSet = true;
            }
        }
    }




    public void recalculateHeight() {
        if (elements.size()>0) {
            GuiElement last = elements.get(elements.size() - 1);
            int wantedHeight = (int) (last.upperY + last.getHeight() + elements.get(0).getUpperY()*1.5f);
            if (usingConsoleOutput) System.out.println("Last pos: " + ((last.upperY + last.getHeight())));
            if (tabCanvas.height > wantedHeight) {
                if (usingConsoleOutput) System.out.println("It must be smaller. From:  " + height + " to " + wantedHeight);
            } else {
                if (usingConsoleOutput) System.out.println("It must be taller. From:  " + height + " to " + wantedHeight);
            }
            if (wantedHeight < visibleHeight) wantedHeight = visibleHeight;
            if (engine.sketchRenderer() == PConstants.JAVA2D) tabCanvas = engine.createGraphics(tabCanvas.width, wantedHeight);
            else {
                tabCanvas = engine.createGraphics(tabCanvas.width, wantedHeight, PConstants.P2D);
                //tabCanvas = engine.createGraphics(tabCanvas.width, wantedHeight, engine.sketchRenderer());
            }
            height = tabCanvas.height;
            maxRelativeY = height - visibleHeight;
            frame.setHeight(height);

            if (maxRelativeY < 0) maxRelativeY = 0;

            if (up != null && down != null){
                if (visibleHeight>=height){
                    up.setWithArrows(false);
                    down.setWithArrows(false);
                }
                else {
                    up.setWithArrows(false);
                    down.setWithArrows(true);
                }
            }

        }
    }

    private void updateArrows(){
        if (up != null && down != null) {
            if (relativeY >= maxRelativeY) {
                down.setWithArrows(false);
                up.setWithArrows(true);
            }

            else if (relativeY <= 0) {
                down.setWithArrows(true);
                up.setWithArrows(false);
            }
            else{
                up.setWithArrows(true);
                down.setWithArrows(true);
            }
        }
    }

    public void scrollUp(float v) {
        if (usingConsoleOutput) System.out.println("Scroll up to " + v);
        scroll(v);
    }

    public void scrollDown(float v) {
        if (usingConsoleOutput) System.out.println("Scroll down to " + (-1f*v));
        scroll(-1f*v);
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }



    private class ArrowsController{
        protected final ImageZoneSimpleData arrowUpData = new ImageZoneSimpleData(0,221,59,267);
        protected final ImageZoneSimpleData arrowDownData = new ImageZoneSimpleData(0,267,59,313);
        protected final ImageZoneSimpleData arrowUpDataCleared = new ImageZoneSimpleData(0,221+149,59,267+149);
        protected final  ImageZoneSimpleData arrowDownDataCleared = new ImageZoneSimpleData(0,267+149,59,313+149);


        protected int upLeftX, upUpperY, downLeftX, downUpperY;
        protected int arrowWidth, arrowHeight;


        public ArrowsController(int upX, int upY, int downX, int downY, int width, int height) {

            this.arrowWidth = width;
            this.arrowHeight = height;
            upLeftX = upX;
            upUpperY = upY;
            downLeftX = downX;
            downUpperY = downY;

        }

        protected void draw(PGraphics graphics){
            if (withArrows){
                graphics.pushStyle();
                graphics.imageMode(PConstants.CORNER);

                graphics.image(graphicFile.getImage(), upLeftX, upUpperY, arrowWidth, arrowHeight, arrowUpData.leftX, arrowUpData.upperY, arrowUpData.rightX, arrowUpData.lowerY);
                graphics.image(graphicFile.getImage(), downLeftX, downUpperY, arrowWidth, arrowHeight, arrowDownData.leftX, arrowDownData.upperY, arrowDownData.rightX, arrowDownData.lowerY);

                graphics.popStyle();
            }
        }
    }


    private class ElementsAddingController{
        private int newLeftX, newUpperY, newCenterX, newCenterY;
        private int newWidth, newHeight;

        protected void add(Class newObject, String name){
            GuiElement newGui = null;
            calculateData(newObject);
            if (newObject == ButtonWithCursor.class){
                newGui = new ButtonWithCursor(engineInteface, newCenterX, newCenterY, newWidth, newHeight, name, tabCanvas);
            }

            if (newGui != null){
                elements.add(newGui);
            }
        }

        private void calculateData(Class newObject) {
            newLeftX = (int) leftX;
            newWidth = tabCanvas.width - 2 * newLeftX;
            newHeight = GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width);
            if (elements.size() == 0) {
                newUpperY = GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width);
            } else {
                GuiElement lastElement = elements.get(elements.size() - 1);
                newUpperY = (int) (lastElement.upperY + lastElement.getHeight() + newHeight);
            }
            newCenterX = newLeftX + newWidth / 2;
            newCenterY = newUpperY + newHeight / 2;

        }


    }

    public void appendAsKeyboardController(IVirtualKeyboardController controller){
        int i = 0;
        for (GuiElement guiElement : elements){
            if (guiElement instanceof DataFieldWithText){
                DataFieldWithText field = (DataFieldWithText) guiElement;
                field.setVirtualKeyboardController(controller);
                System.out.println();
                i++;
            }
            //next lines are not need
            else if (guiElement instanceof DigitDataFieldWithText ){
                DigitDataFieldWithText digitDataFieldWithText = (DigitDataFieldWithText) guiElement;
                digitDataFieldWithText.setVirtualKeyboardController(controller);
            }
            else if (guiElement instanceof TextDataFieldWithText){
                TextDataFieldWithText digitDataFieldWithText = (TextDataFieldWithText) guiElement;
                digitDataFieldWithText.setVirtualKeyboardController(controller);
            }
        }
        if (usingConsoleOutput) System.out.println("For this tab there are " + i + " components that need a virtual keyboard");
    }

}
