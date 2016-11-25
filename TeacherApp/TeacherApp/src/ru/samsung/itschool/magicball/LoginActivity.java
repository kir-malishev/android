package ru.samsung.itschool.magicball;

import android.annotation.SuppressLint;
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

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("CommitPrefEdits")
public class LoginActivity extends Activity {

	public EditText login;
	public EditText pass;
	private ProgressDialog dialog;
	RequestTask task;
	String email;
	String password;
	String remlogin;
	String rempass;
	public static final String DATA_FOR_AUTH = "DataForAuth";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		login = (EditText) findViewById(R.id.authemail);
		pass = (EditText) findViewById(R.id.authpass);
		getauth(email, password);
		login.setText(remlogin);
		pass.setText(rempass);
	}

	public void saveauth(String email, String password) {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString("Login", email);
		editor.putString("Password", password);
		editor.commit();
	}

	public void getauth(String... params) {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		remlogin = sharedPref.getString("Login", "");
		rempass = sharedPref.getString("Password", "");
	}

	public void toformreg(View view) {
		Intent intent = new Intent(this, RegActivity.class);
		startActivity(intent);
		finish();
	}

	public boolean isConnected() throws InterruptedException, IOException {
		String command = "ping -c 1 google.com";
		return (Runtime.getRuntime().exec(command).waitFor() == 0);
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public void showtoast(int text) {
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void toformauth(View view) throws InterruptedException, IOException {
		email = login.getText().toString();
		password = pass.getText().toString();
		if (email.equals("")) {
			showtoast(R.string.noemail);
		} else if (password.equals("")) {
			showtoast(R.string.nopass);
		} else if (isConnected() == false) {
			showtoast(R.string.noconnect);
		} else {
			task = new RequestTask();
			task.execute("http://www.malishevkir.fvds.ru/api/scripts/getauth.php");
		}
	}

	class RequestTask extends AsyncTask<String, String, Boolean> {

		String authresult;
		boolean auth;

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
				authresult = response.toString();
				auth = Boolean.valueOf(authresult);
			} catch (Exception e) {
				System.out.println("Exp=" + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			dialog.dismiss();

			try {
				if (auth) {
					saveauth(email, password);
					// Отправить дальше
					Intent intent = new Intent(LoginActivity.this, CreateActivity.class);
					startActivity(intent);
					finish();

				} else {

					showtoast(R.string.errorlogin);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage("Секундочку");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}
	}
}