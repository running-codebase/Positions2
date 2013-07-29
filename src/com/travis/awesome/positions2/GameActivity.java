package com.travis.awesome.positions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity  extends Activity {
	
	private int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GetExtras();
        
        MainGamePanel gamePanel = new MainGamePanel(this, level);
        setContentView(gamePanel);
	}
	
	public void GetExtras()
	{
		Intent intent = getIntent();
		level = intent.getIntExtra("Level", 1);
	}
}
