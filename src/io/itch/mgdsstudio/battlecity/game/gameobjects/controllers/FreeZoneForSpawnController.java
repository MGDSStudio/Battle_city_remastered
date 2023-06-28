package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Bullet;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.GameMechanics;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;

import java.util.ArrayList;

class FreeZoneForSpawnController {
    private final Coordinate pos;
    private Timer timer;
    private boolean activated;
    private final int timeToActivate;
    private boolean readyForSpawn;

    //private final EnemiesSpawnController enemiesSpawnController;

    FreeZoneForSpawnController(Coordinate pos, int timeToActivate) {
        this.pos = pos;
        this.timeToActivate = timeToActivate;
        //this.enemiesSpawnController = enemiesSpawnController;
    }

    void update(GameRound gameRound){
        boolean zoneFree = isZoneFreeFromEntities(pos, gameRound);
        if (zoneFree){
            if (activated) {
                // All right. wait for timer ends
                //if (timer.isSwitchedOn()){
                    if (timer.isTime()){
                        readyForSpawn = true;
                    }
                //}
            }
            else {
                activate(gameRound.getEngine().getEngine());
            }
        }
        else {
            if (activated){
                deactivate();
            }
            else {
                //All right. wait for zone will be free
            }
        }
    }

    private void activate(PApplet pApplet) {
        activated = true;
        readyForSpawn = false;
        if (timer == null) timer = new Timer(timeToActivate, pApplet);
        else timer.setNewTimer(timeToActivate);
    }

    void deactivate() {
        if (activated) {
            activated = false;
            readyForSpawn = false;
            timer.stop();
        }
    }

    private boolean isZoneFreeFromEntities(Coordinate coordinate, GameRound gameRound) {
        boolean free = true;
        ArrayList<Entity> entities = gameRound.getEntities();
        for (Entity entity : entities){
            if (entity.getClass() != Bullet.class){
                if (GameMechanics.isIntersectionBetweenAllignedRects(coordinate.x, coordinate.y, entity.getPos().x, entity.getPos().y, entity.getWidth(), entity.getHeight(), entity.getWidth(), entity.getHeight())){
                    //free = false;
                    return false;
                }
            }
        }
        return free;
    }

    Coordinate getPos() {
        return pos;
    }

    boolean isReadyForSpawn() {
        return readyForSpawn;
    }


}
