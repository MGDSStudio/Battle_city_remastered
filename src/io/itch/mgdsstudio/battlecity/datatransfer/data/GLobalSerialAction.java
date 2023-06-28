package io.itch.mgdsstudio.battlecity.datatransfer.data;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import processing.data.IntList;


/*
This action must be also serial transferred per Internet in multiplayer mode
 */

public class GLobalSerialAction extends Action{
    private final static int NO_DATA_VALUE = -9999;
    private final static String VALUES_DIVIDER = "_";
    private final static char SECONDARY_TO_TIME = ':';
    private final static char ACTION_NUMBER_CHAR = '№';
    private final static char END_MESSAGE_CHAR = '!';
    private final static String END_MESSAGE_STRING = ""+END_MESSAGE_CHAR;

    private int id;
    private long startTime;
    private final IntList valuesList = new IntList();
    private int commandNumber =-1;   //Action number in the long actions list

    public GLobalSerialAction(char prefix, IntList values, int id, long startTime, int commandNumber){
        this.prefix = prefix;
        this.id = id;
        this.startTime = startTime;
        this.commandNumber = commandNumber;
        for (int i = 0; i < values.size(); i++){
            valuesList.append(values.get(i));
        }
    }

    public GLobalSerialAction(char prefix, int mainValue, int id, long startTime, int commandNumber) {
        init(prefix, mainValue, id, startTime, commandNumber);
    }

    public static GLobalSerialAction createAction(char simpleActionPrefix) {
        GLobalSerialAction GLobalSerialAction = new GLobalSerialAction(simpleActionPrefix, null, -1, -1, -1);
        return GLobalSerialAction;
    }

    private void init(char prefix, int mainValue, int id, long startTime, int commandNumber){
        this.prefix = prefix;
        this.id = id;
        this.startTime = startTime;
        this.commandNumber = commandNumber;
        valuesList.append(mainValue);
    }



    public char getPrefix() {
        return prefix;
    }

    public int getMainValue() {
        if (valuesList.size()>0) {
            return valuesList.get(0);
        }
        else return NO_DATA_VALUE;
    }

