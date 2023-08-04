package io.itch.mgdsstudio.battlecity.mainpackage;

import io.itch.mgdsstudio.battlecity.game.InEditorGraphicData;
import io.itch.mgdsstudio.battlecity.game.InGameGraphicData;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class MultiplatformLauncher {
    //private IEngine engine;
    private MainController mainController;
    //private int playerNumberInMultiplayerMode;

    public MultiplatformLauncher(IEngine engine, int playerNumberInMultiplayerMode) {
        GlobalVariables.init(engine);
        InGameGraphicData.init(engine);
        InEditorGraphicData.init(engine);
        mainController = new MainController(engine, playerNumberInMultiplayerMode);
    }

    public void update() {
        mainController.update();
        mainController.draw();
    }

    public void backPressed(){
        mainController.backPressed();
    }

    public void beginContact(Contact contact) {
        mainController.beginContact(contact);
    }


    public void endContact(Contact contact) {
        mainController.endContact(contact);
    }


    public void preSolve(Contact contact, Manifold manifold) {
        mainController.preSolve(contact, manifold);
    }


    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        mainController.postSolve(contact, contactImpulse);
    }
}
