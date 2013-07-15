package com.travis.awesome.positions2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.travis.awesome.positions2.levels.Level;



public class MainGamePanel extends SurfaceView implements SensorEventListener {
	final static public String tag = "Tracer";
	public final double VECTOR_ANGLE_DELTA_THRESHOLD = 0.002;
	public final int TICKER_THICKNESS = 4;
	public final int TICKER_OVERLAP = 6;
	public final int SMOOTHER_SAMPLE_SIZE = 10;
	
	//Services
	private WindowManager windowManager;
	private Display display;
	private SensorManager mSensorManager;
 	private Sensor mAccelerometer;
	private ToneGenerator tg;


	private GameThread gameThread;  
	private SurfaceHolder holder; 

	//Drawing Variables
	public Paint bluePaint;
	public Paint whitePaint;
	public Paint redPaint;
	public Paint greenPaint;
	
	//Class variables
	Context context;
	List<AccelerometerVectorPoint> accelerometer_vector_points = new ArrayList<AccelerometerVectorPoint>();
	public int screen_width;
	public int screen_height;
	public double current_accel[] = new double[3];
	public double current_normalized_accel[] = new double[3];
	public double current_smoothed_normalized_accel[] = new double[3];
	public double target_accel[] = new double[3];
	public double dot_product;

	//Game Variables
	public Level level_instance;
	
	
public MainGamePanel(Context context, int level) 
	{
	    super(context);
	    this.context = context;
	    this.setKeepScreenOn(true);
	    
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
        tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

	}
	
	public void initializeColours()
	{
	    bluePaint = new Paint();
	    whitePaint= new Paint();
	    redPaint= new Paint();
	    greenPaint= new Paint();
	    
	    bluePaint.setColor(Color.BLUE);
	    whitePaint.setColor(Color.WHITE);
	    redPaint.setColor(Color.RED);
	    greenPaint.setColor(Color.GREEN);
	    whitePaint.setTextSize(23);
	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawColor(Color.BLACK);
		level_instance.drawLevel(canvas);

	}
	


	@Override
	public void onSensorChanged(SensorEvent event) {
	    if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
	 	
        current_accel[0] = event.values[0];
        current_accel[1] = event.values[1];
        current_accel[2] = event.values[2];
        normalizeAccel();
        UpdateSmootherList(0);//TODO - add the timestamp data here
        
        dot_product =  dotProduct(current_accel[0], current_accel[1], current_accel[2], target_accel[0], target_accel[1], target_accel[2]);
        if (dot_product<VECTOR_ANGLE_DELTA_THRESHOLD) //Under the set Threshold
        {

        }
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void playDTMFTONE(int tone_number)
	{
		switch (tone_number){

		case (0):
			tg.startTone(ToneGenerator.TONE_DTMF_0);			
			break;
		case (1):
			tg.startTone(ToneGenerator.TONE_DTMF_1);			
			break;
		case (2):
			tg.startTone(ToneGenerator.TONE_DTMF_2);			
			break;
		case (3):
			tg.startTone(ToneGenerator.TONE_DTMF_3);						
			break;
		case (4):
			tg.startTone(ToneGenerator.TONE_DTMF_4);			
			break;
		case (5):
			tg.startTone(ToneGenerator.TONE_DTMF_5);			
			break;
		case (6):
			tg.startTone(ToneGenerator.TONE_DTMF_6);			
			break;
		case (7):
			tg.startTone(ToneGenerator.TONE_DTMF_7);			
			break;
		case (8):
			tg.startTone(ToneGenerator.TONE_DTMF_8);			
			break;
		case (9):
			tg.startTone(ToneGenerator.TONE_DTMF_9);			
			break;		
			
		case (10):
			tg.startTone(ToneGenerator.TONE_DTMF_S);			
			break;		
		case (11):
			tg.startTone(ToneGenerator.TONE_DTMF_P);			
			break;		
		case (12):
			tg.startTone(ToneGenerator.TONE_DTMF_A);			
			break;		
		case (13):
			tg.startTone(ToneGenerator.TONE_DTMF_B);			
			break;		
		case (14):
			tg.startTone(ToneGenerator.TONE_DTMF_C);			
			break;		
		case (15):
			tg.startTone(ToneGenerator.TONE_DTMF_D);			
			break;		
		default:
			
			break;
		}
		
		
	}

	public void stopTone(){
		tg.stopTone();
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

	
	public void UpdateSmootherList(double timestamp)
	{
		AccelerometerVectorPoint current_point = new AccelerometerVectorPoint(current_normalized_accel, timestamp, true);
		accelerometer_vector_points.add(current_point);
		if (accelerometer_vector_points.size()> SMOOTHER_SAMPLE_SIZE)
		{
			accelerometer_vector_points.remove(0);
		}
		current_smoothed_normalized_accel[0] = 0;
		current_smoothed_normalized_accel[1] = 0;
		current_smoothed_normalized_accel[2] = 0;
		
		
		for (int i = 0; i <accelerometer_vector_points.size(); i++)
		{
			current_smoothed_normalized_accel[0] += accelerometer_vector_points.get(i).normalized_accel[0];
			current_smoothed_normalized_accel[1] += accelerometer_vector_points.get(i).normalized_accel[1];
			current_smoothed_normalized_accel[2] += accelerometer_vector_points.get(i).normalized_accel[2];
		}
		current_smoothed_normalized_accel[0] = current_smoothed_normalized_accel[0]/ accelerometer_vector_points.size();
		current_smoothed_normalized_accel[1] = current_smoothed_normalized_accel[1]/ accelerometer_vector_points.size();
		current_smoothed_normalized_accel[2] = current_smoothed_normalized_accel[2]/ accelerometer_vector_points.size();
	}
	
	public class AccelerometerVectorPoint 
	{
		double normalized_accel[] = new double[3];
		double time_stamp;
		
		
		public AccelerometerVectorPoint(double[] vector, double time_stamp, boolean normalized)
		{
			this.time_stamp = time_stamp;
			if (normalized)
			{
				normalized_accel[0] = vector[0];
				normalized_accel[1] = vector[1];
				normalized_accel[2] = vector[2];
			}
			else
			{
				double vector_length = Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1] + vector[2]*vector[2]);
				normalized_accel[0] = vector[0]/vector_length;
				normalized_accel[1] = vector[1]/vector_length;
				normalized_accel[2] = vector[2]/vector_length;
			}
		}
	}
	
}


/*
 - So for the first one we orbit around the y axis (-1 to 1)
  
  	I want to add a filter that stores the values
  	It would be an array of current vector positions
  	Then I would have a list of them and add on to the end and drop the first and then sum over and divide to get the total
 
  	Now there needs to be a score that is drawn that incrementsd
  Now there needs to be a beep whenever one is hit and a countdown 
  There needs to be a timer
  There needs to be a number that is added to the timer whenever the point is found
  
  
  
 */ 
