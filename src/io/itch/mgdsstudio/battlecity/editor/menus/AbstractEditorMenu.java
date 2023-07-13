package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractEditorMenu {
    protected enum AlignmentType {
        DEFAULT, TWO_COLUMNS, ONE_COLUMN;
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

    protected abstract void guiPressed(GuiElement element);

    protected abstract void guiReleased(GuiElement element);

    private void complete() {
        Logger.debug("Not implemented for the actual menu");
    }

    private void changeStatement() {
        actualStatement = nextStatement;
    }


    protected Rectangle[] getCoordinatesForFrameButtons(int fullCount, AlignmentType alignment){
        Rectangle [] pos = new Rectangle[fullCount];
        int fullWidth = lowerPanelInEditor.getWidth();
        int fullHeight = lowerPanelInEditor.getHeight();
        int left = (int) (lowerPanelInEditor.getCenter().x-fullWidth/2);
        int upper = (int) (lowerPanelInEditor.getCenter().y-fullHeight/2);
        if (alignment == AlignmentType.TWO_COLUMNS){

        }
        return pos;
    }
}
