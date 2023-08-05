package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.Timer;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

import java.util.ArrayList;

//import org.jbox2d.dynamics.contacts.ContactPoint;
// import org.jbox2d.dynamics.contacts.ContactResult;import org.jbox2d.dynamics.joints.Jacobian;

public class PhysicWorld {	//implements Runnable

    //
    //private static final ArrayList<Entity> gameObjectsWithBodies = new ArrayList<GameObject>(30);	//I need to create this object on every frame! That's why it is a field!
    public static Box2DProcessing controller;

    public static boolean assertionErrorAppears = false;
    private boolean gameChangedToSingleThreadAfterAssertionError;

    private static float NORMAL_TIME_STEP = 0.0166f*30f/ 60;
    private static float TIME_STEP_BY_LAGGING = NORMAL_TIME_STEP*2f;
    //private static float TIME_STEP_BY_BULLET_TIME = NORMAL_TIME_STEP / BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS;

    public static boolean bulletTime = false;

    public final static float yFlip = -1;
    //public final static float SCREEN_CENTER_X = Program.engine.width/2f;
    //public final static float SCREEN_CENTER_Y = Program.engine.height/2f;
    public final static float worldScale = 30;

    //Mutable
    private final static Vec2 mutTestPointPixelPos = new Vec2(0,0);
    private final static Vec2 mutTestPointWorldPos = new Vec2(0,0);
    private final static Vec2 mutTestCoordPixelsToWorld = new Vec2(0,0);
    private final Vec2 mutableVec = new Vec2();

    private final static ArrayList <Body> mutBodiesList = new ArrayList<>(30);
    private static Timer lowFPSTimer;
    private static final int LOW_FPS_TIME_TO_SAVE_STEP = 1000;

    private static boolean normalPerformance;
    private static boolean normalPreviousPerformance;
    private final static PVector mutUpperVertex = new PVector();
    private final static PVector mutLowerVertex = new PVector();
    //private static AABB mutAABB = new AABB();

    public PhysicWorld(GameRound gameRound) {
        controller = new Box2DProcessing(gameRound.getEngine().getProcessing(), worldScale);
        controller.createWorld();
        controller.setGravity(0, 0);
        controller.listenForCollisions();
        assertionErrorAppears = false;
    }

    public void update(long framrTime) {
        updateLowPerformance();
        try {
            controller.step(NORMAL_TIME_STEP, 6, 10);
        }
        catch (AssertionError error){
            System.out.println("Can not update world. Assertion error: ");
            error.printStackTrace();
            if (! assertionErrorAppears) {
                assertionErrorAppears = true;

            }
        }
    }

    public Box2DProcessing getController() {
        return controller;
    }

    /*
    public static void init(GameRound gameRound) {
        controller = new Box2DProcessing(gameRound.getEngine(), worldScale);
        controller.createWorld();
        controller.setGravity(0, 0);
        controller.listenForCollisions();
        assertionErrorAppears = false;
    }*/

    public static void initFrameRateSpecificData(int framerate){
        NORMAL_TIME_STEP = 0.0166f*30f/ framerate;
        TIME_STEP_BY_LAGGING = NORMAL_TIME_STEP*2f;
        //TIME_STEP_BY_BULLET_TIME = NORMAL_TIME_STEP / BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS;

    }



    public static ArrayList <Body> getBodiesInCircle(Vec2 center, float radius) {
        ArrayList <Body> bodiesInCircle = new ArrayList<>();
        ArrayList<Body> allBodies = getBodies();
        for (Body body : allBodies){
            if (PApplet.dist(controller.coordWorldToPixels(body.getPosition()).x, controller.coordWorldToPixels(body.getPosition()).y, center.x, center.y)<radius){
                bodiesInCircle.add(body);
            }
        }
        System.out.println("In circle area :" + bodiesInCircle.size() + " objects");
        return bodiesInCircle;
    }

