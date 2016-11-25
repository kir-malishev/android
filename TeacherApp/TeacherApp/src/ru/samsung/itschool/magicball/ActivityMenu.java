package ru.samsung.itschool.magicball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityMenu extends Activity {

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.menu);
	  }
	
	public void play(View view)  
    {  
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
    }
}