package io.itch.mgdsstudio.battlecity.editor.changessaving;

import java.util.ArrayList;

public class ChangesController {
    ArrayList <Change> changes = new ArrayList<>();

    public ChangesController() {
    }

    public boolean areThereUnsavedData() {
        if (changes.size() > 0){
            return true;
        }
        else return false;
    }
}
