package com.travis.awesome.positions2;


public class Timer {
		
	public final int UNINITIALIZED = 0; 
	public final int PAUSED = 1;
	public final int RUNNING = 2;

	public int timer_state = UNINITIALIZED;
	public long start_time;
	public long additional_passed_time;
	
	public Timer() //Constructor for counting up
	{
		timer_state = PAUSED;
		additional_passed_time = 0;
	}
	
	public void StartTimer()
	{
		if (timer_state == PAUSED)
		{
		start_time = System.currentTimeMillis();
		timer_state = RUNNING;
		}
	}
	
	public void PauseTimer()
	{
		if (timer_state == RUNNING)
		{
			additional_passed_time = System.currentTimeMillis() - start_time;
			timer_state = PAUSED;
		}
	}

	public void ResetTimer()
	{
		additional_passed_time = 0;
		timer_state = PAUSED;
	}
	
	public long GetCurrentTime()
	{
		if (timer_state == RUNNING)
		{
			return (System.currentTimeMillis() - start_time) + additional_passed_time;
		}
		else //timer state is paused or hasn't started 
		{
			return additional_passed_time;
		}
	}
}


/*
FUTURE TODO
 - Add the functionality for a countdown timer that triggers a call back on completion
 - current functionality allows a timer to start, be paused and return time passed
//TODO - add a callback when 0 is reached for counting down
 * 
 * 
 * 
*/

