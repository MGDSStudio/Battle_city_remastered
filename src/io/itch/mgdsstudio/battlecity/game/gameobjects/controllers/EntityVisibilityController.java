package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.engine.libs.Geometrie;
import org.jbox2d.dynamics.Body;

public class EntityVisibilityController {

    //private GameRound gr;
    private Entity object;
    //private PlayerTank player;
    private float playerFrontAngle = 0;
    private float playerTurretAngle = 0;
    private float maxVisibilityDist;
    private int timeBetweenTest = 66;
    private int actualTimeAfterLastTest;

    private float alpha = 1;
    private final float TEST_DISTANCE_STEP = 3;
    private final static float MAX_VISIBILITY_DELTA = 90;
    private final static float MIN_VISIBILITY_DELTA = 30;

    private float goalAlpha;
    private final float FULL_VISIBLE_ALPHA = 1f;
    private final float FULL_INVISIBLE_ALPHA = 0f;
    private final float ALPHA_APPEARING_VELOCITY = 0.002f;
    private final float ALPHA_HIDDING_VELOCITY = 0.0001f;
    private float alphaChangingVelocity = ALPHA_HIDDING_VELOCITY;


    private int statement;
    private boolean lineVisibility;

    private interface Statements{
        int AWAY_FROM_PLAYER = 0;
        int IN_FULL_VISIBLE_ANGLE_ZONE = 1;
        int IN_HALF_VISIBLE_ANGLE_ZONE = 2;
        int IN_INVISIBLE_ZONE = 3;

        boolean BEHIND_OBSTACLE = false;
        boolean ON_EYE_RAY_LINE = true;
    }

    public EntityVisibilityController(Entity object) {
        //this.gr = gr;
        this.object  = object;
        //this.player = player;
    }

    public void update(GameRound gameRound, long deltaTime){
        actualTimeAfterLastTest+= deltaTime;
        PlayerTank player = gameRound.getPlayer();
        if (actualTimeAfterLastTest>=timeBetweenTest){
            while(actualTimeAfterLastTest>timeBetweenTest){
                actualTimeAfterLastTest-=timeBetweenTest;
            }
            updatePlayerAngles(player);
            updateStatement(gameRound, player);
        }
        updateGoalAlpha();
        updateActualAlpha(deltaTime);
    }

    private void updateGoalAlpha(){
        if (statement == Statements.IN_INVISIBLE_ZONE || statement == Statements.AWAY_FROM_PLAYER){
            goalAlpha = FULL_INVISIBLE_ALPHA;
            alphaChangingVelocity = ALPHA_HIDDING_VELOCITY;
        }
        else if (statement == Statements.IN_FULL_VISIBLE_ANGLE_ZONE){
            goalAlpha = FULL_VISIBLE_ALPHA;
            alphaChangingVelocity = ALPHA_APPEARING_VELOCITY;
        }
        else{
            //float goalAlpha = calculateTransitionAlphaVale();
            final float range = MAX_VISIBILITY_DELTA-MIN_VISIBILITY_DELTA;
            final float relativeValue = (playerFrontAngle -MIN_VISIBILITY_DELTA)/range;
            goalAlpha = relativeValue*(1-MAX_VISIBILITY_DELTA);
        }
    }

    private void updateActualAlpha(long deltaTime){

    }

    private void updatePlayerAngles(PlayerTank player){
        playerFrontAngle = player.getAngle();
        playerTurretAngle = player.getTurretAbsoluteAngle();
        //Logger.debug("Angle: " + playerFrontAngle + "/" + playerTurretAngle);
        //angle = Geometrie.getPositiveAngleCCWforYAxisDownOriented(player.getPos().x, player.getPos().y, object.getPos().x, object.getPos().y);
    }

    private void updateStatement(GameRound gr, PlayerTank player){
        float dist = Geometrie.dist(object.getPos().x, object.getPos().y, player.getPos().x, player.getPos().y);
        if (dist >= maxVisibilityDist){
            statement = Statements.AWAY_FROM_PLAYER;
        }
        else {
            updateVisibility(gr, player);
            if (lineVisibility == Statements.ON_EYE_RAY_LINE){
                updateNotFullVisibility(player);
            }
            else {
                // must will be invisible
                statement = Statements.IN_INVISIBLE_ZONE;
            }

        }

    }

    private void updateNotFullVisibility(PlayerTank player){
        float playerAngle = player.getAngle();
        float delta = Geometrie.getShortestDeltaAngleInDegrees(playerAngle, this.playerFrontAngle);
        if (delta > MAX_VISIBILITY_DELTA){
            statement = Statements.IN_INVISIBLE_ZONE;
        }
        else if (delta > MIN_VISIBILITY_DELTA){
            statement = Statements.IN_HALF_VISIBLE_ANGLE_ZONE;
        }
        else statement = Statements.IN_FULL_VISIBLE_ANGLE_ZONE;
    }

    private void updateVisibility(GameRound gr, PlayerTank player){
        float x = player.getPos().x;
        float y = player.getPos().y;
        float stepX = TEST_DISTANCE_STEP*(float)Math.cos(Math.toRadians(playerFrontAngle));
        float stepY = TEST_DISTANCE_STEP*(float)Math.sin(Math.toRadians(playerFrontAngle));
        Body b;
        for (int i = 0; i < 100;i++){
            x+=stepX;
            y+=stepY;
            b = gr.getPhysicWorld().getBodyAtPoint(x,y);
            if (b != null){
                if (b.equals(object)){
                    lineVisibility = Statements.ON_EYE_RAY_LINE;
                    return;
                }
                else if (!b.equals(player)){
                    lineVisibility = Statements.BEHIND_OBSTACLE;
                }
            }
            lineVisibility = Statements.ON_EYE_RAY_LINE;
        }
    }

    public float getAlphaUpTo1(){
        //Logger.debug("Alpha: " + alpha);
        return alpha;
    }

    public int getAlphaUpTo255(){
        //Logger.debug("Alpha: " + alpha);
        return (int)(alpha*255f);
    }
}
