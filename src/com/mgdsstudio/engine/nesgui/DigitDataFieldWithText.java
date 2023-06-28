package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import processing.core.PGraphics;

public class DigitDataFieldWithText extends DataFieldWithText {
    //private int value;

    private int maxValue = -1;
    public DigitDataFieldWithText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, int defaultValue) {
        super(engine, centerX, centerY, width, height, name, graphics, ""+defaultValue);
    }

    @Override
    protected void updateFunction() {

    }

    public int getValue(){
        try{
            int value  = Integer.valueOf(data);
            return value;
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    protected void fieldIsNotMoreActive() {
        super.fieldIsNotMoreActive();
        if (maxValue!= -1){
            try{
                int digitValue = getValue();
                if (digitValue>maxValue){
                    System.out.println("Value is too large");
                    data = ""+maxValue;
                    cursor.setCursorToPos(0);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }




    protected void updateDataFilling() {
            if (wasKeyPressed()) {
                //System.out.println("Key pressed " + actualPressedChar);
                if (!charWasAdded) {
                    char newChar;
                    if (os == ANDROID) {
                        if (usingConsoleOutput) System.out.println("In release I need to change it to android. ");
                        newChar = actualPressedChar;
                    }
                    else newChar = engine.key;
                    System.out.println("New char " + newChar);
                    if (newChar == '1' || newChar == '2' || newChar == '3' || newChar == '4' || newChar == '5' || newChar == '6' || newChar == '7' || newChar == '8' || newChar == '9' || newChar == '0') {
                        addCharOnCursorPlace(newChar);
                        System.out.println("New char: " + newChar + " was added. New String: " + data);
                        charWasAdded = true;
                    }
                    else {
                        System.out.println("This char is not digit " + newChar);
                        charWasAdded = updateControlCharsAdding(newChar);
                    }
                    if (!charWasAdded)
                    {
                        System.out.println("New char: " + newChar + " with code " + engine.keyCode +  " can not be added" );
                        charWasAdded = true;
                    }
                }
            }
            else{
                charWasAdded = false;

            }
        if (desktop) {
            actualPressedChar = '@';
        }

    }




}
