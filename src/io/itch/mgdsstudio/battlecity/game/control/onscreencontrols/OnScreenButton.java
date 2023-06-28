package io.itch.mgdsstudio.battlecity.game.control.onscreencontrols;

import io.itch.mgdsstudio.battlecity.game.hud.Panel;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public abstract class OnScreenButton extends OnScreenControl{
    public OnScreenButton(Panel panel, IEngine engine, Coordinate pos, boolean form, int width, int height, float angle) {
        super(panel, engine, pos, form, width, height ,  angle);
    }
}
