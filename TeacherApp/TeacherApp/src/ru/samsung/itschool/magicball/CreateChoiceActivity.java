package ru.samsung.itschool.magicball;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateChoiceActivity extends Activity {

	int position;
	String type;
	LinearLayout list;
	LinearLayout.LayoutParams lEditParams;
	private List<EditText> editTextList = new ArrayList<EditText>();
	EditText editTxt;
	EditText rightans;
	EditText qq;
	int size;
	public static final String DATA_FOR_TEST = "DataForTest";
	public static final String DATA_FOR_AUTH = "DataForAuth";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt("position");
			type = extras.getString("type");
		}

		setContentView(R.layout.qq_with_choice);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		TextView label = (TextView) findViewById(R.id.questionlabel);
		label.setText(getString(R.string.numberqq) + (position + 1));

		rightans = (EditText) findViewById(R.id.ans0);
		qq = (EditText) findViewById(R.id.editqq);

		list = (LinearLayout) findViewById(R.id.questions);
		lEditParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		getqq();
	}

	public String getlogin() {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		return sharedPref.getString("Login", "");
	}

	public void saveqq() {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		String login = getlogin();
		editor.putString(login + "_type_" + position, type);
		editor.putString(login + "_qq_" + position, qq.getText().toString());
		editor.putInt(login + "_size_" + position, editTextList.size() + 1);
		editor.putString(login + "_ans_" + position + "_" + 0, rightans.getText().toString());
		for (int i = 0; i < editTextList.size(); i++) {
			editor.putString(login + "_ans_" + position + "_" + (i + 1), editTextList.get(i).getText().toString());
		}
		editor.commit();
	}

	public void getqq() {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
		String login = getlogin();
		int size = sharedPref.getInt(login + "_size_" + position, 1);
		String question = sharedPref.getString(login + "_qq_" + position, "");
		qq.setText(question);
		String ans;
		ans = sharedPref.getString(login + "_ans_" + position + "_" + 0, "");
		rightans.setText(ans);
		int j = 0;
		for (int i = 0; i < size; i++) {
			ans = sharedPref.getString(login + "_ans_" + position + "_" + (i + 1), "");
			if (ans.equals("") == false) {
				addEdit();
				editTextList.get(j++).setText(ans);
			}
		}
		if (editTextList.size() == 0) {
			addEdit();
		}
	}

	public void showtoast(int text) {
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void addEdit() {
		size = editTextList.size();
		if (size <= 6) {
			editTxt = new EditText(this);
			editTxt.setLayoutParams(lEditParams);
			editTxt.setHint("Вариант №" + (size + 2));
			editTxt.setSingleLine(true);
			editTextList.add(editTxt);
			list.addView(editTxt);
		} else {
			showtoast(R.string.mustnot);
		}
	}

	public void add(View v) {
		addEdit();
	}

	public void remove(View v) {
		size = editTextList.size();
		if (size >= 2) {
			editTextList.get(size - 1).setVisibility(View.GONE);
			editTextList.remove(size - 1);
		} else {
			showtoast(R.string.lessnot);
		}
	}

	public void back(View v) {
		saveqq();
		finish();

	}
}