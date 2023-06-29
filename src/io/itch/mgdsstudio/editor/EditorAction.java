package io.itch.mgdsstudio.editor;

import processing.data.FloatList;
import processing.data.IntList;

public class EditorAction {
    private EditorCommandPrefix prefix;
    private IntList intParameters;
    private FloatList floatParameters;


    public EditorAction(EditorCommandPrefix prefix, IntList intParameters) {
        this.prefix = prefix;
        this.intParameters = intParameters;
    }

    public EditorAction(EditorCommandPrefix prefix) {
        this.prefix = prefix;
    }

    public void addParameter(int param){
        if (intParameters == null) intParameters = new IntList();
        intParameters.append(param);
    }

    public void addParameter(float param){
        if (intParameters == null) floatParameters = new FloatList();
        floatParameters.append(param);
    }

    public EditorCommandPrefix getPrefix() {
        return prefix;
    }

    public IntList getIntParameters() {
        return intParameters;
    }

    public FloatList getFloatParameters() {
        return floatParameters;
    }
}
