package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ai;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.EnemyTank;

public class RemoteAi extends Ai{
    protected RemoteAi(int type, EnemyTank enemyTank) {
        super(type, enemyTank);
        Logger.debug("enemy with id " + enemyTank.getId() + " will be controlled per internet");
    }

    @Override
    protected void updatingAction(GameRound gameRound, long deltaTime) {

    }


}
