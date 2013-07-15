package com.travis.awesome.positions2.levels;

import android.graphics.Canvas;

import com.travis.awesome.positions2.MainGamePanel;
import com.travis.awesome.positions2.Timer;

public class Level {
	
	//CONSTANTS
	public final int ERROR_STATE = -1;
	public final int UNINITIALIZED = 0;
	public final int RUNNING = 1;
	public final int COMPLETED = 2;
	
	//Class Variables
	public int current_state = UNINITIALIZED;
	public MainGamePanel view;
	public Timer timer;
	
	//Game Variables
	public int score = 0;

	
	public void initializeLevel(MainGamePanel view)
	{
		this.view = view;
		timer = new Timer();
	}
	
	public void startLevel()
	{
		current_state = RUNNING;
	}

	public void updateLevel()
	{	
	}
	
	public void finishLevel()
	{
	}
	
	
	public void drawLevel(Canvas canvas)
	{		
	}
}


