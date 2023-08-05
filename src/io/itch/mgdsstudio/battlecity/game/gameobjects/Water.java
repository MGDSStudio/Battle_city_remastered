package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.AnimationInGame;
import io.itch.mgdsstudio.engine.graphic.GraphicManagerSingleton;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.dynamics.BodyType;

public class Water extends SolidObject {
    public final static int NORMAL_GRAPHIC = 0;
    public final static int LAVA = 1;

    private final static int NORMAL_ANIMATION_FREQUENCY = 10; //Sprites per second
    private final ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(832, 0, 832+64, 3*64);

    //super(physicGameWorld, pos, 0, IMMORTAL_LIFE, getWidthForEntity(type), getHeightForEntity(type), RECT, BodyType.STATIC, -1);
    public Water(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int width, int height, int type){
        super(engine, physicWorld, pos, angle, IMMORTAL_LIFE, width, height, BodyForms.RECT, BodyType.STATIC, -1);
        loadGraphicDefaultData(engine);
    }

    private void loadGraphicDefaultData(IEngine engine) {
        int alongX = 1;
        int alongY = 3;
        int first = 0;
        int last = 2;
        int imagesPerSecond = NORMAL_ANIMATION_FREQUENCY;
        int direction = 1;
        int actual = 0;
        int repeateability = AnimationInGame.PLAY_ALWAYS;
        Image image = GraphicManagerSingleton.getManager(engine.getProcessing()).getImage(engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE));
        graphicElementInGame = new AnimationInGame(image, width, height, imageZoneSimpleData, alongX, alongY, first, last, imagesPerSecond, direction, actual, repeateability, -1);
    }

    @Override
    protected void setBodyData() {
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_WATER_OR_COLLECTABLE);
    }

    public static Water create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        Water wall = new Water (engine, physicWorld, pos, values[2], values[3], values[4], values[5]);
        wall.setId(entityData.getId());
        return wall;
    }
}
