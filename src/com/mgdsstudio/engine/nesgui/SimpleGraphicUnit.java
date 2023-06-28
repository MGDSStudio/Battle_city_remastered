package com.mgdsstudio.engine.nesgui;

import processing.core.PApplet;
import processing.core.PImage;

abstract class SimpleGraphicUnit {
    final static boolean FLIP = true;
    final static boolean NO_FLIP = false;
    protected String path;
    PImage image;
    protected PApplet engine;

}
