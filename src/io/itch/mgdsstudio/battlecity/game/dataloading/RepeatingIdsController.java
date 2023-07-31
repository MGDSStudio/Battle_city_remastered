package io.itch.mgdsstudio.battlecity.game.dataloading;

import io.itch.mgdsstudio.battlecity.game.GameRound;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.PhysicWorld;
import io.itch.mgdsstudio.battlecity.game.gameobjects.*;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.Controller;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.EnemiesSpawnController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.LevelEndConditionController;

import java.util.ArrayList;

public class RepeatingIdsController {
    public static final int ID_NOT_ENTCRYPTED = -999999;


    private ArrayList<String> fileContent;
    private ArrayList<StringWithId> stringWithDatas;

    public void deleteRepeatingIds(){
      for (int i = 0; i < stringWithDatas.size(); i++){
        // if ()       
      }
    }

  private class StringWithId{

      private boolean isData = false;
      private String dataString;
      private int id = ID_NOT_ENTCRYPTED;

      private StringWithId(String fullString){
          dataString = fullString;
        if (fullString.length() < 3){
          //isData = false;
        }
        else if (fullString.charAt(0) == '/' || fullString.charAt(0) == '\\' ){
            //comment string    
        }
        else if (!hasIdSpecificChars()){

            Logger.error(" String " + fullString + " doesn't have main data start char");
        }
          else{
                isData = true;
              extractId();
          }
      }

      private void extractId(){
          boolean founded = false;
            for (int i = 0; i < dataString.length(); i++){
                String actualChar = String.valueOf(dataString.charAt(i));
                if (actualChar == DataDecoder.DIVIDER_NAME_ID){
                    int startDigitNumber = i+1;
                    for (int j = startDigitNumber; j < dataString.length(); i++){
                        if (!Character.isDigit(dataString.charAt(j))){
                            String textRepresentation = dataString.substring(i,j-1);
                            try{
                                id = Integer.parseInt(textRepresentation);
                                return;
                            }
                            catch(Exception e){
                                Logger.error("Can not ectract ID 1" );
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }
            }
            Logger.error("Can not extract ID 2" );
      }

      private boolean hasIdSpecificChars(){
          CharSequence whitespace = ""+  DataDecoder.MAIN_DATA_START_CHAR;
          if (!dataString.contains(whitespace)) return false;
          CharSequence afterIdChar = ""+  DataDecoder.DIVIDER_NAME_ID;
          if (!dataString.contains(afterIdChar)) return false;
          return true;
      }

  }


}
