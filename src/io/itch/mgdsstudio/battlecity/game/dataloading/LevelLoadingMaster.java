package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LevelLoadingMaster extends ExternalDataController {
    ArrayList<String> data;
    private GameRound gameRound;
    private int playersConnected, playerNumber;
    private boolean singleplayer;
    private boolean withAiControlledAllies;
    private PlayerProgressControllerSingleton playerProgressControllerSingleton;
    private boolean editorMode;

    public LevelLoadingMaster(GameRound gameRound, int level, IEngine engine, int playersConnected, int playerNumber, boolean singleplayer, PlayerProgressControllerSingleton playerProgressControllerSingleton, boolean editorMode) {
        this.level = level;
        this.engine = engine;
        this.gameRound = gameRound;
        this.playersConnected = playersConnected;
        this.playerNumber = playerNumber;
        this.singleplayer = singleplayer;
        this.playerProgressControllerSingleton = playerProgressControllerSingleton;
        this.editorMode = editorMode;
    }

    public void loadLevelData(ArrayList<Entity> gameObjects, ArrayList <Controller> controllers, PhysicWorld physicWorld) {
        String path = getPathToLevel();
        data = new ArrayList<>();
        boolean successfully = loadingFileDataInJavaMode(path);
        if (successfully) Logger.debug("Successfully loaded data from java");
        else {
            successfully = loadingFileDataInProcessingMode(path);
            if (successfully) Logger.debug("Successfully loaded data from processing");
        }
        if (successfully){
            initObjects(gameObjects, controllers, physicWorld);
            sortEntities(gameObjects);
        }

    }

    private void sortEntities(ArrayList<Entity> gameObjects) {
        Collections.sort(gameObjects, Comparator.comparing(Entity::getGraphicLayer));
    }

    private void initObjects(ArrayList<Entity> gameObjects,ArrayList <Controller> controllers, PhysicWorld physicWorld) {
        DataDecoder dataDecoder = new DataDecoder(physicWorld, gameRound, playerNumber, singleplayer, playerProgressControllerSingleton);
        for (int i = 0; i < data.size(); i++){
            Object object = dataDecoder.getObjectFromString(data.get(i));
            if (object != null) {
                if (object instanceof Entity){
                    Entity entity = (Entity) object;
                    addObjectToList(entity, gameObjects);
                }
                else if (object instanceof Controller){
                    Controller controller = (Controller) object;
                    controllers.add(controller);
                }
            }
        }
    }

    private void addObjectToList(Entity entity, ArrayList<Entity> gameObjects) {
        boolean canBeAdded = true;
        if (singleplayer){
            if (entity.getClass() == PlayerTank.class){
                boolean alreadyHaveOnePlayerTank = false;
                for (Entity added : gameObjects){
                    if (added.getClass() == PlayerTank.class){
                        alreadyHaveOnePlayerTank = true;
                        break;
                    }
                }
                if (alreadyHaveOnePlayerTank) canBeAdded = false;
            }
        }
        if (canBeAdded) gameObjects.add(entity);
    }

    protected boolean loadingFileDataInProcessingMode(String path){
        try {
            String [] dataArray = engine.getEngine().loadStrings(path);
            for (int i = 0; i < dataArray.length; i++){
                data.add(dataArray[i]);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    protected boolean loadingFileDataInJavaMode(String path){
        try {
            data = whenReadWithBufferedReader_thenCorrect(path);
            if (data == null) {
                return false;
            }
        }
        catch (Exception e) {
            //System.out.println("Can not load with this loader; " + e);
            return false;
        }
        return true;
    }

    protected ArrayList<String> whenReadWithBufferedReader_thenCorrect(String fileName) {
        try {
            Logger.debug("sketch path: " + fileName);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            ArrayList<String> data = new ArrayList<>();
            String currentLine = new String();
            while (currentLine != null) {
                if (currentLine.length() > 1) data.add(currentLine);
                try {
                    currentLine = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            reader.close();
            return data;
        } catch (IOException e) {
            Logger.error("Can not load data; " + e);
            return null;
        }
    }
}
