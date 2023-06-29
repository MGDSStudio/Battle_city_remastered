package io.itch.mgdsstudio.editor;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.awt.*;

public class MouseMovementController {
    private final static float MINIMAL_STEP = 50f; //Px per 1000 milis
    private int prevX, prevY;
    private final Vec2 movementVector = new Vec2();
    private long prevFrameTime;
    private int deltaTime;
    private boolean firstInit;
    private IEngine engine;
    private Rectangle rectangle;

    public MouseMovementController(IEngine engine, Rectangle rectangle) {
        this.engine = engine;
        this.rectangle = rectangle;
    }

    public void update(int millis){
        if (!firstInit){
            prevX = engine.getEngine().mouseX;
            prevY = engine.getEngine().mouseY;
            firstInit = true;
        }
        else {
            deltaTime = (int)(millis - prevFrameTime);
            prevFrameTime = millis;
            if (engine.getEngine().mousePressed) {
                if (isMouseOnZone()) {
                    int deltaX = engine.getEngine().mouseX - prevX;
                    int deltaY = engine.getEngine().mouseY - prevY;
                    movementVector.x = deltaX;
                    movementVector.y = deltaY;
                    if (mustBeMovementMade()) {
                        EditorAction editorAction = new EditorAction(EditorCommandPrefix.WORLD_SCROLLING);
                        editorAction.addParameter(deltaX);
                        editorAction.addParameter(deltaY);
                        editorAction.addParameter(deltaTime);
                        EditorListenersManagerSingleton.getInstance().notify(editorAction);
                        Logger.debug("Zone can be scrolled to " + deltaX + "x" + deltaY);
                    }
                }
            }
            prevX = engine.getEngine().mouseX;
            prevY = engine.getEngine().mouseY;
        }
    }

    private boolean isMouseOnZone() {
        if (engine.getEngine().mouseX> rectangle.x){
            if (engine.getEngine().mouseX<(rectangle.x+rectangle.width)){
                if (GlobalConstants.Y_AXIS_DOWN) {
                    if (engine.getEngine().mouseY > rectangle.y) {
                        if (engine.getEngine().mouseY < (rectangle.y + rectangle.height)) {
                            return true;
                        }
                    }
                }
                else {
                    if (engine.getEngine().mouseY < rectangle.y) {
                        if (engine.getEngine().mouseY > (rectangle.y - rectangle.height)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean mustBeMovementMade() {
        if (movementVector.length() > MINIMAL_STEP) return false;
        else return true;
    }
}
