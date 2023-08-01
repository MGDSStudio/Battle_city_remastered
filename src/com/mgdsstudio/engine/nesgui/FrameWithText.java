package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

abstract class FrameWithText extends GuiElement {
    protected final static boolean ALONG_X=  true;
    protected final static ImageZoneSimpleData frameImageZoneSimpleDataWithoutBackground = new ImageZoneSimpleData(0, 32, 34, 66);
    protected EightPartsFrameImage frame;
    protected String data = "no text";
    protected float distanceBetweenTextAndFrameAlongY;
    protected final static float heightRelativeCoef = 0.65f;
    protected int theoreticFontSize;
    protected FrameWithText(IEngine engine, int centerX, float centerY, int width, float textHeight, String name, PGraphics graphics) {
        super(engine, centerX, (int) centerY, width, (int) (textHeight* heightRelativeCoef), name, graphics);
        float frameHeight = textHeight * ListButton.CURSOR_DIMENSIONS_COEF/heightRelativeCoef;
        init(centerX, centerY, (int) (frameHeight));
        theoreticFontSize = (int) textHeight;
    }

    protected void drawFrame(PGraphics graphics) {
        if (actualStatement != HIDDEN) {
            if (graphics != null) {
                graphics.resetMatrix();
                frame.draw(graphics);
                //System.out.println("Frame drawn for " + this.getClass());
            }
        }
    }
    private void init(int centerX, float centerY, int frameHeight) {
        final int NES_SCREEN_X_RESOLUTION = 254;
        int basicWidth = (int) (0.2f * (engine.width * NES_SCREEN_X_RESOLUTION) / engine.width);
        int frameWidth = (width * 1);
        distanceBetweenTextAndFrameAlongY = (frameHeight-height)/2;
        frame = new EightPartsFrameImage(graphicFile, getFrameImageZoneSimpleData(), basicWidth, basicWidth, frameWidth, frameHeight, new PVector(centerX - frameWidth / 2, centerY - frameHeight / 2));
    }

    abstract ImageZoneSimpleData getFrameImageZoneSimpleData();


    @Override
    public void draw(PGraphics graphic) {
        if (actualStatement != HIDDEN) {
            if (!fontInitialized) initFont(graphic);
            drawFrame(graphic);
            drawData(graphic, RIGHT_ALIGNMENT_OS_SPECIFIC);
        }
    }

    protected abstract void drawData(PGraphics graphic, int side);

    @Override
    protected void drawName(PGraphics graphic, int xAlignment){
        graphic.pushStyle();
        graphic.textFont(font);
        graphic.textAlign(xAlignment, PConstants.CENTER);
        graphic.textSize(height);
        if (actualStatement == BLOCKED) {
            graphic.tint(colorRed, colorGreen, colorBlue);
        }
        else {
            graphic.fill(colorRed, colorGreen, colorBlue);
        }
        if (xAlignment == RIGHT_ALIGNMENT_OS_SPECIFIC) graphic.text(getTextToBeDrawn(), leftX+width-distanceBetweenTextAndFrameAlongY, y+textYShifting);
        else if (xAlignment == LEFT_ALIGNMENT_OS_SPECIFIC) graphic.text(getTextToBeDrawn(), leftX+distanceBetweenTextAndFrameAlongY, y+textYShifting);
        else graphic.text(getTextToBeDrawn(), leftX+width/2, y+textYShifting);
        graphic.popStyle();
    }

    protected void translateGui(float dist, boolean axis){
        if (axis == ALONG_X) {
            frame.setLeftUpperCorner((frame.getLeftUpperCorner().x + dist), (frame.getLeftUpperCorner().y));
            setCenterX(x + dist);
        }
        else {
            //System.out.println("Gui will be translated to: " + (frame.getLeftUpperCorner().y+dist) + " with center at: " + y+dist) ;
            frame.setLeftUpperCorner( frame.getLeftUpperCorner().x, (frame.getLeftUpperCorner().y+dist));
            setCenterY(y + dist);
        }
    }
}
