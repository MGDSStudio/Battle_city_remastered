package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.GameElement;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

public abstract class Controller extends GameElement{

    protected boolean ended;
    protected boolean firstUpdated;

    public Controller() {
        super();
    }

    public abstract void update(GameRound gameRound, long deltaTime);

    public final boolean isEnded() {
        return ended;
    }

    public void appendDataTypeA(Object object){

    }

    public void appendDataTypeB(Object object){

    }

    protected void firstUpdating(GameRound gameRound, long deltaTime) {
        firstUpdated = true;
    }

    public void appendButtonsListener(GlobalListener gameProcessController) {
        // not for every listener
    }



    public void executeAction(IEngine engine, PhysicWorld physicWorld, GLobalSerialAction action) {

    }
}
