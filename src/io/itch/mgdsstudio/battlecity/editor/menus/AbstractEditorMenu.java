package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.*;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneFullData;
import io.itch.mgdsstudio.battlecity.utilities.ImageZonesSorterInAccordingToLastUsed;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractEditorMenu {
    protected String back, apply;

    protected final static String KEYBOARD_GUI_NAME = "Keyboard";
    
    protected final LowerPanelInEditor lowerPanelInEditor;
    protected final static int NO_END = 9999;

    protected ArrayList <GuiElement> guiElements;
    protected final static int START_STATEMENT = 0;
    protected int actualStatement = START_STATEMENT;
    protected int nextStatement;
    protected final int endStatement;  // must be overwritten
    protected EditorController editorController;
    private GuiElement lastPressed = null;
    private boolean backCommand = false;
    //private boolean firstTouchFrame;

    protected AbstractEditorMenu(EditorController editorController, LowerPanelInEditor lowerPanelInEditor, int endStatement) {
        this.editorController = editorController;
        this.lowerPanelInEditor = lowerPanelInEditor;
        this.endStatement = endStatement;
        guiElements = new ArrayList<>();
        initDefaultButtonNames();
        //if (lowerPanelInEditor == null) Logger.error("Lower panel is null: " );
        initGui();
    }

    private void initDefaultButtonNames(){
        back = "BACK";
        apply = "APPLY";
    }

    public static AbstractEditorMenu createMenuForType(MenuType actualMenuType, EditorController editorController, LowerPanelInEditor lowerPanel) {
        if (actualMenuType == MenuType.MAIN) return new Main(editorController, lowerPanel);
        else if (actualMenuType == MenuType.FILE) return new FileMenu(editorController, lowerPanel);
        else if (actualMenuType == MenuType.EDIT) return new Edit(editorController, lowerPanel);
        else if (actualMenuType == MenuType.PREFERENCES) return new Preferences(editorController, lowerPanel);
        else if (actualMenuType == MenuType.PLAYER) return new Player(editorController, lowerPanel);
        else if (actualMenuType == MenuType.COLLECTABLE) return new AddCollectable(editorController, lowerPanel);
        else if (actualMenuType == MenuType.WALL) return new AddWall(editorController, lowerPanel);
        
        /*else if (actualMenuType == MenuType.ABOUT) return new About(editorController, lowerPanel);
        else if (actualMenuType == MenuType.ENEMY) return new Enemy(editorController, lowerPanel);
        else if (actualMenuType == MenuType.GRAPHIC) return new Graphic(editorController, lowerPanel);
        else if (actualMenuType == MenuType.WALL) return new Wall(editorController, lowerPanel);
        */
        
        
        
        
        
        
        else {
            Logger.error("No menu fot this type: " + actualMenuType);
            return new Main(editorController, lowerPanel);
        }
    }

    public void backPressed(){
        backCommand = true;
    }


    protected abstract void onBackPressed();
    
    protected abstract void initGui();

    public void update(){
        if (backCommand == true){
            onBackPressed();
            backCommand = false;
        }
        if (actualStatement != nextStatement){
            changeStatement();
        }
        else if (actualStatement == endStatement){
            complete();
        }
        else {
            for (GuiElement element: guiElements){
                element.update(editorController.getEngine().getEngine().mouseX, editorController.getEngine().getEngine().mouseY);
                if (element.getActualStatement() == GuiElement.RELEASED){
                    Logger.debug("Button " + element.getName() + " was released");
                    guiReleased(element);

                }
                else if (element.getActualStatement() == GuiElement.PRESSED){
                    if (element.wasStatementChanged()) Logger.debug("Button " + element.getName() + " was pressed");
                    if (!wasGuiPressedAlsoOnPrevFrame(element)){
                        setConsoleTextForFirstButtonPressing(element);
                    }
                    guiPressed(element);
                }
            }
        }
    }

    protected abstract void setConsoleTextForFirstButtonPressing(GuiElement element);

    private boolean wasGuiPressedAlsoOnPrevFrame(GuiElement element){
        boolean wasLastButtonChanged = false;
        if (lastPressed == null){
            wasLastButtonChanged = true;
            lastPressed = element;
        }
        else {
            if (!lastPressed.equals(element)){
                wasLastButtonChanged = true;
                lastPressed = element;
            }
        }
        return !wasLastButtonChanged;
    }

    public void draw() {
        for (GuiElement guiElement : guiElements){
            guiElement.draw(editorController.getEngine().getEngine().g);
        }
    }

    protected abstract void guiPressed(GuiElement element);

    protected abstract void guiReleased(GuiElement element);

    private void complete() {
        Logger.debug("Not implemented for the actual menu");
    }

    protected void initDataForStatement(int actualStatement)
    {
        initGui();
    }

    protected final void createSubmenuWithDefaultAlignedButtons(String [] names){
        guiElements.clear();
        int buttons = names.length;
        Rectangle [] zones = getCoordinatesForDefaultButtonsAlignment(buttons);
        for (int i = 0; i < buttons; i++){
            GuiElement gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, names[i], editorController.getEngine().getEngine().g, true);
            guiElements.add(gui);
        }
    }
    
    protected Rectangle[] getCoordinatesForDefaultButtonsAlignment(int frameButtonsCount){
        int buttonsCountForCalculations = frameButtonsCount;
        if (buttonsCountForCalculations<6) buttonsCountForCalculations = 6;
        int fullWidth = lowerPanelInEditor.getLowerTab().getWidth();
        int fullHeight = lowerPanelInEditor.getLowerTab().getHeight();

        int left = lowerPanelInEditor.getLowerTab().getLeft();
        int upper = lowerPanelInEditor.getLowerTab().getUpper();
        float buttonRelativeWidth = 0.6f;
        int guiWidth = (int) (fullWidth*buttonRelativeWidth);
        float relativeGap = 0.075f;
        float fullRelativeGapY = (buttonsCountForCalculations+1f)*relativeGap;
        float fullGapY = (float) (fullHeight*fullRelativeGapY);
        int yGap = (int) (fullGapY/(buttonsCountForCalculations+1));
        int guiHeight;
        guiHeight = (int) ((fullHeight-((buttonsCountForCalculations+1f)*yGap))/buttonsCountForCalculations);
        int xGap = (int) ((fullWidth-guiWidth)/2f);
        Rectangle [] positions = calculatePositionsForParams(guiWidth, guiHeight, 1, buttonsCountForCalculations, left, upper, xGap, yGap);
        Logger.debug("Button width: " + guiWidth + "; Full width: " + fullWidth);
        return positions;
    }

    protected void createSubmenuWithDigitKeyboard(boolean withApply, String labelName){
        int fullWidth = lowerPanelInEditor.getLowerTab().getWidth();
        int fullHeight = lowerPanelInEditor.getLowerTab().getHeight();
        int left = lowerPanelInEditor.getLowerTab().getLeft();
        int upper = lowerPanelInEditor.getLowerTab().getUpper();

        int defaultGuis = 2;
        if (withApply) defaultGuis = 3;
        float relativeGap = 0.075f;     //rel gap between button and keyboard
        float fullRelativeGap = (defaultGuis+1f+1f)*relativeGap;    //summ of all gaps - relative
        float fullGap = fullHeight*fullRelativeGap;                 //summ of all gaps - absolute
        float gapY = fullGap/(defaultGuis+1+1);                     //single absolute gap between guis

        float restHeightForGui = fullHeight-fullGap;                //rest height only for guis
        //float relativeHeightOfButtonGuiZone = 0.2f;         //from fullWidth!
        float relativeHeightOfButtonGuiZone = 0.1f;         //from rest height
        float textLabelHeight = relativeHeightOfButtonGuiZone*restHeightForGui;         //from fullWidth!
        float keyboardHeight = restHeightForGui-textLabelHeight*defaultGuis;

        int labelCenterX = (int) (left+fullWidth/2);
        int labelCenterY = (int) (gapY+textLabelHeight/2)+upper;
        int labelWidth = (int) (fullWidth*0.6f);
        int labelHeight = (int) textLabelHeight;
        TextLabel textLabel = new TextLabel(editorController.getEngine(), labelCenterX, labelCenterY, labelWidth, labelHeight, labelName);
        textLabel.setAnotherTextToBeDrawnAsName("");
        //textLabel.setPrefix("");
        Logger.debug("Button height: " + textLabelHeight + "; Keyboard height: " + keyboardHeight + "; full height: " + fullHeight);

        int keyboardX = labelCenterX;

        int keyboardY = (int) (labelCenterY+labelHeight/2+gapY+keyboardHeight/2);
        int keyboardWidth = (int) (labelWidth);
        //public DigitKeyboard(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics) {

        DigitKeyboard keyboard = new DigitKeyboard(editorController.getEngine(), keyboardX, keyboardY, keyboardWidth, (int) keyboardHeight, KEYBOARD_GUI_NAME, editorController.getEngine().getEngine().g);
        keyboard.setEmbeddedGui(textLabel);
        guiElements.add(textLabel);
        guiElements.add(keyboard);

        int upperButtonX = keyboardX;
        int buttonWidth = labelWidth;
        int buttonHeight = labelHeight;
        int firstButtonPosY = (int) (keyboardY+keyboardHeight/2+gapY+labelHeight/2);
        if (withApply){
            ButtonWithFrameSelection apply = new ButtonWithFrameSelection(editorController.getEngine(), upperButtonX, firstButtonPosY, buttonWidth, buttonHeight, this.apply, editorController.getEngine().getEngine().g, true);
            guiElements.add(apply);
            int lowerButtonY = (int) (firstButtonPosY+gapY+buttonHeight);
            ButtonWithFrameSelection back = new ButtonWithFrameSelection(editorController.getEngine(), upperButtonX, lowerButtonY, buttonWidth, buttonHeight, this.back, editorController.getEngine().getEngine().g, true);
            guiElements.add(back);

        }
        else {
            int backButtonY = (int) (keyboardY+keyboardHeight/2+gapY+labelHeight/2);
            ButtonWithFrameSelection back = new ButtonWithFrameSelection(editorController.getEngine(), upperButtonX, backButtonY, buttonWidth, buttonHeight, this.back, editorController.getEngine().getEngine().g, true);
            guiElements.add(back);
        }
    }

    protected final void createSubmenuWithColumnAlignedButtons(String [] names, int alongX){
        guiElements.clear();
        int buttonsCount = names.length;
        Rectangle [] zones = getCoordinatesForSquareButtonsAndColumnAlignment(buttonsCount , alongX);
        //Logger.debug("We have " + zones.length + " zones but all count: " + buttonsCount);
        for (int i = 0; i < buttonsCount; i++){
            GuiElement gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, names[i], editorController.getEngine().getEngine().g, true);
          //  Logger.debug("Button name is: " + names[i] + " and height: " + zones[i].height);

            guiElements.add(gui);
        }
    }


    protected Rectangle[] getCoordinatesForSquareButtonsAndColumnAlignment(int fullCount, final int alongX){
        int fullWidth = lowerPanelInEditor.getLowerTab().getWidth();
        int fullHeight = lowerPanelInEditor.getLowerTab().getHeight();
        int left = lowerPanelInEditor.getLowerTab().getLeft();
        int upper = lowerPanelInEditor.getLowerTab().getUpper();
        //final float xGapCoef = 0.1f;
        int alongY = PApplet.ceil((float)fullCount/alongX);
        float sizesRelationship = (float)fullWidth/fullHeight;
        float countRelationship = (float)alongX/alongY;
        boolean criticalSizeIsX;
        if (sizesRelationship>countRelationship) criticalSizeIsX = false;
        else criticalSizeIsX = true;
        float relativeGapY = 0.1f;
        float relativeGapX = 0.1f;
        if (alongY == 3) relativeGapY = 0.1f;
        else if (alongY == 4) relativeGapY = 0.075f;
        else if (alongY == 5) relativeGapY = 0.065f;
        else if (alongY == 6) relativeGapY = 0.05f;
        else if (alongY == 7) relativeGapY = 0.04f;
        if (alongX == 2) relativeGapX = 0.165f;
        else if (alongX == 3) relativeGapX = 0.135f;
        else if (alongX == 4) relativeGapX = 0.1f;

        float fullRelativeGapX = (alongX+1f)*relativeGapX;
        float fullRelativeGapY = (alongY+1f)*relativeGapY;
        float fullGapX =  (float) fullWidth*fullRelativeGapX;
        float fullGapY = (float) fullHeight*fullRelativeGapY;

        int singleGapY;
        int singleGapX;
        float minimalFullGapY;
        float minimalFullGapX;
        int guiWidth;
        int guiHeight;
        if (!criticalSizeIsX) {
            //Logger.debug("Critical size is X. Relative gap Y: " + relativeGapY + " full: " + fullRelativeGapY + " but along Y: " + alongY + " and along X " + alongX + " relativeX = " + relativeGapX);
            minimalFullGapY = fullGapY;
            minimalFullGapX = fullGapX;
            singleGapY = (int)(minimalFullGapY/(alongY+1f));
            singleGapX = (int)(minimalFullGapX/(alongX+1f));
            //Logger.debug("minimalFullGap " + minimalFullGapY);
            guiWidth = (int) ((fullWidth-minimalFullGapX)/(float)alongX);
        }
        else{
            //Logger.debug("Critical size is Y");
            minimalFullGapY = fullGapX;
            minimalFullGapX = fullGapX;
            singleGapY = (int)(minimalFullGapY/(alongY+1f));
            singleGapX = (int)(minimalFullGapX/(alongX+1f));
            guiWidth = (int) ((fullHeight-minimalFullGapX)/(float)alongX);
        }
        guiHeight = (int) ((fullHeight-((alongY+1f)*singleGapY))/alongY);
        //Logger.debug("Button height must be: " + guiHeight);
        //Logger.debug("Full width x height: " + fullWidth + "x" + fullHeight + " gap: " +  singleGapY + "; gui width: "  + guiWidth);
        //Buttons are squares

        Rectangle [] positions = calculatePositionsForParams(guiWidth, guiHeight, alongX, alongY, left, upper, singleGapX, singleGapY);
        return positions;
    }

    private Rectangle [] calculatePositionsForParams(int guiWidth, int guiHeight, int alongX, int alongY, int left, int upper, int gapX, int gapY){
        Rectangle [] positions = new Rectangle[alongX*alongY];
       

        int fullCount = 0;
        for (int i = 0; i < alongY; i++){
            for (int j = 0; j < alongX; j++){
                int centerX = gapX+guiWidth/2+j*(guiWidth+gapX);
                int centerY = gapY+guiHeight/2+i*(guiHeight+gapY);
                Rectangle rect = new Rectangle(centerX+left, centerY+upper, guiWidth, guiHeight);
                int number = j+i*alongX;
                positions[number] = rect;
                fullCount++;
            }
        }
        //Logger.debug("Must created: along sides " + alongX + "x" + alongY + " summ: " + fullCount);
        return positions;

   

    }

    private void changeStatement(){
        actualStatement = nextStatement;
        initDataForStatement(actualStatement);
    }

    protected GuiElement getGuiByName(String textField) {
        for (GuiElement guiElement : guiElements){
            if (guiElement.getName().equals( textField) || guiElement.getName()==textField){
                return guiElement;
            }
        }
        Logger.error("No GUI for name: " + textField + "; There are names only: ");
        for (GuiElement guiElement : guiElements){
            System.out.println("     " + guiElement.getName());
        }
        return null;
    }

    protected int getDigitValueFromKeyboard(){
        GuiElement keyboard = getGuiByName(KEYBOARD_GUI_NAME);
        if (keyboard != null){
            DigitKeyboard digitKeyboard = (DigitKeyboard) keyboard;
            GuiElement gui = digitKeyboard.getEmbeddedGui();
            try{
                TextLabel label = (TextLabel)gui;
                if (label.getUserData() == null){
                    Logger.error("User data is null. No time");
                }
                String userDataString = (String)label.getUserData();
                int value = 0;
                try{
                    value = Integer.parseInt(userDataString);
                }
                catch (Exception e){

                }
                return value;
            }
            catch (Exception e){
                Logger.error("Can not get value from gui");
                e.printStackTrace();
                return 0;
            }

        }
        else Logger.error("Can not get data value. Can not find keyboard");
        return 0;
    }

    protected void createMenuWithGraphicButtons(int alongX, int alongY, int page) {
        ImageZonesSorterInAccordingToLastUsed sorter = new ImageZonesSorterInAccordingToLastUsed(editorController.getEngine());
        ArrayList < ImageZoneFullData> data = sorter.getSortedData();
        if (data.size()<(alongX*alongY) && page != 0){
            page = 0;
            Logger.debug("Only one page is possible");
        }
        /*AllImageZonesFromFileLoader loader = new AllImageZonesFromFileLoader(editorController.getEngine());
        ArrayList < ImageZoneFullData> datas = loader.getImageZonesWithFulLData();
        Logger.debug("We have: " + datas.size() + " unique tilesets");
        */

    }

}
