package io.itch.mgdsstudio.engine.libs;

public class ImageZoneLoader{
  private final static int NOT_FOUND = -9999;
  private String name;
  private final static String LEFT = "LEFT";
  private final static String UP = "UP";
  private final static String RIGHT = "RIGHT";
  private final static String DOWN = "DOWN";
  private final static String TILESET = "TILESET";
  private final ImageZoneSimpleData data;
  private final int tileset;
  
  public ImageZoneLoader(IEngine engine, String path, String name, int tileset){
    json = engine.getEngine().loadJSONObject(path);
    JSONObject goat = json.getJSONObject(name);
    if (goat!=null){
        int left = goat.getInt(LEFT);
        int up = goat.getInt(UP);
        int right = goat.getInt(RIGHT);
        int down = goat.getInt(DOWN);
        tileset = goat.getInt(TILESET);
        data = new ImageZoneSimpleData(LEFT,UP,RIGHT,DOWN);
    }
    else {
      data = null;
      tileset = NOT_FOUND;
    }
    println(id + ", " + species + ", " + name);
  }
}
  }

  public ImageZoneSimpleData getData(){
    return data;
  }

  public int getTileset(){
    return tileset;
  }
}
  
