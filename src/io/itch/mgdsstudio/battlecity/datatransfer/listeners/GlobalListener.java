package io.itch.mgdsstudio.battlecity.datatransfer.listeners;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;

public interface GlobalListener {
    //void appendData(TransferActionData data);

    //void movementStickRingZoneInteracted(int statement);

    //void movementStickCenterZoneInteracted(int statement);

    void appendCommand(GLobalSerialAction action);
}
