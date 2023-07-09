package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.DataFromMgdsTextParametersListReader;
import processing.data.JSONArray;
import sun.rmi.runtime.Log;

import static javax.swing.UIManager.getString;

public class PlayerProgressControllerSingleton extends DataFromMgdsTextParametersListReader {

    private static PlayerProgressControllerSingleton playerProgressController;


    public final static boolean CAMPAIGN = true;
    public final static boolean SINGLE_MISSIONS = false;
    private final static String CAMPAIGN_PROGRESS_FILE_NAME = "CampaignProgress.txt";
    private final static String SINGLE_MISSIONS_PROGRESS_FILE_NAME = "SingleMissionsProgress.txt";


    private PlayerProgressControllerSingleton(IEngine engine, boolean levelType) {
        String path = engine.getPathToObjectInAssets(CAMPAIGN_PROGRESS_FILE_NAME);
        if (levelType == SINGLE_MISSIONS) path = engine.getPathToObjectInAssets(SINGLE_MISSIONS_PROGRESS_FILE_NAME);
        loadArray(engine, path);
        Logger.debug("This must be got from the readable folder on android");
    }

    public static void dispose(){
        playerProgressController = null;
        Logger.debug("Player progress controller were disposed at " + System.currentTimeMillis());
    }

    public static PlayerProgressControllerSingleton getInstance(IEngine engine, boolean levelType){
        if (playerProgressController == null) playerProgressController = new PlayerProgressControllerSingleton(engine, levelType);
        return playerProgressController;
    }

    public static void init(IEngine engine, boolean levelType){
        if (playerProgressController == null) playerProgressController = new PlayerProgressControllerSingleton(engine, levelType);
    }

    public static PlayerProgressControllerSingleton getInstance(){
        if (playerProgressController == null) Logger.error("Can not get instance. It was not initialized. Call init(IEngine engine, bool levelType) before get instance");
        return playerProgressController;
    }




}
