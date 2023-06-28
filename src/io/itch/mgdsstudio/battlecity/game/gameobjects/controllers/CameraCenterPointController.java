package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import org.jbox2d.common.Vec2;

public class CameraCenterPointController {
    public static final boolean ON_OBJECT = true;
    public static final boolean ON_CROSSHAIR = false;

    private final Entity player;
    private final GameCamera camera;
    private boolean concentrationType;
    private int distanceToCrosshair;
    //private final Vec2 mutBodyCrosshairPos = new Vec2();
    //private final Vec2 mutTurretCrosshairPos = new Vec2();
    private final Vec2 concentrationPoint = new Vec2();

    public CameraCenterPointController(Entity player, GameCamera camera)
    {
        this.player = player;
        this.camera = camera;
    }

    public void update(float bodyAngle, float turretAngle){
        if (concentrationType == ON_OBJECT){
            camera.setPos(player.getPos().x, player.getPos().y);
        }
        else{
            updateConcentrationPoint(bodyAngle, turretAngle);
        }
    }

    private void updateConcentrationPoint(float bodyAngle, float turretAngle){
        float xB = distanceToCrosshair*(float)Math.cos(Math.toRadians(bodyAngle));
        float yB = distanceToCrosshair*(float)Math.sin(Math.toRadians(bodyAngle));

        float xT = distanceToCrosshair*(float)Math.cos(Math.toRadians(turretAngle));
        float yT = distanceToCrosshair*(float)Math.sin(Math.toRadians(turretAngle));

        concentrationPoint.x = (xB+xT)/2;
        concentrationPoint.y = (yB+yT)/2;
        concentrationPoint.x+=player.getPos().x;
        concentrationPoint.y+=player.getPos().y;
        camera.setPos(concentrationPoint.x, concentrationPoint.y);
    }

}
