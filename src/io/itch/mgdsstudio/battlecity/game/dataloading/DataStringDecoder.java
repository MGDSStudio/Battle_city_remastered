package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;
import processing.core.PApplet;


public class DataStringDecoder {
    protected String stringToBeDecoded;
    //byte actualDataElement = 0;
    int actualStartCharNumber = 0;

    public DataStringDecoder(String stringToBeDecoded) {
        this.stringToBeDecoded = stringToBeDecoded;
    }

    public void setNewStringData(String stringToBeDecoded) {
        this.stringToBeDecoded = stringToBeDecoded;
    }

    private String getPathToTexture(char startChar, char endChar) {
        String path = "";
        try {
            path = stringToBeDecoded.substring(stringToBeDecoded.indexOf(startChar) + 1, stringToBeDecoded.indexOf(endChar));
            if (!path.contains(".png") && !path.contains(".gif") && !path.contains(".jpg") && !path.contains(".jpeg")) {
                System.out.println("This object has no graphic data. Path set on ");
                path = "No_data";
            }
        } catch (Exception e) {
            System.out.println("No texture data");
            return null;
        }
        return path;
    }

    private int[] getValuesBetweenChars(char startChar, char dividingChar, String endChar) {
        return getValuesBetweenChars(startChar, dividingChar, endChar.toCharArray()[0]);
    }

