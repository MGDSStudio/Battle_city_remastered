package io.itch.mgdsstudio.battlecity.menu.menus;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import processing.core.PGraphics;

public class MenuEditorPreloading extends Menu {

    private interface ButtonNames {

        String OPEN_EDITOR = "OPEN EDITOR";
        String MANAGE_LEVELS = "MANAGE LEVELS";
        String NEW_SPRITE = "NEW SPRITE";
        String REMOVE_SPRITE = "REMOVE SPRITE";

        String BACK = "BACK";
    }

    public MenuEditorPreloading(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
    }


    @Override
    protected void createGuiElements() {
        createTextButtonFromZone(255, 0, 0, ButtonNames.OPEN_EDITOR);
        createTextButtonFromZone(0, 255, 0, ButtonNames.MANAGE_LEVELS);
        createTextButtonFromZone(255, 255, 0, ButtonNames.NEW_SPRITE);
        createTextButtonFromZone(0, 0, 255, ButtonNames.REMOVE_SPRITE);
        createTextButtonFromZone(255, 125, 0, ButtonNames.BACK);
    }

    @Override
    public void update(MenuController menuController) {
        super.update(menuController);
        if (isGuiReleased(ButtonNames.BACK)) {
            menuController.setNextMenu(MenuType.MAIN);
        } else if (isGuiReleased(ButtonNames.OPEN_EDITOR)) {
            menuController.setNextMenu(MenuType.EDITOR_LOADING);
        } else if (isGuiReleased(ButtonNames.NEW_SPRITE)) {
            //menuController.setNextMenu(MenuType.EDITOR_PRELOADING_WINDOW);
        } else if (isGuiReleased(ButtonNames.MANAGE_LEVELS)) {
            //menuController.setNextMenu(MenuType.EDITOR_PRELOADING_WINDOW);
        }

    }
}

