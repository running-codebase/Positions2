package com.travis.awesome.positions2.levels;

public class Level1 extends Level {
	
	public void initializeLevel()
	{
		super.initializeLevel();
	}

	public void updateLevel()
	{
		
			
	}
	
	public void finishLevel()
	{

		
	}

}

/*
 * 
 
 What needs to be in a level?
 
 - The visual controls. There needs to be a canvas that can be controlled from the view
 - The rules of the game. Each time the update level is called. the current input state must be checked to see if there is a state shift
 - updateLevel has to return the current state
 
 
 
 
 */
