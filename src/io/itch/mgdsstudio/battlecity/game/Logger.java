package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;

public abstract class Logger {

    public final static void debug(String data){
        if (GlobalVariables.debug) log("DEBUG: " + data);
    }

    public final static void correct(String data){
        if (GlobalVariables.debug) log("TO BE CORRECTED: " + data);
    }

    public final static void error(String data){
        log("ERROR: " + data);
    }

    public final static void editor(String data){
        log("EDITOR: " + data);
    }

    public final static void net(String data){
        log("NET: " + data);
    }

    private static void log(String data){
        System.out.println(data);
    }

}
