package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.StringLibrary;
import io.itch.mgdsstudio.engine.libs.data.StringFromFileDeleteMaster;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EntityFromDataFileDeleteMaster {
    private int levelNumber;
    private IEngine engine;

    public EntityFromDataFileDeleteMaster(int levelNumber, IEngine engine) {
        this.levelNumber = levelNumber;
        this.engine = engine;
    }

    public int deleteEntity(String dataString){
        int deleted = 0;
        String levelFileName = ExternalDataController.getLevelFileName(levelNumber);
        StringFromFileDeleteMaster deleteMaster = new StringFromFileDeleteMaster(engine);
        int count = deleteMaster.removeFromFile(levelFileName, dataString);
        if (count == 0){
            int countWithAnotherIds = deleteAllStringsWithSameDataButAnothersIds(levelFileName, dataString);
        }
        return deleted;
    }

    private int deleteAllStringsWithSameDataButAnothersIds(String levelFileName, String dataString) {
        int count = 0;
        Logger.correct("TEST REMOVER!");
        DataStringManagingLibrary dataStringManagingLibrary = new DataStringManagingLibrary(dataString);
        String before = dataStringManagingLibrary.getDataBeforeId();
        String after = dataStringManagingLibrary.getDataAfterId();
        String path = engine.getPathToObjectInUserFolder(ExternalDataController.getLevelFileName(levelNumber));
        ArrayList <String> content = getList(path);
        for (int i = (content.size()-1); i>= 0; i--){
            String existingString = content.get(i);
            DataStringManagingLibrary existingManagin = new DataStringManagingLibrary(existingString);
            String beforeEx = existingManagin.getDataBeforeId();
            String afterEx = existingManagin.getDataAfterId();
            if (beforeEx.equals(before) && afterEx.equals(after)){
                content.remove(i);
                count++;
            }
        }
        save(content, path);

        return  count;
    }

    private void save(ArrayList<String> content, String path) {
        StringLibrary.save(content, path);
    }

    protected ArrayList<String> getList(String path) {
        try {
            Logger.debug("sketch path: " + path);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ArrayList<String> data = new ArrayList<>();
            String currentLine = new String();
            while (currentLine != null) {
                if (currentLine.length() > 1) data.add(currentLine);
                try {
                    currentLine = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            reader.close();
            return data;
        } catch (IOException e) {
            Logger.error("Can not load data; " + e);
            return null;
        }

    }
}
