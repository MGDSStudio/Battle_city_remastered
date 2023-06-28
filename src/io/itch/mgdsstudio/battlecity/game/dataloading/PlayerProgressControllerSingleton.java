package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.data.JSONArray;
import sun.rmi.runtime.Log;

import static javax.swing.UIManager.getString;

public class PlayerProgressControllerSingleton {




    private static PlayerProgressControllerSingleton playerProgressController;
    public final int ERROR_VALUE = -9999;


    public final static boolean CAMPAIGN = true;
    public final static boolean SINGLE_MISSIONS = false;
    private final static String CAMPAIGN_PROGRESS_FILE_NAME = "CampaignProgress.txt";
    private final static String SINGLE_MISSIONS_PROGRESS_FILE_NAME = "SingleMissionsProgress.txt";

    //protected ArrayList <String> data;


    protected String [] data;




    private PlayerProgressControllerSingleton(IEngine engine, boolean levelType) {
        String path = engine.getPathToObjectInAssets(CAMPAIGN_PROGRESS_FILE_NAME);
        if (levelType == SINGLE_MISSIONS) path = engine.getPathToObjectInAssets(SINGLE_MISSIONS_PROGRESS_FILE_NAME);
        loadArray(engine, path);
        Logger.debug("This must be got from the readable folder on android");
    }




    private void loadArray(IEngine engine, String path) {
        data = engine.getEngine().loadStrings(path);
        if (data == null){
            Logger.error("Can not load configuration data. File: " + path + " doesn't exist!");
        }
        else {
            Logger.debug("Data successfully uploaded and decoded");
        }
    }

    public int getIntegerValue(String key){
        for (int i = 0; i < data.length; i++){
            if (!data[i].startsWith("/") && !data[i].startsWith("\\")) {
                String dataKey = getKeyFromString(data[i]);
                if (dataKey.contains(key) || dataKey == key || dataKey.equals(key)) {
                    int value = getValueFromData(data[i]);
                    return value;
                }
            }
        }
        Logger.error("Can not key " + key + " in the data file");
        return ERROR_VALUE;
    }

    public int getIntegerValue(Keys key){
        return getIntegerValue(key.name());
    }

    public float getFloatValueWithDoublePrecision(String key){
        int valueAsInt = getIntegerValue(key);
        if (valueAsInt != ERROR_VALUE){
            float asFloat = (float)valueAsInt/100f;
            return asFloat;
        }
        else {
            Logger.error("Can not get float value! ");
            return ERROR_VALUE;
        }
    }

    public float getFloatValueWithDoublePrecision(Keys key){
        return getFloatValueWithDoublePrecision(key.name());
    }

    public boolean getBooleanValue(String key){
        int value = getIntegerValue(key);
        if (value == 0) return false;
        else return true;
    }



    private int getValueFromData(String dataString) {
        int start = dataString.indexOf('=');
        if (dataString.charAt(start+1) == ' '){
            start++;
        }
        int end = start+1;
        String valueString = "";
        for (int i = end; i<dataString.length(); i++){
            if (isDigit(dataString.charAt(i))){
                valueString+=dataString.charAt(i);
            }
            else {
                if (dataString.charAt(i) == '/' || dataString.charAt(i) == '\\'){
                    break;
                }
            }
        }

        //System.out.println("Data string is: " + dataString + "; Try to get value from the substring: " + valueString);
        int value = Integer.parseInt(valueString);
        System.out.println("Value: " + value);
        return value;
    }

    private boolean isDigit(char charAt) {
        return Character.isDigit(charAt);
    }

    private String getKeyFromString(String dataString) {
        int endChar = 0;
        if (dataString.contains("=")){
            int pos = dataString.indexOf("=");
            if (pos > 0){
                if (dataString.charAt(pos-1) == ' '){
                    pos--;
                }
            }
            String key = dataString.substring(0,pos);
            return key;
        }
        else {
            Logger.error("Can not get key from string: " + dataString);
            return null;
        }
    }


    public static void dispose(){
        playerProgressController = null;
        Logger.debug("Player progress controller were disposed at " + System.currentTimeMillis());
    }

    public static PlayerProgressControllerSingleton getInstance(IEngine engine, boolean levelType){
        if (playerProgressController == null) playerProgressController = new PlayerProgressControllerSingleton(engine, levelType);
        return playerProgressController;
    }

    public static void init(IEngine engine, boolean levelType){
        if (playerProgressController == null) playerProgressController = new PlayerProgressControllerSingleton(engine, levelType);
    }

    public static PlayerProgressControllerSingleton getInstance(){
        if (playerProgressController == null) Logger.error("Can not get instance. It was not initialized. Call init(IEngine engine, bool levelType) before get instance");
        return playerProgressController;
    }


    public void changeIntValue(Keys key, int delta) {
        int actual = getIntegerValue(key);
        if (actual != ERROR_VALUE){
            actual+=delta;
            setValueForKey(key, actual);
        }
        else Logger.error("Can not get for key " + key.name());
    }

    public void setValueForKey(Keys key, int actual) {
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
}
