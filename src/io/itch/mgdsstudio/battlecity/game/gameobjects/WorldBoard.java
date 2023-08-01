package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;

public class WorldBoard extends Wall{

    public WorldBoard(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int form){
        super(engine, physicWorld, pos, angle, width, height, form);
        loadGraphicDefaultData(engine);

    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_LEVEL_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(192,64, 192+64,64+64);
        loadImage(engine, path, width, height, data);
    }

    public static WorldBoard create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        WorldBoard wall = new WorldBoard(engine, physicWorld, pos, values[2], values[3], values[4], values [5]);
        wall.setId(entityData.getId());
        return wall;
    }

    @Override
    public boolean attackBy(Entity obj, GameRound gameRound){
        return !KILLED;
    }
}
