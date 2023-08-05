package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class SingleAnimationZoneFromFileLoader extends ImageZoneLoader{

    private int alongX, alongY, spritesPerSecond, playingType, direction;

    private void initAnimationData(IEngine engine, JSONObject jsonObject) {
        alongX = jsonObject.getInt(ALONG_X);
        alongY = jsonObject.getInt(ALONG_Y);
        spritesPerSecond = jsonObject.getInt(SPRITES_PER_SEC );
        playingType = jsonObject.getInt(PLAYING_TYPE);
        direction = jsonObject.getInt(DIRECTION);
    }

    public SingleAnimationZoneFromFileLoader(IEngine engine, int [] graphicData){
        final String path = engine.getPathToObjectInUserFolder(FILE_NAME_FOR_GRAPHIC_ZONES_FILE);
        JSONArray jsonArray = engine.getProcessing().loadJSONArray(path);
        String nameToBeFind = ""+graphicData[0];
        if (jsonArray!=null){
            Logger.debug("JSON file contains " + jsonArray.size() + " pos");
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key = jsonObject.getString(NAME);
                if (key.equals(nameToBeFind) || key == nameToBeFind){
                    if (jsonObject.getString(TYPE) == ANIMATION  || jsonObject.get(TYPE).equals(ANIMATION)) {
                        initBasicData(engine, jsonObject);
                        initAnimationData(engine, jsonObject);
                        return;
                    }
                    else {
                        Logger.error("This tileset " + key + " is not an animation! Type is: " + jsonObject.getString(TYPE) + " must be " + ANIMATION);
                    }
                }
                else {
                    //Logger.debug("Name " + key + " is not " + nameToBeFind);
                }
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

    public int getAlongX() {
        return alongX;
    }

    public int getAlongY() {
        return alongY;
    }

    public int getSpritesPerSecond() {
        return spritesPerSecond;
    }

    public int getPlayingType() {
        return playingType;
    }

    public int getDirection() {
        return direction;
    }
}
