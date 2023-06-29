package io.itch.mgdsstudio.editor;

import processing.data.IntList;

public class EditorAction {
    private EditorCommandPrefix prefix;
    private IntList parameters;

    public EditorAction(EditorCommandPrefix prefix, IntList parameters) {
        this.prefix = prefix;
        this.parameters = parameters;
    }

    public EditorAction(EditorCommandPrefix prefix) {


        this.prefix = prefix;
    }

    public void addParameter(int param){
        if (parameters == null) parameters = new IntList();
        parameters.append(param);
    }

    public EditorCommandPrefix getPrefix() {
        return prefix;
    }
}
