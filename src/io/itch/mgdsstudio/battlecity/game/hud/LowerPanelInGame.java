package io.itch.mgdsstudio.battlecity.game.hud;

import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.AimingStick;
import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.MovementStick;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

public class LowerPanelInGame extends Panel{
    private MovementStick movementStick;
    private AimingStick aimingStick;

    public LowerPanelInGame(IEngine engine, InGameHud inGameHud, int height, Image image, PlayerTank playerTank) {
        super(engine, inGameHud, height, image, playerTank.getId());
        init();
    }

    @Override
    protected void init() {
        leftUpper = new Coordinate(0,engine.getEngine().height-height);
        center = new Coordinate(leftUpper.x+width/2, leftUpper.y+height/2);
        initSticks();
    }

    private void initSticks() {
        int additionalX = engine.getEngine().width/20;

        int diameter = height-engine.getEngine().width/40;
        if ((2*additionalX+diameter)*2>engine.getEngine().width){
            diameter = ((2*additionalX+diameter)*2);
        }
        Coordinate posLeft = new Coordinate(leftUpper.x+height/2 + additionalX, leftUpper.y+height/2);
        movementStick = new MovementStick(this, engine, posLeft, diameter, 0);

        Coordinate posRight = new Coordinate(engine.getEngine().width-posLeft.x, leftUpper.y+height/2);
        aimingStick = new AimingStick(this, engine, posRight, diameter, 0);
    }

    public void update(PlayerTank playerTank) {
        aimingStick.setAngle((int) playerTank.getTurretAbsoluteAngle());
        aimingStick.update(engine, playerTank);
        movementStick.setAngle((int) playerTank.getAngle());
        movementStick.update(engine, playerTank);

    }

    public void draw(PGraphics graphics){
        super.draw(graphics);
        movementStick.draw(graphics);
        aimingStick.draw(graphics);
    }


    public void setAngleForMovementStick(int angle) {
        movementStick.setAngle(angle);
    }

    public void setAngleForAimStick(int angle) {
        aimingStick.setAngle(angle);
    }
}
