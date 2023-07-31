package io.itch.mgdsstudio.battlecity.game.dataloading;


import java.util.ArrayList;

public class DataStringCreationMaster {

  private final static int NO_DATA = -9999;
  private int [] values, graphicValues = null;
  private int id = NO_DATA;
  private String name;
  private String dataString;

  public DataStringCreationMaster(int[] values, String name) {
    this.values = values;
    this.name = name;
    init();
  }

  public DataStringCreationMaster(ArrayList <Integer> intList, String name) {
    this.values = new int[intList.size()];
    for (int i = 0; i < intList.size(); i++){
      values[i] = intList.get(i);
    }
    this.name = name;
    init();
  }

  public DataStringCreationMaster(int id, int[] values, String name) {
    this.id = id;
    this.values = values;
    this.name = name;
    init();
  }

  public DataStringCreationMaster(int id, int[] values, int [] graphicValues, String name) {
    this.id = id;
    this.values = values;
    this.name = name;
    this.graphicValues = graphicValues;
    init();
  }

  private void init(){
    // I should use the StringBuilder
    //I don't write the tileset name. Only the number
    dataString = ""+name;
    dataString+=' ';
    dataString+=id;
    dataString+=DataDecoder.MAIN_DATA_START_CHAR;
    for (int i = 0; i < (values.length-1); i++){
      dataString+=values[i];
      dataString+= DataDecoder.DIVIDER_BETWEEN_VALUES;
    }
    dataString+=values[values.length-1];
    dataString+=DataDecoder.GRAPHIC_NAME_START_CHAR;
    if (graphicValues != null){
      for (int i = 0; i < (graphicValues.length-1); i++){
        dataString+=graphicValues[i];
        dataString+= DataDecoder.DIVIDER_BETWEEN_VALUES;
      }
      dataString+=values[graphicValues.length-1];
    }
  }


  public String getDataString(){
    return dataString;
  }
}
