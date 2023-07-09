package io.itch.mgdsstudio.battlecity.mainpackage;

import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;

public interface IEngine {

    PApplet getEngine();
    boolean isCircleAreaPressed(Coordinate pos, int diameter);

    String getPathToObjectInAssets(String relativePath);

    void fillTouchesArray(ArrayList<Coordinate> touchScreenPos);


    default boolean isRectAreaPressed(Coordinate center, int width, int height) {
        if (getEngine().mousePressed){
            if (getEngine().mouseX> (center.x-width/2)){
                if (getEngine().mouseX< (center.x+width/2)){
                    if (getEngine().mouseY> (center.y-height/2)){
                        if (getEngine().mouseY< (center.y+height/2)){
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
