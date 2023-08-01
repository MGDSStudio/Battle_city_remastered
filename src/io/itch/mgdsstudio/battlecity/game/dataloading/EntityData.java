package io.itch.mgdsstudio.battlecity.game.dataloading;

import java.util.Arrays;

public class EntityData {
    //private String [] pathes;

    private int [] graphicData;
    //private GraphicData [] graphicData;
    private int [] values;

    private int id = -9999;


    public EntityData(int[] values, int[] graphicData, int id) {
        this.graphicData = graphicData;
        this.values = values;
        this.id = id;
    }

    /*
    public EntityData(int[] values, GraphicData[] graphicData, int id) {
        this.graphicData = graphicData;
        this.values = values;
        this.id = id;
    }
     */

    public int[] getGraphicData() {
        return graphicData;
    }

    /*
    public GraphicData[] getGraphicData() {
        return graphicData;
    }
     */

    public int[] getValues() {
        return values;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        String dataString = this.getClass()+"{id: " + id;
        if (values!=null) dataString+=("; main values list: "+Arrays.toString(values));
        if (graphicData != null) dataString+=("; graphic data list: "+ Arrays.toString(graphicData));
        dataString+="}";
        return dataString;
    }
}
