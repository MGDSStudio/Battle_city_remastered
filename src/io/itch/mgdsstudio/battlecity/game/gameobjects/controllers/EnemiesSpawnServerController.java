package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;

import io.itch.mgdsstudio.battlecity.datatransfer.data.ActionPrefixes;
import io.itch.mgdsstudio.battlecity.datatransfer.data.GLobalSerialAction;
import io.itch.mgdsstudio.battlecity.datatransfer.listeners.GlobalListener;
import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.Coordinate;
import io.itch.mgdsstudio.engine.libs.GameMechanics;
import io.itch.mgdsstudio.engine.libs.Timer;
import processing.core.PApplet;
import processing.data.IntList;

import java.util.ArrayList;

class EnemiesSpawnServerController extends EnemiesSpawnController{
    private ArrayList <Coordinate> placesForEnemiesSpawn;
    private int[] tanksToBeCreated;
    private Timer nextSpawnTimer;
    private int spawnTime;
    private int maxTanksOnScreen;

    //private int aiModel;

    //mutable objects
    private final ArrayList <Entity> mutEnemyTanks = new ArrayList<>();
    private final ArrayList < Coordinate > mutFreeSpawnZones = new ArrayList<>();
    private int controlBy;
    private SpawnPlaceSelector spawnPlaceSelector;
    private int [] aiModelList;

    protected final ArrayList <GlobalListener> globalListeners = new ArrayList<>();
    private int idForNextTank = -1;
    private int startDelay = 2000;
    private Timer timer;


    EnemiesSpawnServerController(IEngine engine, int maxTanksOnScreen, int spawnTime, int tanks0, int tanks1, int tanks2, int tanks3, int tanks4, int tanks5, int controlBy, int aiModelForTanks0, int aiModelForTanks1, int aiModelForTanks2, int aiModelForTanks3, int aiModelForTanks4, int aiModelForTanks5, int additionalData1, int additionalData2, ArrayList<Coordinate> placesForEnemiesSpawn) {
        this.controlBy = controlBy;
        this.placesForEnemiesSpawn = placesForEnemiesSpawn;
        this.spawnTime = spawnTime;
        this.maxTanksOnScreen  = maxTanksOnScreen;
        tanksToBeCreated = new int[4];
        tanksToBeCreated[EnemyTank.Types.SIMPLE_TANK] = tanks0;
        tanksToBeCreated[EnemyTank.Types.FAST_TANK] = tanks1;
        tanksToBeCreated[EnemyTank.Types.EASY_ARMORED_TANK] = tanks2;
        tanksToBeCreated[EnemyTank.Types.GOOD_ARMORED_TANK] = tanks3;
        aiModelList = new int[4];
        aiModelList[EnemyTank.Types.SIMPLE_TANK] = aiModelForTanks0;
        aiModelList[EnemyTank.Types.FAST_TANK] = aiModelForTanks1;
        aiModelList[EnemyTank.Types.EASY_ARMORED_TANK] = aiModelForTanks2;
        aiModelList[EnemyTank.Types.GOOD_ARMORED_TANK] = aiModelForTanks3;
        nextSpawnTimer = new Timer(spawnTime, engine.getEngine());
    }





