package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.DataStringCreationMaster;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.imagezones.SingleImageZoneFromFileLoader;

import java.util.ArrayList;

public class Forest extends GraphicObject{


    public Forest(IEngine engine, Coordinate pos, int angle, int width, int height){
        super(engine, pos, angle, width, height, -1, GraphicLayers.OVER_SKY_LEVEL);
        //loadGraphicDefaultData(engine);
    }

    public static Forest create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        /*int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        Forest wall = new Forest(engine, pos, values[2], values[3], values[4], values[5]);
        wall.setId(entityData.getId());
        return wall;

         */
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

    /*public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(896,64, 960, 128);
        loadImage(engine, path, width, height, data);
    }*/

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
}
