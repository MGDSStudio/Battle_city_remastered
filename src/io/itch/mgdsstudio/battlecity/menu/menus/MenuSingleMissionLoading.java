package io.itch.mgdsstudio.battlecity.menu.menus;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import processing.core.PGraphics;

public class MenuSingleMissionLoading extends Menu{
    private boolean firstFrameEnded;
    private int nextLevel;
    public MenuSingleMissionLoading(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
        nextLevel = bundle.getNextLevel();
    }

    @Override
    public void update(MenuController menuController) {
        if (!firstFrameEnded){
            firstFrameEnded = true;
        }
        else {
            MenuDataStruct menuDataStruct = new MenuDataStruct();
            menuDataStruct.setNextLevel(nextLevel);
            menuController.transferToGame(menuDataStruct);
        }
    }
}
