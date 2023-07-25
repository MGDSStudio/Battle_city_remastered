package io.itch.mgdsstudio.battlecity.editor;

public interface ISelectable {
    boolean isSelected();
    void setSelected(boolean selected);
    String getInEditorName();

    String getDataString();
}
