package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ai;

import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.TankController;

import java.util.ArrayList;

public abstract class Ai {
    protected int type;
    protected EnemyTank enemyTank;
    protected TankController tankController;

    protected boolean startUpdatingCompleted;
    private int levelStartTime = -1;
    private final static int LEVEL_NORMAL_START_TIME_BEFORE_FIRST_AI_UPDATING = 1000;
    protected final ArrayList<GlobalListener> globalListeners = new ArrayList<>();
    ///private Timer levelStartTimer;

    public Ai(int type, EnemyTank enemyTank) {
        this.type = type;
        this.enemyTank = enemyTank;
        this.tankController = enemyTank.getTankController();
    }

    public static Ai createAi(int aiType, EnemyTank enemyTank, int controlledBy) {
        if (aiType == EnemyTank.CONTROL_IN_EDITOR) return new NotMovableAi(aiType, enemyTank);
        else if (controlledBy == EnemyTank.CONTROL_PER_INTERNET || aiType < 0) return new RemoteAi(aiType, enemyTank);
        else if (aiType == EnemyTank.CONTROL_PER_AI){
            return new AiControllerModel1(aiType, enemyTank);
        }
        else return new AiControllerModel1(aiType, enemyTank);
    }

    protected final void levelStartWaitingUpdate(GameRound gameRound){
        if (!startUpdatingCompleted){
            if (levelStartTime<0){
                levelStartTime = gameRound.getEngine().getProcessing().millis();
            }
            else if (gameRound.getEngine().getProcessing().millis()>(LEVEL_NORMAL_START_TIME_BEFORE_FIRST_AI_UPDATING+levelStartTime)){
                startUpdatingCompleted = true;
            }
        }
    }

    public final void update(GameRound gameRound, long deltaTime){
        levelStartWaitingUpdate(gameRound);
        if (startUpdatingCompleted){
            updatingAction(gameRound, deltaTime);
        }
    }

    protected abstract void updatingAction(GameRound gameRound, long deltaTime);

    public void appendListener(GlobalListener globalListener) {
        globalListeners.add(globalListener);
    }

    protected int getCommandNumber(){
        return -1;
    }
}
