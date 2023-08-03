package io.itch.mgdsstudio.battlecity.utilities;

import io.itch.mgdsstudio.battlecity.editor.menus.utilities.SubmenuWithTilesetButtonsCreationMaster;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.AllImageZonesFromFileLoader;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneFullData;

import java.util.ArrayList;

public class ImageZonesSorterInAccordingToLastUsed {
    private final IEngine engine;
    private ArrayList<ImageZoneFullData> sortedData;

    public ImageZonesSorterInAccordingToLastUsed(IEngine engine) {
        this.engine = engine;
        AllImageZonesFromFileLoader loader = new AllImageZonesFromFileLoader(engine);
        ArrayList < ImageZoneFullData> unsortedData = loader.getImageZonesWithFulLData();
        String line = "";
        for (int i = 0; i < unsortedData.size(); i++) line+=unsortedData.get(i).getName() + ",";
        Logger.debug("We have: " + unsortedData.size() + " unique unsorted tilesets. Line: " + line);
        SubmenuWithTilesetButtonsCreationMaster master = new SubmenuWithTilesetButtonsCreationMaster(engine);
        sortedData = master.sortObjectsInAccordingToLastUsed(unsortedData);
        line = "";
        for (int i = 0; i < unsortedData.size(); i++) line+=sortedData.get(i).getName() + ",";
        Logger.debug("We have now: " + sortedData.size() + " unique sorted tilesets. Line: " + line);
    }

    public ArrayList<ImageZoneFullData> getSortedData() {
        return sortedData;
    }
}
