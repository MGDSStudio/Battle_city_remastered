package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.GraphicManagerSingleton;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

import java.util.ArrayList;

abstract class GraphicPart implements GraphicData{
    protected Coordinate offset;
    protected GraphicPart parent;
    protected ImageZoneSimpleData imageZoneSimpleData;
    protected ArrayList <GraphicPart> children;
    protected float relativeGraphicScale = 1f;
    protected Tank tank;
    private Image image;
    protected int width, height;

    GraphicPart(GraphicPart parent, Tank tank, IEngine engine, float relativeGraphicScale) {
        this.parent = parent;
        this.tank = tank;
        this.relativeGraphicScale = relativeGraphicScale;
        GraphicManagerSingleton graphicManagerSingleton = GraphicManagerSingleton.getManager(engine.getEngine());
        image = graphicManagerSingleton.getImage(engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE));
        this.width = (int) (tank.getWidth()*relativeGraphicScale);
        this.height = (int) (tank.getHeight()*relativeGraphicScale);
        offset = new Coordinate(0,0);
    }

    public Coordinate getOffset() {
        return offset;
    }

    public void appendChild(GraphicPart graphicPart){
        if (children == null){
            children = new ArrayList<>();
        }
        children.add(graphicPart);
    }

    void draw(PGraphics graphics, GameCamera gameCamera){
        //graphics.image(image.getImage(), 0,0,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
        graphics.image(image.getImage(), offset.x,offset.y,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
    }


    protected void update(PGraphics graphics){}



    public void setGraphic(ImageZoneSimpleData graphicForPlayerTurret) {
        this.imageZoneSimpleData = graphicForPlayerTurret;
    }
}
