package io.itch.mgdsstudio.battlecity.mainpackage;

import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;

import java.util.ArrayList;

public interface IEngine {

    PApplet getProcessing();
    boolean isCircleAreaPressed(Coordinate pos, int diameter);

    String getPathToObjectInAssets(String relativePath);

    void fillTouchesArray(ArrayList<Coordinate> touchScreenPos);


    default boolean isRectAreaPressed(Coordinate center, int width, int height) {
        if (getProcessing().mousePressed){
            if (getProcessing().mouseX> (center.x-width/2)){
                if (getProcessing().mouseX< (center.x+width/2)){
                    if (getProcessing().mouseY> (center.y-height/2)){
                        if (getProcessing().mouseY< (center.y+height/2)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    String getPathToSpriteInAssets(int spritesheetNumber);

    String getPathToObjectInUserFolder(String fileName);

}
