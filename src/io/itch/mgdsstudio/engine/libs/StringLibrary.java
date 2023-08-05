package io.itch.mgdsstudio.engine.libs;

import io.itch.mgdsstudio.battlecity.game.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class StringLibrary {
    public static final int ERROR_CODE = -1;
    public static final String  ERROR = "";

    public static String getClassNameFromFullName(String full){
        for (int i = full.length()-1; i >= 0; i--){
            if (full.charAt(i) == '.'){
                return full.substring(i+1);
            }
        }
        Logger.debug("This full class name is not located in a package");
        return full;
    }

    public static int getDigitFromString(String text){
        String textRepresentation = "";
        for (int i = 0 ; i < text.length(); i++){
            if (Character.isDigit(text.charAt(i))){
                textRepresentation+=text.charAt(i);
            }
        }
        if (textRepresentation.length() > 0){
            return Integer.parseInt(textRepresentation);
        }
        else {
            Logger.error("Don digits in string: " + text);
            return ERROR_CODE;
        }
    }

    public static boolean save(ArrayList  <String> data, String path){
        PrintWriter output = null;
        boolean saved;
        try {
            output = new PrintWriter((new FileWriter(path, true)));
            for (String dataString : data){
                output.println(dataString);
            }
            output.flush();
            output.close();
            output = null;
            Logger.debug("Data successfully saved");
            saved = true;
        }
        catch (IOException e){
            System.out.println("System is busy. Can not save");
            saved = false;
        }
        finally {
            if (output != null ) output.close();
        }

        return saved;
    }

    public static String getStringBetween(String data, char startChar, char endChar){
        if (data.contains(""+startChar)){
            if (data.contains(""+endChar)){
                int startPos = data.indexOf(startChar);
                int endPos = data.indexOf(endChar);
                try {
                    return data.substring(startPos+1, endPos-1);
                }
                catch (Exception e){
                    Logger.error("Can not get substring from " + data);
                    e.printStackTrace();
                    return ERROR;
                }

            }
        }
        return ERROR;
    }
}
