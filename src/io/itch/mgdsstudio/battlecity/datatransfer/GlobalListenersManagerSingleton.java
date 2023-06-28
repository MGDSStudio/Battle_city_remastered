package io.itch.mgdsstudio.battlecity.datatransfer;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;

import java.util.ArrayList;

/*  This class is used to transfer data betweeen devices in mustyplayer mode
 */

public class GlobalListenersManagerSingleton {
    private ArrayList<GlobalListener> globalListeners;
    private static GlobalListenersManagerSingleton instance;

    private GlobalListenersManagerSingleton()
    {
        this.globalListeners = new ArrayList<>();
    }

    public static GlobalListenersManagerSingleton getInstance(){
        if (instance == null) instance = new GlobalListenersManagerSingleton();
        return instance;
    }

    public void dispose(){
        globalListeners.clear();
    }

    public void addAsListener(GlobalListener globalListener){
        if (!globalListeners.contains(globalListener)){
            globalListeners.add(globalListener);
        }
    }

    public void removeListener(GlobalListener globalListener){
        if (globalListeners.contains(globalListener)){
            globalListeners.remove(globalListener);
        }
    }

    public void notify(GLobalSerialAction command){
        for (int i = (globalListeners.size()-1); i >= 0; i--){
            if (globalListeners.get(i) == null){
                globalListeners.remove(i);
            }
            else globalListeners.get(i).appendCommand(command);
        }
    }



}
