package io.itch.mgdsstudio.battlecity.menu;

public class MenuDataStruct {

    public final static int NO_DATA = -9999;
    private MenuType nextMenuType;
    private int levelEndCode;

    public void setNextLevel(int nextLevel) {
        this.nextLevel = nextLevel;
    }

    private int nextLevel = NO_DATA;

    public MenuDataStruct(MenuType nextMenuType) {
        this.nextMenuType = nextMenuType;
    }

    public MenuDataStruct() {

    }

    public MenuType getNextMenuType() {
        return nextMenuType;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public void setNextMenu(MenuType menuType) {
        this.nextMenuType = menuType;
    }

    public void setLevelEndCode(int levelEndCode) {
        this.levelEndCode = levelEndCode;
    }

    public int getLevelEndCode() {
        return levelEndCode;
    }
}
