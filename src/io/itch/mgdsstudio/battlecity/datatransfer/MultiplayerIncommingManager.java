package io.itch.mgdsstudio.battlecity.datatransfer;

public class MultiplayerIncommingManager extends MultiplayerManager{
    private static boolean created;
    private static MultiplayerIncommingManager manager;

    private MultiplayerIncommingManager() {
        created = true;
    }

    public static MultiplayerIncommingManager getManager(){
        if (created) return manager;
        else return new MultiplayerIncommingManager();
    }

    public static void dispose(){
        manager = null;
        created = false;
    }

}