    public int getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "SerialAction{" +
                "prefix=" + prefix +
                ", id=" + id +
                ", startTime=" + startTime +
                ", valuesList=" + valuesList +
                ", commandNumber=" + commandNumber +
                '}';
    }

    public String getSerialized() {
        String serializedString = ""+id+prefix;
        for (int i = 0; i < valuesList.size(); i++){
            serializedString+=valuesList.get(i);
            if (i < (valuesList.size()-1)){
                serializedString+=VALUES_DIVIDER;
            }
            else serializedString+=SECONDARY_TO_TIME;
        }
        serializedString+=(startTime);
        serializedString+=ACTION_NUMBER_CHAR;
        serializedString+=commandNumber;
        serializedString+=END_MESSAGE_CHAR;
        return serializedString;
    }

    private static IntList getValuesList(String string){
        String [] valuesAsStrings = string.split(VALUES_DIVIDER);
        String singleStringValue = "";
        char [] charArray = valuesAsStrings[0].toCharArray();
        for (int i = (charArray.length-1);  i >= 0; i--){
            if (isDigit(charArray[i])){
                singleStringValue=(charArray[i]+singleStringValue);
            }
        }
        IntList values = new IntList();
        //Logger.debugLog("First value is: " + singleStringValue + " in string: " + string);
        values.append((Integer.parseInt(singleStringValue)));
        // append another values
        if (valuesAsStrings.length > 1){
            if (valuesAsStrings.length >2){
                for (int i = 1; i < valuesAsStrings.length-2; i++){
                    //Logger.debugLog("Value "+ i +" is: " + valuesAsStrings[i]);
                    try {
                        values.append((Integer.parseInt(valuesAsStrings[i])));
                    }
                    catch (Exception e){
                        Logger.debug("Can not cast: " + valuesAsStrings[i] + " to a string. " + e);
                    }
                }
            }
            else {
                Logger.debug("This action has only two values");
            }
            charArray = valuesAsStrings[valuesAsStrings.length-1].toCharArray();
            for (int i = 0;  i < charArray.length; i++){
                if (isDigit(charArray[i])){
                    singleStringValue+=charArray[i];
                }
                else break;
            }
            values.append((Integer.parseInt(singleStringValue)));
        }
        else Logger.debug("This action has only one value");
        return values;


    }

    public static GLobalSerialAction[] encryptStringsInCommand(String serialized){
        String [] singleSerializedActions = stringToSingleSubstringsDivider(serialized, END_MESSAGE_STRING);
        GLobalSerialAction[] actions = new GLobalSerialAction[singleSerializedActions.length];
        for (int i = 0; i < singleSerializedActions.length; i++){
            GLobalSerialAction GLobalSerialAction = encryptStringInCommand(singleSerializedActions[i]);
            actions[i] = GLobalSerialAction;
        }
        return actions;
    }

    private static String[] stringToSingleSubstringsDivider(String source, String divider){
        String [] singleStrings = source.split(divider);
        if (GlobalVariables.debug){
            String logInfo = "Single strings in the source were splitet into " + singleStrings.length + " single strings. ";
            for (int i = 0; i < singleStrings.length; i++){
                logInfo+=singleStrings[i];
                if (i<(singleStrings.length-1)) logInfo+="._.";
            }
            Logger.debug(logInfo);
        }
        return singleStrings;
    }



    private static GLobalSerialAction encryptStringInCommand(String serialized){
        int id = getIdFromString(serialized);
        char prefix = getPrefixFromString(serialized);
        String stringWithValues = getStringWithValues(serialized, prefix);
        IntList values = getValuesFromString(stringWithValues, VALUES_DIVIDER);
        int startActionTime = getStartActionTime(serialized);
        int actionNumber = getActionNumber(serialized);
        GLobalSerialAction GLobalSerialAction = new GLobalSerialAction(prefix, values, id, startActionTime, actionNumber);
        return GLobalSerialAction;

    }

    private static IntList getValuesFromString(String stringWithValues, String valuesDivider) {
        String [] subvalues = stringWithValues.split(valuesDivider);
        System.out.println("From string: " + stringWithValues + " we have got : " + subvalues.length + " single strings");
        for (int i = 0; i < subvalues.length; i++){
            System.out.println("*** " + subvalues[i]);
        }
        IntList list = new IntList();
        for (int i = 0; i < subvalues.length; i++){
            boolean onlyDigits = true;
            char[] chars = subvalues[i].toCharArray();
            for (int j =0; j < chars.length; j++){
                if (!isDigit(chars[j])){
                    onlyDigits = false;
                    break;
                }
            }
            if (onlyDigits){
                int value = Integer.parseInt(subvalues[i]);
                list.append(value);
            }
            else {
                System.out.println("This string: " + subvalues[i] + " can not be parsed to the integer");
            }
        }
        return list;
    }


    private static int getActionNumber(String serialized) {
        int actionNumber = getValueBetweenChars(ACTION_NUMBER_CHAR, END_MESSAGE_CHAR, serialized);
        return actionNumber;
    }

    private static int getStartActionTime(String serialized) {
        int startActionTime = getValueBetweenChars(SECONDARY_TO_TIME, ACTION_NUMBER_CHAR, serialized);
        return startActionTime;
    }

    private static char getPrefixFromString(String fullSource) {
        for (int i =0 ; i < fullSource.length() ; i++){
            if (!isDigit(fullSource.charAt(i))){
                return fullSource.charAt(i);
            }
        }
        Logger.error("Can not get prefix from: " + fullSource);
        return 'Z';
    }

    private static int getIdFromString(String fullSource){
        int id;
        String dataString;
        for (int i = 0; i < fullSource.length(); i++){
            if (!isDigit(fullSource.charAt(i))){
                dataString = fullSource.substring(0,i);
                id = Integer.parseInt(dataString);
                return id;
            }

        }
        Logger.error("Can not get id from: " + fullSource);
        return -1;
    }

    private static String getStringWithValues(String fullSource, char startChar){
        int start = fullSource.indexOf(startChar);
        int end = fullSource.indexOf(SECONDARY_TO_TIME);
        String resultString = fullSource.substring(start+1, end);
        return resultString;
    }

    private static void logDebugInfo1(IntList values, String serialized) {
        String dataString = values.size() + " values in list: " ;
        for (int i = 0; i < values.size(); i++) {
            dataString+=values.get(i);
            if (i < (values.size()-1)) dataString+=", ";
        }
        dataString+=(" in the source string: " + serialized);
        Logger.debug(dataString);
    }

    private static int getValueBetweenChars(char first, char second, String dataString){
        int startCharPos = dataString.indexOf(first);
        int endCharPos = dataString.indexOf(second);
        if (endCharPos < 0){
            endCharPos = dataString.length();
        }
        if (startCharPos>0 && endCharPos > 0){
            if (endCharPos > startCharPos) {
                startCharPos= startCharPos+1;

                String substring = dataString.substring(startCharPos, endCharPos);
                int value = Integer.parseInt(substring);
                //Logger.debugLog("Try to get substring between " + startCharPos + " and " + endCharPos + " for chars: " + first + " and " + second + " in string: " + dataString  + ". We got value: " + value);
                return value;
            }
            else {
                Logger.error("In string: " + dataString + " chars: " + first + " and " + second + " can not be right founded");
                return NO_DATA_VALUE;
            }
        }
        else {
            if (startCharPos == endCharPos) Logger.error("The pos for chars are the same: " + startCharPos + " and " + endCharPos);
            else {
                if (endCharPos < 0) Logger.error("End char pos was not founded: " + second + " in the data string: " + dataString);
                if (startCharPos < 0) Logger.error("Start char pos was not founded: " + first + " in the data string: " + dataString);
            }
            return NO_DATA_VALUE;
        }
    }

    private static boolean isDigit(char testChar){
        if (testChar == '1' || testChar == '2' || testChar == '3' || testChar == '4' ||
           testChar == '5' || testChar == '6' || testChar == '7' || testChar == '8' ||
           testChar == '9' ||testChar == '0' || testChar == '-') return true;
        else return false;
    }




    public IntList getValuesList() {
        return valuesList;
    }
}
