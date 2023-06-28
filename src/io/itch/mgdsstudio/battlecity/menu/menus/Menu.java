package io.itch.mgdsstudio.battlecity.menu.menus;

import com.mgdsstudio.engine.nesgui.*;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.menu.MenuController;
import io.itch.mgdsstudio.battlecity.menu.MenuDataStruct;
import io.itch.mgdsstudio.battlecity.menu.MenuType;
import io.itch.mgdsstudio.battlecity.menu.menudataencryption.MenuGuiDataEncryptor;
import io.itch.mgdsstudio.battlecity.menu.menudataencryption.RectGuiZone;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.StringLibrary;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class Menu  {

    protected ArrayList <GuiElement> guiElements;
    protected int upperPixel, lowerPixel;
    protected MenuType menuType;
    protected IEngine engine;
    protected PGraphics graphics;
    protected Image backgroundImage, maskImage;
    protected float widthScale;

    private final static String GRAPHIC_EXTENSION = ".png";
    private MenuGuiDataEncryptor daraEncryptor;
    protected boolean firstFrameEnded;

    protected Menu(MenuType menuType, PGraphics graphics, IEngine engine) {
        this.menuType = menuType;
        this.engine = engine;
        this.graphics = graphics;
        loadBackgroundImage();
        widthScale = (float)engine.getEngine().width/ backgroundImage.getImage().width;
        Logger.debug("Graphic relative scale is: " + widthScale);
        guiElements = new ArrayList<>();
        //placeGui(daraEncryptor);
        removeMask();
        createGuiElements();
    }

    private void removeMask() {

    }

    protected void createGuiElements(){
        Logger.error("Gui elements are not created for actual menu " + menuType);
    }



    public static Menu create(MenuType menuType, IEngine engine, PGraphics graphics, MenuDataStruct bundle){
        if (menuType == MenuType.SINGLE_MISSION_COMPLETED) return new MenuSingleMissionCompleted(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.SINGLE_MISSION_LOADING) return new MenuSingleMissionLoading(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.SINGLE_MISSION_LOOSED) return new MenuSingleMissionLoosed(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.SINGLE_MISSIONS) return new MenuSingleMissions(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.CAMPAIGN) return new MenuCampaign(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.MAIN) return new MenuMain(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.EDITOR_LOADING) return new MenuEditorLoading(engine, graphics, menuType, bundle);
        else if (menuType == MenuType.EDITOR_PRELOADING_WINDOW) return new MenuEditorPreloading(engine, graphics, menuType, bundle);
        else {
            Logger.error("No implementation for menu: " + menuType.name());
            return new MenuSingleMissionCompleted(engine, graphics, menuType, bundle);
        }
    }

    static String getPathToMainImage(IEngine engine, Menu menu){
        String name = menu.getClass().toString();
        Logger.debug("Path to background file: " + name);
        name = engine.getPathToObjectInAssets(StringLibrary.getClassNameFromFullName(name)+GRAPHIC_EXTENSION);
        return name;
    }

    private void loadBackgroundImage(){
        backgroundImage = new Image(engine.getEngine(),(getPathToMainImage(engine, this)));
        maskImage = new Image(engine.getEngine(),(getPathToMaskImage(engine, this)));
        float relationship = GlobalVariables.sidesRelationshipHeightToWidth;
        Logger.debug("Sizes relationship for this display: " + relationship);
        int visibleImagePartHeight;
        visibleImagePartHeight = (int)(relationship* backgroundImage.getImage().width);
        upperPixel = (backgroundImage.getImage().height/2)-visibleImagePartHeight/2;
        lowerPixel = (backgroundImage.getImage().height/2)+visibleImagePartHeight/2;
    }

    private String getPathToMaskImage(IEngine engine, Menu menu) {
        final String maskSuffix = "_mask";
        String name = menu.getClass().toString();
        name = engine.getPathToObjectInAssets(StringLibrary.getClassNameFromFullName(name)+maskSuffix+GRAPHIC_EXTENSION);
        return name;
    }

    public void update(MenuController menuController) {
        if (!firstFrameEnded){
            firstFrameEnded = true;
        }
        for (int i = 0; i < guiElements.size(); i++){
            guiElements.get(i).update(engine.getEngine().mouseX, engine.getEngine().mouseY);
        }
    }

    public void draw(PGraphics graphics) {

        graphics.image(backgroundImage.getImage(),0,0, graphics.width, graphics.height, 0, upperPixel, backgroundImage.getImage().width, lowerPixel);
        for (int i = 0; i < guiElements.size(); i++){
            guiElements.get(i).draw(graphics);
        }
    }

    public void dispose(){

    }

    protected GuiElement createTextButtonFromZone(int r, int g, int b, String name){
        if (daraEncryptor == null){
            daraEncryptor = new MenuGuiDataEncryptor(engine.getEngine(), maskImage.getImage());
        }
        RectGuiZone campaignButtonZone =  daraEncryptor.getZone(r,g,b);
        int x = campaignButtonZone.getCenterX();
        int y = campaignButtonZone.getCenterY();
        int w = campaignButtonZone.getW();
        int h = campaignButtonZone.getH();
        NoTextCursorButton guiElement = new NoTextCursorButton(engine, x, y, w, h, name, graphics);
        guiElements.add(guiElement);
        return guiElement;
    }

    protected GuiElement createTextLabelFromZone(int r, int g, int b, String name){
        if (daraEncryptor == null){
            daraEncryptor = new MenuGuiDataEncryptor(engine.getEngine(), maskImage.getImage());
        }
        RectGuiZone campaignButtonZone =  daraEncryptor.getZone(r,g,b);
        int x = campaignButtonZone.getCenterX();
        int y = campaignButtonZone.getCenterY();
        int w = campaignButtonZone.getW();
        int h = campaignButtonZone.getH();
        TextLabel guiElement = new TextLabel(engine, x, y, w, h, name, graphics);
        guiElements.add(guiElement);
        return guiElement;
    }

    protected GuiElement createButtonInFrameFromZone(int r, int g, int b, String name){
        if (daraEncryptor == null){
            daraEncryptor = new MenuGuiDataEncryptor(engine.getEngine(), maskImage.getImage());
        }
        RectGuiZone campaignButtonZone =  daraEncryptor.getZone(r,g,b);
        int x = campaignButtonZone.getCenterX();
        int y = campaignButtonZone.getCenterY();
        int w = campaignButtonZone.getW();
        int h = campaignButtonZone.getH();
        //IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics
        NoTextButtonWithFrameSelection guiElement = new NoTextButtonWithFrameSelection(engine, x, y, w, h, name, graphics);
        guiElements.add(guiElement);
        return guiElement;
    }

    protected boolean isGuiReleased(String name) {
        for (GuiElement guiElement : guiElements){
            if (guiElement.getActualStatement() == GuiElement.RELEASED){
                if (guiElement.getName() == name || guiElement.getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    protected GuiElement getGuiByName(String name) {
        for (GuiElement guiElement : guiElements){
                if (guiElement.getName() == name || guiElement.getName().equals(name)){
                    return guiElement;
                }

        }
        Logger.error("Can not get GUI with name: " + name);
        return null;
    }

    public void backPressed() {
        Logger.correct("Back pressed is not implemented");
    }
}
