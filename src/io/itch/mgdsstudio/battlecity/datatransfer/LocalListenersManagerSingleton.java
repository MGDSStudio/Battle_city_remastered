package io.itch.mgdsstudio.battlecity.datatransfer;

import io.itch.mgdsstudio.battlecity.datatransfer.data.LocalAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.LocalListener;

import java.util.ArrayList;

/*  This class is used to transfer data betweeen classes without needed to be transfered via internet e.g. data for HUD
 */
public class LocalListenersManagerSingleton {
    private ArrayList<LocalListener> localListeners;
    private static LocalListenersManagerSingleton instance;

    private LocalListenersManagerSingleton()
    {
        this.localListeners = new ArrayList<>();
    }

    public static LocalListenersManagerSingleton getInstance(){
        if (instance == null) instance = new LocalListenersManagerSingleton();
        return instance;
    }

    public void dispose(){
        localListeners.clear();
    }

    public void addAsListener(LocalListener localListener){
        if (!localListeners.contains(localListener)){
            localListeners.add(localListener);
        }
    }

    public void removeListener(LocalListener globalListener){
        if (localListeners.contains(globalListener)){
            localListeners.remove(globalListener);
        }
    }

    public void notify(LocalAction command){
        for (int i = (localListeners.size()-1); i >= 0; i--){
            if (localListeners.get(i) == null){
                localListeners.remove(i);
            }
            else localListeners.get(i).appendCommand(command);
        }
    }
}
