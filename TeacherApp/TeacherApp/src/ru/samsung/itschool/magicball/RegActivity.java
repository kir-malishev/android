package ru.samsung.itschool.magicball;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
public class RegActivity extends Activity {

	public EditText login;
	public EditText pass;
	public EditText pass1;
	private ProgressDialog dialog;
	RequestTask task;
	String email;
	String password;
	String password1;
	public static final String DATA_FOR_AUTH = "DataForAuth";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		login = (EditText) findViewById(R.id.editemail);
		pass = (EditText) findViewById(R.id.editpass);
		pass1 = (EditText) findViewById(R.id.editpass1);
	}

	public void saveauth(String email, String password) {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString("Login", email);
		editor.putString("Password", password);
		editor.commit();
	}

	public void back(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public boolean isConnected() throws InterruptedException, IOException {
		String command = "ping -c 1 google.com";
		return (Runtime.getRuntime().exec(command).waitFor() == 0);
	}

	public void showtoast(int text) {
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void registration(View view) throws InterruptedException, IOException {

		email = login.getText().toString();
		password = pass.getText().toString();
		password1 = pass1.getText().toString();
		if (email.equals("")) {
			showtoast(R.string.noemail);
		} else if (isValidEmail(login.getText().toString()) == false) {
			showtoast(R.string.erremail);
		} else if (password.equals("")) {
			showtoast(R.string.nopass);
		} else if (password.length() < 6) {
			showtoast(R.string.veryshortpass);
		} else if (password1.equals("")) {
			showtoast(R.string.nopass1);
		} else if (password.equals(password1) == false) {
			showtoast(R.string.otherpass);
		} else if (isConnected() == false) {
			showtoast(R.string.noconnect);
		} else {
			task = new RequestTask();
			task.execute("http://www.malishevkir.fvds.ru/api/scripts/getreg.php");
		}
	}

	class RequestTask extends AsyncTask<String, String, Boolean> {

		String regresult;
		boolean reg;
		boolean err;

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				// создаем запрос на сервер
				DefaultHttpClient hc = new DefaultHttpClient();
				ResponseHandler<String> res = new BasicResponseHandler();
				// он у нас будет посылать post запрос
				HttpPost postMethod = new HttpPost(params[0]);
				postMethod.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"Project For Step In Future By Kirill Malyshev 1.0");
				// будем передавать два параметра
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				// передаем параметры из наших текстбоксов
				// лоигн
				nameValuePairs.add(new BasicNameValuePair("login", email));
				// пароль
				nameValuePairs.add(new BasicNameValuePair("pass", password));
				// собераем их вместе и посылаем на сервер
				postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// получаем ответ от сервера
				String response = hc.execute(postMethod, res);
				regresult = response.toString();
				reg = Boolean.valueOf(regresult);
			} catch (IOException e) {
				System.out.println("Exp=" + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			dialog.dismiss();

			try {
				if (reg) {
					saveauth(email, password);
					Intent intent = new Intent(RegActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					showtoast(R.string.emailalreadywas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			dialog = new ProgressDialog(RegActivity.this);
			dialog.setMessage("Секундочку");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}
	}
}