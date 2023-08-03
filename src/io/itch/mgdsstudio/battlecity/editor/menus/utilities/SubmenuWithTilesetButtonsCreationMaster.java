package io.itch.mgdsstudio.battlecity.editor.menus.utilities;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneFullData;
import io.itch.mgdsstudio.engine.libs.imagezones.SingleImageZoneFromFileLoader;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class SubmenuWithTilesetButtonsCreationMaster {
    private final static String FILE_NAME  = "Last_used_tilesets.txt";
    private IEngine engine;
    private String path;
    private String [] lastAppliedAsStrings;

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

    public ArrayList <ImageZoneFullData> sortObjectsInAccordingToLastUsed(ArrayList <ImageZoneFullData> unsortedData){
        lastAppliedAsStrings = engine.getEngine().loadStrings(path);
        int [] lastAppliedAsInt = new int[lastAppliedAsStrings.length];
        for (int i = 0; i < lastAppliedAsInt.length; i++) {
            if (lastAppliedAsStrings[i].length()>0) lastAppliedAsInt[i] = Integer.parseInt(lastAppliedAsStrings[i]);
            else Logger.error("Remove free string from file " + path);
        }
        //ArrayList<String> lastUsed = whenReadWithBufferedReader_thenCorrect(path);
        Logger.debug("Start to sort: " + lastAppliedAsStrings.length + " last used");
        for (int i = (lastAppliedAsStrings.length-1); i>= 0; i--){
            for (int j = 0; j < unsortedData.size(); j++){
                if (unsortedData.get(j).getName().equals(lastAppliedAsStrings[i])){
                    int number =  getIndextOf(unsortedData, lastAppliedAsStrings[i]);
                    ImageZoneFullData existing = unsortedData.get(number);
                    unsortedData.remove(existing);
                    unsortedData.add(0,existing);
                    Logger.debug("Pos changed");
                    break;
                }
                else Logger.debug("Name " + unsortedData.get(j).getName() + " is not " + lastAppliedAsStrings[i]);
            }
        }
        return unsortedData;
    }

    private int getIndextOf(ArrayList <ImageZoneFullData> unsortedData, String key){
        for (int j = 0; j < unsortedData.size(); j++){
            if (unsortedData.get(j).getName().equals(key)) return j;
        }
        return -1;
    }

    public void resortObjectsInFile(String lastUsed){
        File file = new File(path);
        if (file.exists()){
            ArrayList <String> content = whenReadWithBufferedReader_thenCorrect(path);
            if (content.contains(lastUsed)) content.remove(lastUsed);
            content.add(0,lastUsed);
            lastAppliedAsStrings = new String[content.size()];
            for (int i = 0; i < content.size(); i++){
                lastAppliedAsStrings[i] = content.get(i);
            }
        }
        else {
            lastAppliedAsStrings = new String[1];
            lastAppliedAsStrings[0] = lastUsed;
            Logger.debug("File "+ path + " is not in folder");
        }
        save(path);
    }

    protected ArrayList<String> whenReadWithBufferedReader_thenCorrect(String path) {
        try {
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
            for (String dataString : lastAppliedAsStrings){
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

    public boolean addInfoAndSave(int lastUsed){
        ArrayList <String> existing = getExisting();
        existing.add(0, ""+lastUsed);
        for (String  st: existing){
            Logger.debug("Exist: " + st);
        }

        ArrayList <String> withoutRepeating = deleteRepeating(existing);
        PrintWriter output = null;
        boolean saved;
        try {
            output = new PrintWriter((new FileWriter(path, false)));
            for (int i = 0; i < withoutRepeating.size(); i++) output.println(withoutRepeating.get(i));
            //for (int i = (withoutRepeating.size()-1); i >= 0; i--) output.println(lastUsed);
            output.flush();
            output.close();
            output = null;
            Logger.debug("Data successfully saved " + withoutRepeating.size() + " strings must be in file " + withoutRepeating);
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

    private ArrayList <String> deleteRepeating(ArrayList<String> existing) {
        ArrayList <String > rest = new ArrayList<>();
        for (int i = 0; i < existing.size(); i++){
            boolean alreadyExists = false;
            for (String inRest : rest){
                if (existing.get(i).equals(inRest)){
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) rest.add(existing.get(i));
        }
        Logger.debug((existing.size()-rest.size()) + " were deleted");
        return rest;
    }

    private ArrayList <String> getExisting(){
        ArrayList <String> existing = new ArrayList<>();
        String [] content = engine.getEngine().loadStrings(path);
        for (int i = 0; i < content.length; i++) existing.add(content[i]);
        /*
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String currentLine = new String();
            while (currentLine != null) {
                if (currentLine.length() > 1) existing.add(currentLine);
                try {
                    currentLine = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            reader.close();
            return existing;
        } catch (IOException e) {
            Logger.error("Can not load data; " + e);
            return null;
        }
        */
        return existing;
    }
}
