package io.itch.mgdsstudio.battlecity.game.net;

import processing.data.IntList;

public interface ISerializeable {
    //char SERIALIZE_DATA_DIVIDER = ",";

    public IntList getSerializedIntData();
}
