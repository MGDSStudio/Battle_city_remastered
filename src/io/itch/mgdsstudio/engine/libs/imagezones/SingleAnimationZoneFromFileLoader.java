package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class SingleAnimationZoneFromFileLoader extends ImageZoneLoader{

    private final static String ALONG_X = "ALONG_X";
    private final static String ALONG_Y = "ALONG_Y";
    private final static String SPRITES_PER_SEC = "SPRITES_PER_SEC";
    private final static String PLAYING_TYPE = "PLAYING_TYPE";
    private final static String DIRECTION = "DIRECTION";

    /*
    "NAME": "11",
    "TILESET": 1,
    "ID": 11,
    "LEFT": 832,
    "UP": 0,
    "RIGHT": 896,
    "DOWN": 192,
    "ALONG_X": 1,
    "ALONG_Y": 3,
    "SPRITES_PER_SEC": 10,
    "PLAYING_TYPE": 0,
    "TYPE": "ANIMATION",
    "COMMENT": "WATER"
     */

    private int alongX, alongY, spritesPerSecond, playingType, direction;

    private void initAnimationData(IEngine engine, JSONObject jsonObject) {
        /*int left = jsonObject.getInt(LEFT);
        int up = jsonObject.getInt(UP);
        int right = jsonObject.getInt(RIGHT);
        int down = jsonObject.getInt(DOWN);
        this.tileset = jsonObject.getInt(TILESET);
        pathToTileset = engine.getPathToSpriteInAssets(tileset);
        data = new ImageZoneSimpleData(left, up, right, down);*/
        alongX = jsonObject.getInt(ALONG_X);
        alongY = jsonObject.getInt(ALONG_Y);
        spritesPerSecond = jsonObject.getInt(SPRITES_PER_SEC );
        playingType = jsonObject.getInt(PLAYING_TYPE);
        direction = jsonObject.getInt(DIRECTION);
    }

    public SingleAnimationZoneFromFileLoader(IEngine engine, int [] graphicData){
        //Logger.debug("It must be a singleton to ");
        final String path = engine.getPathToObjectInUserFolder(FILE_NAME_FOR_GRAPHIC_ZONES_FILE);
        JSONArray jsonArray = engine.getEngine().loadJSONArray(path);
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
