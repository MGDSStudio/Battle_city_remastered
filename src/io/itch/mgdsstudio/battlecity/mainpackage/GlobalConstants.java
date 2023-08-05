package io.itch.mgdsstudio.battlecity.mainpackage;

public interface GlobalConstants {

    String NAME_FOR_LEVEL_GRAPHIC_FILE = "Game_graphic.png";
    //String NAME_FOR_LEVEL_GRAPHIC_FILE = "Game_graphic.png";

    String TILESET_PREFIX = "Tileset_";
    String TILESET_EXTENSION = ".png";
    String NAME_FOR_TANK_GRAPHIC_FILE = "Tileset_1.png";

    String NAME_FOR_HUD_GRAPHIC_FILE = "HUD.png";

    int WINDOWS = 0;
    int LINUX = 1;
    int ANDROID = 2;

    int EASY_DIFFICULTY = 0;
    int MEDIUM_DIFFICULTY = 1;
    int HARD_DIFFICULTY = 2;

    // Program args:
    int PLAYER_IN_SINGLEPLAYER_MODE = -1;
    int PLAYER_AS_SERVER = 0;
    int USER_1_AS_CLIENT = 1;

    //Fonts
    String MAIN_FONT = "Font1.vlw";
    String SECONDARY_FONT = "Font2.vlw";

    //Renderers
    boolean JAVA_RENDERER = false;
    boolean OPENGL_RENDERER = true;

    boolean Y_AXIS_DOWN = true;
    int ERROR_CODE = -9999;
    int FPS_IN_GAME = 60;
    int FPS_IN_EDITOR = 30;

}
