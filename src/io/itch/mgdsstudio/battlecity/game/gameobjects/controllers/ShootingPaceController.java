package io.itch.mgdsstudio.battlecity.game.gameobjects.controllers;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Tank;
import io.itch.mgdsstudio.engine.libs.*;
import processing.core.*;

public class ShootingPaceController
{
	private final Tank tank;
    private Timer timer;
	private int timeToNextShot;
	
	public ShootingPaceController(Tank tank)
	{
		this.tank = tank;
	}
	
	public void update(PApplet engine){
		if (timer == null){
			timer = new Timer(timeToNextShot, engine);
		}
	}
	
	public void shot(){
		timer.setNewTimer(timeToNextShot);
	}
	
	public boolean readyForShot(){
		if (timer.isTime()){
			return true;
		}
		else return false;
	}
	
	public int getReverseRestTimeRelativeToNumber(int number){
		return PApplet.floor(((1f-timer.getRelativeRestTime())*number));
	}
	
}
