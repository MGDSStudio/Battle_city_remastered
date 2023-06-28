package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ai;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;

public class NotMovableAi extends Ai{

    protected NotMovableAi(int type, EnemyTank enemyTank) {
        super(type, enemyTank);
        Logger.debug("enemy with id " + enemyTank.getId() + " will stay static");
    }

    @Override
    protected void updatingAction(GameRound gameRound, long deltaTime) {

    }
}
