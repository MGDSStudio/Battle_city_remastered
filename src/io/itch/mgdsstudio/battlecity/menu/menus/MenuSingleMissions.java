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
import processing.core.PApplet;
import processing.core.PGraphics;

import java.io.File;
import java.util.ArrayList;

public class MenuSingleMissions extends Menu{

    private ArrayList <File> fullTextesInRenderZone;
    private final int maxButtonsAlongY = 7;
    private int actualPage = 1, pages = -1;

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
        fullTextesInRenderZone = singleMissionsLoadingMaster.getFiles();
        guiElements.remove(getGuiByName(ButtonNames.ZONE));
        createButtonsInZoneForActualPage();

    }

    private void createButtonsInZoneForActualPage() {
        removeAllExistingButtonsInZone();
        int zoneHeight = rectGuiZoneForCursorButtons.getH();
        int zoneWidth = rectGuiZoneForCursorButtons.getW();
        float relativeGapBetweenButtons = 0.6f; //from buttonHeight
        int buttonHeight = (int) (zoneHeight/(maxButtonsAlongY*(1+relativeGapBetweenButtons)+2));
        int firstButtonY = buttonHeight+rectGuiZoneForCursorButtons.getUp()+buttonHeight/2;
        int x = rectGuiZoneForCursorButtons.getCenterX();
        int buttonWidth = zoneWidth-4*buttonHeight;
        Logger.debug("Zone has dims: " + zoneWidth + "x" + zoneHeight + "; Button height: " + buttonHeight + "; Y: " + firstButtonY);

        int lastText = maxButtonsAlongY;
        if (fullTextesInRenderZone.size()<=maxButtonsAlongY){
            lastText = fullTextesInRenderZone.size();
            if (pages <= 0) pages = 1;
            hidePrevAndNextButtons();
            Logger.debug("There is only one page");
        }
        else {
            if (pages <= 0) pages = PApplet.floor(fullTextesInRenderZone.size()/maxButtonsAlongY)+1;
            updatePrevAndNextButtonsVisibility();
            //Logger.correct("Not implemented for more than 7 levels");
        }
        for (int i = 0; i < lastText; i++){
            int realNumber = getNumberInArrayInAccordingToActualPage(i);
            if (realNumber < fullTextesInRenderZone.size()) {
                int number = StringLibrary.getDigitFromString(fullTextesInRenderZone.get(realNumber).getName());
                String name = fullTextesInRenderZone.get(realNumber).getName();
                int y = (int) (firstButtonY + i * buttonHeight * (1 + relativeGapBetweenButtons));
                GuiElement button = new ButtonWithCursor(engine, x, y, buttonWidth, buttonHeight, name, graphics);
                ButtonsInZoneData userData = new ButtonsInZoneData(number);
                button.setUserData(userData);
                guiElements.add(button);
                Logger.debug("button " + i + " was created with param: " + x + "x" + y + "; WxH: " + buttonWidth + "x" + buttonHeight + " and name: " + name);
            }
        }
        setTextForPageNumbers();
    }

    private void updatePrevAndNextButtonsVisibility() {
        if (actualPage<=1) getGuiByName(ButtonNames.PREV).setActualStatement(GuiElement.BLOCKED);
        else getGuiByName(ButtonNames.PREV).setActualStatement(GuiElement.ACTIVE);
        if (actualPage >= pages) getGuiByName(ButtonNames.NEXT).setActualStatement(GuiElement.BLOCKED);
        else getGuiByName(ButtonNames.NEXT).setActualStatement(GuiElement.ACTIVE);
    }

    private void removeAllExistingButtonsInZone() {
        int count = 0;
            for (int i = (guiElements.size()-1); i >= 0; i--){
                if (guiElements.get(i).getUserData() != null){
                    Object obj = guiElements.get(i).getUserData();
                    if (obj instanceof ButtonsInZoneData){
                        guiElements.remove(guiElements.get(i));
                        count++;
                    }
                }
            }
            Logger.debug(count + " elements were deleted from gui array");
    }

    protected int getNumberInArrayInAccordingToActualPage(int i){
        return (actualPage-1)*maxButtonsAlongY+i;
    }

    private void setTextForPageNumbers() {
        GuiElement guiElement = getGuiByName(ButtonNames.PAGE_NUMBER);
        String text = "PAGE: " + actualPage + '/' + pages;
        guiElement.setAnotherTextToBeDrawnAsName(text);
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
        createTextLabelFromZone(255,125,0, ButtonNames.PAGE_NUMBER);
        createTextButtonFromZone(255,255,0, ButtonNames.ZONE);
        rectGuiZoneForCursorButtons = new RectGuiZone(getGuiByName(ButtonNames.ZONE));
    }



    @Override
    public void update(MenuController menuController) {
        super.update(menuController);
        if (isGuiReleased(ButtonNames.PREV)){
            transferToPrevPage();
        }
        else if (isGuiReleased(ButtonNames.NEXT)){
            transferToNextPage();
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

    protected void transferToPrevPage() {
        actualPage--;
        if (actualPage < 1) actualPage = 1;
        updateDataForActualPage();

    }

    private void updateDataForActualPage() {
        createButtonsInZoneForActualPage();
        setTextForPageNumbers();
    }

    protected void transferToNextPage() {
        actualPage++;
        if (actualPage > pages) actualPage = pages;
        updateDataForActualPage();
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


    private class ButtonsInZoneData{
        private int nextLevelNumber;

        public int getNextLevelNumber() {
            return nextLevelNumber;
        }

        public ButtonsInZoneData(int nextLevelNumber) {

            this.nextLevelNumber = nextLevelNumber;
        }
    }

}