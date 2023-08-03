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
            String type = jsonObject.getString(TYPE);
            String pathToTileset = engine.getPathToSpriteInAssets(tileset);
            ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(left, up, right, down);
            ImageZoneFullData imageZoneFullData;
            if (type.equals(SPRITE)) imageZoneFullData = new ImageZoneFullData(imageZoneSimpleData, pathToTileset, key);
            else{
                int alongX = jsonObject.getInt(ALONG_X);
                int alongY = jsonObject.getInt(ALONG_Y);
                int spritesPerSecond = jsonObject.getInt(SPRITES_PER_SEC);
                int playingType = jsonObject.getInt(PLAYING_TYPE);
                int direction  = jsonObject.getInt(DIRECTION);
                imageZoneFullData = new AnimationZoneFullData(imageZoneSimpleData, pathToTileset, key, alongX, alongY, spritesPerSecond, playingType, direction);
            }
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
