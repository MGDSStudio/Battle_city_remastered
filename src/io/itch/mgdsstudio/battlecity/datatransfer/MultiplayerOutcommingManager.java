package io.itch.mgdsstudio.battlecity.datatransfer;

public class MultiplayerOutcommingManager extends MultiplayerManager{

    private static boolean created;
    private static MultiplayerOutcommingManager manager;

    private MultiplayerOutcommingManager() {
        created = true;
    }

    public static MultiplayerOutcommingManager getManager(){
        if (created) return manager;
        else return new MultiplayerOutcommingManager();
    }

    public static void dispose(){
        manager = null;
        created = false;
    }
}
