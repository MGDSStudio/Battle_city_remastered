package io.itch.mgdsstudio.engine.graphic;

import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.Map;

public class GraphicManagerSingleton {
    private static GraphicManagerSingleton graphicManagerSingleton;
    private static boolean wasInit;
    private final HashMap<String, Image> images = new HashMap<>();
    //private final HashMap<String, Image> images = new HashMap<>();
    private final PApplet engine;

    private GraphicManagerSingleton(PApplet pApplet){
        this.engine = pApplet;
    }

    public static GraphicManagerSingleton getManager(PApplet engine){
        if (!wasInit) {
            graphicManagerSingleton = new GraphicManagerSingleton(engine);
            wasInit = true;
        }
        return graphicManagerSingleton;
    }

    public Image getImage(String path){
        if (images.containsKey(path)){
            for (Map.Entry singleImage :  images.entrySet()) {
                if (singleImage.getKey() == path || singleImage.getKey().equals(path) || singleImage.getKey().toString().contains(path)){
                    return (Image) singleImage.getValue();
                }
            }
        }
        else{
            Image image = new Image(engine, path);
            images.put(path, image);
            return image;
        }
        {
            if (GlobalVariables.debug){
                System.out.println("Can not load graphic " + path + ". We have only: ");
                for (Map.Entry singleImage :  images.entrySet()) {
                    System.out.print(singleImage.getKey() + ", ");

                }
                System.out.println();
            }
            return null;
        }
    }
}
