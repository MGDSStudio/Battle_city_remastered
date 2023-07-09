package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.util.ArrayList;

public abstract class AbstractEditorMenu {
    protected final LowerPanelInEditor lowerPanelInEditor;
    protected final static int NO_END = 9999;
    protected ArrayList <GuiElement> guiElements;
    protected final static int START_STATEMENT = 0;
    protected int actualStatement;
    protected int nextStatement;
    protected final int endStatement;  // must be overwritten

    protected AbstractEditorMenu(LowerPanelInEditor lowerPanelInEditor, int endStatement) {
        this.lowerPanelInEditor = lowerPanelInEditor;
        this.endStatement = endStatement;
        guiElements = new ArrayList<>();
    }

    public void update(){
        if (actualStatement != nextStatement){
            changeStatement();
        }
        else if (actualStatement == endStatement){
            complete();
        }
    }

    private void complete() {
        Logger.debug("Not implemented for the actual menu");
    }

    private void changeStatement() {
        actualStatement = nextStatement;
    }
}
