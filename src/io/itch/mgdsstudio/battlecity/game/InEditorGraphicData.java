package io.itch.mgdsstudio.battlecity.game;

import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PApplet;
import processing.core.PConstants;

public class InEditorGraphicData {
    public static float graphicScaleX, graphicScaleY;
    public static float fullGraphicWidth, fullGraphicHeight;
    public static int RESOLUTION_X;
    public static int RESOLUTION_Y;
    public static float lowerHeight;
    public static float upperHeight;
    public final static float SINGLE_TEXT_LINE_RELATIVE_WIDTH = 0.1f;
    public static float singleTextLineHeight;
    public static float leftBoardLineWidth;
    public static float rightBoardLineWidth;
    public static String renderer;

    public static int theoreticalWidthOfFramesWithNoZomming;

    public static int graphicCenterX, graphicCenterY;

    private InEditorGraphicData(IEngine eng){
        singleTextLineHeight = SINGLE_TEXT_LINE_RELATIVE_WIDTH*eng.getEngine().width;
        leftBoardLineWidth = singleTextLineHeight;
        rightBoardLineWidth = singleTextLineHeight *0.5f;
        lowerHeight = eng.getEngine().height*0.35f;
        upperHeight = leftBoardLineWidth;
        theoreticalWidthOfFramesWithNoZomming= (int) (0.8f*(eng.getEngine().width * GuiElement.NES_SCREEN_X_RESOLUTION)/eng.getEngine().width);
    }


    public static void init(IEngine eng){
        PApplet engine = eng.getEngine();
        InEditorGraphicData instanceForInit = new InEditorGraphicData(eng);
        calculateResolution(engine, upperHeight, lowerHeight);
        graphicScaleX = engine.width/(float)RESOLUTION_X;
        graphicScaleY = engine.height/(float)RESOLUTION_Y;
        graphicCenterX = (int) ((engine.width-leftBoardLineWidth-rightBoardLineWidth)/2+leftBoardLineWidth);
        graphicCenterY = (int) (upperHeight+(engine.height-upperHeight-lowerHeight)/2+0);
        if (GlobalVariables.getOs() == GlobalConstants.LINUX){
            renderer = PConstants.JAVA2D;
        }
        else renderer = PConstants.P2D;
    }

    private static void calculateResolution(PApplet engine, float upperPanelHeight, float lowerPanelHeight){
        RESOLUTION_X = 512;
        //RESOLUTION_X = 256+128;
        float restHeight = engine.height-(upperPanelHeight+lowerPanelHeight);
        float relationship = restHeight/(float)engine.width;
        RESOLUTION_Y = (int) (relationship*RESOLUTION_X);
        fullGraphicWidth = engine.width-leftBoardLineWidth-rightBoardLineWidth;
        fullGraphicHeight = fullGraphicWidth*relationship;
    }
}
