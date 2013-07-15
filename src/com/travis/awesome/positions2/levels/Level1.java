package com.travis.awesome.positions2.levels;

import android.graphics.Canvas;

import com.travis.awesome.positions2.MainGamePanel;

public class Level1 extends Level {
	
	private final long GAME_LENGTH = 30000;
	private final long BONUS_TIME = 3000;
	private long additional_time = 0;
	
		
	public void initializeLevel(MainGamePanel view)
	{
		super.initializeLevel(view);
		view.target_accel[1] = Math.random(); 
	}

	public void startLevel()
	{
		super.startLevel();
	}
	
	public void updateLevel()
	{
		super.updateLevel();
		//Check if user reached goal
		if ((view.current_normalized_accel[1] < (((view.target_accel[1]*2 - 1) + view.VECTOR_ANGLE_DELTA_THRESHOLD/2)  )
				&& (view.current_normalized_accel[1] > (((view.target_accel[1]*2 - 1) - view.VECTOR_ANGLE_DELTA_THRESHOLD/2)))))
			{
				playSounds();
				this.score++;
				view.target_accel[1] = Math.random();
				additional_time += BONUS_TIME;
			}
		
		//Check if level has finished
		if ((GAME_LENGTH + additional_time - timer.GetCurrentTime())<=0)//GAME OVER
		{
			this.current_state = COMPLETED;
		}
	}
	
	public void finishLevel()
	{
		super.finishLevel();
		timer.PauseTimer();
	}
	
	
	public void playSounds()
	{
		for (int i = 0; i<15; i++)
		{
			view.playDTMFTONE(i);			
		}
		view.stopTone();
	}
	
	

	
	public void drawLevel(Canvas canvas)
	{
		
		DrawVerticalRectangle(canvas);
		DrawTickerRepresentingTarget(canvas);
		//DrawTickerRepresentingY(canvas);
		DrawTickerRepresentingYBox(canvas);
		// DrawCurrentAccelerometerVector(canvas);
	    DrawScore(canvas);	    
	    DrawTimer(canvas);
	}

	public void DrawScore(Canvas canvas)
	{
		canvas.drawText("Score: " + score, 350, 50, view.whitePaint);
	}
	public void DrawTimer(Canvas canvas)
	{
		canvas.drawText("Time left : " + ((GAME_LENGTH + additional_time - timer.GetCurrentTime())/1000), 350, 80, view.whitePaint);
	}
	
	public void DrawCurrentAccelerometerVector(Canvas canvas)
	{
	
		canvas.drawText("x: " + view.current_normalized_accel[0], 50, 50, view.bluePaint);
		canvas.drawText("y: " + view.current_normalized_accel[1], 50, 150, view.bluePaint);
		canvas.drawText("z: " + view.current_normalized_accel[2], 50, 200, view.bluePaint);
	}
	public void DrawVerticalRectangle(Canvas canvas)
	{
		//Draw rectangle at 10% x and 80% y
		canvas.drawRect((float)(view.screen_width/2 - (view.screen_width*0.05)),
				(float)(view.screen_height/2 - (view.screen_height*0.4)),
				(float)(view.screen_width/2 + (view.screen_width*0.05)),
				(float)(view.screen_height/2 + (view.screen_height*0.4)),
				view.whitePaint);
	}
	public void DrawTickerRepresentingTarget(Canvas canvas)
	{
	float ticker_position = (float) (view.target_accel[1] * (view.screen_height*0.8));	

	canvas.drawRect((float)(view.screen_width/2 - (view.screen_width*0.05))- view.TICKER_OVERLAP,
			(float)(view.screen_height/2 - (view.screen_height*0.4) + ticker_position - view.TICKER_THICKNESS/2), 
			(float)(view.screen_width/2 + (view.screen_width*0.05)) + view.TICKER_OVERLAP, 
			(float)(view.screen_height/2 - (view.screen_height*0.4) + ticker_position  + view.TICKER_THICKNESS/2),
			view.greenPaint);

	}
	public void DrawTickerRepresentingY(Canvas canvas)
	{
		float offset =(float) ((view.screen_height*0.8)*((view.current_smoothed_normalized_accel[1]+1)/2));//calculates the position of the ticker
		
		canvas.drawRect((float)(view.screen_width/2 - (view.screen_width*0.05))- view.TICKER_OVERLAP,
				(float)(view.screen_height/2 - (view.screen_height*0.4) + offset - view.TICKER_THICKNESS/2), 
				(float)(view.screen_width/2 + (view.screen_width*0.05)) + view.TICKER_OVERLAP, 
				(float)(view.screen_height/2 - (view.screen_height*0.4) + offset + view.TICKER_THICKNESS/2), 
				view.redPaint);
	}
	
	public void DrawTickerRepresentingYBox(Canvas canvas)
	{
		float offset =(float) ((view.screen_height*0.8)*((view.current_smoothed_normalized_accel[1]+1)/2));//calculates the position of the ticker
		
		//left top right bottom
		canvas.drawRect((float)(view.screen_width/2 - (view.screen_width*0.05))- view.TICKER_OVERLAP,
				(float)(view.screen_height/2 - (view.screen_height*0.4) + offset - view.TICKER_THICKNESS/2 - view.TICKER_THICKNESS), 
				(float)(view.screen_width/2 + (view.screen_width*0.05)) + view.TICKER_OVERLAP, 
				(float)(view.screen_height/2 - (view.screen_height*0.4) + offset + view.TICKER_THICKNESS/2 - view.TICKER_THICKNESS), 
				view.redPaint);

		canvas.drawRect((float)(view.screen_width/2 - (view.screen_width*0.05))- view.TICKER_OVERLAP,
				(float)(view.screen_height/2 - (view.screen_height*0.4) + offset - view.TICKER_THICKNESS/2 + view.TICKER_THICKNESS), 
				(float)(view.screen_width/2 + (view.screen_width*0.05)) + view.TICKER_OVERLAP, 
				(float)(view.screen_height/2 - (view.screen_height*0.4) + offset + view.TICKER_THICKNESS/2 + view.TICKER_THICKNESS), 
				view.redPaint);

		
	}
	
}

/*
 * 
 
 What needs to be in a level?
 
 - The visual controls. There needs to be a canvas that can be controlled from the view
 - The rules of the game. Each time the update level is called. the current input state must be checked to see if there is a state shift
 - updateLevel has to return the current state
 
 
 
 
 */
