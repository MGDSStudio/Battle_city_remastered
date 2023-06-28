package io.itch.mgdsstudio.battlecity.menu;

import io.itch.mgdsstudio.battlecity.game.GamePart;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import io.itch.mgdsstudio.battlecity.menu.menus.Menu;
import processing.core.PGraphics;

public class MenuController extends GamePart {
    private Menu menu;
    //private final static int GRAPHIC_WIDTH = 1080;
    private static int graphicHeight = -1;
    private final PGraphics graphics;
    private MenuType actualMenuType, nextMenuType;



    private MenuDataStruct bundle;

    public MenuController(IEngine engine, MainController mainController, MenuDataStruct bundle) {
        super(engine, mainController);
        if (bundle != null){
            actualMenuType = bundle.getNextMenuType();
            this.bundle = bundle;
        }
        else {
            actualMenuType = MenuType.SINGLE_MISSION_LOADING;
            this.bundle = new MenuDataStruct();
        }
        nextMenuType = actualMenuType;
        if (graphicHeight < 0){

        }
        graphics = engine.getEngine().createGraphics(engine.getEngine().width, engine.getEngine().height, GlobalVariables.getRenererAsString());
        graphics.noSmooth();
        changeMenu(nextMenuType);
    }


    @Override
    public void update() {
        if (nextMenuType != actualMenuType){
            changeMenu(nextMenuType);
        }
        else menu.update(this);
    }

    private void changeMenu(MenuType nextMenuType) {
        menu = Menu.create(nextMenuType, engine, graphics, bundle);
        actualMenuType = nextMenuType;
    }

    @Override
    public void draw() {
        graphics.beginDraw();
        menu.draw(graphics);
        graphics.endDraw();
        engine.getEngine().pushMatrix();
        engine.getEngine().translate(engine.getEngine().width/2,engine.getEngine().height/2);
        engine.getEngine().image(graphics,0,0,engine.getEngine().width, engine.getEngine().height);
        engine.getEngine().popMatrix();
    }

    public void transferToGame(MenuDataStruct menuDataStruct) {
        mainController.startGame(menuDataStruct.getNextLevel());
    }

    public void setNextMenu(MenuType nextMenu) {
        nextMenuType = nextMenu;
    }


    public MenuDataStruct getBundle() {
        return bundle;
    }

    public void transferToEditor(MenuDataStruct menuDataStruct) {
        mainController.startEditor(menuDataStruct.getNextLevel());
    }

    @Override
    public void backPressed() {
        menu.backPressed();
    }
}
