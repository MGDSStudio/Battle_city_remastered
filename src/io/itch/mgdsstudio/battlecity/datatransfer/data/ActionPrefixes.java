package io.itch.mgdsstudio.battlecity.datatransfer.data;

import io.itch.mgdsstudio.battlecity.game.Logger;

public abstract class ActionPrefixes
{
	public final static int DOES_NOT_TOUCHED = 0;
	public final static int CW = 1;
	public final static int CCW = 2;
	public final static int FORWARD = 3;
	public final static int BACKWARD = 4;

	//Right stick control
	public final static char AIMING_STICK_TURRET_ROTATION = 'R';	//CW, CCW, CENTERED == DOESNOT TOUCHED
	public final static char AIMING_STICK_SHOOTING = 'r';			//PRESSED or RELEASED
	//Left stick control
	public final static char MOVEMENT_STICK_BODY_ROTATION = 'l';	//CW, CCW, CENTERED
	public final static char MOVEMENT_STICK_RUN_AND_ROTATION = 'L';
	public final static char WANT_TO_CONNECT_FOR_PLAY = 'C';
	public final static char START_GAME = 'S';

	public final static char EXIT_FROM_GAME = 'E';

	public final static char ENEMY_TANK_CREATED = 'T';

	public static boolean isTankControlSpecificAction(int dataPrefix){
		if (dataPrefix == AIMING_STICK_SHOOTING || dataPrefix == AIMING_STICK_TURRET_ROTATION ||
			dataPrefix == MOVEMENT_STICK_BODY_ROTATION || dataPrefix == MOVEMENT_STICK_RUN_AND_ROTATION){
			return true;
		}
		return false;
	}

	public static boolean isControllerSpecificAction(char prefix) {
		if (prefix == ENEMY_TANK_CREATED){
			return true;
		}
		else return false;
	}

	//Logger.debug("This function is used to determine how must action be transfered");
	public static boolean mustBeActionTransferredGlobally(char prefix){
		if (prefix == CW || prefix == CCW || prefix == FORWARD || prefix == BACKWARD ||
			prefix == AIMING_STICK_TURRET_ROTATION || prefix == AIMING_STICK_SHOOTING ||
			prefix == MOVEMENT_STICK_BODY_ROTATION || prefix == MOVEMENT_STICK_RUN_AND_ROTATION ||
			prefix == WANT_TO_CONNECT_FOR_PLAY || prefix == START_GAME || prefix == EXIT_FROM_GAME || prefix == ENEMY_TANK_CREATED){
			return true;
		}
		else return false;
	}

	public static boolean mustBeActionTransferredGlobally(GLobalSerialAction action){
		return mustBeActionTransferredGlobally(action.getPrefix());
	}

}
