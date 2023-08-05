package io.itch.mgdsstudio.battlecity.android;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MultiplatformLauncher;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;

import java.util.ArrayList;

public class AndroidLauncher extends PApplet implements ContactListener, IEngine {
    private MultiplatformLauncher launcher;
    private int multiplayerMode;

    public AndroidLauncher(int multiplayerMode) {
        this.multiplayerMode = multiplayerMode;
    }

    public void settings() {
        String renderer;
        size(displayWidth, displayHeight, P2D);

        smooth(0);
    }

    public void setup() {
        launcher = new MultiplatformLauncher(this, multiplayerMode);
    }

    public void draw() {
        launcher.update();
        /*
        gameMainController.update();
        gameMainController.draw();

        try {
            readConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void beginContact(Contact contact) {
        launcher.beginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        launcher.endContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        launcher.preSolve(contact, manifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        launcher.postSolve(contact, contactImpulse);
    }

    @Override
    public PApplet getProcessing() {
        return this;
    }

    @Override
    public boolean isCircleAreaPressed(Coordinate pos, int diameter) {
        if (mousePressed){
            if (dist(pos.x, pos.y, mouseX, mouseY) < diameter/2) return true;

        }
        return false;
    }

    @Override
    public String getPathToObjectInAssets(String relativePath) {
        //String path = "app//src//main//Assets//"+relativePath;
        //System.out.println(path);
        return relativePath;
    }

    @Override
    public void fillTouchesArray(ArrayList<Coordinate> touchScreenPos) {
        System.out.println("Must be overriden!");
    }

    @Override
    public boolean isRectAreaPressed(Coordinate center, int width, int height) {
        if (mousePressed){
            if (mouseX> (center.x-width/2)){
                if (mouseX< (center.x+width/2)){
                    if (mouseY> (center.y-height/2)){
                        if (mouseY< (center.y+height/2)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getPathToSpriteInAssets(int spritesheetNumber) {
        if (spritesheetNumber == 0) return getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        else {
            Logger.error("We dont have implemented another sprites");
            return  getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        }
    }

    @Override
    public String getPathToObjectInUserFolder(String fileName) {
        Logger.correct("This function must get files from the internal storage");
        return getPathToObjectInAssets(fileName);
    }
}