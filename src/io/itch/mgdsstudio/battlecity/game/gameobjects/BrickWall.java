package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.imagezones.SingleImageZoneFromFileLoader;

public class BrickWall extends Wall{


    //BrickWall 5:366,118,0,16,16,0#
    private BrickWall(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int form){
        super(engine, physicWorld, pos, angle, width, height, form);

    }
    public static BrickWall create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        int[] graphicData = entityData.getGraphicData();
        BrickWall wall = new BrickWall (engine, physicWorld, pos, values[2], values[3], values[4], values[5]);
        wall.loadGraphicDefaultData(engine,graphicData);
        wall.setId(entityData.getId());
        return wall;
    }

    private void loadGraphicDefaultData(IEngine engine, int [] graphicData){
        this.graphicData = graphicData;
        SingleImageZoneFromFileLoader loader = new SingleImageZoneFromFileLoader(engine, graphicData);
        final ImageZoneSimpleData data = loader.getData();
        final String pathToTileset = loader.getPath();
        loadImage(engine, pathToTileset, width, height, data);
    }

    @Override
    public boolean attackBy(Entity attackingObject, GameRound gameRound){
         return KILLED;
    }



}
