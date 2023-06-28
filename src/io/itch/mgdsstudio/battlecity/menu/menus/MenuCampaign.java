package io.itch.mgdsstudio.battlecity.menu.menus;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import processing.core.PGraphics;

public class MenuCampaign extends Menu{

    private interface ButtonNames{
        String CONTINUE = "CONTINUE";
        String START_NEW_GAME = "START_NEW_GAME";
        String CUSTOMIZE = "CUSTOMIZE";
        String BACK = "BACK";
    }
    public MenuCampaign(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
    }



    @Override
    protected void createGuiElements(){
        createTextButtonFromZone(255,0,0, ButtonNames.CONTINUE);
        createTextButtonFromZone(0,255,0, ButtonNames.START_NEW_GAME);
        createTextButtonFromZone(255,255,0, ButtonNames.CUSTOMIZE);
        createTextButtonFromZone(0,0,255, ButtonNames.BACK);
    }

    @Override
    public void update(MenuController menuController) {
        super.update(menuController);
        if (isGuiReleased(ButtonNames.CONTINUE)){
           // menuController.setNextMenu(MenuType.SINGLE_MISSIONS);
        }
        else if (isGuiReleased(ButtonNames.START_NEW_GAME)){
            // menuController.setNextMenu(MenuType.SINGLE_MISSIONS);
        }
        else if (isGuiReleased(ButtonNames.CUSTOMIZE)){
            // menuController.setNextMenu(MenuType.SINGLE_MISSIONS);
        }
        else if (isGuiReleased(ButtonNames.BACK)){
            menuController.setNextMenu(MenuType.MAIN);
        }
    }




}
