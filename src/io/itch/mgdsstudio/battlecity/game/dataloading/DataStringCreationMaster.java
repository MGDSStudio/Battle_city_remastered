package io.itch.mgdsstudio.battlecity.game.dataloading;


public class DataStringCreationMaster {

  private final static int NO_DATA = -9999;
  private int [] values, graphicValues;
  private int tileset;
  private String name;
  private String dataString;

  /*
  from DataDecoder

  public static final char END_ROW_SYMBOL = '!';
    static final public char MAIN_DATA_START_CHAR = ':';
    static final public char GRAPHIC_NAME_START_CHAR = '#';
    static final public char GRAPHIC_NAME_END_CHAR = ';';
    static final public char DIVIDER_BETWEEN_VALUES = ',';
    static final public char DIVIDER_BETWEEN_GRAPHIC_DATA = 'x';
    static final protected char VERTICES_START_CHAR = '%';
    static final protected char DIVIDER_BETWEEN_VERTICES = 'v';
   */


  private void init(){
    // I should use the StringBuilder
    //ArmoredWall 8:350,282,0,32,32,0#
    dataString = ""+name;
    
  }
  
  public String getDataDtring(){
    return dataString;
  }
}
