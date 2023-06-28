package io.itch.mgdsstudio.battlecity.menu.menus;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import io.itch.mgdsstudio.battlecity.menu.menudataencryption.MenuGuiDataEncryptor;
import io.itch.mgdsstudio.battlecity.menu.menudataencryption.RectGuiZone;
import processing.core.PConstants;
import processing.core.PGraphics;

public class MenuMain extends Menu{

    private interface ButtonNames{
        String CAMPAIGN = "CAMPAIGN";
        String SINGLE_MISSIONS = "SINGLE_MISSIONS";
        String LEVELS_EDITOR = "LEVELS_EDITOR";
        String OPTIONS = "OPTIONS";
        String PRIVACY_POLICY = "PRIVACY_POLICY";
        String EXIT = "EXIT";
    }
    public MenuMain(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
    }



    @Override
    protected void createGuiElements(){
        createTextButtonFromZone(255,0,0, ButtonNames.CAMPAIGN);
        createTextButtonFromZone(0,255,0, ButtonNames.SINGLE_MISSIONS);
        createTextButtonFromZone(255,255,0, ButtonNames.LEVELS_EDITOR);
        createTextButtonFromZone(0,0,255, ButtonNames.OPTIONS);
        createTextButtonFromZone(255,125,0, ButtonNames.PRIVACY_POLICY);
        createTextButtonFromZone(255,0,255, ButtonNames.EXIT);
    }
    @Override
    public void update(MenuController menuController) {
        super.update(menuController);
        if (isGuiReleased(ButtonNames.CAMPAIGN)){
            menuController.setNextMenu(MenuType.CAMPAIGN);
        }
        else if (isGuiReleased(ButtonNames.SINGLE_MISSIONS)){
            menuController.setNextMenu(MenuType.SINGLE_MISSIONS);
        }
        else if (isGuiReleased(ButtonNames.LEVELS_EDITOR)){
            //menuController.setNextMenu(MenuType.EDITOR_LOADING);
            menuController.setNextMenu(MenuType.EDITOR_PRELOADING_WINDOW);
        }


    }




}
