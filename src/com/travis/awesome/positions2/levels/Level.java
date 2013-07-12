package com.travis.awesome.positions2.levels;

public class Level {
	
	//CONSTANTS
	public static final int ERROR_STATE = -1;
	public static final int UNINITIALIZED = 0;
	public static final int RUNNING = 1;
	public static final int COMPLETED = 2;
	
	//Class Variables
	public int current_state = UNINITIALIZED;
	
	
	
	public void initializeLevel()
	{
		
		current_state = RUNNING;
	}

	public void updateLevel()
	{
		
	}
	
	public void finishLevel()
	{

		
	}
}


