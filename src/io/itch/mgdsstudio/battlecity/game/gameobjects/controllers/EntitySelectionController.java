package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import processing.core.PApplet;
import processing.core.PConstants;

public class EntitySelectionController {
   // private ISelectable selectable;
    private boolean selected;
    private int startSelectionTime;
    private int max, min, fullVisibleAlpha;
    private float coef;
    private float alpha;

    public EntitySelectionController(int min, int max, int period) {
        this.min = min;
        this.max = max;
        this.fullVisibleAlpha = 255;
        coef = PConstants.TWO_PI / period;
    }

    public EntitySelectionController() {
        this.min = 125;
        this.max = 255;
        this.fullVisibleAlpha = 255;
        coef = PConstants.TWO_PI / 2000;
    }

    public void setSelected(int millis){
        startSelectionTime  = millis;
        selected = true;
    }

    public void clearSelection(){
        selected = false;
    }

    public int getAlpha(int millis){
        if (selected) {
            alpha = (min + ((max - min) * PApplet.sin((millis-startSelectionTime)* coef)));
            if (alpha < min) alpha = min;
            return (int) alpha;
        }
        else return fullVisibleAlpha;
    }

    public boolean isSelected() {
        return selected;
    }
}
