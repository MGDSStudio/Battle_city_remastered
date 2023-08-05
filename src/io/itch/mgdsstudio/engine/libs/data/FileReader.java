package io.itch.mgdsstudio.engine.libs.data;

import io.itch.mgdsstudio.battlecity.game.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileReader {

    public ArrayList<String > getFileContent(String path){
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new java.io.FileReader(path));
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
