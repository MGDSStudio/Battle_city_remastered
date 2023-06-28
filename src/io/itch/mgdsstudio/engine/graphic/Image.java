package io.itch.mgdsstudio.engine.graphic;

import io.itch.mgdsstudio.engine.texturepacker.TextureDecodeManager;
import io.itch.mgdsstudio.engine.texturepacker.TexturePacker;
import processing.core.PApplet;
import processing.core.PImage;

public class Image {
    protected String path;
    private PImage image;
    private PApplet engine;


    public Image(PApplet engine, String path) {
        this.path = path;
        this.engine = engine;
        if (isMgdsTexture()) {
            loadMgdsImage(engine);
        }
        else image = engine.loadImage(path);
    }



    public Image(String path, PImage anotherPImage) {
        this.path = path;
        image = anotherPImage;
        //image.
    }



    public Image(Image anotherImage) {
        this.image = anotherImage.image;
    }

    public void loadNewImage (String path) {
        this.path = path;
        if (isMgdsTexture()) {
            //System.out.println("This image " + path + " is a mgds");
            loadMgdsImage(engine);
        }
        else image = engine.loadImage(path);
        //image = Program.engine.loadImage(path);
    }


    private void loadMgdsImage(PApplet engine){
        TextureDecodeManager decodeManager = new TextureDecodeManager(path, engine);
        image = decodeManager.getDecodedImage();
    }

    public void setNewPictureFromAnotherImage (Image image) {
        this.image = image.getImage();
    }

    public PImage getImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    private boolean isMgdsTexture(){
        if (path.contains(TexturePacker.MGDS_FILE_EXTENSION)){
            return true;
        }
        else return false;
    }

    public PApplet getEngine() {
        return engine;
    }
}
