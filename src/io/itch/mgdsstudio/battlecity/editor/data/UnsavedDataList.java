package io.itch.mgdsstudio.battlecity.editor.data;

import io.itch.mgdsstudio.battlecity.editor.UnsavedDataLabel;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.engine.libs.StringLibrary;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UnsavedDataList {
    private ArrayList <String> data = new ArrayList<>();
    private String path;
    private UnsavedDataLabel label;
    public UnsavedDataList(String path, UnsavedDataLabel label) {
        this.path = path;
        this.label = label;
    }

    public boolean areThereUnsavedData(){
        if (data.size()>0) return true;
        else return false;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void addNewObjectString(String stringParameters) {
        data.add(stringParameters);
        label.setActive(true);
    }

    public boolean save(){
        boolean saved =  StringLibrary.save(data, path);

        if (saved){
            label.setActive(false);
        }
        return saved;
        /*PrintWriter output = null;
        boolean saved;
        try {
            output = new PrintWriter((new FileWriter(path, true)));
            for (String dataString : data){
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
        if (saved){
            label.setActive(false);
        }
        return saved;*/
    }
}
