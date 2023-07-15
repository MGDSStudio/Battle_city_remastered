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
    protected int actualStatement;
    protected int nextStatement;
    protected final int endStatement;  // must be overwritten
    protected EditorController editorController;

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
                if (element.getActualStatement() == GuiElement.RELEASED){
                    guiReleased(element);
                }
                else if (element.getActualStatement() == GuiElement.PRESSED){
                    guiPressed(element);
                }
            }
        }
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
    }


    protected Rectangle[] getCoordinatesForFrameButtons(int fullCount, AlignmentType alignment){

        int fullWidth = lowerPanelInEditor.getWidth();
        int fullHeight = lowerPanelInEditor.getHeight();
        int left = (int) (lowerPanelInEditor.getCenter().x-fullWidth/2);
        int upper = (int) (lowerPanelInEditor.getCenter().y-fullHeight/2);
        //final float xGapCoef = 0.1f;
        int alongX = 1;
        int alongY = 1;
        if (alignment == AlignmentType.FOUR_COLUMNS){
            alongX = 4;
            alongY = PApplet.ceil(fullCount/alongX);
        }
        float relativeGap = 0.05f;
        float fullRelativeGapX = (alongX+2)*relativeGap;
        float fullRelativeGapY = (alongY+2)*relativeGap;
        float fullGapX = fullWidth*fullRelativeGapX;
        float fullGapY = fullHeight*fullRelativeGapY;

        int singleGap;
        float minimalFullGap;
        int guiWidth;
        if (fullGapY<fullGapX) {
            minimalFullGap = fullGapY;
            singleGap = (int)(minimalFullGap/alongY);
            guiWidth = (int) ((fullWidth-minimalFullGap)/alongX);
        }
        else{
            minimalFullGap = fullGapX;
            singleGap = (int)(minimalFullGap/alongX);
            guiWidth = (int) ((fullHeight-minimalFullGap)/alongY);
        }
        int guiHeight = guiWidth;

        //Buttons are squares
        Rectangle [] positions = calculatePositionsForParams(guiWidth, guiHeight, alongX, alongY, left, upper, singleGap, singleGap);
        return positions;
    }

    private Rectangle [] calculatePositionsForParams(int guiWidth, int guiHeight, int alongX, int alongY, int left, int upper, int gapX, int gapY){
        Rectangle [] positions = new Rectangle[alongX*alongY];
        int fullCount = 0;
        for (int i = 0; i < alongX; i++){
            for (int j = 0; j < alongY; j++){
                int centerX = gapX+guiWidth/2+i*(guiWidth+gapX);
                int centerY = gapY+guiHeight/2+j*(guiHeight+gapY);
                Rectangle rect = new Rectangle(centerX+left, centerY+upper, guiWidth, guiHeight);
                positions[fullCount] = rect;
                fullCount++;
            }
        }
        return positions;
    }
}
