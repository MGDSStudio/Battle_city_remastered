package io.itch.mgdsstudio.battlecity.game.control.onscreencontrols;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.hud.Panel;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;

public class RectButton extends OnScreenControl {


    public RectButton(Panel panel, IEngine engine) {
        super(panel, engine, panel.getCenter(), RECTANGULAR, panel.getWidth(), panel.getHeight(), 0);

    }

    @Override
    protected void updatePlayerInteraction(IEngine engine) {
        /*//Logger.debugLog("Pos: " + pos.x + 'x' + pos.y + " sizes: " + width + "x" + height);
        if (previousPressedStatement){
            Logger.debugLog("Button was pressed");

            if (!engine.getEngine().mousePressed){
                Logger.debugLog("Button was released");
                pressedStatement = true;
                writeDataToListeners();
            }
            else if (pressedStatement) pressedStatement = false;
        }
        else if (pressedStatement) pressedStatement = false;*/
    }

    @Override
    protected void writeDataToListeners() {
        if (getListeners().size()>0) {
            GLobalSerialAction GLobalSerialAction = new GLobalSerialAction(ActionPrefixes.EXIT_FROM_GAME, -1,-1,-1,-1);
            for (GlobalListener globalListener : getListeners()){
                globalListener.appendCommand(GLobalSerialAction);
            }
        }


    }

    @Override
    protected void updateActualGraphic() {

    }

    @Override
    protected void rotateAndDrawSubgraphic(PGraphics graphics) {

    }
}
