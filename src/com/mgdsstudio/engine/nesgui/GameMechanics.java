package com.mgdsstudio.engine.nesgui;


abstract class GameMechanics{



    static boolean isPointInRect(float pointX, float pointY, float leftUpperX, float leftUpperY,float rectWidth, float rectHeight){
        if (pointX>leftUpperX &&
                pointX<(leftUpperX+rectWidth) &&
                pointY>leftUpperY &&
                pointY<(leftUpperY+rectHeight)) return true;
        else return false;
    }


    }