    public void update(GameRound gameRound, long deltaTime){
        if (!ended) {
            if (!firstUpdated){
                if (timer == null) timer = new Timer(startDelay, gameRound.getEngine().getEngine());
                if (timer.isTime()) {
                    firstUpdating(gameRound, deltaTime);
                }
            }
            else {
                if (gameRound.getEntitiesCount(EnemyTank.class)<maxTanksOnScreen) {
                    spawnPlaceSelector.update(gameRound);
                    if (gameRound.getEngine().getEngine().frameCount % 5 == 0) {
                        Coordinate spawnPos = spawnPlaceSelector.getPlaceForSpawn(gameRound);
                        if (spawnPos != null) {
                            int type = getNextTankType(gameRound.getEngine().getEngine());
                            Logger.debug("Next tank will be spawned at " + spawnPos.toString());
                            generateNextTank(gameRound, spawnPos, type);
                            spawnPlaceSelector.stopSpawning();
                            if (getTanksToBeCreatedCount() <= 0) ended = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendButtonsListener(GlobalListener gameProcessController) {
        globalListeners.add(gameProcessController);
    }

    @Override
    protected void firstUpdating(GameRound gameRound, long deltaTime) {
        super.firstUpdating(gameRound, deltaTime);
        int spawnedAtStart = 0;
        while ((gameRound.getEntitiesCount(EnemyTank.class) < maxTanksOnScreen))
        {
            Coordinate spawnPos = getNextSpawnPosForStartGenerating(gameRound);
            if (spawnPos != null) {
                int type = getNextTankType(gameRound.getEngine().getEngine());
                generateNextTank(gameRound, spawnPos, type);
                if (getTanksToBeCreatedCount() <= 0) ended = true;
                spawnedAtStart++;
            }
            else {
                break;
            }
        }
        spawnPlaceSelector = new SpawnPlaceSelector(spawnTime);
    }

    private int getTanksToBeCreatedCount(){
        int count = 0;
        for (int i = 0; i < tanksToBeCreated.length; i++){
            count+=tanksToBeCreated[i];
        }
        return count;
    }

    private int getNextTankType(PApplet engine) {
        int count = getTanksToBeCreatedCount();
        int randomNumber = PApplet.floor(engine.random(0,count));
        int type = 0;
        for (int i = 0; i < tanksToBeCreated.length; i++){
            for (int j = 0; j < tanksToBeCreated[i]; j++){
                if (type == randomNumber){
                    tanksToBeCreated[i]--;
                    Logger.debug("Next tank will have type " + i);
                    return i;
                }
                type+=1;
            }
        }
        Logger.error("Can not select right type for tank! We must create: " + count + " tanks" + " random number: " + randomNumber);
        return 0;
    }

    private void generateNextTank(GameRound gameRound, Coordinate spawnPos, int type) {
        int angleToPlayersCamp = getAngleToObject(gameRound, spawnPos, Camp.class);
        int angleToNearestPlayer =  getAngleToObject(gameRound, spawnPos, PlayerTank.class);
        EnemyTank enemyTank = new EnemyTank(gameRound.getEngine(), gameRound.getPhysicWorld(), new Coordinate(spawnPos.x, spawnPos.y), angleToNearestPlayer, angleToPlayersCamp, -1, Tank.ENEMY, type, controlBy, aiModelList[type]);

        IntList newTankData = enemyTank.getSerializedIntData();

        //int nextId = GameElement.getNextId();
        enemyTank.setId(GameElement.getNextId());
        int nextId = enemyTank.getId();
        for (GlobalListener globalListener : globalListeners) {
            GLobalSerialAction GLobalSerialAction = new GLobalSerialAction(ActionPrefixes.ENEMY_TANK_CREATED, newTankData, nextId, gameRound.getTimeFromLevelBegan(), getCommandNumber());
            Logger.debug("String : " + GLobalSerialAction.getSerialized() + " will be send. Values list: " + newTankData);
            globalListener.appendCommand(GLobalSerialAction);
            enemyTank.appendListener(globalListener);
        }
        nextSpawnTimer.setNewTimer(spawnTime);
        gameRound.addEntityToStart(enemyTank);
        Logger.debug("Enemy tank created with id: " + enemyTank.getId() + " at: " + spawnPos);
    }

    /*
    private void generateNextTank(GameRound gameRound, Coordinate spawnPos, int type) {
        int angleToPlayersCamp = getAngleToObject(gameRound, spawnPos, Camp.class);
        int angleToNearestPlayer =  getAngleToObject(gameRound, spawnPos, PlayerTank.class);
        //int angle = 90;
        EnemyTank enemyTank = new EnemyTank(gameRound.getEngine(), gameRound.getPhysicGameWorld(), new Coordinate(spawnPos.x, spawnPos.y), angleToNearestPlayer, angleToPlayersCamp, -1, Tank.ENEMY, type, controlBy, aiModelList[type]);
        IntList newTankData = enemyTank.getSerializedIntData();

        //public SerialAction (char prefix, IntList values, int id, long startTime, int commandNumber){
        //;

        int nextId = GameElement.getNextId();
        //Logger.debugLog("String : " + serialAction.getSerialized() + " will be send");
        for (Listener listener : listeners) {
            SerialAction serialAction = new SerialAction(DataPrefixes.ENEMY_TANK_CREATED, newTankData, nextId, gameRound.getTimeFromLevelBegan(), getCommandNumber());
            Logger.debugLog("String : " + serialAction.getSerialized() + " will be send. Values list: " + newTankData);
            listener.appendCommand(serialAction);
            enemyTank.appendListener(listener);
        }
        nextSpawnTimer.setNewTimer(spawnTime);
        gameRound.addEntity(enemyTank);
        Logger.debugLog("Enemy tank created with id: " + enemyTank.getId());
    }
     */


    protected int getCommandNumber(){
        return -1;
    }



    private int getAngleToObject(GameRound gameRound, Coordinate spawnPos, Class className) {
        ArrayList<Integer> angles = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        for (Entity entity : gameRound.getEntities()){
            if (entity.getClass() == className){
                Coordinate pos = entity.getPos();
                if (spawnPos == null) Logger.debug("spawn is null");
                float angle = GameMechanics.getAngleToPointInDegrees(spawnPos.x, spawnPos.y, pos.x, pos.y);
                angles.add((int)angle);
                float dist = PApplet.dist(spawnPos.x, spawnPos.y, pos.x, pos.y);
                distances.add((int)dist);
            }
        }
        if (distances.size()==0) return (int) gameRound.getEngine().getEngine().random(360);
        else {
            int nearestDist = 999999;
            int nearestNumber = -1;
            for (int i = 0; i < distances.size(); i++) {
                if (distances.get(i) < nearestDist) {
                    nearestDist = distances.get(i);
                    nearestNumber = i;
                }
            }
            return angles.get(nearestNumber);
        }
    }

    private boolean isZoneFreeFromEntities(Coordinate coordinate, GameRound gameRound) {
        boolean free = true;
        ArrayList <Entity> entities = gameRound.getEntities();
        for (Entity entity : entities){
            if (entity.getClass() != Bullet.class){

                if (GameMechanics.isIntersectionBetweenAllignedRects(coordinate.x, coordinate.y, entity.getPos().x, entity.getPos().y, entity.getWidth(), entity.getHeight(), entity.getWidth(), entity.getHeight())){
                    free =false;
                }
            }
        }
        return free;
    }

    private Coordinate getNextSpawnPosForStartGenerating(GameRound gameRound) {
        mutFreeSpawnZones.clear();
        mutEnemyTanks.clear();
        ArrayList <Entity> enemies = gameRound.getEntitiesForType(EnemyTank.class, mutEnemyTanks);
        for (Coordinate coordinate : placesForEnemiesSpawn){
            boolean placeFree = true;
            for (Entity entity : enemies){
                if (GameMechanics.isIntersectionBetweenAllignedRects(coordinate.x, coordinate.y, entity.getPos().x, entity.getPos().y, entity.getWidth(), entity.getHeight(), entity.getWidth(), entity.getHeight())){
                    placeFree = false;
                    break;
                }
            }
            if (placeFree){
                mutFreeSpawnZones.add(coordinate);
            }
        }
        if (mutFreeSpawnZones.size() == 0) return null;
        else if (mutFreeSpawnZones.size() == 1) return mutFreeSpawnZones.get(0);
        else {
            int randomPlace = (int) gameRound.getEngine().getEngine().random(0,mutFreeSpawnZones.size());
            return mutFreeSpawnZones.get(randomPlace);
        }
    }

    private class SpawnPlaceSelector{
        private ArrayList <FreeZoneForSpawnController> controllers;

        public SpawnPlaceSelector(int time) {
            controllers = new ArrayList<>();
            for (Coordinate coordinate : placesForEnemiesSpawn){
                FreeZoneForSpawnController controller = new FreeZoneForSpawnController(coordinate, time);
                controllers.add(controller);
            }
        }

        private void update(GameRound gameRound){
            for (FreeZoneForSpawnController controller : controllers){
                controller.update(gameRound);
            }
        }

        public Coordinate getPlaceForSpawn(GameRound gameRound) {
            int maxReadyPlaces = 0;
            for (FreeZoneForSpawnController controller : controllers){
                if (controller.isReadyForSpawn()){
                    maxReadyPlaces++;
                }
            }
            if (maxReadyPlaces > 0) {
                int randomPos = (int)(gameRound.getEngine().getEngine().random(maxReadyPlaces));
                Logger.debug("Place: " + randomPos + " was selected. Wa had only " + maxReadyPlaces + " places for enemy generation");
                return controllers.get(randomPos).getPos();
            }
            else return null;
        }

        public void stopSpawning() {
            for (FreeZoneForSpawnController controller : controllers){
                controller.deactivate();
            }
        }


    }

}
