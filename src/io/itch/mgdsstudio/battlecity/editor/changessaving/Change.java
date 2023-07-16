package io.itch.mgdsstudio.battlecity.editor.changessaving;

import io.itch.mgdsstudio.battlecity.game.Logger;

import java.util.ArrayList;

public class Change {
    private ArrayList <String> strings;
    private ChangingType type;

    public Change(String newString, ChangingType type) {
        this.strings = new ArrayList<>();
        strings.add(newString);
        this.type = type;
    }

    public Change( String newString, String oldString, ChangingType type) {
        this.strings = new ArrayList<>();
        strings.add(newString);
        strings.add(oldString);
        this.type = type;
    }

    public ChangingType getType() {
        return type;
    }

    public String getNewString(){
        return strings.get(0);
    }


    public String getOldString(){
        if (strings.size()>1){
            return strings.get(1);
        }
        else {
            Logger.error("No data for this type " + type.name() + " for old string");
            return  null;
        }
    }
}
