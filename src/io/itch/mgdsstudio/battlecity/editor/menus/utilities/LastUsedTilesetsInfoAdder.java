package io.itch.mgdsstudio.battlecity.editor.menus.utilities;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LastUsedTilesetsInfoAdder {

    private String path;

    public LastUsedTilesetsInfoAdder(IEngine engine, String fileName) {
        this.path = engine.getPathToObjectInUserFolder(fileName);
    }

    public boolean addInfoAndSave(int lastUsed){
        PrintWriter output = null;
        boolean saved;
        try {
            output = new PrintWriter((new FileWriter(path, true)));
            output.println(lastUsed);
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
