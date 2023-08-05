package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.dataloading.DataStringCreationMaster;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.EntitySelectionController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ISelectable;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.imagezones.SingleImageZoneFromFileLoader;
import processing.core.PGraphics;

import java.util.ArrayList;

public class Forest extends GraphicObject implements ISelectable {

    private final EntitySelectionController entitySelectionController;
    public Forest(IEngine engine, Coordinate pos, int angle, int width, int height){
        super(engine, pos, angle, width, height, -1, GraphicLayers.OVER_SKY_LEVEL);
        entitySelectionController = new EntitySelectionController();
    }

    public static Forest create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        int[] graphicData = entityData.getGraphicData();
        Forest sprite = new Forest(engine, pos, values[2], values[3], values[4]);
        sprite.loadGraphicDefaultData(engine,graphicData);
        int id = entityData.getId();
        if (id == NO_ID){
            sprite.setNextId();
        }
        return sprite;
    }


    private void loadGraphicDefaultData(IEngine engine, int [] graphicData){
        this.graphicData = graphicData;
        SingleImageZoneFromFileLoader loader = new SingleImageZoneFromFileLoader(engine, graphicData);
        final ImageZoneSimpleData data = loader.getData();
        final String pathToTileset = loader.getPath();
        loadImage(engine, pathToTileset, width, height, data);
    }


    public String getDataString(){
        int posX = (int) pos.x;
        int posY = (int) pos.y;
        ArrayList<Integer> dataList = new ArrayList<>();
        dataList.add(posX);
        dataList.add(posY);
        dataList.add((int)angle);
        dataList.add(width);
        dataList.add(height);
        ArrayList<Integer> graphicList = new ArrayList<>();
        if (graphicData != null){
            for (int i = 0; i < graphicData.length; i++){
                graphicList.add(graphicData[i]);
            }
        }
        DataStringCreationMaster dataStringCreationMaster = new DataStringCreationMaster(getId(), dataList, graphicList, this.getClass().getSimpleName());
        String dataString = dataStringCreationMaster.getDataString();
        return dataString;
    }

    @Override
    public boolean isSelected() {
        return entitySelectionController.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) entitySelectionController.setSelected(engine.getProcessing().millis());
        else entitySelectionController.clearSelection();
    }
    @Override
    public String getInEditorName() {
        return "FOREST AT " + (int)pos.x + "x" + (int)pos.y;
    }

    @Override
    public void draw(PGraphics graphics, Camera gameCamera) {
        if (entitySelectionController.isSelected()) {
            drawWithAlpha(graphics, gameCamera, entitySelectionController.getAlpha(engine.getProcessing().millis()));
        }
        else super.draw(graphics, gameCamera);
    }

}
