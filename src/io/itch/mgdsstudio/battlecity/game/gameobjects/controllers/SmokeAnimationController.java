package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;

import java.util.ArrayList;

public class SmokeAnimationController {
    private Entity tank;
    private ArrayList<SmokeAnimation> animations;

    public void update(long actualTime){

    }

    private class SmokeAnimation{
        private final int deltaAlpha = 150;
        private int actualAlpha;
        private float x,y;
        private final float startScale, endScale;

        public SmokeAnimation(int actualAlpha, float x, float y, float startScale, float endScale)
        {
            this.actualAlpha = actualAlpha;
            this.x = x;
            this.y = y;
            this.startScale = startScale;
            this.endScale = endScale;
        }

    }

}