    private int[] getValuesBetweenChars(char startChar, char dividingChar, char endChar) {
        int[] values = new int[valuesNumber(dividingChar, endChar)];
        String testString = "";
        //int startCharPos = stringToBeDecoded.indexOf(startChar)+1;
        //int endCharPos = stringToBeDecoded.indexOf(endChar);
        //testString += testString.concat(stringToBeDecoded.substring(startCharPos, endCharPos));
        testString += testString.concat(stringToBeDecoded);

        Logger.debug("Test string to get main data: " + testString);
        for (int i = 0; i < values.length; i++) {
            try {
                int endSymbolPosition = testString.indexOf(dividingChar);
                if (endSymbolPosition != -1) {
                    String textDataToBeReturn = "";
                    if (startChar == DataDecoder.MAIN_DATA_START_CHAR) {
                        int indexOfSymbol = testString.indexOf(startChar);
                        textDataToBeReturn = testString.substring(indexOfSymbol + 1, endSymbolPosition);
                    } else {
                        int indexOfSymbol = testString.indexOf(" ");
                        textDataToBeReturn = testString.substring(indexOfSymbol + 1, endSymbolPosition);
                    }
                    testString = testString.substring(endSymbolPosition + 1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                } else {
                    String textDataToBeReturn = "";
                    if (endChar == DataDecoder.END_ROW_SYMBOL) textDataToBeReturn = testString.substring(0);
                    else {
                        try {
                            textDataToBeReturn = testString.substring(0, testString.indexOf(endChar));
                        } catch (Exception e) {
                            System.out.println("This string has no symbol " + endChar);
                            textDataToBeReturn = testString.substring(0, DataDecoder.END_ROW_SYMBOL);
                        }
                    }
                    testString = testString.substring(endSymbolPosition + 1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                //System.out.println("Test string after " + i + " itteration: " + testString);
            } catch (Exception e) {
                System.out.println("Can not devide. Must be:  " + values.length + " values. TestString: " + testString + " Deviders: " + startChar + "," + dividingChar + "," + endChar + " Exception: " + e);

                System.out.println("");
            }
        }
        return values;
    }

    private int[] getValuesBetweenCharsNew(char startChar, char dividingChar, char endChar) {
        String testString = "";
        int startCharPos = stringToBeDecoded.indexOf(startChar)+1;
        int endCharPos = stringToBeDecoded.indexOf(endChar);
        if (startCharPos<0 || endCharPos < 0){
            return new int[0];
        }
        testString += testString.concat(stringToBeDecoded.substring(startCharPos, endCharPos));
        //Logger.debug("Source string: " + stringToBeDecoded + " is " + testString);
        if (testString.contains(""+dividingChar)){
            String [] singleTextValues = PApplet.split(testString, dividingChar);
            int values [] = new int[singleTextValues.length];
            for (int i = 0; i < values.length; i++){
                values[i] = Integer.parseInt(singleTextValues[i]);
            }
            return values;

        }
        else {
            //Logger.debug("only one value");
            int value = Integer.parseInt(testString);
            int [] values = new int[]{value};
            return values;
        }
    }

    private int valuesNumber(char devidingChar, char endChar) {
        int valuesNumber = 0;
        boolean stopped = false;
        String lengthTest = "";
        lengthTest = lengthTest.concat(stringToBeDecoded);
        if (endChar == DataDecoder.END_ROW_SYMBOL || !stringToBeDecoded.contains("" + endChar)) {
            while (!stopped) {
                if (lengthTest.indexOf(devidingChar) != -1) {
                    valuesNumber++;
                    actualStartCharNumber = lengthTest.indexOf(devidingChar) + 1;
                    lengthTest = lengthTest.substring(actualStartCharNumber);
                } else stopped = true;
            }
        } else {
            lengthTest = lengthTest.concat(stringToBeDecoded);
            //System.out.println("String was " + lengthTest);
            lengthTest = lengthTest.substring(0, lengthTest.indexOf(endChar));
            //System.out.println("String is " + lengthTest);
            while (!stopped) {
                if (lengthTest.indexOf(devidingChar) != -1) {
                    valuesNumber++;
                    actualStartCharNumber = lengthTest.indexOf(devidingChar) + 1;
                    lengthTest = lengthTest.substring(actualStartCharNumber);
                } else stopped = true;
            }
        }
        if (valuesNumber != 0) valuesNumber++;
        actualStartCharNumber = 0;
        return valuesNumber;
    }

    private int getValueBetweenChars(String startChar, char endChar) {
        int startCharPos = stringToBeDecoded.indexOf(startChar)+1;
        int endCharPos = stringToBeDecoded.indexOf(endChar);
        if (startCharPos > 0 && endCharPos > 0 && endCharPos > startCharPos) {
            String stringValue = stringToBeDecoded.substring(startCharPos, endCharPos);
            System.out.println("Values: " + stringValue);
            int value = Integer.parseInt(stringValue);
            return value;
        }
        Logger.error("Can not get value between " + startChar + " end " + endChar + " in string " + stringToBeDecoded + "; Can not get object's id");
        return 1;
    }


    public EntityData getDecodedData(String type) {
        int[] values = getValuesBetweenCharsNew(DataDecoder.MAIN_DATA_START_CHAR, DataDecoder.DIVIDER_BETWEEN_VALUES, DataDecoder.GRAPHIC_NAME_START_CHAR);

        if (values != null) {
            int id = getValueBetweenChars(DataDecoder.DIVIDER_NAME_ID, DataDecoder.MAIN_DATA_START_CHAR);
            int [] graphicValues = getValuesBetweenCharsNew(DataDecoder.GRAPHIC_NAME_START_CHAR, DataDecoder.DIVIDER_BETWEEN_GRAPHIC_DATA, DataDecoder.GRAPHIC_NAME_END_CHAR);

            EntityData entityData = new EntityData(values, graphicValues, id);
            return entityData;
        } else {
            Logger.error("No values founded at " + stringToBeDecoded);
        }
        return null;
    }

    /*
    public EntityData getDecodedData(String type) {
        int[] values = getValuesBetweenChars(DataDecoder.MAIN_DATA_START_CHAR, DataDecoder.DIVIDER_BETWEEN_VALUES, DataDecoder.GRAPHIC_NAME_START_CHAR);
        GraphicData[] graphicDataArray = null;
        if (values != null) {
            int id = getValueBetweenChars(DataDecoder.DIVIDER_NAME_ID, DataDecoder.MAIN_DATA_START_CHAR);
            String[] paths = getStringsBetweenChars(DataDecoder.GRAPHIC_NAME_START_CHAR, (DataDecoder.GRAPHIC_NAME_END_CHAR));
            if (paths != null) {
                String[] graphicData = getStringsBetweenChars(DataDecoder.GRAPHIC_NAME_END_CHAR, DataDecoder.GRAPHIC_NAME_START_CHAR);
                if (graphicData != null) {
                    int[][] graphicDataList = getIntDataLists(graphicData, DataDecoder.DIVIDER_BETWEEN_GRAPHIC_DATA);
                    if (graphicDataList != null) {
                        graphicDataArray = new GraphicData[graphicData.length];
                        for (int i = 0; i < graphicDataArray.length; i++) graphicDataArray[i] = new GraphicData(graphicDataList[i], paths[i]);
                    } else {
                        Logger.debug("this entity graphic data " + type + " at string " + stringToBeDecoded + " can not be casted into integer");
                    }
                } else {
                    //Logger.debugLog("this entity " + type + " has no graphic data at string " + stringToBeDecoded);
                }
            } else {
                //Logger.debugLog("this entity " + type + " has no graphic paths at string " + stringToBeDecoded);
            }
            EntityData entityData = new EntityData(values, graphicDataArray, id);
            return entityData;
        } else {
            Logger.error("No values founded at " + stringToBeDecoded);
        }
        return null;
    }
     */


    private int[][] getIntDataLists(String[] paths, char divider) {
        int size1 = paths.length;
        int size2 = paths[0].split(String.valueOf(divider)).length;
        int[][] dataList = new int[size1][size2];
        for (int i = 0; i < paths.length; i++) {
            String[] singleValues = paths[i].split(String.valueOf(divider));
            for (int k = 0; k <= singleValues.length; k++) {
                int value = -1;
                try {
                    value = Integer.parseInt(singleValues[k]);
                } catch (Exception e) {
                    Logger.debug(e.toString());
                }
                dataList[i][k] = value;
            }
        }

        return dataList;
    }

    private String[] getStringsBetweenChars(char graphicNameStartChar, char graphicNameEndChar){
        return getStringsBetweenChars("" + graphicNameStartChar, ""+graphicNameEndChar);
    }

    private String[] getStringsBetweenChars(String graphicNameStartChar, String graphicNameEndChar) {
        if (stringToBeDecoded.contains(graphicNameStartChar)){
            if (stringToBeDecoded.contains(graphicNameEndChar)){
                int count1 = getCountForChar(graphicNameStartChar);
                int count2 = getCountForChar(graphicNameEndChar);
                if (count1 == count2){
                    String [] dataStrings = new String[count1];
                    int firstAppearing = stringToBeDecoded.indexOf(graphicNameStartChar);
                    String nextSubstring= stringToBeDecoded.substring(firstAppearing+1);
                    for (int i = 0; i < dataStrings.length; i++){
                        int endPos = nextSubstring.indexOf(graphicNameEndChar);
                        dataStrings[i] = nextSubstring.substring(0,endPos);
                        nextSubstring = nextSubstring.substring(endPos+1);
                    }
                    return dataStrings;
                }
            }
        }
        return null;
    }


    private int getCountForChar(String graphicNameEndChar){
        return getCountForChar(graphicNameEndChar.toCharArray()[0]);
    }

    private int getCountForChar(char graphicNameEndChar){
        int count = 0;
        char chatToBeFounded = graphicNameEndChar;
        for (int i = 0; i < stringToBeDecoded.length(); i++){
            if (stringToBeDecoded.charAt(i) == chatToBeFounded){
                count++;
            }
        }
        return count;
    }
}
