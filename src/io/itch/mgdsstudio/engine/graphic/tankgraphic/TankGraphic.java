package io.itch.mgdsstudio.engine.graphic.tankgraphic;

import io.itch.mgdsstudio.battlecity.game.camera.Camera;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public class TankGraphic implements GraphicData{
    private Tank tank;
    private BodyGraphic bodyGraphic;
    private TrackGraphic tracksGraphic, rightTrackGraphic;
    private TurretGraphic turretGraphic;
    //private CannonGraphic cannonGraphic;
    //private TrackGraphic trackGraphic;
    private int graphicType;



    public TankGraphic(Tank tank, IEngine engine) {
        this.tank = tank;
        this.graphicType = tank.getType();
        tracksGraphic = new TrackGraphic(null, tank, engine, 1.7f, true);
        turretGraphic = new TurretGraphic(null, tank, engine, 1.7f, graphicType);
        bodyGraphic = new BodyGraphic(null, tank, engine, 1.7f, graphicType);
    }

    public void draw(PGraphics graphics, Camera gameCamera){
        startRender(graphics, gameCamera);
        update(graphics);
        tracksGraphic.draw(graphics, gameCamera);
        //rightTrackGraphic.draw(graphics, gameCamera);
        bodyGraphic.draw(graphics, gameCamera);
        turretGraphic.update(graphics);
        //cannonGraphic.update(graphics);
        //cannonGraphic.draw(graphics, gameCamera);
        turretGraphic.draw(graphics, gameCamera);
        endRender(graphics);
    }

    private void update(PGraphics graphics) {
        tracksGraphic.update(graphics);
        //rightTrackGraphic.update(graphics);

    }

    private void endRender(PGraphics graphics){
        graphics.popMatrix();
        graphics.popStyle();
    }

    private void startRender(PGraphics graphics, Camera gameCamera){
        graphics.pushMatrix();
        graphics.pushStyle();
        Vec2 vibrationPlace = tank.getTankController().getRelativeVibrationPos();
        graphics.translate(gameCamera.getDrawPosX(tank.getPos().x +  vibrationPlace.x), gameCamera.getDrawPosY(tank.getPos().y+ vibrationPlace.y));

        graphics.rotate(PApplet.radians(tank.getAngle()));
    }

    public void updateGraphicForActualStatement(int turretType, int chassisType) {
        if (tank instanceof PlayerTank) turretGraphic.setGraphic(GraphicData.getGraphicForPlayerTurret(turretType));
        else turretGraphic.setGraphic(GraphicData.getGraphicForEnemyTurret(turretType));
    }

}
