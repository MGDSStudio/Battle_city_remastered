package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PApplet;
import processing.core.PConstants;

public class InGameGraphicData {
    public static float graphicScaleX, graphicScaleY;
    public static float fullGraphicWidth, fullGraphicHeight;
    public static int RESOLUTION_X;
    public static int RESOLUTION_Y;
    public static float lowerHeight;
    public static float upperHeight;
//public static final int RESOLUTION_X = 512, RESOLUTION_Y = RESOLUTION_X;
    public static String renderer;

    public static int graphicCenterX, graphicCenterY;

    private InGameGraphicData(IEngine eng){
        lowerHeight = eng.getProcessing().width*0.4f;
        upperHeight = eng.getProcessing().width*0.25f;
    }

    public static void init(IEngine eng){
        PApplet engine = eng.getProcessing();
        InGameGraphicData instanceForInit = new InGameGraphicData(eng);
        calculateResolution(engine, upperHeight, lowerHeight);
        graphicScaleX = engine.width/(float)RESOLUTION_X;
        graphicScaleY = engine.height/(float)RESOLUTION_Y;
        graphicCenterX = engine.width/2;
        graphicCenterY = engine.height/2;
        if (GlobalVariables.getOs() == GlobalConstants.LINUX){
            renderer = PConstants.JAVA2D;
        }
        else renderer = PConstants.P2D;
    }

    private static void calculateResolution(PApplet engine, float upperPanelHeight, float lowerPanelHeight){
        RESOLUTION_X = 256;
        //RESOLUTION_X = 256+128;
        float restHeight = engine.height-(upperPanelHeight+lowerPanelHeight);
        float relationship = restHeight/(float)engine.width;
        RESOLUTION_Y = (int) (relationship*RESOLUTION_X);
        fullGraphicWidth = engine.width;
        fullGraphicHeight = fullGraphicWidth*relationship;
    }
}
