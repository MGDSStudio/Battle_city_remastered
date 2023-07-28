package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.EnemiesSpawnController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.LevelEndConditionController;

public class RepeatingIdsController {
    public static final char END_ROW_SYMBOL = '!';
    static final public char MAIN_DATA_START_CHAR = ':';
    static final public char GRAPHIC_NAME_START_CHAR = '#';
    static final public char GRAPHIC_NAME_END_CHAR = ';';
    static final public char DIVIDER_BETWEEN_VALUES = ',';
    static final public char DIVIDER_BETWEEN_GRAPHIC_DATA = 'x';
    static final protected char VERTICES_START_CHAR = '%';
    static final protected char DIVIDER_BETWEEN_VERTICES = 'v';
    public final static String DIVIDER_NAME_ID = " ";

    private ArrayList <String> fileContent;

    public void deleteRepeatingIds(){
      
    }

  private class StringWithId{
      private bool isData;
      private String dataString;
      private int id;

      private StringWithId(String fullString){
          dataString = fullString;
        if (fullString.length < 3){
          isData = false;
        }
        else if (fullString.charAt(0) == '/' || fullString.charAt(0) == '\' ){

        }
      }
  }
}
