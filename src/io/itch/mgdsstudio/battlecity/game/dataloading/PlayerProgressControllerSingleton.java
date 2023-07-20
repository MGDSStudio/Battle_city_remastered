package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.DataFromMgdsTextParametersListReader;

import static javax.swing.UIManager.getString;

public class PlayerProgressControllerSingleton extends DataFromMgdsTextParametersListReader {

    private static PlayerProgressControllerSingleton playerProgressController;


    public final static boolean CAMPAIGN = true;
    public final static boolean SINGLE_MISSIONS = false;
    private final static String CAMPAIGN_PROGRESS_FILE_NAME = "CampaignProgress.txt";
    private final static String SINGLE_MISSIONS_PROGRESS_FILE_NAME = "SingleMissionsProgress.txt";


    private PlayerProgressControllerSingleton(IEngine engine, boolean levelType) {
        this.engine = engine;

        String path = engine.getPathToObjectInAssets(CAMPAIGN_PROGRESS_FILE_NAME);
        if (levelType == SINGLE_MISSIONS) path = engine.getPathToObjectInAssets(SINGLE_MISSIONS_PROGRESS_FILE_NAME);

        this.filePath = path;
        updateData();
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


    @Override
    public void saveOnDisk(){
        String path = filePath;
        Logger.debug("Try to save data on disk at " + path);
        engine.getEngine().saveStrings(path, data);
        Logger.debug("Data saved on disk ");
    }

}
