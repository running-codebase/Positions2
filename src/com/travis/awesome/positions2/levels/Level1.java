package com.travis.awesome.positions2.levels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas.VertexMode;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.widget.HorizontalScrollView;

import com.travis.awesome.positions2.MainGamePanel;

public class Level1 extends Level {
	
	private final long GAME_LENGTH = 30000;
	private final long BONUS_TIME = 3000;
	private long additional_time = 0;
	private float percent_complete = 0;
	private int completion_goal = 25;

	
	private float bar_thickness;
	private float bar_height;
	private float screen_vertical_middle;
	private float screen_horizonatal_middle;
	private float target_thickness;
	private float target_total_width;
	private float target_time_left_width;
	private float selector_width;
	private float border_thickness;
	private float selector_seperation;
	private float score_radius;

	private Path top_selector_border_path= new Path();
	private Path top_selector_path = new Path();
	private Path bottom_selector_path = new Path();
	private Path bottom_selector_border_path= new Path();
	
	
	public void initializeLevel(MainGamePanel view)
	{
		super.initializeLevel(view);
		view.target_accel[1] = Math.random(); 
		initializeVisualVariables();
	}

	public void startLevel()
	{
		super.startLevel();
	}
	
	public void updateLevel()
	{
		super.updateLevel();
		long time_left = (GAME_LENGTH + additional_time - timer.GetCurrentTime());

		//Check if user reached goal
		if ((view.current_normalized_accel[1] < (((view.target_accel[1]*2 - 1) + view.VECTOR_ANGLE_DELTA_THRESHOLD/2)  )
				&& (view.current_normalized_accel[1] > (((view.target_accel[1]*2 - 1) - view.VECTOR_ANGLE_DELTA_THRESHOLD/2)))))
			{
				playSounds();
				this.score++;
				view.target_accel[1] = Math.random();
				additional_time += BONUS_TIME;
				percent_complete = (float)score/completion_goal;
			}
		
		
		
		
		//Check if level has finished
		if (time_left <=0)//GAME OVER
		{
			this.current_state = COMPLETED;
		}
		else if (score>=completion_goal)
		{
			this.current_state = COMPLETED;
		}
		else
		{
			float total_width_percent = Math.max(Math.min(((float)time_left/GAME_LENGTH),1.0f), 0.0f);//Force the values between 1 and 0
			float time_left_percent = Math.max(Math.min(((float)(time_left - additional_time)/GAME_LENGTH), 1.0f), 0.0f);
			
			target_total_width =  (float) (view.screen_width*0.95*total_width_percent);
			target_time_left_width = (float)(view.screen_width*0.95*time_left_percent);
			
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

	
	public void initializeVisualVariables(){

		
		screen_vertical_middle = view.screen_height/2;
		screen_horizonatal_middle = view.screen_width/2;
		bar_thickness = (float) (view.screen_width*0.02);
		bar_height = (float) (view.screen_height*0.8);
		target_thickness = (float) (bar_thickness*0.33);
		selector_width = bar_thickness*2;
		border_thickness = (float) (bar_thickness*0.2);
		selector_seperation = (float) (target_thickness*3);
		score_radius = (float) (view.screen_width*0.1);
	}
	
	
	public void drawLevel(Canvas canvas)
	{
		DrawBackground(canvas);
		//DrawScoreCircle(canvas);
		DrawVerticalRectangle(canvas);
		DrawTickerRepresentingTarget(canvas);
		DrawSelector(canvas);
		
		
		
		
		//  DrawTickerRepresentingY(canvas);
		//  DrawTickerRepresentingYBox(canvas);
		//  DrawCurrentAccelerometerVector(canvas);
		//  DrawScore(canvas);	    
		//  DrawTimer(canvas);
	    DrawDebug(canvas);
	}

	public void DrawScore(Canvas canvas)
	{
		canvas.drawText("Score: " + score, 350, 50, view.blackPaint);
	}
	public void DrawTimer(Canvas canvas)
	{
		canvas.drawText("Time left : " + ((GAME_LENGTH + additional_time - timer.GetCurrentTime())/1000), 350, 80, view.blackPaint);
	}
	
	public void DrawDebug(Canvas canvas)
	{
		long time_left = (GAME_LENGTH + additional_time - timer.GetCurrentTime());
		
		canvas.drawText("Target accel: " +  (view.target_accel[1]*2 - 1),350, 50, view.blackPaint);
		canvas.drawText("current accel: " + view.current_normalized_accel[1] , 350, 80, view.blackPaint);
		//canvas.drawText("Percentage: " + (((float)time_left)/GAME_LENGTH), 350, 110, view.blackPaint);
		
	
	}
	
	
	
	public void DrawBackground(Canvas canvas)
	{
		canvas.drawColor(Color.WHITE);
	}
	public void DrawScoreCircle(Canvas canvas){
		
		canvas.drawCircle(screen_horizonatal_middle, screen_vertical_middle, score_radius + border_thickness , view.blackPaint);
		canvas.drawCircle(screen_horizonatal_middle, screen_vertical_middle, score_radius, view.whitePaint);

		if (percent_complete!=0)
		{
			canvas.drawCircle(screen_horizonatal_middle, screen_vertical_middle, score_radius*percent_complete, view.blackPaint);
		}
	}
	
	
	public void DrawCurrentAccelerometerVector(Canvas canvas)
	{
	
		canvas.drawText("x: " + view.current_normalized_accel[0], 50, 50, view.bluePaint);
		canvas.drawText("y: " + view.current_normalized_accel[1], 50, 150, view.bluePaint);
		canvas.drawText("z: " + view.current_normalized_accel[2], 50, 200, view.bluePaint);
	}
	public void DrawVerticalRectangle(Canvas canvas)
	{

		canvas.drawRect(screen_horizonatal_middle - bar_thickness/2 - border_thickness ,
				screen_vertical_middle - bar_height/2 - border_thickness,
				screen_horizonatal_middle + bar_thickness/2 + border_thickness,
				screen_vertical_middle + bar_height/2 + border_thickness,
				view.blackPaint);
		
		canvas.drawRect(screen_horizonatal_middle - bar_thickness/2 ,
				screen_vertical_middle - bar_height/2,
				screen_horizonatal_middle + bar_thickness/2 ,
				screen_vertical_middle + bar_height/2,
				view.whitePaint);

	}
	
	public void DrawTickerRepresentingTarget(Canvas canvas)
	{
		float ticker_position = (float) (view.target_accel[1] * (view.screen_height*0.8) + (screen_vertical_middle - bar_height/2));	
	
		canvas.drawRect(screen_horizonatal_middle - target_total_width/2 - border_thickness, 
				ticker_position - target_thickness/2 - border_thickness,
				screen_horizonatal_middle + target_total_width/2 + border_thickness,
				ticker_position + target_thickness/2 + border_thickness,
				view.blackPaint);
	
		canvas.drawRect(screen_horizonatal_middle - target_total_width/2, 
				ticker_position - target_thickness/2,
				screen_horizonatal_middle + target_total_width/2,
				ticker_position + target_thickness/2,
				view.greenPaint);

		canvas.drawRect(screen_horizonatal_middle - target_time_left_width/2, 
				ticker_position - target_thickness/2,
				screen_horizonatal_middle + target_time_left_width/2,
				ticker_position + target_thickness/2,
				view.redPaint);
		
		//Draw Ticker Brackets
		
		canvas.drawRect(
				(float)(screen_horizonatal_middle - (view.screen_width*0.95/2) - 2*border_thickness),
				ticker_position - target_thickness/2 - border_thickness,
				(float)(screen_horizonatal_middle - (view.screen_width*0.95/2) - border_thickness),
				ticker_position + target_thickness/2 + border_thickness,
				view.blackPaint);
		
		canvas.drawRect(
				(float)(screen_horizonatal_middle - (view.screen_width*0.95/2) - border_thickness),
				ticker_position - target_thickness/2 - 2*border_thickness,
				(float)(screen_horizonatal_middle - (view.screen_width*0.95/2)) + bar_thickness,
				ticker_position - target_thickness/2 - border_thickness,
				view.blackPaint);
		canvas.drawRect(
				(float)(screen_horizonatal_middle - (view.screen_width*0.95/2) - border_thickness),
				ticker_position + target_thickness/2 + 2*border_thickness,
				(float)(screen_horizonatal_middle - (view.screen_width*0.95/2)) + bar_thickness,
				ticker_position + target_thickness/2 + border_thickness,
				view.blackPaint);
		
		
		
		
		
		
}
	

	public void DrawTickerBrackets(Canvas canvas)
	{
		
		// left, top , right , bottom
		
		
		
	}
	public void DrawSelector(Canvas canvas)
	{
		float offset =(float)( screen_vertical_middle + bar_height/2 * view.current_smoothed_normalized_accel[1]);//calculates the position of the ticker

		
		top_selector_path.reset();
		top_selector_path.moveTo(screen_horizonatal_middle, offset - selector_seperation/2);
		top_selector_path.lineTo(screen_horizonatal_middle - selector_width/2, offset - selector_seperation/2);
		top_selector_path.lineTo(screen_horizonatal_middle - selector_width, offset - selector_seperation/2 - selector_width/2);
		top_selector_path.lineTo(screen_horizonatal_middle - selector_width + target_thickness, offset - selector_seperation/2 - selector_width/2 - target_thickness);
		top_selector_path.lineTo(screen_horizonatal_middle - selector_width/2 + target_thickness/2 , offset - selector_seperation/2 - target_thickness);
		top_selector_path.lineTo(screen_horizonatal_middle + selector_width/2 - target_thickness/2, offset - selector_seperation/2 - target_thickness);
		top_selector_path.lineTo(screen_horizonatal_middle + selector_width - target_thickness, offset - selector_seperation/2 - selector_width/2 - target_thickness);
		top_selector_path.lineTo(screen_horizonatal_middle + selector_width, offset - selector_seperation/2 - selector_width/2);
		top_selector_path.lineTo(screen_horizonatal_middle + selector_width/2, offset - selector_seperation/2);
		top_selector_path.lineTo(screen_horizonatal_middle, offset - selector_seperation/2);

		top_selector_border_path.reset();
		top_selector_border_path.moveTo(screen_horizonatal_middle , offset - selector_seperation/2 + border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle - selector_width/2 - border_thickness, offset - selector_seperation/2 + border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle - selector_width - 2*border_thickness, offset - selector_seperation/2 - selector_width/2 - border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle - selector_width + target_thickness - border_thickness, offset - selector_seperation/2 - selector_width/2 - target_thickness - 2*border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle - selector_width/2 + target_thickness/2 + border_thickness, offset - selector_seperation/2 - target_thickness - border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle + selector_width/2 - target_thickness/2 - border_thickness, offset - selector_seperation/2 - target_thickness - border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle + selector_width - target_thickness + border_thickness, offset - selector_seperation/2 - selector_width/2 - target_thickness - 2*border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle + selector_width + 2*border_thickness, offset - selector_seperation/2 - selector_width/2 - border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle + selector_width/2 + border_thickness, offset - selector_seperation/2 + border_thickness);
		top_selector_border_path.lineTo(screen_horizonatal_middle , offset - selector_seperation/2 + border_thickness);

		
		bottom_selector_path.reset();
		bottom_selector_path.moveTo(screen_horizonatal_middle, offset + selector_seperation/2);
		bottom_selector_path.lineTo(screen_horizonatal_middle - selector_width/2, offset + selector_seperation/2);
		bottom_selector_path.lineTo(screen_horizonatal_middle - selector_width, offset + selector_seperation/2 + selector_width/2);
		bottom_selector_path.lineTo(screen_horizonatal_middle - selector_width + target_thickness, offset + selector_seperation/2 + selector_width/2 + target_thickness);
		bottom_selector_path.lineTo(screen_horizonatal_middle - selector_width/2 + target_thickness/2 , offset + selector_seperation/2 + target_thickness);
		bottom_selector_path.lineTo(screen_horizonatal_middle + selector_width/2 - target_thickness/2, offset + selector_seperation/2 + target_thickness);
		bottom_selector_path.lineTo(screen_horizonatal_middle + selector_width - target_thickness, offset + selector_seperation/2 + selector_width/2 + target_thickness);
		bottom_selector_path.lineTo(screen_horizonatal_middle + selector_width, offset + selector_seperation/2 + selector_width/2);
		bottom_selector_path.lineTo(screen_horizonatal_middle + selector_width/2, offset + selector_seperation/2);
		bottom_selector_path.lineTo(screen_horizonatal_middle, offset + selector_seperation/2);

		
		bottom_selector_border_path.reset();
		bottom_selector_border_path.moveTo(screen_horizonatal_middle , offset + selector_seperation/2 - border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle - selector_width/2 - border_thickness, offset + selector_seperation/2 - border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle - selector_width - 2*border_thickness, offset + selector_seperation/2 + selector_width/2 + border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle - selector_width + target_thickness - border_thickness, offset + selector_seperation/2 + selector_width/2 + target_thickness + 2*border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle - selector_width/2 + target_thickness/2 + border_thickness, offset + selector_seperation/2 + target_thickness + border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle + selector_width/2 - target_thickness/2 - border_thickness, offset + selector_seperation/2 + target_thickness + border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle + selector_width - target_thickness + border_thickness, offset + selector_seperation/2 + selector_width/2 + target_thickness + 2*border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle + selector_width + 2*border_thickness, offset + selector_seperation/2 + selector_width/2 + border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle + selector_width/2 + border_thickness, offset + selector_seperation/2 - border_thickness);
		bottom_selector_border_path.lineTo(screen_horizonatal_middle, offset + selector_seperation/2 - border_thickness);

		
		
		canvas.drawPath(top_selector_border_path, view.blackPaint);
		canvas.drawPath(top_selector_path, view.whitePaint);		
		canvas.drawPath(bottom_selector_border_path, view.blackPaint);
		canvas.drawPath(bottom_selector_path, view.whitePaint);

	}
	
	


	/*
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
	*/
	
	
}

/*
 * 
 
 What needs to be in a level?
 
 - The visual controls. There needs to be a canvas that can be controlled from the view
 - The rules of the game. Each time the update level is called. the current input state must be checked to see if there is a state shift
 - updateLevel has to return the current state
 
 
 
 
 */
