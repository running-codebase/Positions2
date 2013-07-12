package com.travis.awesome.positions2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;



public class MainGamePanel extends SurfaceView implements SensorEventListener {
	final static public String tag = "Tracer";
	final static int VECTOR_ANGLE_DELTA_THRESHOLD = 5;
	final static int TICKER_THICKNESS = 4;
	final static int TICKER_OVERLAP = 6;
	
	
	//Services
	private WindowManager windowManager;
	private Display display;
	private SensorManager mSensorManager;
 	private Sensor mAccelerometer;
 	

	private GameThread gameThread;  
	private SurfaceHolder holder; 

	//Drawing Variables
	Paint bluePaint;
	Paint whitePaint;
	Paint redPaint;
	
	//Class variables
	Context context;
	public int screen_width;
	public int screen_height;
	public double current_accel[] = new double[3];
	public double current_normalized_accel[] = new double[3];
	public double target_accel[] = new double[3];
	public double dot_product;

	
	
	
	
public MainGamePanel(Context context, int level) 
	{
	    super(context);
	    this.context = context;
	
	    initScreen();
	    initServices();
	    initializeColours();
	    
	    gameThread = new GameThread(this, level); 
	    holder = getHolder();
	    holder.addCallback(new SurfaceHolder.Callback() {

	        @Override
	        public void surfaceDestroyed(SurfaceHolder holder) {
	            boolean retry = true;
	            Log.d(tag, "Inside SurfaceHolder Callback - surfaceDestroyed");
	            gameThread.setRunning(false); // Stop the Thread from running because the surface was destroyed.  Can't play a game with no surface!!  

	            while (retry) { 
	                try {
	                    Log.d(tag, "Inside SurfaceHolder Callback - surfaceDestroyed - while statement");
	                    gameThread.join();
	                    retry = false; //Loop until game thread is done, making sure the thread is taken care of.
	                } catch (InterruptedException e) {
	                    //  In case of catastrophic failure catch error!!!
	                }
	            }

	        }

	        @Override
	        public void surfaceCreated(SurfaceHolder holder) {
	            // let there be Surface!
	            Log.d(tag, "Inside SurfaceHolder Callback - surfaceCreated");
	            gameThread.setRunning(true); // Now we start the thread
	            gameThread.start(); // and begin our game's logical processing

	        }

	        @Override
	        public void surfaceChanged(SurfaceHolder holder, int format,
	                int width, int height) {
	            // The code to resize the screen ratio when it flips from landscape to portrait and vice versa

	        }
	    });
	}
	private void initScreen()
	{
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;
	}
	
	private void initServices()
	{
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}
	
	public void initializeColours()
	{
	    bluePaint = new Paint();
	    whitePaint= new Paint();
	    redPaint= new Paint();
	    bluePaint.setColor(Color.BLUE);
	    whitePaint.setColor(Color.WHITE);
	    redPaint.setColor(Color.RED);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawColor(Color.BLACK);
		DrawVerticalRectangle(canvas);
		DrawTickerRepresentingY(canvas);
	    DrawCurrentAccelerometerVector(canvas);

	
	}
	
	public void DrawCurrentAccelerometerVector(Canvas canvas)
	{
	
		//canvas.drawRect(0, 10,100,50, bluePaint);	
		canvas.drawText("x: " + current_normalized_accel[0], 50, 50, bluePaint);
		canvas.drawText("y: " + current_normalized_accel[1], 50, 150, bluePaint);
		canvas.drawText("z: " + current_normalized_accel[2], 50, 200, bluePaint);
		
		
		
	/*
	 * Draw a number
	 * Make it count
	 * Draw the accelerometer values
	 * Turn that into a number from 0 to 180 
	
			 
	 */
	}
	
	public void DrawVerticalRectangle(Canvas canvas)
	{
		//Draw rectangle at 10% x and 80% y
		canvas.drawRect((float)(screen_width/2 - (screen_width*0.05)), (float)(screen_height/2 - (screen_height*0.4)), (float)(screen_width/2 + (screen_width*0.05)), (float)(screen_height/2 + (screen_height*0.4)), whitePaint);
	}

	public void DrawTickerRepresentingY(Canvas canvas)
	{
		float offset =(float) ((screen_height*0.8)*((current_normalized_accel[1]+1)/2));//calculates the position of the ticker
		
		canvas.drawRect((float)(screen_width/2 - (screen_width*0.05))- TICKER_OVERLAP,
				(float)(screen_height/2 - (screen_height*0.4) + offset - TICKER_THICKNESS/2), 
				(float)(screen_width/2 + (screen_width*0.05)) + TICKER_OVERLAP, 
				(float)(screen_height/2 - (screen_height*0.4) + offset + TICKER_THICKNESS/2), redPaint);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	    if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
	 	
        current_accel[0] = event.values[0];
        current_accel[1] = event.values[1];
        current_accel[2] = event.values[2];
        normalizeAccel();
        
        dot_product =  dotProduct(current_accel[0], current_accel[1], current_accel[2], target_accel[0], target_accel[1], target_accel[2]);
        if (dot_product<VECTOR_ANGLE_DELTA_THRESHOLD) //Under the set Threshold
        {

        }
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
		return 180/Math.PI*Math.acos((x1*x2 + y1*y2 + z1*z2)/ (Math.sqrt((x1*x1 + y1*y1 + z1*z1)) * Math.sqrt(x2*x2 + y2*y2 + z2*z2)));
	}

	public void normalizeAccel()
	{
		double vector_length = Math.sqrt(current_accel[0]*current_accel[0] + current_accel[1]*current_accel[1] + current_accel[2]*current_accel[2]);
		current_normalized_accel[0] = current_accel[0]/vector_length;
		current_normalized_accel[1] = current_accel[1]/vector_length;
		current_normalized_accel[2] = current_accel[2]/vector_length;
	}
	
}


/*
 - So for the first one we orbit around the y axis (-1 to 1)
  
 */ 
