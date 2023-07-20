package io.itch.mgdsstudio.battlecity.editor.data;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.Keys;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.DataFromMgdsTextParametersListReader;

public class EditorPreferencesSingleton extends DataFromMgdsTextParametersListReader {

    private static EditorPreferencesSingleton editorPreferencesInstance;

    private final static String FILE_NAME = "Editor_pref.txt";

    private EditorPreferencesSingleton(IEngine engine) {
        this.engine = engine;
        String path = engine.getPathToObjectInUserFolder(FILE_NAME);
        this.filePath = path;
        updateData();
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

    public void setValueForKey(EditorPreferences key, int actual) {
        String valueAsString = ""+actual;
        for (int i = 0; i < data.length; i++){
            if (data[i].contains(key.name())){
                if (data[i].startsWith(key.name())){
                    int startChar = data[i].indexOf('=');
                    if (startChar >= 0){
                        startChar+=2;
                        String firstSubstring = data[i].substring(0, startChar);
                        String lastSubstring = "";
                        int commentStart = data[i].indexOf('/');
                        if (commentStart > 0){
                            lastSubstring = data[i].substring(commentStart);
                        }
                        String newString = firstSubstring+valueAsString+lastSubstring;
                        Logger.debug("String " + data[i] + " replaced with " + newString);
                        data[i] = newString;

                        return;
                    }
                }
            }
        }
        Logger.error("String "+ key.name() + " value: " + actual + " can not be replaced");
    }

    @Override
    public void saveOnDisk(){
        String path = filePath;
        Logger.debug("Try to save data on disk at " + path);
        Logger.debug("Data list for saving: ");
        for (int i = 0; i < data.length; i++){
            System.out.println(i + ": " + data[i]);
        }
        engine.getEngine().saveStrings(path, data);
        Logger.debug("Data saved on disk ");
    }
}
