package io.itch.mgdsstudio.engine.libs;

import io.itch.mgdsstudio.battlecity.game.Logger;

public abstract class StringLibrary {
    public static final int ERROR_CODE = -1;

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
}
