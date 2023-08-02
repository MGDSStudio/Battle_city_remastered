package io.itch.mgdsstudio.battlecity.editor;

import io.itch.mgdsstudio.battlecity.editor.data.EditorPreferencesSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;

import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;
import java.util.ArrayList;

public class Cross extends Entity implements EditorActionsListener{
    public final static int POINT_SIZE = 2;
    private ArrayList <EditorAction> actions = new ArrayList<>();
    private boolean visible = true;
    private Vec2 theoreticalCoordinate;
    private final ImageZoneSimpleData RECT = new ImageZoneSimpleData(35,362,35+16,362+16);
    private final ImageZoneSimpleData LU = new ImageZoneSimpleData(34,497,34+16,497+16);
    private final ImageZoneSimpleData RU = new ImageZoneSimpleData(51,497,51+16,497+16);
    private final ImageZoneSimpleData LD = new ImageZoneSimpleData(34,514,34+16,514+16);
    private final ImageZoneSimpleData RD = new ImageZoneSimpleData(51,514,51+16,514+16);
    private final ImageZoneSimpleData ARROW = new ImageZoneSimpleData(65,289, 78,307+18);


    private final Coordinate realCoordinate = new Coordinate(0,0);

    private final EditorController editorController;
    private int gridStep, gridStartX, gridStartY;

    private final int linesThickness;

    private float mapZoneCenterX, mapZoneCenterY;

    public enum Statement{
        INVISIBLE_AS_CROSS, INVISIBLE_AS_CELL_CENTER, CROSS, CELL_CENTER, TRIANGLE_RIGHT_UP, TRIANGLE_RIGHT_DOWN, TRIANGLE_LEFT_UP, TRIANGLE_LEFT_DOWN;
    }

    private Statement statement;
    private boolean toCellCenter;

    private GameRound gameRound;
    private final ArrayList<ISelectable> mutObjects = new ArrayList<>();

    public Cross(EditorController editorController) {
        super(editorController.getEngine(), new Coordinate(0,0), 0, IMMORTAL_LIFE, getSize(editorController), getSize(editorController));
        EditorListenersManagerSingleton singleton = EditorListenersManagerSingleton.getInstance();
        singleton.addAsListener(this);
        this.editorController = editorController;
        this.gameRound = editorController.getGameRound();
        theoreticalCoordinate = new Vec2(0,0);
        linesThickness = (int) (6f*((float) this.editorController.getEngine().getEngine().width)/((float)(500f)));
        mapZoneCenterX = this.editorController.getHud().getGraphicLeftPixel()+ this.editorController.getGraphicWidth()/2;
        mapZoneCenterY = this.editorController.getHud().getGraphicUpperPixel()+ this.editorController.getGraphicHeight()/2;
        initGridStartParameters();
        setStatement(Statement.CROSS);
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
        if (!statement.equals(Statement.INVISIBLE_AS_CROSS) && !statement.equals(Statement.INVISIBLE_AS_CELL_CENTER)){
             visible = true;
        }
        else visible = false;
        if (statement.equals(Statement.INVISIBLE_AS_CROSS) || (statement.equals(Statement.CROSS))) toCellCenter = false;
        else toCellCenter = true;
    }

    private void initGridStartParameters() {

       EditorPreferencesSingleton pref = EditorPreferencesSingleton.getInstance();
       gridStep = editorController.getGrid().getGridStep();
       gridStartX = editorController.getGrid().getGridShiftingX();
       gridStartY = editorController.getGrid().getGridShiftingX();
       //Logger.editor("Cross data: step: " + gridStep + "; Start: " + gridStartX + "x" + gridStartY);
    }

    private static int getSize(EditorController editorController){
        return editorController.getEngine().getEngine().width/15;
    }

    private void update(Camera editorCamera){
        updateActions();
        if (visible){
            theoreticalCoordinate.x = mapZoneCenterX+editorCamera.getPos().x;
            theoreticalCoordinate.y = mapZoneCenterY+editorCamera.getPos().y;
            if (toCellCenter) updateRealCoordinateForCellCenter();
            else updateRealCoordinateForCorner();

            pos.x = realCoordinate.x;
            pos.y = realCoordinate.y;
        }
    }


