package io.itch.mgdsstudio.editor;

import io.itch.mgdsstudio.battlecity.datatransfer.data.LocalAction;

public interface EditorActionsListener {

    void appendCommand(EditorAction action);
}
