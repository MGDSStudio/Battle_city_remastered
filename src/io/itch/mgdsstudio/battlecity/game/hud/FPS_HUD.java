package io.itch.mgdsstudio.battlecity.game.hud;

import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;

public class FPS_HUD {
    private boolean graphicRenderer;
	private final IEngine engine;

    private int x,y;
    private String fps;
    private String fpsSuffix;
    private boolean threeDim;
    private int updatingFreq;
    private long memory;
    private boolean singleplayer;
	

    public FPS_HUD(IEngine engine, boolean graphicRenderer, boolean threeDim, float x, float y, int updatingFreq, boolean server, boolean singleplayer){
        //String builder must be used
        this.engine  = engine;
        this.x = (int) x;
        this.y = (int)y;
        this.threeDim = threeDim;
	    this.graphicRenderer = graphicRenderer;
        this.updatingFreq = updatingFreq;
        this.singleplayer = singleplayer;
        initString(server);
    }

    private void initString(boolean server) {
        fps = "";
        fpsSuffix = "";
        if (graphicRenderer == GlobalConstants.OPENGL_RENDERER) {
            fpsSuffix += " OPEN GL";
            if (threeDim) fpsSuffix += " 3D";
            else fpsSuffix += " 2D";
        } else fpsSuffix += " JAVA";
        fpsSuffix+='\n';
        if (singleplayer) fpsSuffix+="SINGLEPLAYER";
        else {
            if (server) {
                fpsSuffix += "SERVER";
            } else fpsSuffix += "CLIENT";
        }

    }
    public void showFrameRateWithRenderer(PGraphics graphics) {
        if (GlobalVariables.debug) {
            graphics.pushStyle();
            //graphics.textFont
            graphics.fill(engine.getEngine().color(0,255,9));
            graphics.textSize(engine.getEngine().height / 75);
            if (engine.getEngine().frameCount%10 == 0) memory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
            if (engine.getEngine().frameCount%updatingFreq==0) {
                fps = "FPS: " + (byte) engine.getEngine().frameRate;
                fps+=fpsSuffix;
                fps+='\n';
                fps+="RAM: "+memory;
            }
            try {
                graphics.text(fps, x,y);
                //engine.getEngine().text(FPS, 9 * engine.getEngine().width / 16, UpperPanel.HEIGHT + (11.5f * engine.getEngine().height / 13));
            } catch (Exception e) {
                System.out.println("Can not draw frame rate text;");
                e.printStackTrace();
            }
            graphics.popStyle();
        }
    }
}
