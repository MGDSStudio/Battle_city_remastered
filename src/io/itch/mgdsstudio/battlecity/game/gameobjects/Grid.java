package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferences;
import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferencesSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Grid extends Entity{

    private boolean visible = true;
    private final Vec2 mutTheoreticalCoordinate = new Vec2();

    private final Coordinate mutRealCoordinate = new Coordinate(0,0);

    private final EditorController editorController;
    private int gridStep, gridNullPointX, gridNullPointY;

    private final int linesThickness;

    private final Coordinate leftUpperFrameCorner, rightLowerFrameCorner;
    private final int alongX, alongY;
    private final Coordinate leftLineUpper = new Coordinate(0,0), upperLineLeft = new Coordinate(0,0);
    private final float lineWidth, lineHeight;


    public Grid(EditorController editorController) {
        super(editorController.getEngine(), new Coordinate(0,0), 0, IMMORTAL_LIFE, getWidth(editorController), getHeight(editorController));
        this.editorController = editorController;
        leftUpperFrameCorner = new Coordinate(editorController.getHud().getGraphicLeftPixel(), editorController.getHud().getGraphicUpperPixel());
        rightLowerFrameCorner = new Coordinate(editorController.getHud().getGraphicRightPixel(), editorController.getHud().getGraphicLowerPixel());
        linesThickness = (int) (2f*((float) this.editorController.getEngine().getEngine().width)/((float)(500f)));
        initGridStartParameters();
        alongX = PApplet.ceil(rightLowerFrameCorner.x-leftUpperFrameCorner.x)+0;
        alongY = PApplet.ceil(rightLowerFrameCorner.y-leftUpperFrameCorner.y)+0;
        lineWidth = rightLowerFrameCorner.x-leftUpperFrameCorner.x+gridStep*2;
        lineHeight = rightLowerFrameCorner.y-leftUpperFrameCorner.y+gridStep*2;
    }

    private void initGridStartParameters() {
        EditorPreferencesSingleton pref = EditorPreferencesSingleton.getInstance();
        gridStep = pref.getIntegerValue(EditorPreferences.GRID_STEP.name());
        gridNullPointX = pref.getIntegerValue(EditorPreferences.GRID_START_X.name());
        gridNullPointY = pref.getIntegerValue(EditorPreferences.GRID_START_Y.name());
        Logger.editor("Grid data: step: " + gridStep + "; Start: " + gridNullPointX + "x" + gridNullPointY);
    }

    private static int getWidth(EditorController editorController){
        return editorController.getGraphicWidth();
    }
    private static int getHeight(EditorController editorController){
        return editorController.getGraphicHeight();
    }


    private void update(Camera editorCamera){
        if (visible){
            updateLinePositions(editorCamera, upperLineLeft);
            updateLinePositions(editorCamera, leftLineUpper);
        }
    }

    private void updateLinePositions(Camera editorCamera, Coordinate result) {
        mutTheoreticalCoordinate.x = leftUpperFrameCorner.x+editorCamera.getPos().x;
        mutTheoreticalCoordinate.y = leftUpperFrameCorner.y+editorCamera.getPos().y;
        float x = mutTheoreticalCoordinate.x- gridNullPointX;
        float y = mutTheoreticalCoordinate.y- gridNullPointY;
        float restAfterDivisionX = x%gridStep;
        if (PApplet.abs(restAfterDivisionX) < (gridStep/2)){
            if (restAfterDivisionX < 0)  mutRealCoordinate.x = x-restAfterDivisionX;
            else mutRealCoordinate.x = x-restAfterDivisionX;
        }
        else {
            if (restAfterDivisionX < 0)  mutRealCoordinate.x = x-restAfterDivisionX-gridStep;
            else mutRealCoordinate.x = x-restAfterDivisionX+gridStep;
        }

        float restAfterDivisionY = y%gridStep;
        if (PApplet.abs(restAfterDivisionY) < (gridStep/2)){
            if (restAfterDivisionY < 0)  mutRealCoordinate.y = y-restAfterDivisionY;
            else mutRealCoordinate.y = y-restAfterDivisionY;
        }
        else {
            if (restAfterDivisionY < 0)  mutRealCoordinate.y = y-restAfterDivisionY-gridStep;
            else mutRealCoordinate.y = y-restAfterDivisionY+gridStep;
        }
        result.x = mutRealCoordinate.x-gridStep;
        result.y = mutRealCoordinate.y-gridStep;
    }

    public void draw(PGraphics graphic, Camera editorCamera){
        if (visible){
            update(editorCamera);
            drawRealPos(editorCamera,graphic);
        }
    }

    private void drawRealPos(Camera editorCamera, PGraphics graphic) {
        drawHorizontals(editorCamera, graphic);
        drawVerticals(editorCamera, graphic);
    }

    private void drawHorizontals(Camera editorCamera, PGraphics graphic){
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.noFill();
        graphic.strokeWeight(linesThickness);
        graphic.stroke(25,25,25,128);
        graphic.translate(editorCamera.getDrawPosX(upperLineLeft.x), editorCamera.getDrawPosY(upperLineLeft.y));
        for (int i = 0; i < alongY; i++){
            graphic.line(0, i*gridStep,lineWidth, i*gridStep);
        }
        graphic.popStyle();
        graphic.popMatrix();
    }

    private void drawVerticals(Camera editorCamera, PGraphics graphic){
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.noFill();
        graphic.strokeWeight(linesThickness);
        graphic.stroke(25,25,25,128);
        graphic.translate(editorCamera.getDrawPosX(leftLineUpper.x), editorCamera.getDrawPosY(leftLineUpper.y));
        for (int i = 0; i < alongX; i++){
            graphic.line(i*gridStep, 0,i*gridStep, lineHeight);
        }
        graphic.popStyle();
        graphic.popMatrix();
    }


    public Coordinate getActualCrossPos(){
        return mutRealCoordinate;
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
