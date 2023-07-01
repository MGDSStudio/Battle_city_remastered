package io.itch.mgdsstudio.battlecity.game.camera;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.hud.Hud;
import io.itch.mgdsstudio.battlecity.game.hud.InGameHud;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class GameCamera extends Camera{
    private World world;

    private Entity entity;

    private int magnetTo = Magneting.TO_COORDINATE;


    public GameCamera(IEngine engine) {
        pos = new Coordinate(0,0);
        magnetTo = Magneting.TO_COORDINATE;
        firstInit = true;
        this.engine = engine;
    }

    public GameCamera(IEngine engine, Coordinate pos) {
        this.pos = new Coordinate(pos.x,pos.y);
        magnetTo = Magneting.TO_COORDINATE;
        firstInit = true;
        this.engine = engine;
    }

    public GameCamera(IEngine engine, Entity entity, Hud inGameHud) {
        if (entity == null) {
            Logger.error("Trouble. Entity is null");
            pos = new Coordinate(0,0);
            magnetTo = Magneting.TO_COORDINATE;
        }
        else {
            pos = new Coordinate(entity.getPos().x, entity.getPos().y);
            this.entity = entity;
            magnetTo = Magneting.TO_ENTITY_WITHOUT_SPRING;
        }
        this.inGameHud = inGameHud;
        this.engine = engine;
    }


    public void update(){
        if (!firstInit){
            initGraphicCenter(inGameHud);
            firstInit = true;
        }
        else {
            if (magnetTo == Magneting.TO_ENTITY_WITHOUT_SPRING) {
                if (entity == null) {
                    magnetTo = Magneting.TO_COORDINATE;
                } else {
                    pos.x = entity.getPos().x;
                    pos.y = entity.getPos().y;
                }
            } else if (magnetTo == Magneting.TO_COORDINATE) {
                //do nothing
            }
        }
    }

    @Override
    public void shiftCameraPos(Vec2 distance) {
        pos.x+=distance.x;
        pos.y+=distance.y;
    }

    public void resetEntity(){
        entity = null;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
