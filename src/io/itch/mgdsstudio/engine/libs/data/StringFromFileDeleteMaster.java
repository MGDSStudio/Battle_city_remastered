package io.itch.mgdsstudio.engine.libs.data;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.StringLibrary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StringFromFileDeleteMaster {
    private final IEngine engine;
    private ArrayList <String> restString;

    public StringFromFileDeleteMaster(IEngine engine){
        this.engine = engine;
    }


    public int removeFromFile(String fileName, String toBeDeleted) {
        String path = engine.getPathToObjectInUserFolder(fileName);
        ArrayList <String> existing = getList(path);
        int removed = 0;
        for (int i  = (existing.size()-1); i >= 0; i--){
            if (existing.get(i).equals(toBeDeleted)){
                existing.remove(toBeDeleted);
                removed++;
            }
        }
        if (removed > 0){
            save(existing, path);
        }
        return removed;
    }

    private void save(ArrayList <String> existing, String path) {
        StringLibrary.save(existing, path);
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
