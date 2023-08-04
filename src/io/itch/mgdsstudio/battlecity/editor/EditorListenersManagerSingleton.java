package io.itch.mgdsstudio.battlecity.editor;

import java.util.ArrayList;

public class EditorListenersManagerSingleton {

    private ArrayList<EditorActionsListener> localListeners;
    private static EditorListenersManagerSingleton instance;

    private EditorListenersManagerSingleton()
    {
        this.localListeners = new ArrayList<>();
    }

    public static EditorListenersManagerSingleton getInstance(){
        if (instance == null) instance = new EditorListenersManagerSingleton();
        return instance;
    }

    public void dispose(){
        localListeners.clear();
    }

    public void addAsListener(EditorActionsListener localListener){
        if (!localListeners.contains(localListener)){
            localListeners.add(localListener);
        }

    }

    public void removeListener(EditorActionsListener globalListener){
        if (localListeners.contains(globalListener)){
            localListeners.remove(globalListener);
        }
    }

    public void notify(EditorAction command){
        for (int i = (localListeners.size()-1); i >= 0; i--){
            if (localListeners.get(i) == null){
                localListeners.remove(i);
            }
            else localListeners.get(i).appendCommand(command);
        }
    }
}
