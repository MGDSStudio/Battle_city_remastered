package io.itch.mgdsstudio.battlecity.game.camera;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.editor.EditorAction;
import io.itch.mgdsstudio.editor.EditorActionsListener;
import io.itch.mgdsstudio.editor.EditorCommandPrefix;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.DeltaTimeController;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.data.FloatList;
import processing.data.IntList;

import java.util.ArrayList;

public class EditorCamera extends Camera implements EditorActionsListener {


    private ArrayList <EditorAction> actions;
    private CameraMovementController cameraMovementController;
    public  EditorCamera(IEngine engine) {
        init(engine, new Coordinate(0,0));

    }

    public  EditorCamera(IEngine engine, Coordinate pos) {
        init(engine, pos);
    }

    private void init(IEngine engine, Coordinate pos){
        this.pos = pos;
        this.engine = engine;
        actions = new ArrayList<>();
        cameraMovementController = new CameraMovementController(engine.getEngine());
    }

    @Override
    public void update() {
        updateActions();
        cameraMovementController.update();
    }

    private void updateActions() {
        for (int i = (actions.size()-1); i >= 0; i--){
            if (actions.get(i).getPrefix() == EditorCommandPrefix.WORLD_SCROLLING){
                FloatList floats = actions.get(i).getFloatParameters();
                IntList integers = actions.get(i).getIntParameters();
                /*Logger.debug("Data size: " + floats.size());
                for (int j = 0; j < floats.size(); j++){
                    Logger.debug("Num: " + j + "; Value: " + floats.get(j));
                }*/
                cameraMovementController.appendVelocity(floats.get(0), floats.get(1), integers.get(0));
                actions.remove(i);
            }
        }
        if (actions.size()>20){
            Logger.error("Too many actions " + actions.size());
        }
    }

    @Override
    public void shiftCameraPos(Vec2 distance) {
        cameraMovementController.appendVelocity(distance);
    }



    @Override
    public void appendCommand(EditorAction action) {
        actions.add(action);
        //Logger.debug("Camera got action");
    }


    private class CameraMovementController{
        private Vec2 velocity;
        private final Vec2 mutableVelocity = new Vec2(0,0);
        private final float brakingCoef = 0.92f;
        private boolean stopped = true;
        private DeltaTimeController deltaTimeController;
        private final float deadZoneVelocity = 0.05f;

        public CameraMovementController(PApplet engine) {
            deltaTimeController = new DeltaTimeController(engine);
            velocity = new Vec2();
        }

        private void appendVelocity(Vec2 newVel){
            if (newVel.length()>velocity.length()){
                velocity.x = newVel.x;
                velocity.y = newVel.y;
                stopped = false;
            }
        }

        private void appendVelocity(float dX, float dY, int deltaTime){
            mutableVelocity.x = dX;
            mutableVelocity.y = dY;

            appendVelocity(mutableVelocity);
        }

        private void update(){
            deltaTimeController.update();
            if (!stopped){
                updateActualPos(deltaTimeController.getDeltaTime());
            }
        }

        private void updateActualPos(int deltaTime) {
            pos.x+=velocity.x;
            pos.y+=velocity.y;
            if ((velocity.length()*deltaTime) < deadZoneVelocity){
                velocity.x = 0;
                velocity.y = 0;
                stopped = true;
            }
            else {
                velocity.x*=brakingCoef;
                velocity.y*=brakingCoef;
            }

        }
    }
}
