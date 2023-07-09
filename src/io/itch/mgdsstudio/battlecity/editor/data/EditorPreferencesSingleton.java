package io.itch.mgdsstudio.battlecity.editor.data;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.DataFromMgdsTextParametersListReader;

public class EditorPreferencesSingleton extends DataFromMgdsTextParametersListReader {

    private static EditorPreferencesSingleton editorPreferencesInstance;
    private final static String FILE_NAME = "Editor_pref.txt";

    private EditorPreferencesSingleton(IEngine engine) {
        String path = engine.getPathToObjectInUserFolder(FILE_NAME);
        loadArray(engine, path);
        Logger.debug("This must be got from the readable folder on android");
    }

    public static void dispose(){
        editorPreferencesInstance = null;
        Logger.debug("Player progress controller were disposed at " + System.currentTimeMillis());
    }

    public static EditorPreferencesSingleton getInstance(IEngine engine){
        if (editorPreferencesInstance == null) editorPreferencesInstance = new EditorPreferencesSingleton(engine);
        return editorPreferencesInstance;
    }

    public static void init(IEngine engine, boolean levelType){
        if (editorPreferencesInstance == null) editorPreferencesInstance = new EditorPreferencesSingleton(engine);
    }

    public static EditorPreferencesSingleton getInstance(){
        if (editorPreferencesInstance == null) Logger.error("Can not get instance. It was not initialized. Call init(IEngine engine, bool levelType) before get instance");
        return editorPreferencesInstance;
    }

}
