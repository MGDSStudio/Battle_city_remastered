package io.itch.mgdsstudio.battlecity.game.dataloading;


public class DataStringCreationMaster {

  private final static int NO_DATA = -9999;
  private int [] values, graphicValues;
  private int tileset;
  private String name;
  private String dataString;

  private void init(){
    // I should use the StringBuilder
    dataString = ""+name;
    
  }
  
  public String getDataDtring(){
    return dataString;
  }
}
