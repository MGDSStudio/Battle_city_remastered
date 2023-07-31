package io.itch.mgdsstudio.battlecity.game.dataloading;

public class GraphicData {
    private int [] values;
    private String path;

    public GraphicData(int[] values, String path) {
        this.values = values;
        this.path = path;
    }

    public int[] getValues() {
        return values;
    }

    public String getPath() {
        return path;
    }
}
