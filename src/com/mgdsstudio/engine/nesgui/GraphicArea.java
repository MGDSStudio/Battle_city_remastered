package com.mgdsstudio.engine.nesgui;

import processing.core.PGraphics;
import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

public class GraphicArea {
    enum Dir{
        LEFT, RIGHT, UP, DOWN;
    }


    private PGraphics graphics;
    private GraphicAreaSelector graphicAreaSelector;
    private String path;

    private final static String LOG_PREFIX = "Graphic area: ";

    private void log(String text){
        System.out.println(LOG_PREFIX + text);
    }

    private class ScaleMaster{

    }

    private class MoveMaster{

    }

    private class ImageZoneData{
        private int xL, xR, yU, yD;
        protected final static boolean SUCCESSFULLY = false;
        private ImageZoneSimpleData imageZoneSimpleData;

        public ImageZoneData(int xL, int xR, int yU, int yD, ImageZoneSimpleData imageZoneSimpleData) {
            this.xL = xL;
            this.xR = xR;
            this.yU = yU;
            this.yD = yD;
            this.imageZoneSimpleData = imageZoneSimpleData;
        }

        boolean changeDim(Dir sideToBeChanged, Dir moveSide, int step){
            boolean succesfully = true;
            if (sideToBeChanged == Dir.LEFT){
                if (moveSide == Dir.RIGHT){
                    succesfully = moveLeftToRight(step);
                }
                else if (moveSide == Dir.LEFT){
                    succesfully = moveLeftToLeft(step);
                }
                else {
                    log("Error dir " + sideToBeChanged) ;
                }
            }
            else if (sideToBeChanged == Dir.RIGHT){
                if (moveSide == Dir.RIGHT){
                    succesfully = moveRightToRight(step);
                }
                else if (moveSide == Dir.LEFT){
                    succesfully = moveRightToLeft(step);
                }
                else {
                    log("Error dir " + sideToBeChanged);
                }
            }
            else if (sideToBeChanged == Dir.UP){
                if (moveSide == Dir.UP){
                    succesfully = moveUpToUp(step);
                }
                else if (moveSide == Dir.DOWN){
                    succesfully = moveUpToDown(step);
                }
                else {
                    log("Error dir " + sideToBeChanged) ;
                }
            }
            else if (sideToBeChanged == Dir.DOWN){
                if (moveSide == Dir.UP){
                    succesfully = moveDownToUp(step);
                }
                else if (moveSide == Dir.DOWN){
                    succesfully = moveDownToDown(step);
                }
                else {
                    log("Error dir " + sideToBeChanged) ;
                }
            }
            return succesfully;
        }

        boolean move(Dir dir, int step){
            boolean status = !SUCCESSFULLY;
            for (int i = 0; i < step; i++) {
                if (dir == Dir.RIGHT) {
                    status = moveRightToRight(1);
                    if (status == SUCCESSFULLY){
                        moveLeftToRight(1);
                    }
                }
                else if (dir == Dir.LEFT){
                    status = moveLeftToLeft(1);
                    if (status == SUCCESSFULLY) {
                        moveRightToLeft(1);
                    }
                }
                else if (dir == Dir.UP){
                    status = moveUpToUp(1);
                    if (status == SUCCESSFULLY) {
                        moveDownToUp(1);
                    }
                }
                else if (dir == Dir.DOWN){
                    status = moveDownToDown(1);
                    if (status == SUCCESSFULLY) {
                        moveUpToDown(1);
                    }
                }
                else {
                    log("Error direction");
                }

            }
            return status;
        }

        private boolean moveLeftToRight(int step){
            xL+=step;
            if (xL >= xR){
                xL = xR-1;
                log("Can not move xL to right");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveLeftToLeft(int step){
            xL-=step;
            if (xL < imageZoneSimpleData.leftX){
                xL = imageZoneSimpleData.leftX;
                log("Can not move xL to left");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveRightToRight(int step){
            xR+=step;
            if (xR > imageZoneSimpleData.rightX){
                xR = imageZoneSimpleData.rightX;
                log("Can not move xR to right");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveRightToLeft(int step){
            xR-=step;
            if (xR <= xL){
                xR = xL+1;
                log("Can not move xR to left");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveUpToDown(int step){
            yU+=step;
            if (yU >= yD){
                yU = yD-1;
                log("Can not move yU to down");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveUpToUp(int step){
            yU-=step;
            if (yU < imageZoneSimpleData.upperY){
                yU = imageZoneSimpleData.upperY;
                log("Can not move yU to up");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveDownToDown(int step){
            yU+=step;
            if (yU > imageZoneSimpleData.lowerY){
                yU = imageZoneSimpleData.lowerY;
                log("Can not move yD to down");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }

        private boolean moveDownToUp(int step){
            yU-=step;
            if (yD <= yU){
                yU = yU+1;
                log("Can not move yD to up");
                return !SUCCESSFULLY;
            }
            else return SUCCESSFULLY;
        }
    }
}
