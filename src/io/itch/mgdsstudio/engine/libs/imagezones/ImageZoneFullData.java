package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

public class ImageZoneFullData {
    protected final ImageZoneSimpleData data;
    protected final String path;
    protected final String name;


    public ImageZoneFullData(ImageZoneSimpleData data, String path, String name) {
        this.data = data;
        this.path = path;
        this.name = name;
    }

    public ImageZoneSimpleData getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
