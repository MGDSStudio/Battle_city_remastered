package io.itch.mgdsstudio.battlecity.game;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.battlecity.mainpackage.MainController;
import org.jbox2d.dynamics.contacts.Contact;

public abstract class GamePart {



    protected IEngine engine;
    protected MainController mainController;
    protected static int difficulty;


    public GamePart(IEngine engine, MainController mainController) {
        this.engine = engine;
        this.mainController = mainController;
    }

    public abstract void update();


    public final IEngine getEngine() {
        return engine;
    }

    public abstract void draw();

    public void beginContact(Contact contact) {
    }

    public void backPressed() {

    }
}
