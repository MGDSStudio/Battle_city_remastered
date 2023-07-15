package io.itch.mgdsstudio.battlecity.game.hud;

import com.mgdsstudio.engine.nesgui.EightPartsFrameImage;
import io.itch.mgdsstudio.battlecity.game.InEditorGraphicData;
import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.AimingStick;
import io.itch.mgdsstudio.battlecity.game.control.onscreencontrols.MovementStick;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PGraphics;

import java.awt.*;

public class LowerPanelInEditor extends Panel{
    private InEditorLowerTab lowerTab;
    private final EightPartsFrameImage inEditorGameWorldFrame;
    //private final Rectangle worldZone;
    public LowerPanelInEditor(IEngine engine, Hud inGameHud, int height, Image image, PlayerTank playerTank, EightPartsFrameImage inEditorGameWorldFrame) {
        super(engine, inGameHud, height, image, playerTank.getId());
        this.inEditorGameWorldFrame = inEditorGameWorldFrame;
        init();
    }

    @Override
    protected void init() {
        leftUpper = new Coordinate(0,engine.getEngine().height-height);
        center = new Coordinate(leftUpper.x+width/2, leftUpper.y+height/2);
        lowerTab = new InEditorLowerTab(engine, inEditorGameWorldFrame, (int) InEditorGraphicData.rightBoardLineWidth);

    }



    public void update(PlayerTank playerTank) {

    }

    public void draw(PGraphics graphics){
        super.draw(graphics);
        lowerTab.draw(graphics);
    }


}
