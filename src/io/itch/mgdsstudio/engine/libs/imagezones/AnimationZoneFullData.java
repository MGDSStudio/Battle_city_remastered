package io.itch.mgdsstudio.engine.libs.imagezones;

public class AnimationZoneFullData extends ImageZoneFullData{
    private int alongX, alongY, spritesPerSecond, playingType, direction;
    public AnimationZoneFullData(ImageZoneSimpleData data, String path, String name, int alongX, int  alongY, int  spritesPerSecond, int  playingType, int  direction) {
        super(data, path, name);
        this.alongX = alongX;
        this.alongY = alongY;
        this.spritesPerSecond = spritesPerSecond;
        this.playingType = playingType;
        this.direction  =direction;
    }

    public int getAlongX() {
        return alongX;
    }

    public int getAlongY() {
        return alongY;
    }

    public int getSpritesPerSecond() {
        return spritesPerSecond;
    }

    public int getPlayingType() {
        return playingType;
    }

    public int getDirection() {
        return direction;
    }
}
