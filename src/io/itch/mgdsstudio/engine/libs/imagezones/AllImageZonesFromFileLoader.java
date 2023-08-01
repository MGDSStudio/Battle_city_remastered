package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

public class AllImageZonesFromFileLoader extends ImageZoneLoader{


    private ArrayList <ImageZoneFullData> imageZonesWithFulLData;

    public AllImageZonesFromFileLoader(IEngine engine){
        imageZonesWithFulLData = new ArrayList<>();
        final String path = engine.getPathToObjectInUserFolder(FILE_NAME_FOR_GRAPHIC_ZONES_FILE);
        JSONArray jsonArray = engine.getEngine().loadJSONArray(path);
        if (jsonArray!=null){
            Logger.debug("JSON file contains " + jsonArray.size() + " pos");
            for (int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String key = jsonObject.getString(NAME);
            int left = jsonObject.getInt(LEFT);
            int up = jsonObject.getInt(UP);
            int right = jsonObject.getInt(RIGHT);
            int down = jsonObject.getInt(DOWN);
            int tileset = jsonObject.getInt(TILESET);
            String pathToTileset = engine.getPathToSpriteInAssets(tileset);
            ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(left, up, right, down);
            ImageZoneFullData imageZoneFullData = new ImageZoneFullData(imageZoneSimpleData, pathToTileset, key);
            imageZonesWithFulLData.add(imageZoneFullData);
            }
        }
        else{
            Logger.error("JSON array is null");
        }
        {
            Logger.error("Not found data in JSON");
        }
    }

    public ArrayList<ImageZoneFullData> getImageZonesWithFulLData() {
        return imageZonesWithFulLData;
    }
}
