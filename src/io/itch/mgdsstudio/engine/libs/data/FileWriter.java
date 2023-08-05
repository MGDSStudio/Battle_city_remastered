package io.itch.mgdsstudio.engine.libs.data;

import io.itch.mgdsstudio.battlecity.game.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWriter {

    public boolean writeDataInFile(String path, ArrayList<String> data){
        PrintWriter output = null;
        boolean saved;
        try {
            output = new PrintWriter((new java.io.FileWriter(path, false)));
            for (String string : data) output.println(string);
            output.flush();
            output.close();
            output = null;
            //Logger.debug("Data successfully saved to " + path);
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
