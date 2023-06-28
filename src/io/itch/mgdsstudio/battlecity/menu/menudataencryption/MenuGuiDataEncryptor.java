package io.itch.mgdsstudio.battlecity.menu.menudataencryption;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.IntList;

import java.util.HashMap;

public class MenuGuiDataEncryptor {
    /*Data on the upper panel left
        ARMOR
        TANKS
        XP
        MONEY

        right:
        MOTOR:
        WEAPON:
        AMMO:
        TIME:
        achievements

        */


    private final static int BLACK_COLOR = 0; //MUST be known
    private HashMap<Integer, RectGuiZone> zones = new HashMap<>();
    private IntList colors;
    private PApplet engine;

    public MenuGuiDataEncryptor(PApplet engine, PImage image)
    {
        this.engine = engine;
        initColors(image);
        initZones(image);
    }

    public RectGuiZone getZone(int r, int g, int b){
        int key = engine.color(r,g,b);
        if (zones.containsKey(key)){
            RectGuiZone zone = zones.get(key);
            //Logger.debug("Zone for color: " + r + ',' + g + ',' + b + " is at " + (zone.getLeft()+zone.getW()/2) + "x" + (zone.getUp()+zone.getH()/2) + " sizes: " + zone.getW() + "x" + zone.getH());
            return zone;
        }
        else {
            System.out.println("Can not find color " + r +","+ g + "," + b);
            return null;
        }
    }



    private void initColors(PImage image){
        image.loadPixels();
        int []pixels = image.pixels;
        int backgroundColor = pixels[0];
        colors = new IntList();

        for (int i = 1; i < pixels.length; i++){
            if (pixels[i] != backgroundColor){
                if (!colors.hasValue(pixels[i])){
                    colors.append(pixels[i]);
                }
            }
        }
        System.out.println("This menu file has :" + colors.size() + " unique zones. Background: " + (int)engine.red(backgroundColor) + "x" + (int)engine.green(backgroundColor) + "x" + (int)engine.blue(backgroundColor) + "; Sizes: " + image.width + "x" + image.height);
        /*Logger.debug("Colors:");
        for (int i = 0; i < colors.size(); i++){
            System.out.println((int)engine.red(colors.get(i)) + "x" + (int)engine.green(colors.get(i)) + "x" + (int)engine.blue(colors.get(i)));
        }*/
    }

    private void initZones(PImage image){
        ZonesFinder zonesFinder = new ZonesFinder(image, engine.width, engine.height);
        int r,g,b;
        for (int i = 0; i < colors.size(); i++){
            r = (int)engine.red(colors.get(i));
            g = (int)engine.green(colors.get(i));
            b = (int)engine.blue(colors.get(i));
            RectGuiZone zone = zonesFinder.getRectZoneForColor(r,g,b);
            zones.put(colors.get(i), zone);
        }
    }


}
