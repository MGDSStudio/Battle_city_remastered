package io.itch.mgdsstudio.battlecity.mainpackage;

import processing.core.PApplet;
import processing.core.PConstants;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

public abstract class GlobalVariables {
    public final static boolean debug = true;

    //public static int difficulty;
    public static int language = Languages.ENGLISH;
    public static float sidesRelationshipHeightToWidth;
    private static int os = 0;
    private static boolean desktop;
    private static boolean renderer;
    private static boolean twoDim;
    public final static float yDirectionCoefficient = 1;    //if Y-Axis shows down - 1 (Processing) if up - -1 (libGDX)

    private static int screenCenterX, screenCenterY;
    private final static Random randomGenerator = new Random();

    public static void init(IEngine engine){
        initOsConstants(engine.getEngine());
        initRenderer(engine);
        sidesRelationshipHeightToWidth = (float)engine.getEngine().height/(float)engine.getEngine().width;
        screenCenterX = engine.getEngine().width/2;
        screenCenterY = engine.getEngine().height/2;
    }

    public static void init(PApplet engine){
        initOsConstants(engine);
        initRenderer(engine);
        sidesRelationshipHeightToWidth = (float)engine.height/(float)engine.width;
        screenCenterX = engine.width/2;
        screenCenterY = engine.height/2;
    }

    private static void initRenderer(IEngine engine) {
        String rendererString = engine.getEngine().sketchRenderer();
        if (rendererString.contains("open")) renderer = GlobalConstants.OPENGL_RENDERER;
        else renderer = GlobalConstants.JAVA_RENDERER;
        if (rendererString.contains("3D") || rendererString.contains("3d")) twoDim = false;
        else twoDim = true;
        System.out.println("Renderer OPEN GL: " + renderer + "; String: " + rendererString + " two dims: " + twoDim);
    }

    private static void initRenderer(PApplet engine) {
        String rendererString = engine.sketchRenderer();
        if (rendererString.contains("open")) renderer = GlobalConstants.OPENGL_RENDERER;
        else renderer = GlobalConstants.JAVA_RENDERER;
        if (rendererString.contains("3D") || rendererString.contains("3d")) twoDim = false;
        else twoDim = true;
        System.out.println("Renderer OPEN GL: " + renderer + "; String: " + rendererString + " two dims: " + twoDim);
    }

    public static int getScreenCenterX() {
        return screenCenterX;
    }

    public static int getScreenCenterY() {
        //return screenCenterX;
        return screenCenterY;
    }

    private static void initOsConstants(PApplet engine) {
        String OS = System.getProperty("os.name");
        Properties p = System.getProperties();
        Enumeration keys = p.keys();
        boolean isAndroid = false;
        while(keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) p.get(key);
            if (value.contains("Dalvik") || value.contains("dalvik")){
                isAndroid = true;
            }
        }
        if (OS.contains("indows")) {
            os = GlobalConstants.WINDOWS;
            if (debug) System.out.println("This is Windows");
            desktop = true;
        } else {
            if (isAndroid) {
                os = GlobalConstants.ANDROID;
                if (debug) System.out.println("This is Android");
                desktop = false;
            }
            else {
                os = GlobalConstants.LINUX;
                if (debug) System.out.println("This is Linux");
                desktop = true;
            }
        }
    }

    public static int getOs() {
        return os;
    }

    public static boolean isDesktop() {
        return desktop;
    }

    public static boolean getRenderer() {
        return renderer;
    }

    public static boolean is3D(){
        return !twoDim;
    }

    public static String getRenererAsString() {
        if (renderer == GlobalConstants.JAVA_RENDERER) return PConstants.JAVA2D;
        else return PConstants.P2D;
    }

    public static int getRandomIntValue(int min, int max) {
        int value = randomGenerator.nextInt(max-min) + min;
        return value;
    }
}
