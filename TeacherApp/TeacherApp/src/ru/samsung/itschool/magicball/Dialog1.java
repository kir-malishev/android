package ru.samsung.itschool.magicball;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
 
public class Dialog1 extends DialogFragment implements OnClickListener {
 
  final String LOG_TAG = "myLogs";
 
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    getDialog().setTitle("Title!");
    //View v = inflater.inflate(R.layout.dialog1, null);
    View v = inflater.inflate(R.layout.dialog1, null);
    v.findViewById(R.id.add).setOnClickListener(this);
    v.findViewById(R.id.remove).setOnClickListener(this);
    v.findViewById(R.id.back).setOnClickListener(this);
    return v;
  }
 
  /*public void onClick(View v) {
    Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
    dismiss();
  }*/
  
	public void showtoast(int text) {

	}
  public void add(View v)
  {
	  
  }
 
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    Log.d(LOG_TAG, "Dialog 1: onDismiss");
  }
 
  public void onCancel(DialogInterface dialog) {
    super.onCancel(dialog);
    Log.d(LOG_TAG, "Dialog 1: onCancel");
  }

@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	
}
}