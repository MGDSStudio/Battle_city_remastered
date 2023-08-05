package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

public abstract class ExternalDataController {
    public final static String LEVEL_PREFIX = "Level_";
    public final static String LEVEL_EXTENSION = ".txt";
    protected int level;
    protected IEngine engine;

    protected String getPathToLevel(){
        String levelName = LEVEL_PREFIX +level+LEVEL_EXTENSION;
        Logger.debug(" Try to load level: " + levelName);
        return engine.getPathToObjectInAssets(levelName);

    }

    public static String getLevelFileName(int number){
        return LEVEL_PREFIX+number+LEVEL_EXTENSION;
    }
}
