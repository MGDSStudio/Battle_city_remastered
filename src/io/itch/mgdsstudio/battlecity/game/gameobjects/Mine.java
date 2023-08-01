package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.camera.GameCamera;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.dataloading.EntityData;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.game.textes.AbstractText;
import io.itch.mgdsstudio.battlecity.game.textes.DissolvingAndUpwardsMovingText;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.*;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.Timer;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class Mine extends SolidObject {
    private final static ImageZoneSimpleData REST_AFTER_EXPLOSION_ZONE_DATA = new ImageZoneSimpleData(768,64, 768+64,64+64);
    public final static boolean PLAYER = true;
    private ArrayList <Entity> tanks = new ArrayList<>();
    private boolean owner;
    private Vec2 inWorldPos;
    private boolean exploded;


    private ColorChangingController colorController;

    private final static int NORMAL_DIAMETER = 15;

/*
    protected Mine(IEngine engine, Coordinate pos, int owner) {
        super(engine, physicGameWorld, pos, angle, 1, width, height, BodyForms.RECT, BodyType.STATIC, -1);
        super(engine, pos, 0, NORMAL_WIDTH, NORMAL_WIDTH, 0, GraphicLayers.ON_GROUND_LAYER);
        this.owner = getOwnerForInt(owner);
        //this.owner = !PLAYER;
        if (this.owner == PLAYER || GlobalVariables.debug) {
            loadGraphicDefaultData(engine);
        }
    }*/

    protected Mine (IEngine engine, PhysicWorld physicWorld, Coordinate pos, int ownerAsInt) {
        super(engine, physicWorld, pos, 0, IMMORTAL_LIFE, NORMAL_DIAMETER, NORMAL_DIAMETER, BodyForms.CIRCLE, BodyType.STATIC, -1);

        this.owner = getOwnerForInt(ownerAsInt);
        Logger.debug("Mine owner is player: " + (owner == PLAYER));
        if (this.owner == PLAYER || GlobalVariables.debug) {
            loadGraphicDefaultData(engine);
        }
        graphicLayer = GraphicObject.GraphicLayers.ON_GROUND_LAYER;
        body.setUserData(BodyData.MINE);
    }


    @Override
    protected void setBodyData() {
        if (owner == PLAYER) {
            setFilterDataForCategory(CollisionFilterCreator.CATEGORY_MINE_FOR_PLAYER);
            System.out.println("Body data set as mine for player ");
        }
        else {
            setFilterDataForCategory(CollisionFilterCreator.CATEGORY_MINE_FOR_ENEMY);
            System.out.println("Body data set as mine for enemy");
        }
    }

    private static boolean getOwnerForInt(int owner) {
        if (owner == 0) {
            //Logger.debugLog("Mine owner is player");
            return PLAYER;
        }
        else {
            //Logger.debugLog("Mine owner is enemy");
            return !PLAYER;
        }
    }

    public static Mine create(IEngine engine, PhysicWorld physicWorld, EntityData entityData) {
        int [] values = entityData.getValues();
        Coordinate pos = new Coordinate(values[0], values[1]);
        int intOwner = values[2];
        Mine mine = new Mine(engine, physicWorld, pos, intOwner);
        mine.setId(entityData.getId());
        return mine;
    }

    public void loadGraphicDefaultData(IEngine engine){
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data = new ImageZoneSimpleData(267,53, 291, 78);
        loadImage(engine, path, width, height, data);
        if (owner == PLAYER) {
            colorController = new ColorChangingController(engine, path);
        }
    }



    @Override
    public void update(GameRound gameRound, long deltaTime) {


        if (!exploded) {
            super.update(gameRound, deltaTime);
            if (owner == PLAYER) {
                colorController.update(gameRound);
            }
            tanks = gameRound.getEntitiesForType(EnemyTank.class, tanks);
            //
            for (Entity entity : tanks) {
                if (entity != null) {
                    if (entity instanceof Tank) {
                        Tank tank = (Tank) entity;
                        Body tankBody = tank.getBody();
                        for (Fixture tankFixture = tankBody.getFixtureList(); tankFixture != null; tankFixture = tankFixture.getNext()) {

                            /*
                            if (tankFixture.testPoint(inWorldPos)) {
                                Logger.debugLog("Exploded");
                                tank.attackBy(this, gameRound);
                                gameRound.addExplosion(pos.clone(), 0f, IAnimations.MINE_EXPLOSION);
                                gameRound.addExplosion(tank.getPos().clone(), 0f, IAnimations.TANK_DESTRUCTION);
                                gameRound.deleteObjectAfterActualLoop(this);
                                exploded = true;
                                return;
                            }*/
                        }
                    }
                }
            }
        }
    }

    @Override
    public int collisionWithObject(Body collidedBody, GameRound gameRound){
        Entity object = gameRound.getObjectByBody(collidedBody);
        if (object instanceof Tank){
            Tank tank = (Tank) object;
            tank.attackBy(this, gameRound);
            gameRound.removeEntity(this);
            Logger.debug("Exploded");
            //tank.attackBy(this, gameRound);
            gameRound.addExplosion(pos.clone(), 0f, IAnimations.MINE_EXPLOSION);
            gameRound.addExplosion(tank.getPos().clone(), 0f, IAnimations.TANK_DESTRUCTION);
            gameRound.deleteObjectAfterActualLoop(this);
            DissolvingAndUpwardsMovingText text = new DissolvingAndUpwardsMovingText(gameRound.getEngine(), new Coordinate(tank.getPos().x, tank.getPos().y-tank.getHeight()*0.75f), "DESTROYED", AbstractText.WHITE);
            gameRound.addEntityToEnd(text);


            //final ImageZoneSimpleData data = new ImageZoneSimpleData(768,64, 768+64,64+64);
            //loadImage(engine, path, width, height, data);

    //Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData
            //GraphicManager.getManager()
            int x = (int)pos.x;
            int y = (int)pos.y;
            int angle = (int) engine.getEngine().random(360);
            //public SpriteInGame(IEngine engine, Coordinate pos, int angle, int width, int height, int thirdDim, int layer, int spritesheetNumber, int left, int up, int right, int down){
            SpriteInGame rest = new SpriteInGame(engine, new Coordinate(x,y), angle, Entity.ENTITY_NORMAL_DIM, Entity.ENTITY_NORMAL_DIM, 0, GraphicObject.GraphicLayers.GROUND_LAYER, 0, REST_AFTER_EXPLOSION_ZONE_DATA.leftX, REST_AFTER_EXPLOSION_ZONE_DATA.upperY, REST_AFTER_EXPLOSION_ZONE_DATA.rightX, REST_AFTER_EXPLOSION_ZONE_DATA.lowerY) ;
            gameRound.addEntityOnGround(rest);
            exploded = true;
        }
        return 0;
    }

    @Override
    protected void firstUpdating(GameRound gameRound, long deltaTime){
        inWorldPos = gameRound.getPhysicWorld().getController().coordPixelsToWorld(new PVector(pos.x, pos.y));
        Logger.debug("Mine pos: " + pos + " in world: " + inWorldPos.x + "x" + inWorldPos.y);
        if (owner == PLAYER) gameRound.getEntitiesForType(EnemyTank.class, tanks);  //I must not to update this every frame
        else gameRound.getEntitiesForType(PlayerTank.class, tanks);
    }

    public void draw(PGraphics graphics, GameCamera gameCamera) {
        if (!exploded) {
            super.draw(graphics, gameCamera);
            if (owner == PLAYER) {

            }
        }
        //graphicElementInGame.draw(graphics, gameCamera, pos.x, pos.y);
    }

    private class ColorChangingController{
        private Timer timer;
        private final int MAX_TIME = 1250;
        private final int MAX_TIME_FOR_IN_NEAREST_AREA = 750;
        private final float MAX_DIST_FOR_LONG_BLINK = 150;
        private boolean firstLoopEnded;
        private boolean actualBasicGraphic = true;
        private GraphicElementInGame mainGraphicObject, secondaryGraphicObject;
        private ColorChangingController(IEngine engine, String path){
            mainGraphicObject = graphicElementInGame;
            final ImageZoneSimpleData secondaryData = new ImageZoneSimpleData(267, 53 + 25, 291, 78 + 25);
            loadSecondaryImage(engine, path, width, height, secondaryData);
        }

        protected final void loadSecondaryImage(IEngine engine, String path, int graphicWidth, int graphicHeight, ImageZoneSimpleData data){
            GraphicManager manager = GraphicManager.getManager(engine.getEngine());
            Image graphicImage = manager.getImage(path);
            secondaryGraphicObject = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
        }


        private void update(GameRound gameRound){
            if (firstLoopEnded) {
                if (owner == PLAYER) {
                    if (timer.isTime()){
                        if (graphicElementInGame.equals(mainGraphicObject)){
                            graphicElementInGame = secondaryGraphicObject;
                        }
                        else graphicElementInGame = mainGraphicObject;
                        int timeToNextBlink = getTimeForNextBlink(gameRound);
                        timer.setNewTimer( timeToNextBlink);
                        //Logger.debugLog("Color changed!");
                    }
                }
            }
            else {
                timer = new Timer(MAX_TIME, gameRound.getEngine().getEngine());
                firstLoopEnded = true;
            }
        }

        private int getTimeForNextBlink(GameRound gameRound) {
            ArrayList <Entity> entities = gameRound.getEntities();
            float minDist = 99999;
            for (Entity entity : entities){
                if (entity instanceof EnemyTank){
                    Coordinate tankPos = entity.getPos();
                    float dist = PApplet.dist(pos.x, pos.y, tankPos.x, tankPos.y);
                    if (dist < minDist){
                        minDist = dist;
                    }
                }
            }
            if (minDist > MAX_DIST_FOR_LONG_BLINK){
                return MAX_TIME;
            }
            else {
                return (int) PApplet.map(minDist,0,MAX_DIST_FOR_LONG_BLINK, 14, MAX_TIME_FOR_IN_NEAREST_AREA );
            }
        }
    }
}



