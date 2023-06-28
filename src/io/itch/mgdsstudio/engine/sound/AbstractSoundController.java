package io.itch.mgdsstudio.engine.sound;

import processing.sound.SoundFile;

public abstract class AbstractSoundController extends Thread{

    protected static SoundFile file;
    protected static String pathToSoundtrack;
    //protected float volume = TrackData.NORMAL_AUDIO;

    public abstract void update();

    public abstract void pausePlay();

    public abstract void resumePlay();

    public abstract void startToPlay();

    public void removeFromMemory(){
        file = null;
        pathToSoundtrack = null;
    }
}
