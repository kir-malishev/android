package ru.samsung.itschool.magicball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ActivityResult extends Activity {
	
	private TextView text;
	
	String right;
	String i;

	  protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.result);
		    
		    
		    Bundle extras = getIntent().getExtras();
		    if (extras != null) {
		         right = extras.getString("countsol");
		         i = extras.getString("countqq");
		    }

            text = (TextView)findViewById(R.id.countright);
            
            text.setText(right + " из " + i);
		  }
	  
	  public void startmenu(View view)  
	    {  
		  Intent intent = new Intent(this, LoginActivity.class);
		  startActivity(intent);
	    }
	}

/*
new Handler().postDelayed(new Runnable() {
@Override
public void run() {
    startActivity(new Intent(getApplicationContext(), ActivityMenu.class));
}
}, 3000);

text.setText("Верно " + right + " из " + i);
*/