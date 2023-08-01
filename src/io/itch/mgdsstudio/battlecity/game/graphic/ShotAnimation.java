package io.itch.mgdsstudio.battlecity.game.graphic;

import io.itch.mgdsstudio.engine.graphic.AnimationInGame;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

public class ShotAnimation extends AnimationInGame {


    public ShotAnimation(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData, int alongX, int alongY, int firstInTheTable, int lastInTheTable, int imagesPerSecond, int direction, int actual, int playingCharacter, int keySprite) {
        super(image, width, height, imageZoneSimpleData, alongX, alongY, firstInTheTable, lastInTheTable, imagesPerSecond, direction, actual, playingCharacter, keySprite);
    }
}
