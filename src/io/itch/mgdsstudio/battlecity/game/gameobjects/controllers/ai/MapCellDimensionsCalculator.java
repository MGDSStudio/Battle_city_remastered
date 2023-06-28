package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ai;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.SolidObject;

public class MapCellDimensionsCalculator {
    private int cellWidth, cellHeight;
    private GameRound gr;

    private int calculateCellDims(){
        int smallestDim;
        int dimsSum=0;
        int count=0;
        for (Entity gameObject : gr.getEntities()){
            if (gameObject instanceof SolidObject){
                smallestDim = gameObject.getWidth();
                if (smallestDim > gameObject.getHeight()){
                    smallestDim = gameObject.getHeight();
                }
                dimsSum+=smallestDim;
                count++;
            }
        }
        smallestDim = dimsSum/count;
        Logger.correct("Mitsize of a single Cell is: "+ smallestDim);
        return smallestDim;
    }

}
