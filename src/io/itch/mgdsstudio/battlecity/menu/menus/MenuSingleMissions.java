package io.itch.mgdsstudio.battlecity.menu.menus;

import com.mgdsstudio.engine.nesgui.ButtonWithCursor;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import io.itch.mgdsstudio.battlecity.menu.menudataencryption.RectGuiZone;
import io.itch.mgdsstudio.battlecity.menu.menus.loading.SingleMissionsLoadingMaster;
import io.itch.mgdsstudio.engine.libs.StringLibrary;
import processing.core.PGraphics;
import sun.rmi.runtime.Log;

import java.io.File;
import java.util.ArrayList;

public class MenuSingleMissions extends Menu{

    protected RectGuiZone rectGuiZoneForCursorButtons;

    private interface ButtonNames{
        String PREV = "PREV";
        String NEXT = "NEXT";
        String PAGE_NUMBER = "1/1";
        String BACK = "BACK";
        String ZONE = "ZONE";
    }


    public MenuSingleMissions(IEngine engine, PGraphics graphics, MenuType menuType, MenuDataStruct bundle) {
        super(menuType, graphics, engine);
        loadFilesList();
    }

    private void loadFilesList() {
        SingleMissionsLoadingMaster singleMissionsLoadingMaster = new SingleMissionsLoadingMaster(engine, engine.getPathToObjectInUserFolder(""));
        ArrayList <File> userLevels = singleMissionsLoadingMaster.getFiles();
        createButtonsInZone(userLevels);

    }

    private void createButtonsInZone(ArrayList <File> userLevels) {
        guiElements.remove(getGuiByName(ButtonNames.ZONE));
        int zoneHeight = rectGuiZoneForCursorButtons.getH();
        int zoneWidth = rectGuiZoneForCursorButtons.getW();
        float relativeGapBetweenButtons = 0.6f; //from buttonHeight
        float maxButtonsAlongY = 7;
        int buttonHeight = (int) (zoneHeight/(maxButtonsAlongY*(1+relativeGapBetweenButtons)+2));
        int firstButtonY = buttonHeight+rectGuiZoneForCursorButtons.getUp()+buttonHeight/2;
        int x = rectGuiZoneForCursorButtons.getCenterX();
        int buttonWidth = zoneWidth-4*buttonHeight;
        Logger.debug("Zone has dims: " + zoneWidth + "x" + zoneHeight + "; Button height: " + buttonHeight + "; Y: " + firstButtonY);
        if (userLevels.size()<=7){
            for (int i = 0; i < userLevels.size(); i++){
                int number = StringLibrary.getDigitFromString(userLevels.get(i).getName());
                String name = userLevels.get(i).getName();
                int y = (int) (firstButtonY+i*buttonHeight*(1+relativeGapBetweenButtons));
                GuiElement button = new ButtonWithCursor(engine, x, y, buttonWidth, buttonHeight, name, graphics);
                button.setUserData(new Integer(number));
                guiElements.add(button);
                Logger.debug("button " + i +  " was created with param: " + x + "x" + y + "; WxH: " + buttonWidth + "x" + buttonHeight + " and name: " + name);
            }
            hidePrevAndNextButtons();
        }
        else {
            Logger.correct("Not implemented for more than 7 levels");
        }
    }

    protected void hidePrevAndNextButtons() {
        getGuiByName(ButtonNames.PREV).setActualStatement(GuiElement.BLOCKED);
        getGuiByName(ButtonNames.NEXT).setActualStatement(GuiElement.BLOCKED);
    }


    @Override
    protected void createGuiElements(){
        createTextButtonFromZone(255,0,0, ButtonNames.BACK);
        createButtonInFrameFromZone(0,255,0, ButtonNames.PREV);
        createButtonInFrameFromZone(0,0,255, ButtonNames.NEXT);
        createTextButtonFromZone(255,125,0, ButtonNames.PAGE_NUMBER);
        createTextButtonFromZone(255,255,0, ButtonNames.ZONE);
        rectGuiZoneForCursorButtons = new RectGuiZone(getGuiByName(ButtonNames.ZONE));
    }



    @Override
    public void update(MenuController menuController) {
        super.update(menuController);
        if (isGuiReleased(ButtonNames.PREV)){
            // menuController.setNextMenu(MenuType.SINGLE_MISSIONS);
        }
        else if (isGuiReleased(ButtonNames.NEXT)){
            // menuController.setNextMenu(MenuType.SINGLE_MISSIONS);
        }
        else if (isGuiReleased(ButtonNames.BACK)){
            menuController.setNextMenu(MenuType.MAIN);

        }
        else {
            GuiElement guiElement = getReleasedButton();
            if (guiElement != null){
                int levelNumber = getLevelNumberFromGui(guiElement);
                if (levelNumber != GlobalConstants.ERROR_CODE) {
                    menuController.getBundle().setNextLevel(levelNumber);
                    menuController.setNextMenu(MenuType.SINGLE_MISSION_LOADING);
                }

            }
        }
    }

    private int getLevelNumberFromGui(GuiElement guiElement) {
        try {
            int number = (Integer) guiElement.getUserData();
            return number;
        }
        catch (Exception e){
            Logger.error("Can not get data in gui " + guiElement.getName());
            return GlobalConstants.ERROR_CODE;
        }


    }

    protected GuiElement getReleasedButton() {
        for (GuiElement guiElement : guiElements){
            if (guiElement.getActualStatement() == GuiElement.RELEASED){
                Logger.debug("Button " + guiElement.getName() + " was released");
                return guiElement;
            }
        }
        return null;
    }



}