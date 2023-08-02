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

  public DataStringCreationMaster(ArrayList <Integer> valuesAsList, String name) {
    this.values = new int[valuesAsList.size()];
    for (int i = 0; i < valuesAsList.size(); i++){
      values[i] = valuesAsList.get(i);
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

  public DataStringCreationMaster(int id, ArrayList<Integer>  valuesAsList, ArrayList<Integer>  graphicValuesAsList, String name) {
    this.id = id;
    this.values = new int[valuesAsList.size()];
    for (int i = 0; i < values.length; i++){
      this.values[i] = valuesAsList.get(i);
    }
    this.graphicValues = new int[graphicValuesAsList.size()];
    for (int i = 0; i < this.graphicValues.length; i++){
      this.graphicValues[i] = graphicValuesAsList.get(i);
    }
    this.name = name;
    init();
    /*
    this.id = id;
    int[] values = new int[valuesAsList.size()];
    for (int i = 0; i < values[i]; i++){
      values[i] = valuesAsList.get(i);
    }
    int [] graphicValuesArray = new int[graphicValuesAsList.size()];
    for (int i = 0; i < graphicValuesArray[i]; i++){
      graphicValuesArray[i] = graphicValuesAsList.get(i);
    }
    this.values = values;
    this.name = name;
    this.graphicValues = graphicValuesArray;
    init();
     */

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
      dataString+=graphicValues[graphicValues.length-1];
      dataString+=DataDecoder.GRAPHIC_DATA_END_CHAR;
    }
  }


  public String getDataString(){
    return dataString;
  }
}
