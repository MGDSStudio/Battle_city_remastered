package io.itch.mgdsstudio.engine.libs;

public class ImageZoneLoader{
  private String name;
  private final static String LEFT = "LEFT";
  private final static String UP = "UP";
  private final static String RIGHT = "RIGHT";
  private final static String DOWN = "DOWN";
  private final ImageZoneSimpleData data;
  
  public ImageZoneLoader(IEngine engine, String path, String name){
    json = engine.getEngine().loadJSONObject(path);
    JSONObject goat = json.getJSONObject(name);
    if (goat!=null){
        int left = goat.getInt(LEFT);
        int up = goat.getInt(UP);
        int right = goat.getInt(RIGHT);
        int down = goat.getInt(DOWN);
      data 
    }
    else data = null;
    println(id + ", " + species + ", " + name);
  }
}
  }

  public ImageZoneSimpleData getData(){
    return data;
  }
}
  
