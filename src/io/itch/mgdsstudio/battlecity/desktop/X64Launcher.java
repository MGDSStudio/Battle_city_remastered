package io.itch.mgdsstudio.battlecity.desktop;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MultiplatformLauncher;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;

import java.util.ArrayList;

public class X64Launcher extends PApplet implements ContactListener , IEngine {
    private MultiplatformLauncher launcher;
    private static int multiplayerMode;

    public static void main(String[] passedArgs) {
        System.out.println("Desktop version loaded successfully");
        String[] appletArgs = new String[] { "io.itch.mgdsstudio.battlecity.desktop.X64Launcher" };
        System.out.print("Args: ");
        for (int i = 0; i < passedArgs.length; i++){
            System.out.println(":" + passedArgs[i]);
        }
        if (passedArgs.length>0){
            multiplayerMode = Integer.parseInt(passedArgs[0]);
        }
        if (multiplayerMode == GlobalConstants.PLAYER_AS_SERVER) System.out.println("This user is a server");
        else if (multiplayerMode > GlobalConstants.PLAYER_AS_SERVER) System.out.println("This user is a user with the number: " + multiplayerMode + " in multiplayer mode");
        else if (multiplayerMode == GlobalConstants.PLAYER_IN_SINGLEPLAYER_MODE) System.out.println("This user is a user with the number: " + multiplayerMode + " in singleplayer mode");
        if (passedArgs != null) {
            try {
                PApplet.main(PApplet.concat(appletArgs, passedArgs));
            }
            catch (Exception e){
                e.printStackTrace();
                PApplet.main(appletArgs);
            }
        }
        else {
            PApplet.main(appletArgs);
        }
    }

    public void settings() {
        size(600,  900, P2D);
        //size(480,  1080, P2D);
        //size(480,  1080, P2D);
        smooth(0);
    }

    public void setup() {
        if (multiplayerMode == 0) surface.setLocation(480, 140);
        else  surface.setLocation(480+490, 140);
        launcher = new MultiplatformLauncher(this, multiplayerMode);
    }

    public void draw() {
        launcher.update();
    }

    @Override
    public void keyPressed(){
        if (key == 'H' || key == 'h' || key == 'Р' || key == 'р'){
            launcher.backPressed();
        }
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
    public PApplet getEngine() {
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
        String path = "app//src//main//Assets//"+relativePath;
        return path;
    }

    @Override
    public void fillTouchesArray(ArrayList<Coordinate> touchScreenPos) {
        touchScreenPos.get(0).x = mouseX;
        touchScreenPos.get(0).y = mouseY;
        if (touchScreenPos.size()>1) {
            for (int i = (touchScreenPos.size()-1); i>= 0; i--){
                touchScreenPos.remove(i);
            }
        }
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
        return getPathToObjectInAssets(fileName);
    }
}