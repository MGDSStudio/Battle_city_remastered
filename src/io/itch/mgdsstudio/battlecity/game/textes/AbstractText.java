package io.itch.mgdsstudio.battlecity.game.textes;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public abstract class AbstractText extends Entity {

    //protected float actualX, actualY;
    protected String text;
    protected int color;
    public final static int BLACK = -16777216;
    //public final static int WHITE = -1;
    protected int fontHeight;
    public final static int WHITE = -1;
    public final static int GRAY = 180;
    protected Timer timer;
    protected static PFont font;
    protected IEngine engine;
    protected static boolean fontUploaded;



    protected AbstractText(IEngine engine, Coordinate pos) {
        super(engine, pos, 0, IMMORTAL_LIFE, 1, 1);
        this.engine = engine;
    }

    protected void createFont() {
        if (!fontUploaded) {
            if (GlobalVariables.getOs() == GlobalConstants.ANDROID) font = engine.getEngine().loadFont(engine.getPathToObjectInAssets((GlobalConstants.SECONDARY_FONT)));
            else font = engine.getEngine().loadFont(engine.getPathToObjectInAssets((GlobalConstants.SECONDARY_FONT)));
            fontUploaded = true;
            System.out.println("Font created new from panel font");
        }
    }



   // public abstract void draw(PGraphics graphics, GameCamera gameCamera);

    public abstract boolean canBeDeleted();

    public void setColor(int color) {
        this.color = color;
    }

}
