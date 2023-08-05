package io.itch.mgdsstudio.engine.libs;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.Keys;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;


/*  This class is used as parent to create data reader/writer for preferences
*
 */
public abstract class DataFromMgdsTextParametersListReader {
    public final int ERROR_VALUE = -9999;
    protected String [] data;
    protected IEngine engine;
    protected String filePath;

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

    public void updateData(){
        loadArray(engine, filePath);
        Logger.debug("This must be got from the readable folder on android");
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

    protected boolean isDigit(char charAt) {
        return Character.isDigit(charAt);
    }

    public void changeIntValue(Keys key, int delta) {
        int actual = getIntegerValue(key);
        if (actual != ERROR_VALUE){
            actual+=delta;
            setValueForKey(key, actual);
        }
        else Logger.error("Can not get for key " + key.name());
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

    protected void loadArray(IEngine engine, String path) {
        data = engine.getProcessing().loadStrings(path);
        if (data == null){
            Logger.error("Can not load configuration data. File: " + path + " doesn't exist!");
        }
        else {
            Logger.debug("Data successfully uploaded and decoded");
        }
    }

    public void saveOnDisk(){
        String path = filePath;
        Logger.debug("Try to save data on disk at " + path);
        engine.getProcessing().saveStrings(path, data);
        Logger.debug("Data saved on disk ");
    }

}
