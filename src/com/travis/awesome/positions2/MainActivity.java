package com.travis.awesome.positions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {


	private Button button1;
	private Button button2;
	private Button button3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        InitLayout();

	}

	
	public void InitLayout()
	{
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.layout_game_window, menu);
        return true;
    }
	
	

	@Override
	public void onClick(View view) {
		  
		switch(view.getId()){
		case (R.id.button1):
			StartGame(1);
			break;
		case (R.id.button2):
			StartGame(2);
			break;
		case (R.id.button3):
			StartGame(3);
			break;
		}
	}
	
	
	public void StartGame(int level)
	{
		Intent i = new Intent(this, GameActivity.class);
		i.putExtra("Level", level);
		startActivity(i);
	}
	
}

