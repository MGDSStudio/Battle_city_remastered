package io.itch.mgdsstudio.battlecity.editor;

import processing.data.FloatList;
import processing.data.IntList;

public class EditorAction {
    private EditorCommandPrefix prefix;
    private IntList intParameters;
    private FloatList floatParameters;
    private String stringParameters;

    public EditorAction(EditorCommandPrefix prefix, IntList intParameters) {
        this.prefix = prefix;
        this.intParameters = intParameters;
    }

    public EditorAction(EditorCommandPrefix prefix) {
        this.prefix = prefix;
    }

    public EditorAction(EditorCommandPrefix prefix, String stringParameters) {
        this.prefix = prefix;
        this.stringParameters = stringParameters;
    }

    public void addIntParameter(int param){
        if (intParameters == null) intParameters = new IntList();
        intParameters.append(param);
    }

    public void addFloatParameter(float param){
        if (floatParameters == null) floatParameters = new FloatList();
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

    public String getStringParameters() {
        return stringParameters;
    }
}
