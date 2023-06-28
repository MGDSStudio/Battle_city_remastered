package io.itch.mgdsstudio.battlecity.datatransfer.listeners;

import io.itch.mgdsstudio.battlecity.datatransfer.data.LocalAction;

public interface LocalListener {
    void appendCommand(LocalAction action);
}
