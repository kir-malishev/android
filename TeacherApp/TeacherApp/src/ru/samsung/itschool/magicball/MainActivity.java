package ru.samsung.itschool.magicball;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("DrawAllocation")
public class MainActivity extends Activity {
	/*int[] qq = {R.string.qq1_1, R.string.qq1_2, R.string.qq1_3, R.string.qq1_4,
		R.string.qq1_5, R.string.qq1_6, R.string.qq1_7, R.string.qq1_8, R.string.qq1_9, R.string.qq1_10};*/
	/*String[][] ans = {{"3", "2", "1", "4"}, {"5", "6", "4", "3"}, {"0", "-2", "-1", "10"},
			{"10", "9", "8", "7"}, {"5", "50", "55", "2"}, {"230", "218", "222", "228"},
			{"111", "121", "131", "110"}, {"3", "4", "2", "0"}, {"130", "142", "136", "132"},
			{"4", "5", "6", "1"}};*/
	/*String[] sol = {"3", "5", "-1", "8", "5", "228", "121", "3", "132", "5"};*/
	int i = 0;
	boolean k = true;
	long lastT = -1;
	String[] num_ans;
	int right = 0;
	private TextView text;
	private TextView timer;
	private Timer myTimer;
	String[] qq;
	String[][] ans = new String[10][4];
	String[] sol;
	String[] a;
	String[][] answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GraphicsView myview=new GraphicsView(this);
        //setContentView(myview);
        
        //ans = new String[10][4];
        
        Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	         qq = extras.getStringArray("qq");
	         sol = extras.getStringArray("sol");
	         /*ans[0] = extras.getStringArray("ans0");
	         ans[1] = extras.getStringArray("ans1");
	         ans[2] = extras.getStringArray("ans2");
	         ans[3] = extras.getStringArray("ans3");
	         ans[4] = extras.getStringArray("ans4");
	         ans[5] = extras.getStringArray("ans5");
	         ans[6] = extras.getStringArray("ans6");
	         ans[7] = extras.getStringArray("ans7");
	         ans[8] = extras.getStringArray("ans8");
	         ans[9] = extras.getStringArray("ans9");*/
	        
	       for (int j = 0; j < 10; j++)
	         {
	        	 String[] a;
	        	 a = extras.getStringArray("ans" + Integer.toString(j));
	        	 for (int g = 0; g < 4; g++)
	        	 {
	        		 ans[j][g] = a[g];
	        	 }
	        	 
	         }
	    }
	    answers = randomize(ans);
	    
	    
        
       
        text = (TextView)findViewById(R.id.message);

        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        text.startAnimation(in);
        
        

   	 	
   	 timer = (TextView) findViewById(R.id.timer);
   	 
   	 greeting();
   	 
    }
   
    String[][] randomize(String[][] a) {
        Random rnd = new Random();
        for (int n = 0; n <= a.length - 1; n++)
        {
        	for (int c = 1; c < 4; c++) {            
                int j = rnd.nextInt(c);
                String temp = a[n][c];
                a[n][c] = a[n][j];
                a[n][j] = temp;
            }
        }
        return a;
    }
    
    //String[][] answers = randomize(ans);
    int mCurrentPeriod = 0;
    public void TimerMethod() {
    	// This method is called directly by the timer
    	// and runs in the same thread as the timer.

    	// We call the method that will work with the UI
    	// through the runOnUiThread method.
    	this.runOnUiThread(Timer_Tick);
    	}
    private Runnable Timer_Tick = new Runnable() {
    	public void run() {
    		mCurrentPeriod++;
        	String temp = (new SimpleDateFormat("mm:ss")).format(new Date(
        	15000 - mCurrentPeriod * 1000));
        	timer.setText(temp);
        	// This method runs in the same thread as the UI.
        	// Do something to the UI thread here
        	if (mCurrentPeriod >= 15)
        	{
        		timer.setText(R.string.end);
        		k = true;
        		i++;
        		/*myTimer.cancel();
        		i++;
        		k = true;
        		greeting();*/
        	}
    	}
    	};
    	public void greeting()
    	{
    		
    		Button ans1 = (Button) findViewById(R.id.answer1);
        	Button ans2 = (Button) findViewById(R.id.answer2);
        	Button ans3 = (Button) findViewById(R.id.answer3);
        	Button ans4 = (Button) findViewById(R.id.answer4);
            if (i == 10)
            {
            	myTimer.cancel();
            	Intent intent = new Intent(this, ActivityResult.class);
            	 String right_str = Integer.toString(right);
            	 String i_str = Integer.toString(i);
            	intent.putExtra("countsol", right_str);
            	intent.putExtra("countqq", i_str);
                startActivity(intent);
                finish();
            }
            else 
            {
            	if (k == true)
                {
               	 	ans1.setText(answers[i][0]);
               	 	ans2.setText(answers[i][1]);
               	 	ans3.setText(answers[i][2]);
               	 	ans4.setText(answers[i][3]);
               	 	text.setText(qq[i]);
               	 	k = false;
                }
            	
            
        }
            mCurrentPeriod = 0;
            timer.setText(R.string.time15);
            myTimer = new Timer();
        	myTimer.schedule(new TimerTask() {
        	@Override   	
        	public void run() {
        	TimerMethod();
        	}
        	}, 1000, 1000);
    	}
    
    public void next(View view)  
    {  
    	myTimer.cancel();
    	greeting();
    }
	public void answer(View view)  
    {  
		 myTimer.cancel();
    	 Button ans1 = (Button) findViewById(R.id.answer1);
    	 Button ans2 = (Button) findViewById(R.id.answer2);
    	 Button ans3 = (Button) findViewById(R.id.answer3);
    	 Button ans4 = (Button) findViewById(R.id.answer4);
    	 if (k == false)
    	 {   		 
    		 switch (view.getId())
             {
                 case R.id.answer1:
                	 if (answers[i][0].equals(sol[i]))
                	 {
                		 ans1.setText("Верно!");
                		 right++;
                	 }
                	 else ans1.setText("Неверно...");
                     break; 
                 case R.id.answer2:
                	 if (answers[i][1].equals(sol[i]))
                	 {
                		 ans2.setText("Верно!");
                		 right++;
                	 }
                	 else ans2.setText("Неверно...");
                     break;
                 case R.id.answer3:
                	 if (answers[i][2].equals(sol[i]))
                	 {
                		 ans3.setText("Верно!");
                		 right++;
                	 }
                	 else ans3.setText("Неверно...");
                     break;
                 case R.id.answer4:
                	 if (answers[i][3].equals(sol[i]))
                	 {
                		 ans4.setText("Верно!");
                		 right++;
                	 }
                	 else ans4.setText("Неверно...");
                     break; 
    	 }
    		 k = true;
    		 i++;
    	 
         }
    }
    
    
}