    public static void makeAllBodiesInactive() {
        for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()){
            b.setActive(false);
        }
    }

    public static boolean isPointInBody(Vec2 testPoint, Body body) {
        for (Fixture f = body.getFixtureList(); f != null; f=f.getNext()){
            if (f.testPoint(testPoint)){
                return true;
            }
        }
        return false;
    }


    public static void update() {
        updateLowPerformance();
        try {
            controller.step(NORMAL_TIME_STEP, 6, 10);
        }
        catch (AssertionError error){
            System.out.println("Can not update world. Assertion error: ");
            error.printStackTrace();
            if (! assertionErrorAppears) {
                assertionErrorAppears = true;

            }
        }
    }


    private static void updateLowPerformance() {
        /*
        normalPreviousPerformance = normalPerformance;
        if (Program.engine.frameRate >= (Program.NORMAL_FPS)*0.6f){
            normalPerformance = true;
        }
        else {
            normalPerformance = false;
        }
        if (normalPerformance != normalPreviousPerformance){
            if (lowFPSTimer == null) lowFPSTimer = new Timer(LOW_FPS_TIME_TO_SAVE_STEP);
            else {
                if (lowFPSTimer.isTime()){
                    lowFPSTimer.setNewTimer(LOW_FPS_TIME_TO_SAVE_STEP);
                }
            }
        }*/
    }




    public static boolean arePointInAnyBody(PVector point) {
        /*
        for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
            //if (!b.isBullet()) {
            for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {
                //trying++;
                if (f != null) {
                    try {
                        if (f.testPoint(coordPixelsToWorld(point))) {
                            //if (f.m_filter.categoryBits != CollisionFilterCreator.CATEGORY_CAMERA) return true;
                            if (b.getUserData() != null) {
                                if (b.getUserData().equals(GameCamera.CAMERA_BODY_NAME)) {
                                    //System.out.println("It's not a camera; " + b.getUserData());
                                    //return false;
                                } else return true;
                            } else return true;
                        }
                    }
                    catch (Exception e){
                        System.out.println("Maybe this fixture were already deleted  " + (f == null));
                        return false;
                    }
                }
            }
            //}
        }*/
        return false;
    }

    public static boolean arePointInAnyBodyButNotInBullet(PVector pointInPixelsCorrdinates) {
        //Vec2 testPoint = coordPixelsToWorld(pointInPixelsCorrdinates);
        /*
        //int trying = 0;
        for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
            //if (!b.isBullet()) {
            for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {

                if (f != null) {
                    try {
                        if (f.testPoint(testPoint)) {

                            if (b.getUserData() != null) {
                                if (b.getUserData().equals(GameCamera.CAMERA_BODY_NAME)) {

                                    return false;
                                } else if (b.getUserData().equals(Bullet.BULLET)) {
                                    //System.out.println("It's not a camera; " + b.getUserData());
                                    return false;
                                } else if (b.isBullet()) {
                                    //return false;	//} was so else if (b.isBullet()) return false;
                                }
                                else return true;
                            } else return true;
                        }
                    }
                    catch (Exception e){
                        System.out.println("This body is in a some null fixture " + (f == null));

                    }
                }
                else return false;
            }

        }*/
        return false;
    }

    public static Vec2 getBodyPixelCoord(Body body){
        return coordWorldToPixels(body.getPosition().x, body.getPosition().y);
    }

    public static Vec2 coordWorldToPixels(float worldX, float worldY) {
        /*float pixelX = PApplet.map(worldX, 0.0F, 1.0F, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale);
        float pixelY = PApplet.map(worldY, 0.0F, 1.0F, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale);
        if (yFlip == -1.0F) {
            pixelY = PApplet.map(pixelY, 0.0F, (float) Program.engine.height, (float) Program.engine.height, 0.0F);
        }
        mutTestPointWorldPos.x = pixelX;
        mutTestPointWorldPos.y = pixelY;*/
        return mutTestPointWorldPos;
    }

    public static Vec2 coordPixelsToWorld(PVector point) {
        /*
        float worldX = PApplet.map(point.x, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale, 0.0F, 1.0F);
        float worldY = point.y;
        if (yFlip == -1.0F) {
            worldY = PApplet.map(point.y, (float) Program.engine.height, 0.0F, 0.0F, (float) Program.engine.height);
        }
        worldY = PApplet.map(worldY, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale, 0.0F, 1.0F);
        mutTestPointPixelPos.x = worldX;
        mutTestPointPixelPos.y = worldY;
        */
        return mutTestPointPixelPos;
    }

    public static Vec2 coordPixelsToWorld(Coordinate point) {
        /*
        float worldX = PApplet.map(point.x, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale, 0.0F, 1.0F);
        float worldY = point.y;
        if (yFlip == -1.0F) {
            worldY = PApplet.map(point.y, (float) Program.engine.height, 0.0F, 0.0F, (float) Program.engine.height);
        }
        worldY = PApplet.map(worldY, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale, 0.0F, 1.0F);
        mutTestPointPixelPos.x = worldX;
        mutTestPointPixelPos.y = worldY;
        */
        return mutTestPointPixelPos;
    }

    public static Vec2 coordPixelsToWorld(float pixelX, float pixelY) {
        /*
        float worldX = PApplet.map(pixelX, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale, 0.0F, 1.0F);
        float worldY = pixelY;
        if (yFlip == -1.0F) {
            worldY = PApplet.map(pixelY, (float) Program.engine.height, 0.0F, 0.0F, (float) Program.engine.height);
        }
        worldY = PApplet.map(worldY, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale, 0.0F, 1.0F);
        mutTestCoordPixelsToWorld.x = worldX;
        mutTestCoordPixelsToWorld.y = worldY;
        */
        return mutTestCoordPixelsToWorld;
    }

    public Body getBodyAtPoint(float x, float y) {
        mutableVec.x = x;
        mutableVec.y = y;
        for (Body b = controller.world.getBodyList(); b != null; b.getNext()){
            for (Fixture f = b.getFixtureList(); f != null; f.getNext()){
                if (f.testPoint(mutableVec)){
                    return b;
                }
            }
        }
        return null;
    }





    public static Entity getGameObjectByBody(GameRound gameRound, Body testBody){
        /*
        for (Person person : gameRound.getPersons()) {
            if (person.body.equals(testBody)) return person;
        }
        for (RoundPipe roundPipe : gameRound.getRoundPipes()) {
            if (roundPipe.hasFlower()) {

            }
            if (roundPipe.body.equals(testBody)) return roundPipe;
        }
        try {
            for (int i = 0; i < gameRound.collectableObjectsController.getObjectsNumber(); i++) {
                if (gameRound.collectableObjectsController.getCollectableObjects().get(i).body.equals(testBody))
                    return gameRound.collectableObjectsController.getCollectableObjects().get(i);
            }
        }
        catch (Exception e){
            System.out.println("Can not get object by body: " + e);
        }
        for (int i = 0; i < gameRound.roundElements.size(); i++) {
            if (gameRound.roundElements.get(i).body.equals(testBody)) return gameRound.roundElements.get(i);
        }
        for (int i = 0; i < gameRound.getBullets().size();i++){
            if (gameRound.getBullets().get(i).body.equals(testBody)) return gameRound.getBullets().get(i);
        }
        for (int i = 0; i < gameRound.getRoundRotatingSticks().size(); i++){
            if (gameRound.getRoundRotatingSticks().get(i).body.equals(testBody)) return gameRound.getRoundRotatingSticks().get(i);
        }
        for (int i = 0; i < gameRound.getLaunchableWhizbangsController().getWhizbangsNumber(); i++){
            if (gameRound.getLaunchableWhizbangsController().getWhizbangs().get(i).body.equals(testBody)) return gameRound.getLaunchableWhizbangsController().getWhizbangs().get(i);
        }
        for (int i = 0; i < gameRound.getMoveablePlatformsControllers().size(); i++){
            for (int j = 0; j < gameRound.getMoveablePlatformsControllers().get(i).getPlatforms().size(); j++){
                if (gameRound.getMoveablePlatformsControllers().get(i).getPlatforms().get(j).body.equals(testBody)) {
                    return gameRound.getMoveablePlatformsControllers().get(i).getPlatforms().get(j);
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < gameRound.getRoundPipes().size(); j++) {
                if (gameRound.getRoundPipes().get(j).hasFlower() && gameRound.getRoundPipes().get(j).getPlantController()!= null) {
                    ArrayList <Body> bodies = gameRound.getRoundPipes().get(j).getPlantController().getBodies();
                    if (bodies.get(i).equals(testBody)) {
                        return gameRound.getRoundPipes().get(j).getPlantController().getPlants().get(i);
                    }
                }
            }
        }
        if (testBody != null && testBody.getUserData() != null) {
            if (testBody.getUserData() == "Camera body") {
                PhysicGameWorld.controller.world.destroyBody(testBody);
                System.out.println("This body " + testBody + " belongs to no one object and was deleted");
            }
        }

        //System.out.println("Can not determine object with body");*/
        return null;
    }

    public static ArrayList<Entity> getGameObjectsWithBodies(GameRound gameRound){

        /*
        gameObjectsWithBodies.clear();
        for (Person person : gameRound.getPersons()) {
            if (person.body != null) gameObjectsWithBodies.add(person);
        }
        for (RoundPipe roundPipe : gameRound.getRoundPipes()) {
            if (roundPipe.hasFlower()) {
                //if (roundPipe.)
                //if (roundPipe.flower.jaw1 != null || roundPipe.flower.jaw2 != null || roundPipe.flower.body != null) gameObjectsWithBodies.add(roundPipe.flower);
            }
        }
        for (int i = 0; i < gameRound.collectableObjectsController.getObjectsNumber(); i++) {
            if (gameRound.collectableObjectsController.getCollectableObjects().get(i).body!= null) gameObjectsWithBodies.add(gameRound.collectableObjectsController.getCollectableObjects().get(i));
        }
        for (int i = 0; i < gameRound.roundElements.size(); i++) {
            if (gameRound.roundElements.get(i).body != null) gameObjectsWithBodies.add(gameRound.roundElements.get(i));
        }
        for (int i = 0; i < gameRound.getBullets().size();i++){
            if (gameRound.getBullets().get(i).body != null) gameObjectsWithBodies.add(gameRound.getBullets().get(i));
        }
        return gameObjectsWithBodies;*/
        return null;
    }

    public static ArrayList<Body> getBodies(){
        mutBodiesList.clear();
        for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
            mutBodiesList.add(b);
        }
        return mutBodiesList;
    }



    public static boolean areThereBody(Vec2 pos, int testCircleRadius) {
        return false;
        /*
        try {
            System.out.println("This fun works not properly");
            Body testBody = controller.world.getBodyList();
            for (int i = 0; i < controller.world.getBodyCount(); i++) {
                if (Program.engine.dist(pos.x, pos.y, controller.coordWorldToPixels((testBody.getPosition())).x, controller.coordWorldToPixels((testBody.getPosition())).y) < testCircleRadius) {
                    return true;
                }
                else testBody.getNext();	// falsch!

            }
            return false;
        }
        catch (Exception e) {
            System.out.println("Can not test area to find a body");
            return false;
        }

        */
    }



}