package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONObject;

public class ImageZoneLoader {
    public final static String FILE_NAME_FOR_GRAPHIC_ZONES_FILE = "Graphic_zones.json";
    public final static String LEFT = "LEFT";
    public final static String UP = "UP";
    public  final static String RIGHT = "RIGHT";
    public final static String DOWN = "DOWN";
    public final static String TILESET = "TILESET";
    public final static String NAME = "NAME";

    public final static String ALONG_X = "ALONG_X";
    public final static String ALONG_Y = "ALONG_Y";
    public final static String SPRITES_PER_SEC = "SPRITES_PER_SEC";
    public final static String PLAYING_TYPE = "PLAYING_TYPE";
    public final static String DIRECTION = "DIRECTION";

    protected final static int NOT_FOUND = -9999;
    protected String SPRITE = "SPRITE";
    protected String ANIMATION = "ANIMATION";
    protected final static String TYPE = "TYPE";

    protected int tileset;
    protected String pathToTileset;
    protected ImageZoneSimpleData data;


    protected void initBasicData(IEngine engine, JSONObject jsonObject){
        int left = jsonObject.getInt(LEFT);
        int up = jsonObject.getInt(UP);
        int right = jsonObject.getInt(RIGHT);
        int down = jsonObject.getInt(DOWN);
        this.tileset = jsonObject.getInt(TILESET);
        pathToTileset = engine.getPathToSpriteInAssets(tileset);
        data = new ImageZoneSimpleData(left, up, right, down);
    }
}
