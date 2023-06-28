package io.itch.mgdsstudio.battlecity.menu.menus.loading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.ExternalDataController;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.io.File;

public class SingleMissionsLoadingMaster extends LoadingMaster
{
    public SingleMissionsLoadingMaster(IEngine engine, String dir) {
        super(engine, ExternalDataController.LEVEL_PREFIX, ExternalDataController.LEVEL_EXTENSION, dir);
        //init(engine);
    }

    @Override
    protected void init(IEngine engine){
        Logger.correct("It must collect files in the for under android");
            File directory = new File(dir);
            if (directory.isDirectory()){
                File[] content = directory.listFiles();
                if (content.length < 1){
                     Logger.error("The directory " + directory.getAbsolutePath() + " is clear");
            }
            else {
                    fillArrayWithLevelPathes(content);
            }
        }
        else Logger.error("The " + dir + "is not a folder name");
    }

    private void fillArrayWithLevelPathes(File [] content){
        for (int I = 0; I < content.length; I++){
                String name = content[I].getName();
            if (name.contains(filePrefix) && name.contains(fileExtension)){
                     files.add(content[I]);
            }
        }
}
	

}