    private void updateActions() {
        if (actions.size()>0){
            for (int i = (actions.size()-1); i >= 0; i--){
                if (actions.get(i).getPrefix().equals(EditorCommandPrefix.GRID_STEP_CHANGED)){
                    gridStep = editorController.getGrid().getGridStep();
                }
                else if (actions.get(i).getPrefix().equals(EditorCommandPrefix.GRID_SHIFTING_CHANGED)){
                    gridStep = editorController.getGrid().getGridShiftingX();
                }
                actions.remove(i);
            }
        }
        if (actions.size()>20){
            Logger.error("Too many actions " + actions.size() + this.getClass());
        }
    }

    private void updateRealCoordinateForCellCenter(){
        float x = theoreticalCoordinate.x-gridStartX;
        float y = theoreticalCoordinate.y-gridStartY;
        float restAfterDivisionX = x%gridStep;
        float deltaX = (gridStep/2)-restAfterDivisionX;
        realCoordinate.x = x+deltaX;
        float restAfterDivisionY = y%gridStep;
        float deltaY = (gridStep/2) - restAfterDivisionY;
        realCoordinate.y = y+deltaY;
    }

    private void updateRealCoordinateForCorner() {
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
            if (visible){
                 drawRealPos(editorCamera,graphic);
            }
        }
    }

    private void drawRealPos(Camera editorCamera, PGraphics graphic) {
        graphic.pushMatrix();
        graphic.scale(1f);
        graphic.pushStyle();
        graphic.noFill();
        graphic.translate(editorCamera.getDrawPosX(realCoordinate.x), editorCamera.getDrawPosY(realCoordinate.y));
        if (statement.equals(Statement.CROSS)){
             renderCross(graphic);
        }
        else if (statement.equals(Statement.CELL_CENTER)){
             renderCellConture(graphic);
        }
        else renderTriangle();
        graphic.popStyle();
        graphic.popMatrix();
    }

    private void renderCross(PGraphics graphic){
        //To add arrow rendering
        graphic.strokeWeight(linesThickness);
        graphic.stroke(0,255,0);
        graphic.line(-width/2, 0, width/2,0);
        graphic.line(0, -width/2, 0, width/2);
    }

    private void renderCellConture(PGraphics graphic){
        graphic.strokeWeight(linesThickness);
        graphic.stroke(0,255,0);
        float halfW = width/2;
        graphic.line(-halfW, -halfW, halfW, -halfW);
        graphic.line(-halfW, halfW, halfW, halfW);
        graphic.line(-halfW, -halfW, -halfW, halfW);
        graphic.line(halfW, -halfW, halfW, halfW);
    }

    private void renderTriangle(){
        //Logger.debug("Triangle must be implementted");
        if (statement.equals(Statement.TRIANGLE_LEFT_UP)){

            graphic.line()
        }
    }

    public ArrayList <ISelectable> getObjectsUnder(){
         ArrayList <Entity> entities = gameRound.getEntities();
         Rectangle testArea = new Rectangle();
        float x = realCoordinate.x;
        float y = realCoordinate.y;
        if (statement == Statement.CROSS  ){
           testArea.width = POINT_SIZE;
            testArea.height = testArea.width;
        }
        else if (statement != Statement.INVISIBLE_AS_CROSS){
            testArea.width = gridStep;
            testArea.height = testArea.width;
        }
        testArea.x = (int) (realCoordinate.x - testArea.width/2f);
        testArea.y = (int) (realCoordinate.y - testArea.height/2f);
        mutObjects.clear();
         for (Entity entity : entities){
              boolean inZone = entity.inZone(testArea);
             if (entity instanceof ISelectable && inZone){
                    mutObjects.add((ISelectable)entity);
             }
         }
         return mutObjects;
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

    @Override
    public void appendCommand(EditorAction action) {
        actions.add(action);
        //Logger.debug("Camera got action");
    }

    private class DrawingController{
         private Image image;
        // private 

    }
}
