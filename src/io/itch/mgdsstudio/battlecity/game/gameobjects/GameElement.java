package io.itch.mgdsstudio.battlecity.game.gameobjects;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import processing.data.IntList;

public abstract class GameElement {

    private int id = 0;
    private static int nextId = 1;

    private final static IntList idList = new IntList();

    public GameElement() {
        //setNextId();
    }

    protected static int getNextId() {
        if (!idList.hasValue(nextId)) return nextId;
        else {
            int trying = 1;
            //nextId;
            while(idList.hasValue(nextId)){
                nextId++;
                //Logger.errorLog("Try: " + trying + " id: " + nextId + " values in list: " + idList.toString() + " and contains  " + nextId + ":" + idList.hasValue(nextId));
            }
        }
        return nextId;
    }



    protected void setNextId(){
        boolean exists = false;
        if (idList.hasValue(nextId)) exists = true;
        if (!exists){
            id = nextId;
            idList.append(id);
            nextId++;
        }
        else {
            nextId++;
            setNextId();
        }
    }

    public void setId(int id) {
        boolean exists = false;
        if (idList.hasValue(id)) exists = true;
        if (!exists){
            this.id = id;
            idList.append(id);
        }
        else {
            setNextId();
        }
        if (id>12) {
            Logger.debug("There is: " + idList.size() + " ids; :" + idList);

        }
    }

    public int getId(){
        return id;
    }

    public static void disposeAfterLevelEnded(){
        nextId = 1;
    }

    public void dispose(GameRound gameRound) {
    }
}
