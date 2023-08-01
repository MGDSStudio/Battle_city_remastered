package io.itch.mgdsstudio.engine.libs;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class ImageZoneLoader{
  public final static String FILE_NAME_FOR_GRAPHIC_ZONES_FILE = "Graphic_zones.json";
  private final static int NOT_FOUND = -9999;
  private String name;
  private final static String LEFT = "LEFT";
  private final static String UP = "UP";
  private final static String RIGHT = "RIGHT";
  private final static String DOWN = "DOWN";
  private final static String TILESET = "TILESET";
  private final static String NAME = "NAME";
  private ImageZoneSimpleData data;
  private int tileset;
  private String pathToTileset;
  
  public ImageZoneLoader(IEngine engine, String path, String name){
    JSONObject json = engine.getEngine().loadJSONObject(path);
    JSONObject jsonObject = json.getJSONObject(name);
    if (jsonObject!=null){
        int left = jsonObject.getInt(LEFT);
        int up = jsonObject.getInt(UP);
        int right = jsonObject.getInt(RIGHT);
        int down = jsonObject.getInt(DOWN);
        this.tileset = jsonObject.getInt(TILESET);
        data = new ImageZoneSimpleData(left, up,right, down);
    }
    else {
      data = null;
        this.tileset = NOT_FOUND;
    }
  }

  public ImageZoneLoader(IEngine engine, int [] graphicData){
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
          int left = jsonObject.getInt(LEFT);
          int up = jsonObject.getInt(UP);
          int right = jsonObject.getInt(RIGHT);
          int down = jsonObject.getInt(DOWN);
          this.tileset = jsonObject.getInt(TILESET);
          pathToTileset = engine.getPathToSpriteInAssets(tileset);
          data = new ImageZoneSimpleData(left, up,right, down);
          return;
        }
        else {
          Logger.debug("Name " + key + " is not " + nameToBeFind);
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

  public int getTileset(){
    return tileset;
  }

  public String getPath() {
    return pathToTileset;
  }
}
  
