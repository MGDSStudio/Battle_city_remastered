package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.dataloading.DataStringCreationMaster;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.dynamics.BodyType;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class Wall extends SolidObject {
    //protected GraphicElementInGame image;
    //public ImageInGame(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData) {
    protected Wall(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int bodyForm) {
        super(engine, physicWorld, pos, angle, 1, width, height, bodyForm, BodyType.STATIC, -1);

    }

    //BrickWall 7:600,520,0,40,40,3#2;
    public String getDataString(){
        int posX = (int) pos.x;
        int posY = (int) pos.y;
        ArrayList<Integer> dataList = new ArrayList<>();
        dataList.add(posX);
        dataList.add(posY);
        dataList.add(width);
        dataList.add(height);
        dataList.add(bodyForm);
        ArrayList<Integer> graphicList = new ArrayList<>();
        if (graphicData != null){
            for (int i = 0; i < graphicData.length; i++){
                graphicList.add(graphicData[i]);
            }
        }
        graphicList.add(1);
        DataStringCreationMaster dataStringCreationMaster = new DataStringCreationMaster(getId(), dataList, graphicList, this.getClass().getSimpleName());
        String dataString = dataStringCreationMaster.getDataString();
        return dataString;
    }

    public int getDyingAnimationType() {
        return IAnimations.DUST_SPLASH;
    }

    @Override
    public void draw(PGraphics graphics, Camera gameCamera) {
        if (graphicElementInGame != null) graphicElementInGame.drawWithTransformations(graphics, gameCamera, this);
        //if (graphicElementInGame != null) graphicElementInGame.draw(graphics, gameCamera, pos.x, pos.y);
    }


}
