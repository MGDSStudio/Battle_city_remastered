package com.mgdsstudio.engine.nesgui;

import processing.core.PGraphics;

interface IKeyboard {

    void shiftCursorRight();

    void shiftCursorLeft();

    void setChar(char newChar);

    void update(int x, int y);

    void draw(PGraphics graphics);

}
