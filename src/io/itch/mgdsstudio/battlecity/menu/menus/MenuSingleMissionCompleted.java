package io.itch.mgdsstudio.battlecity.menu.menus;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import processing.core.PGraphics;

public class MenuSingleMissionCompleted extends Menu{

    public MenuSingleMissionCompleted(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
    }
}
