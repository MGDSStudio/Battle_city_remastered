package io.itch.mgdsstudio.battlecity.editor;

import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferences;
import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferencesSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;

import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Cross extends Entity {
    private boolean visible = true;
    private Vec2 theoreticalCoordinate;

    private final Coordinate realCoordinate = new Coordinate(0,0);

    private final EditorController editorController;
    private int gridStep, gridStartX, gridStartY;

    private final int linesThickness;

    private float mapZoneCenterX, mapZoneCenterY;


    public Cross(EditorController editorController) {
        super(editorController.getEngine(), new Coordinate(0,0), 0, IMMORTAL_LIFE, getSize(editorController), getSize(editorController));
       this.editorController = editorController;
        theoreticalCoordinate = new Vec2(0,0);



        linesThickness = (int) (6f*((float) this.editorController.getEngine().getEngine().width)/((float)(500f)));
        mapZoneCenterX = this.editorController.getHud().getGraphicLeftPixel()+ this.editorController.getGraphicWidth()/2;
        mapZoneCenterY = this.editorController.getHud().getGraphicUpperPixel()+ this.editorController.getGraphicHeight()/2;
        initGridStartParameters(editorController);
    }

    private void initGridStartParameters(EditorController editorController) {
       EditorPreferencesSingleton pref = EditorPreferencesSingleton.getInstance();
       gridStep = pref.getIntegerValue(EditorPreferences.GRID_STEP.name());
       gridStartX = pref.getIntegerValue(EditorPreferences.GRID_START_X.name());
       gridStartY = pref.getIntegerValue(EditorPreferences.GRID_START_Y.name());
       //Logger.editor("Cross data: step: " + gridStep + "; Start: " + gridStartX + "x" + gridStartY);
    }

    private static int getSize(EditorController editorController){
        return editorController.getEngine().getEngine().width/15;
    }

    private void update(Camera editorCamera){
        if (visible){

            theoreticalCoordinate.x = mapZoneCenterX+editorCamera.getPos().x;
            theoreticalCoordinate.y = mapZoneCenterY+editorCamera.getPos().y;
            updateRealCoordinate();
            pos.x = realCoordinate.x;
            pos.y = realCoordinate.y;
        }
    }

    private void updateRealCoordinate() {
        float x = theoreticalCoordinate.x-gridStartX;
        float y = theoreticalCoordinate.y-gridStartY;
        float restAfterDivisionX = x%gridStep;
        if (PApplet.abs(restAfterDivisionX) < (gridStep/2)){
            if (restAfterDivisionX < 0)  realCoordinate.x = x-restAfterDivisionX;
            else realCoordinate.x = x-restAfterDivisionX;
        }
        else {
            if (restAfterDivisionX < 0)  realCoordinate.x = x-restAfterDivisionX-gridStep;
            else realCoordinate.x = x-restAfterDivisionX+gridStep;
        }

        float restAfterDivisionY = y%gridStep;
        if (PApplet.abs(restAfterDivisionY) < (gridStep/2)){
            if (restAfterDivisionY < 0)  realCoordinate.y = y-restAfterDivisionY;
            else realCoordinate.y = y-restAfterDivisionY;
        }
        else {
            if (restAfterDivisionY < 0)  realCoordinate.y = y-restAfterDivisionY-gridStep;
            else realCoordinate.y = y-restAfterDivisionY+gridStep;
        }
    }

    public void draw(PGraphics graphic, Camera editorCamera){
        if (visible){
            update(editorCamera);
            drawRealPos(editorCamera,graphic);
        }
    }

    private void drawRealPos(Camera editorCamera, PGraphics graphic) {
        graphic.pushMatrix();
        graphic.scale(1f);
        graphic.pushStyle();
        graphic.noFill();
        graphic.translate(editorCamera.getDrawPosX(realCoordinate.x), editorCamera.getDrawPosY(realCoordinate.y));
        //Logger.debug("Drawn for: " + (int)(realCoordinate.x) + "x" + (int)(realCoordinate.y) + " at: " + editorCamera.getDrawPosX(realCoordinate.x) + "x" + editorCamera.getDrawPosY(realCoordinate.y));
        graphic.strokeWeight(linesThickness);
        graphic.stroke(0,255,0);
        graphic.line(-width/2, 0,width/2,0);
        graphic.line(0, -width/2, 0,width/2);

        graphic.popStyle();
        graphic.popMatrix();
    }


    public Coordinate getActualCrossPos(){
        return realCoordinate;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean mustBeAlwaysAbove() {
        return true;
    }
}
