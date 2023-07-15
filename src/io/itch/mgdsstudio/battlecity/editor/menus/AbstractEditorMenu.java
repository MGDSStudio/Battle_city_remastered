package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractEditorMenu {


    protected enum AlignmentType {
        DEFAULT, FOUR_COLUMNS,  TWO_COLUMNS, ONE_COLUMN;
    }
    protected final LowerPanelInEditor lowerPanelInEditor;
    protected final static int NO_END = 9999;

    protected ArrayList <GuiElement> guiElements;
    protected final static int START_STATEMENT = 0;
    protected int actualStatement = START_STATEMENT;
    protected int nextStatement;
    protected final int endStatement;  // must be overwritten
    protected EditorController editorController;
    private GuiElement lastPressed = null;
    //private boolean firstTouchFrame;

    protected AbstractEditorMenu(EditorController editorController, LowerPanelInEditor lowerPanelInEditor, int endStatement) {
        this.editorController = editorController;
        this.lowerPanelInEditor = lowerPanelInEditor;
        this.endStatement = endStatement;
        guiElements = new ArrayList<>();
        //if (lowerPanelInEditor == null) Logger.error("Lower panel is null: " );
        initGui();
    }

    protected abstract void initGui();

    public void update(){
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
                    guiReleased(element);
                }
                else if (element.getActualStatement() == GuiElement.PRESSED){
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

    private void changeStatement() {
        actualStatement = nextStatement;
        initDataForStatement(actualStatement);
    }

    protected abstract void initDataForStatement(int actualStatement);


    protected Rectangle[] getCoordinatesForFrameButtons(int fullCount, AlignmentType alignment){

        int fullWidth = lowerPanelInEditor.getLowerTab().getWidth();
        int fullHeight = lowerPanelInEditor.getLowerTab().getHeight();
        int left = lowerPanelInEditor.getLowerTab().getLeft();
        int upper = lowerPanelInEditor.getLowerTab().getUpper();
        //final float xGapCoef = 0.1f;
        int alongX = 1;
        int alongY = 1;
        if (alignment == AlignmentType.FOUR_COLUMNS){
            alongX = 4;
            alongY = PApplet.ceil(fullCount/alongX);
        }
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
            //guiHeight = (int) ((fullHeight-((alongY+1f)*singleGap))/alongY);
        }
        else{
            minimalFullGap = fullGapX;
            singleGap = (int)(minimalFullGap/(alongY+1f));
            guiWidth = (int) ((fullHeight-minimalFullGap)/alongY);

        }
        guiHeight = (int) ((fullHeight-((alongY+1f)*singleGap))/alongY);
        //int guiHeight = guiWidth;
        //Logger.debug("Full height: " + fullHeight + "; minimalFullGap: " + minimalFullGap + "; guiHeight: " + guiHeight + "; singleGap: " + singleGap);
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

        /*
        int fullCount = 0;
        for (int i = 0; i < alongX; i++){
            for (int j = 0; j < alongY; j++){
                int centerX = gapX+guiWidth/2+i*(guiWidth+gapX);
                int centerY = gapY+guiHeight/2+j*(guiHeight+gapY);
                Rectangle rect = new Rectangle(centerX+left, centerY+upper, guiWidth, guiHeight);
                int number = i+j*alongX;
                positions[number] = rect;
                fullCount++;
            }
        }
        return positions;
         */

    }
}
