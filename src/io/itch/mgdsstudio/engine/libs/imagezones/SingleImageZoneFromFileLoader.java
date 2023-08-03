package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class SingleImageZoneFromFileLoader extends ImageZoneLoader {
  //private String name;

  
  public SingleImageZoneFromFileLoader(IEngine engine, int [] graphicData){
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
          if (jsonObject.getString(TYPE) == SPRITE || jsonObject.get(TYPE).equals(SPRITE)) {
            initBasicData(engine, jsonObject);
            return;
          }
          else {
            Logger.error("This tileset " + key + " is not an sprite! The type is: " + jsonObject.getString(TYPE) + " must be " + SPRITE);
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
}
  
