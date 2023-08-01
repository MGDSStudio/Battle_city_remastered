package io.itch.mgdsstudio.engine.libs.imagezones;

import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

public class ImageZoneFullData {
    public final ImageZoneSimpleData data;
    public final String path;

    public ImageZoneFullData(ImageZoneSimpleData data, String path) {
        this.data = data;
        this.path = path;
    }
}
