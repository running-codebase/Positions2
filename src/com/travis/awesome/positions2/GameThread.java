package com.travis.awesome.positions2;

import android.graphics.Canvas;
import android.util.Log;

import com.travis.awesome.positions2.levels.Level;
import com.travis.awesome.positions2.levels.Level1;
import com.travis.awesome.positions2.levels.Level2;

public class GameThread extends Thread {
	final static public String tag = "Tracer";

	private MainGamePanel view;
	private boolean running = false;

	
	static final long FPS = 30; 
	
	//Thread variables
	private int level;
	
	Level levelInstance;	
	
	public GameThread(MainGamePanel view, int level) {
		Log.d(tag, "inside GameThread");
		this.view = view;
		this.level = level;
		GenerateLevel(level);

	}

	
	private void GenerateLevel(int level)
	{
		switch(level){
			case (1):
				levelInstance = new Level1();
				break;
			case (2):
				levelInstance = new Level2();				
				break;
		}
	}
	
	public void setRunning(boolean run) {
		Log.d(tag, "inside GameThread - setRunning");
		running = run; 
	}

	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
		long startTime;
		long sleepTime;
		Log.d(tag, "inside GameThread - run");

		while (running) { 
			Canvas c = null; 
			Log.d(tag, "inside GameThread - run - while loop");
			startTime = System.currentTimeMillis(); 
			try 
			{
				
				
				c = view.getHolder().lockCanvas(); 
				synchronized (view.getHolder()) { 
					view.onDraw(c); 
				}
			} 
			finally 
			{
				if (c != null) 
				{
					view.getHolder().unlockCanvasAndPost(c); 
				}
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime); 
			try {
				if (sleepTime > 0) 
				{
					sleep(sleepTime); 
				} 
			} 
			catch (Exception e) 
			{

			}

		}
	}
}