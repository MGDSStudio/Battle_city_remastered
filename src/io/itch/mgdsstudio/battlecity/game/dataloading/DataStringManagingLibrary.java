package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.Logger;

public class DataStringManagingLibrary {
    private String sourceDataString;
    private final static String ERROR = "";

    public DataStringManagingLibrary(String sourceDataString) {
        this.sourceDataString = sourceDataString;
    }

    public String getDataBeforeId(){
        return getStringBeforeId();
    }

    public String getDataAfterId(){
        return getStringAfterId();
    }

    private String getStringBeforeId(){
        int pos = sourceDataString.indexOf(DataDecoder.DIVIDER_NAME_ID);
        if (pos>=0) {
            return sourceDataString.substring(0, pos);
        }
        Logger.error("Can not get substring");
        return ERROR;
    }

    private String getStringAfterId(){
        int pos = sourceDataString.indexOf(DataDecoder.MAIN_DATA_START_CHAR);
        if (pos>=0) {
            return sourceDataString.substring(pos);
        }
        Logger.error("Can not get substring");
        return ERROR;
    }


}
