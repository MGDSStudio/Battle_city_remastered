package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalVariables;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.graphic.GraphicManagerSingleton;
import io.itch.mgdsstudio.engine.graphic.Image;
import io.itch.mgdsstudio.engine.graphic.ImageInGame;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.libs.AxisAlignedBoundingBox;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class SolidObject extends Entity{

    public interface BodyForms{
        int RECT = 0;
        int CIRCLE = 1;
        int TANK = 2;
        int SW_TRIANGLE = 3;
        int SE_TRIANGLE = 4;
        int NW_TRIANGLE = 5;
        int NE_TRIANGLE = 6;

    }

    //public final static int POLYGON = 3;

    protected Body body;
    protected AxisAlignedBoundingBox aabb;
    protected final int bodyForm;

    protected SolidObject(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int angle, int life, int width, int height, int bodyForm, BodyType bodyType, int additionalDim) {
        super(engine, pos, angle, life, width, height);
        this.bodyForm = bodyForm;
        createBody(physicWorld, bodyForm, additionalDim, bodyType);

        setBodyData();
        if (bodyForm == BodyForms.TANK || bodyForm == BodyForms.RECT) aabb = new AxisAlignedBoundingBox(width, height, pos, angle);
        else aabb = new AxisAlignedBoundingBox(width, pos);
    }

    protected void setBodyData() {
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT_OR_ENEMY);
    }

    protected void setFilterDataForCategory(int category) {
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            f.m_filter.categoryBits = category;
            f.m_filter.maskBits = CollisionFilterCreator.getMaskForCategory(category);
        }
    }

    private void createBody(PhysicWorld physicWorld, int bodyForm, int additionalDim, BodyType bodyType){
        if (bodyForm == BodyForms.RECT) createRectBody(physicWorld, width, height, bodyType);
        else if (bodyForm == BodyForms.CIRCLE) createCircleBody(physicWorld, width, bodyType);
        else if (bodyForm == BodyForms.TANK) createTankBody(physicWorld, width, height, additionalDim);
        else createTriangleBody(physicWorld, width, height, bodyType, bodyForm);

    }

    private void createCircleBody(PhysicWorld physicWorld, int diameter, BodyType bodyType) {
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(physicWorld.controller.coordPixelsToWorld(new PVector(pos.x, pos.y)));
        bd.angle = (float) Math.toRadians(angle);
        body = PhysicWorld.controller.createBody(bd);
        CircleShape shape = new CircleShape();
        shape.m_radius = physicWorld.controller.scalarPixelsToWorld(diameter/2);
        body.createFixture(shape, 150f);
        body.getFixtureList().setFriction(0.001f);
        body.setAngularDamping(9.99f);
        body.setGravityScale(0);
    }

    protected void createTankBody(PhysicWorld physicWorld, int width, int height, int additionalDim) {
        PolygonShape sd = new PolygonShape();
        float box2dW = physicWorld.controller.scalarPixelsToWorld(width/2);
        float box2dH = physicWorld.controller.scalarPixelsToWorld(height/2);
        sd.setAsBox(box2dW, box2dH);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 2f;
        fd.friction = 0.1f;
        fd.restitution = 0.05f;
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(physicWorld.controller.coordPixelsToWorld(new Vec2(pos.x, pos.y)));
        bd.angle = (float) Math.toRadians(angle);
        body = physicWorld.controller.createBody(bd);
        body.createFixture(fd);
        body.setGravityScale(0f);
        //setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    protected void createRectBody(PhysicWorld physicWorld, int width, int height, BodyType bodyType) {
        PolygonShape sd = new PolygonShape();
        float box2dW = physicWorld.controller.scalarPixelsToWorld(width/2);
        float box2dH = physicWorld.controller.scalarPixelsToWorld(height/2);
        /*
        float box2dW = physicWorld.controller.scalarPixelsToWorld(width/2);
        float box2dH = physicWorld.controller.scalarPixelsToWorld(height/2);
         */
        sd.setAsBox(box2dW, box2dH);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 2f;
        fd.friction = 0.1f;
        fd.restitution = 0.05f;
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(physicWorld.controller.coordPixelsToWorld(new Vec2(pos.x, pos.y)));
        bd.angle = (float) Math.toRadians(angle);
        body = physicWorld.controller.createBody(bd);
        body.createFixture(fd);
        body.setGravityScale(0f);
    }

    protected void createTriangleBody(PhysicWorld physicWorld, int width, int height, BodyType bodyType, int bodyForm ) {
        PolygonShape sd = new PolygonShape();
        float box2dW = physicWorld.controller.scalarPixelsToWorld(width/2);
        float box2dH = physicWorld.controller.scalarPixelsToWorld(height/2);
        sd.setAsBox(box2dW, box2dH);
        Vec2[] vertices = new Vec2[3];
        //Y-axis is flipped in box 2D
        if (bodyForm == BodyForms.NE_TRIANGLE){ //error
            vertices[0] = new Vec2(-box2dW, +box2dH* GlobalVariables.yDirectionCoefficient);
            vertices[1] = new Vec2(box2dW, +box2dH* GlobalVariables.yDirectionCoefficient);
            vertices[2] = new Vec2(box2dW, -box2dH* GlobalVariables.yDirectionCoefficient);
        }
        else if (bodyForm == BodyForms.NW_TRIANGLE){
            vertices[0] = new Vec2(-box2dW, box2dH* GlobalVariables.yDirectionCoefficient);
            vertices[1] = new Vec2(-box2dW, -box2dH* GlobalVariables.yDirectionCoefficient);
            vertices[2] = new Vec2(box2dW, box2dH* GlobalVariables.yDirectionCoefficient);
        }
        else if (bodyForm == BodyForms.SW_TRIANGLE){    //error
            vertices[0] = new Vec2(-box2dW, +box2dH* GlobalVariables.yDirectionCoefficient);
            vertices[1] = new Vec2(-box2dW, -box2dH* GlobalVariables.yDirectionCoefficient);
            vertices[2] = new Vec2(box2dW, -box2dH* GlobalVariables.yDirectionCoefficient);
        }
        else if (bodyForm == BodyForms.SE_TRIANGLE){
            vertices[0] = new Vec2(box2dW, -box2dW* GlobalVariables.yDirectionCoefficient);
            vertices[1] = new Vec2(box2dW, box2dW* GlobalVariables.yDirectionCoefficient);
            vertices[2] = new Vec2(-box2dW, -box2dW* GlobalVariables.yDirectionCoefficient);
        }
        sd.set(vertices, vertices.length);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 2f;
        fd.friction = 0.1f;
        fd.restitution = 0.05f;
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(physicWorld.controller.coordPixelsToWorld(new Vec2(pos.x, pos.y)));
        bd.angle = (float) Math.toRadians(angle);
        body = physicWorld.controller.createBody(bd);
        body.createFixture(fd);
        body.setGravityScale(0f);

/*
        Vec2 centerInPixelCoordinates = getPixelCenter(pointsPositionsInPixelCoordinates);
        Vec2 centerInWorldCoordinates = PhysicGameWorld.controller.vectorPixelsToWorld(centerInPixelCoordinates);
        Vec2[] relativeCenterVerteciesInPixelCoordinate = getPixelRelativeCenterCoordinates(centerInPixelCoordinates, pointsPositionsInPixelCoordinates);
        PolygonShape sd = new PolygonShape();
        Vec2[] vertices = new Vec2[pointsPositionsInPixelCoordinates.size()];
        for (int i = 0; i <pointsPositionsInPixelCoordinates.size(); i++){
            vertices[i] = new Vec2(PhysicGameWorld.controller.coordPixelsToWorld(relativeCenterVerteciesInPixelCoordinate[i]));
        }
        sd.set(vertices, vertices.length);
        System.out.println("Center: " + centerInPixelCoordinates + "; In world: " + centerInWorldCoordinates + "; Coordinates: " + pointsPositionsInPixelCoordinates.size() +" or " + vertices.length);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(centerInWorldCoordinates);
        bd.angle = (float) Math.toRadians(-angle);
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(sd, 1.0f);
        body.getFixtureList().setDensity(1);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        */


    }

    public int getDyingAnimationType() {
        return IAnimations.NO_ANIMATION;
    }

    public boolean attackBy(Entity attackingObject, GameRound gameRound){
        return !KILLED;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void update(GameRound gameRound, long deltaTime) {
       super.update(gameRound, deltaTime);
       aabb.update(angle);
       Vec2 posInGame = gameRound.getPhysicWorld().getController().getBodyPixelCoord(body);
       pos.x = posInGame.x;
       pos.y = posInGame.y;
       angle = PApplet.degrees(body.getAngle());
       if (angle<0) angle=360+angle;
       else if (angle>360) angle=angle-360;
    }

    public AxisAlignedBoundingBox getAabb() {
        return aabb;
    }

    protected final void loadImage(IEngine engine, String path, int graphicWidth, int graphicHeight, ImageZoneSimpleData data){
        GraphicManagerSingleton manager = GraphicManagerSingleton.getManager(engine.getProcessing());
        Image graphicImage = manager.getImage(path);
        if (this.getClass() == Bullet.class){
            Logger.debug("Get image: " + graphicImage.getPath());
        }
        graphicElementInGame = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
    }

    protected final void loadAnimation(IEngine engine, String path, int graphicWidth, int graphicHeight, ImageZoneSimpleData data){
        GraphicManagerSingleton manager = GraphicManagerSingleton.getManager(engine.getProcessing());
        Image graphicImage = manager.getImage(path);
        if (this.getClass() == Bullet.class){
            Logger.debug("Get image: " + graphicImage.getPath());
        }
        graphicElementInGame = new ImageInGame(graphicImage, graphicWidth, graphicHeight, data);
    }

    public int collisionWithObject(Body tankBody, GameRound gameRound){
        return 0;
    }


    @Override
    public void dispose(GameRound gameRound) {
        if (body != null){
            body.setActive(false);
            gameRound.getPhysicWorld().getController().world.destroyBody(body);
            body = null;
        }
    }

}
