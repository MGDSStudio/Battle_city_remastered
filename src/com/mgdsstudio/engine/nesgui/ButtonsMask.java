package com.mgdsstudio.engine.nesgui;

class ButtonsMask {
    int segmentsAlongX, segmentsAlongY;
    boolean digit = true;
    private KeyData [][] keyData;

    ButtonsMask(int width, int height, boolean digit){
        this.digit = digit;
        create(width, height);
    }

    private void create(int width, int height) {
        /*if (digit){
            keyData = new KeyData{
                {new KeyData(1,1, "9"), },
                {},
                {},
                {}
            };
        }*/
    }


}
