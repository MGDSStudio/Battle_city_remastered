package io.itch.mgdsstudio.battlecity.editor.changessaving;

import io.itch.mgdsstudio.battlecity.game.EditorController;

import java.util.ArrayList;

public class ChangesController {
    private ArrayList <Change> changes = new ArrayList<>();
    private EditorController editorController;
    public ChangesController(EditorController editorController) {
        this.editorController = editorController;
    }

    public boolean areThereUnsavedData() {
        if (changes.size() > 0){
            return true;
        }
        else return false;
    }

    public void addNewUnsavedData(String dataString) {
        Change change = new Change(dataString, ChangingType.OBJECT_ADDED);
        changes.add(change);
    }
}
