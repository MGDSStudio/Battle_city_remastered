package io.itch.mgdsstudio.battlecity.mainpackage;

public abstract class GlobalData {

    public String getPathToAssets(){
        if (GlobalVariables.isDesktop()) return "Assets//";
        else return "Assets//";
    }
}
