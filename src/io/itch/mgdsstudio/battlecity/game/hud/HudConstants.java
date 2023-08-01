package io.itch.mgdsstudio.battlecity.game.hud;

import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

public class HudConstants {
    public final static String RELATIVE_PATH = "HUD.png";
    //Neutral
    public final static ImageZoneSimpleData LEFT_STICK_NEUTRAL_ZONE = new ImageZoneSimpleData(0,0, 127, 127);
    public final static ImageZoneSimpleData RIGHT_STICK_NEUTRAL_ZONE = new ImageZoneSimpleData(127,0, 127+127, 127);
    //CCW
    public final static ImageZoneSimpleData LEFT_STICK_CW = new ImageZoneSimpleData(0,127, 127, 127+127);
    public final static ImageZoneSimpleData RIGHT_STICK_CW = new ImageZoneSimpleData(127,127, 127+127, 127+127);
    //CW
    public final static ImageZoneSimpleData LEFT_STICK_CCW = new ImageZoneSimpleData(0,127+127, 127, 127+127+127);
    public final static ImageZoneSimpleData RIGHT_STICK_CCW = new ImageZoneSimpleData(127,127+127, 127+127, 127+127+127);

    //bullets
    public final static ImageZoneSimpleData NO_BULLET = new ImageZoneSimpleData(127,127+127+127, 127+127, 127+127+127+127);
    public final static ImageZoneSimpleData BULLET = new ImageZoneSimpleData(0,127+127+127, 127, 127+127+127+127);



    public final static ImageZoneSimpleData BLACK_CIRCLE = new ImageZoneSimpleData(0,255, 382, 127);
    public final static ImageZoneSimpleData BLACK_RECT = new ImageZoneSimpleData(300,40,350,90);




}
