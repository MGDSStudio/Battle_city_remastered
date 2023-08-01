package io.itch.mgdsstudio.engine.libs;

public class ImageZonesLoadingSingleton {
    private static ImageZonesLoadingSingleton imageZonesLoadingSingleton;

    private ImageZonesLoadingSingleton(){

    }

    public static ImageZonesLoadingSingleton getInstance() {
        return imageZonesLoadingSingleton;
    }
}
