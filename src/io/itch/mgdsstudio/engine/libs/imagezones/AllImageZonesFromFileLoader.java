package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

public class AllImageZonesFromFileLoader extends ImageZoneLoader{
    private ImageZoneSimpleData data;
    private int tileset;
    private String pathToTileset;
    private ArrayList

    public AllImageZonesFromFileLoader(IEngine engine){
        //Logger.debug("It must be a singleton to ");
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
                    this.tileset = jsonObject.getInt(TILESET);
                    pathToTileset = engine.getPathToSpriteInAssets(tileset);
                    data = new ImageZoneSimpleData(left, up,right, down);


            }
        }
        else{
            Logger.error("JSON array is null");
        }
        {
            Logger.error("Not found data in JSON");
            data = null;
            this.tileset = NOT_FOUND;
            pathToTileset = null;
        }
    }

    public ImageZoneSimpleData getData(){
        return data;
    }

    public String getPath() {
        return pathToTileset;
    }
}
