package com.travis.awesome.positions2;

import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

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
	
	public GameThread(MainGamePanel view, int level) {
		Log.d(tag, "inside GameThread");
		this.view = view;
		this.level = level;
		GenerateLevel(view);
	}

	
	private void GenerateLevel(MainGamePanel view)
	{
		switch(level){
			case (1):
				view.level_instance = new Level1();
				break;
			case (2):
				view.level_instance = new Level2();				
				break;
		}
		view.level_instance.initializeLevel(view);
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

		view.level_instance.startLevel();
		view.level_instance.timer.StartTimer();//Start the game timer

		while (running && view.level_instance.current_state == view.level_instance.RUNNING) { 
			Canvas c = null; 
			Log.d(tag, "inside GameThread - run - while loop");
			startTime = System.currentTimeMillis(); 
			try 
			{
				c = view.getHolder().lockCanvas(); 
				synchronized (view.getHolder()) { 
					view.level_instance.updateLevel();
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
		view.level_instance.finishLevel();
		view.FinishLevel();
	}
}