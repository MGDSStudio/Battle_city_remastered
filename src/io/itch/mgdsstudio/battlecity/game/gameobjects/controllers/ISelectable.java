package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

public interface ISelectable {
    boolean isSelected();
    void setSelected(boolean selected);
    String getInEditorName();

    String getDataString();
}
