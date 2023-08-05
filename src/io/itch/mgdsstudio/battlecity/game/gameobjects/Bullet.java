package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.mainpackage.GlobalConstants;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;
import io.itch.mgdsstudio.engine.graphic.debuggraphic.DebugCircle;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import processing.core.PApplet;
import processing.core.PVector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;


//Bullet becomes no id
public class Bullet extends SolidObject {
    //I need to use fabric model
    private float horizontalAngle;
    private float verticalAngle;
    //private final static float NORMAL_BULLET_VELOCITY = 6;
    //private final static float FAST_BULLET_VELOCITY = 12;

    private int ownerType;



    public interface Types{
        int SIMPLE_BULLET = 0;
        int FAST_BULLET = 1;
        int MISSILE = 2;
    }
    protected int type;
    private int velocity;
    
    public Bullet(IEngine engine, PhysicWorld physicWorld, Coordinate pos, int horizontalAngle, int verticalAngle, int type, int ownerType, int velocity){
        super(engine, physicWorld, pos, horizontalAngle, 1, getBulletRadius(type), getBulletRadius(type), BodyForms.CIRCLE, BodyType.DYNAMIC, -1);
        this.velocity = velocity;
        this.horizontalAngle = horizontalAngle;
        this.verticalAngle = verticalAngle;
        this.type = type;
        this.ownerType = ownerType;
        applyVelocity();
        setBodyData();
        initGraphic(engine);
        graphicLayer =GraphicObject.GraphicLayers.OVER_SKY_LEVEL;
    }

    private void initGraphic(IEngine engine) {
        final String path = engine.getPathToObjectInAssets(GlobalConstants.NAME_FOR_TANK_GRAPHIC_FILE);
        final ImageZoneSimpleData data;
        if (type == Types.MISSILE) data = new ImageZoneSimpleData(266,30, 293,45);
        else data = new ImageZoneSimpleData(273,5, 286,18);
        loadImage(engine, path, width*5, height*5, data);
    }

    @Override
    protected void createDebugGraphic(IEngine engine) {
        debugGraphic = new DebugCircle(this, engine.getProcessing());
    }
    
    private void applyVelocity(){
        PVector velocityNullVector = new PVector(1f,0);        
        velocityNullVector.rotate(PApplet.radians(-1f*horizontalAngle));
        velocityNullVector.mult(velocity);
        /*if (type == Types.SIMPLE_BULLET) velocityNullVector.mult(velocity);
        else velocityNullVector.mult(velocity); */
        Vec2 velocityVector = new Vec2(velocityNullVector.x, velocityNullVector.y);
        body.setLinearVelocity(velocityVector);
    }

    private static int getBulletRadius(int type) {
        return ENTITY_NORMAL_DIM/16;
    }


    @Override
    protected void setBodyData() {
        body.setUserData(BodyData.BULLET);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_BULLET);
    }

    public boolean canCrushArmoredObjects() {
        if (type == Types.MISSILE) return true;
        else return false;
    }

    public int getType() {
        return type;
    }

    public int getOwnerType() {
        return ownerType;
    }


    public boolean isMissile() {
        if (type == Types.MISSILE) return true;
        else return false;
    }
}
