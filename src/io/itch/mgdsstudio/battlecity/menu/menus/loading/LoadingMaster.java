package io.itch.mgdsstudio.battlecity.menu.menus.loading;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.io.File;
import java.util.ArrayList;

public abstract class LoadingMaster {
    protected String filePrefix;
    protected String fileExtension;

    protected String dir;

    protected ArrayList<File> files = new ArrayList<>();


    public LoadingMaster(IEngine engine, String filePrefix, String fileExtension, String dir) {
        this.filePrefix = filePrefix;
        this.fileExtension = fileExtension;
        this.dir = dir;
        init(engine);
    }

    protected abstract void init(IEngine engine);

    public int getFilesNumber(){
        return files.size();
    }

    public ArrayList<File> getFiles(){
        return files;
    }
}
