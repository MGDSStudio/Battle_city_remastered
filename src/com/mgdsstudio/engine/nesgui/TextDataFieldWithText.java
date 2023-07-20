package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import jogamp.graph.font.typecast.ot.table.Program;
import processing.core.PFont;
import processing.core.PGraphics;

public class TextDataFieldWithText extends DataFieldWithText {



    //private int value;

    public TextDataFieldWithText(IEngine engine, int centerX, int centerY, int width, int height, String name, PGraphics graphics, String defaultValue) {
        super(engine, centerX, centerY, width, height, name, graphics, defaultValue);
        manualScrollable = true;
    }

    @Override
    protected void updateFunction() {

    }

    public String getData(){
        return data;
    }

    protected void updateDataFilling() {
        if (wasKeyPressed()) {
            //System.out.println("Key pressed");
            if (!charWasAdded) {
                char newChar = engine.key;
                if (os == ANDROID) {
                    if (usingConsoleOutput) System.out.println("In release I need to change it to android. ");
                    newChar = actualPressedChar;
                    //System.out.println("Char has code " + keyCode + " and character " + Program.actualPressedValue + " and keyCode " + Program.actualPressedKeyCode);
                }
                Character character = newChar;
                charWasAdded = updateControlCharsAdding(newChar);
                if (!charWasAdded) {
                    if (newChar != '-' && newChar != '%' && newChar != '/' && newChar != '^') {
                        PFont.Glyph glyph = font.getGlyph(character);
                        if (glyph!=null || engine.keyCode != 16) {  //double points
                            addCharOnCursorPlace(newChar);
                            charWasAdded = true;
                        }
                        else System.out.println("This char " + newChar + "  can not be added to the string. Glyph is null " + (glyph == null) + " and char code: " + engine.keyCode);
                    }
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
