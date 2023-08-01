package io.itch.mgdsstudio.battlecity.editor.menus;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.SingleImageZoneFromFileLoader;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class SubmenuWithTilesetButtonsCreationMaster {
    private final static String FILE_NAME  = "Last_used_tilesets.txt";
    private IEngine engine;
    private String path;
    private String [] lastApplied;

    public SubmenuWithTilesetButtonsCreationMaster(IEngine engine) {
        this.engine = engine;
        init();
    }

    private void init(){
        path = engine.getPathToObjectInUserFolder(FILE_NAME);
    }



    public ArrayList <String> getAvaliableTilesets(){
        ArrayList <String> avaliableTilesets = new ArrayList<>();
        String pathToImageZoneFile = SingleImageZoneFromFileLoader.FILE_NAME_FOR_GRAPHIC_ZONES_FILE;
        JSONArray jsonArray = engine.getEngine().loadJSONArray(path);
        if (jsonArray!=null){
            Logger.debug("JSON file contains " + jsonArray.size() + " pos");
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString(SingleImageZoneFromFileLoader.NAME);
                avaliableTilesets.add(name);
            }
        }
        return avaliableTilesets;
    }

    public void resortObjectsInFile(String lastUsed){
        File file = new File(path);
        if (file.exists()){
            ArrayList <String> content = whenReadWithBufferedReader_thenCorrect(path);
            if (content.contains(lastUsed)) content.remove(lastUsed);
            content.add(0,lastUsed);
            lastApplied = new String[content.size()];
            for (int i = 0; i < content.size(); i++){
                lastApplied[i] = content.get(i);
            }
        }
        else {
            lastApplied = new String[1];
            lastApplied[0] = lastUsed;
            Logger.debug("File "+ path + " is not in folder");
        }
        save(path);
    }

    protected ArrayList<String> whenReadWithBufferedReader_thenCorrect(String path) {
        try {
            //Logger.debug("sketch path: " + path);
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

    private boolean save(String path){
        PrintWriter output = null;
        boolean saved;
        try {
            output = new PrintWriter((new FileWriter(path, true)));
            for (String dataString : lastApplied){
                output.println(dataString);
            }
            output.flush();
            output.close();
            output = null;
            Logger.debug("Data successfully saved");
            saved = true;
        }
        catch (IOException e){
            System.out.println("System is busy. Can not save");
            saved = false;
        }
        finally {
            if (output != null ) output.close();
        }

        return saved;
    }
}
