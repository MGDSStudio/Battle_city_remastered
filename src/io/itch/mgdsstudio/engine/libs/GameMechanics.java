package io.itch.mgdsstudio.engine.libs;


import processing.core.PApplet;
import processing.core.PVector;

public abstract class GameMechanics{
    private final static PVector mutVector1 = new PVector();

public static boolean isIntersectionBetweenAllignedRects(float majorRectCenterX, float majorRectCenterY, float smallRectCenterX, float smallRectCenterY, float majorWidth, float majorHeight, float smallRectWidth, float smallRectHeight){
    float ax = majorRectCenterX-majorWidth/2f;
    float ay = majorRectCenterY-majorHeight/2f;
    float ax1 = majorRectCenterX+majorWidth/2f;
    float ay1 = majorRectCenterY+majorHeight/2f;

    float bx = smallRectCenterX-smallRectWidth/2f;
    float by = smallRectCenterY-smallRectHeight/2f;
    float bx1 = smallRectCenterX+smallRectWidth/2f;
    float by1 = smallRectCenterY+smallRectHeight/2f;
    return(
            (
                    (
                            ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                    ) && (
                            ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                    )
            )||(
                    (
                            ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                    ) && (
                            ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                    )
            )
    )||(
            (
                    (
                            ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                    ) && (
                            ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                    )
            )||(
                    (
                            ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                    ) && (
                            ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                    )
            )
    );
}


    public static float getAngleToPointInRadians(float x, float y, float x1, float y1) {
        float deltaX = x1-x;
        float deltaY = y1-y;
        mutVector1.x = deltaX;
        mutVector1.y = deltaY;
        return mutVector1.heading();
    }

    public static float getAngleToPointInDegrees(float x, float y, float x1, float y1) {
        float angle = PApplet.degrees(getAngleToPointInRadians(x, y, x1, y1));
        if (angle<0) angle+=360;
        return      angle;
    }
}

