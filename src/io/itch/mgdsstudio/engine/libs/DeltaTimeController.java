package io.itch.mgdsstudio.engine.libs;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.editor.EditorAction;
import io.itch.mgdsstudio.editor.EditorCommandPrefix;
import io.itch.mgdsstudio.editor.EditorListenersManagerSingleton;
import processing.core.PApplet;

public class DeltaTimeController {

    private long prevFrameTime;
    private int deltaTime;
    private PApplet engine;
    private boolean firstInit;

    public DeltaTimeController(PApplet engine) {
        this.engine = engine;
    }

    public void update(){
        if (!firstInit){
            prevFrameTime = engine.millis();
            firstInit = true;
        }
        else {
            deltaTime = (int)(engine.millis() - prevFrameTime);
            prevFrameTime = engine.millis();
        }
    }

    public int getDeltaTime(){
        return deltaTime;
    }
}
