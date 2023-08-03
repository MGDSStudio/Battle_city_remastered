package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.game.graphic.IAnimations;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;

class CollisionsController extends Controller {
    //private GameRound gameRound;
    private final ArrayList <Contact> beginContacts = new ArrayList<>();
    private final ArrayList <Contact> endContacts = new ArrayList<>();
    private final ArrayList <Contact> preContacts = new ArrayList<>();
    private final ArrayList <Contact> postContacts = new ArrayList<>();

    CollisionsController(GameRound gameRound) {
        //this.gameRound = gameRound;
        //System.out.println("Maybe I need to create a pool of the contacts");
    }

    @Override
    public void update(GameRound gameRound, long deltaTime){
        updateObjectsTaking(gameRound);
        updateShooting(gameRound);
        updateMineColliding(gameRound);
        beginContacts.clear();
        endContacts.clear();
        preContacts.clear();
        postContacts.clear();
    }

    private void updateMineColliding(GameRound gameRound) {
        for (int i = beginContacts.size()-1; i>= 0; i--){
            Body b1 = beginContacts.get(i).getFixtureA().getBody();
            Body b2 = beginContacts.get(i).getFixtureB().getBody();
            if (b1.getUserData() != null && b1.getUserData() == BodyData.MINE) {
                contactMineWithObject(b1, b2, gameRound);
                beginContacts.remove(i);
            }
            else if (b2.getUserData() != null && b2.getUserData() == BodyData.MINE) {
                contactMineWithObject(b2, b1, gameRound);
                beginContacts.remove(i);
            }
        }
    }

    private void updateObjectsTaking(GameRound gameRound) {
        for (int i = beginContacts.size()-1; i>= 0; i--){
            Body b1 = beginContacts.get(i).getFixtureA().getBody();
            Body b2 = beginContacts.get(i).getFixtureB().getBody();
            if (b1.getUserData() != null && b1.getUserData() == BodyData.COLLECTABLE) {
                contactCollectableWithObject(b1, b2, gameRound);
                beginContacts.remove(i);
            }
            else if (b2.getUserData() != null && b2.getUserData() == BodyData.COLLECTABLE) {
                contactCollectableWithObject(b2, b1, gameRound);
                beginContacts.remove(i);
            }
        }
    }

    private void contactCollectableWithObject(Body collectableBody, Body tankBody, GameRound gameRound) {
        SolidObject collectable = (SolidObject) gameRound.getObjectByBody(collectableBody);
        if (collectable.getBody().isActive()) {
            if (tankBody == null) Logger.error("Tank body is null! Can not get contact with collectable");
            if (collectable == null) Logger.error("Collectable is null! Can not get contact with collectable");
            else collectable.collisionWithObject(tankBody, gameRound);
        }

    }

    private void contactMineWithObject(Body mineBody, Body tankBody, GameRound gameRound) {
        Mine mine = (Mine) gameRound.getObjectByBody(mineBody);
        mine.collisionWithObject(tankBody, gameRound);
    }

    private void updateShooting(GameRound gameRound) {
        for (int i = beginContacts.size()-1; i>= 0; i--){
            Body b1 = beginContacts.get(i).getFixtureA().getBody();
            Body b2 = beginContacts.get(i).getFixtureB().getBody();
            if (b1.getUserData() != null && b1.getUserData() == BodyData.BULLET) {
                contactBulletWithObject(b1, b2, gameRound);
                beginContacts.remove(i);
            }
            else if (b2.getUserData() != null && b2.getUserData() == BodyData.BULLET) {
                contactBulletWithObject(b2, b1, gameRound);
                beginContacts.remove(i);
            }
        }
    }

    private void contactBulletWithObject(Body bulletBody, Body objectBody, GameRound gameRound) {
        Entity object = gameRound.getObjectByBody(objectBody);
        Entity bullet = gameRound.getObjectByBody(bulletBody);
        SolidObject solidObject = (SolidObject) object;
        if (solidObject != null) {
            if (bullet instanceof Bullet) {
                //Logger.debugLog("Collision of a bullet with: " + objectWithBody.getClass());
                boolean killed = solidObject.attackBy(bullet, gameRound);
                if (killed) {
                    gameRound.deleteObjectAfterActualLoop(object);
                    //removeObject(objectWithBody, gameRound);
                    int type = solidObject.getDyingAnimationType();
                    if (type >= 0) gameRound.addExplosion(solidObject.getPos(), bullet.getAngle(), type);
                }
                gameRound.deleteObjectAfterActualLoop(bullet);
                //removeObject(bullet, gameRound);
                gameRound.addExplosion(bullet.getPos(), bullet.getAngle(), IAnimations.BULLET_EXPLOSION);
            } else
                Logger.error("Can not update contact with bullet. Object " + solidObject.getClass() + " is not a bullet");
        }
        else {
            Logger.error("Object with body is null. object was null: " + (object == null));
            if (object != null) Logger.error("Object was: " + object.getClass());
        }
    }

    private void removeObject(Entity entity, GameRound gameRound) {
        if (entity instanceof SolidObject){
            SolidObject solidObject = (SolidObject) entity;
            solidObject.getBody().setActive(false);
            gameRound.getPhysicWorld().getController().destroyBody(solidObject.getBody());
        }
        gameRound.removeEntity(entity);
    }

    @Override
    public void appendDataTypeA(Object object) {
        if (object instanceof  Contact){
            beginContacts.add((Contact) object);
        }
    }

    /*
    public void beginContact(Contact contact) {
        beginContacts.add(contact);
    }


    public void endContact(Contact contact) {
        endContacts.add(contact);
    }


    public void preSolve(Contact contact, Manifold manifold) {

    }


    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }*/


}
