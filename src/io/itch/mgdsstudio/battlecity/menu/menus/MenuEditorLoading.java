package io.itch.mgdsstudio.battlecity.menu.menus;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import processing.core.PGraphics;

public class MenuEditorLoading extends Menu{


    private int nextLevel = 1;
    public MenuEditorLoading(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
        if (bundle != null) nextLevel = bundle.getNextLevel();
        else {

        }
    }

    @Override
    public void update(MenuController menuController) {
        if (firstFrameEnded) {
            MenuDataStruct menuDataStruct = new MenuDataStruct();
            menuDataStruct.setNextLevel(nextLevel);
            menuController.transferToEditor(menuDataStruct);

        }
        super.update(menuController);
        //Logger.debug("Updating");
    }





}
