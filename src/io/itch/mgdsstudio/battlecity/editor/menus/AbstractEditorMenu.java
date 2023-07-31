package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.*;
import com.mgdsstudio.engine.nesgui.TextArea;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;
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
        else if (actualMenuType == MenuType.FILE) return new File(editorController, lowerPanel);
        else if (actualMenuType == MenuType.EDIT) return new Edit(editorController, lowerPanel);
        else if (actualMenuType == MenuType.PREFERENCES) return new Preferences(editorController, lowerPanel);
        else if (actualMenuType == MenuType.PLAYER) return new Player(editorController, lowerPanel);
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

    protected abstract void initDataForStatement(int actualStatement);

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
        //if (frameButtonsCount )
        //int fullHeight = lowerPanelInEditor.getLowerTab().getHeight();


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

        //TextDataFieldWithText textLabel = new TextDataFieldWithText(editorController.getEngine(), labelCenterX, labelCenterY, labelWidth, labelHeight, labelName, editorController.getEngine().getEngine().g, labelName);
        //DigitDataFieldWithText textLabel = new DigitDataFieldWithText(editorController.getEngine(), labelCenterX, labelCenterY, labelWidth, labelHeight, labelName, editorController.getEngine().getEngine().g, 0);

        //DigitDataFieldWithText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, int defaultValue)

        //TextDataFieldWithText textLabel = new TextDataFieldWithText(editorController.getEngine(), labelCenterX, labelCenterY, labelWidth, labelHeight, labelName, editorController.getEngine().getEngine().g, labelName);
        //public TextDataFieldWithText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, String defaultValue) {
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

        int backButtonX = keyboardX;
        int backButtonWidth = labelWidth;
        int backButtonHeight = labelHeight;
        int backButtonY = (int) (keyboardY+keyboardHeight/2+gapY+labelHeight/2);


        ButtonWithFrameSelection back = new ButtonWithFrameSelection(editorController.getEngine(), backButtonX, backButtonY, backButtonWidth, backButtonHeight, this.back, editorController.getEngine().getEngine().g, true);
        guiElements.add(back);

    }



    protected Rectangle[] getCoordinatesForSquareButtonsAndColumnAlignment(int fullCount, int alongX){
        int fullWidth = lowerPanelInEditor.getLowerTab().getWidth();
        int fullHeight = lowerPanelInEditor.getLowerTab().getHeight();
        int left = lowerPanelInEditor.getLowerTab().getLeft();
        int upper = lowerPanelInEditor.getLowerTab().getUpper();
        //final float xGapCoef = 0.1f;
        
        int alongY = PApplet.ceil(fullCount/alongX);

       
        float relativeGap = 0.1f;
        float fullRelativeGapX = (alongX+1f)*relativeGap;
        float fullRelativeGapY = (alongY+1f)*relativeGap;
        float fullGapX =  (float) fullWidth*fullRelativeGapX;
        float fullGapY = (float) fullHeight*fullRelativeGapY;

        float sizesRelationship = (float)fullWidth/fullHeight;
        float countRelationship = (float)alongX/alongY;
        boolean criticalSizeIsX;
        if (sizesRelationship>countRelationship) criticalSizeIsX = false;
        else criticalSizeIsX = true;

        int singleGap;
        float minimalFullGap;
        int guiWidth;
        int guiHeight;
        if (!criticalSizeIsX) {
            minimalFullGap = fullGapY;
            singleGap = (int)(minimalFullGap/(alongX+1f));
            guiWidth = (int) ((fullWidth-minimalFullGap)/alongX);
        }
        else{
            minimalFullGap = fullGapX;
            singleGap = (int)(minimalFullGap/(alongY+1f));
            guiWidth = (int) ((fullHeight-minimalFullGap)/alongY);
        }
        guiHeight = (int) ((fullHeight-((alongY+1f)*singleGap))/alongY);
        Logger.debug("Full width x height: " + fullWidth + "x" + fullHeight + " gap: " +  singleGap + "; gui width: "  + guiWidth);
        //Buttons are squares
        Rectangle [] positions = calculatePositionsForParams(guiWidth, guiHeight, alongX, alongY, left, upper, singleGap, singleGap);
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

}
