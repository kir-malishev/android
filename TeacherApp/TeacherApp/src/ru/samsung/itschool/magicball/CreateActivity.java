package ru.samsung.itschool.magicball;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint({ "CommitPrefEdits", "UseValueOf" })
public class CreateActivity extends Activity implements OnItemClickListener {

	AlertDialog.Builder ad;
	Context context;
	final ArrayList<Item> answers = new ArrayList<Item>();
	ArrayAdapter<String> adapter;
	ListView list;
	public static final String DATA_FOR_TEST = "DataForTest";
	public static final String DATA_FOR_AUTH = "DataForAuth";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtest);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		list = (ListView) findViewById(R.id.answers);

	}

	public String getlogin() {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		return sharedPref.getString("Login", "");
	}
	
	public String getpass() {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		return sharedPref.getString("Password", "");
	}

	public void setadd(String header, String subHeader, String type) {
		answers.add(new Item(header, subHeader, type));
		list.setAdapter(new MyAdapter(this, answers));
		list.setOnItemClickListener(this);
	}

	public void showtoast(String text) {
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void newans(View v) {
		if (answers.size() <= 200)
			choicetypeans();
		else
			showtoast(getString(R.string.cannotadd));
	}

	public void removeans(View v) {
		int size = answers.size();
		if (size >= 1) {
			answers.remove(size - 1);
			list.setAdapter(new MyAdapter(this, answers));
			list.setOnItemClickListener(this);
			SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
			Editor editor = sharedPref.edit();
			String login = getlogin();
			editor.remove(login + "_type_" + (size - 1));
			editor.remove(login + "_qq_" + (size - 1));
			editor.remove(login + "_size_" + (size - 1));
			for (int j = 0; j < 8; j++)
				editor.remove(login + "_ans_" + (size - 1) + "_" + j);
			editor.commit();
		} else
			showtoast(getString(R.string.cannotdiminish));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		String type = answers.get(position).getType();

		Intent intent = new Intent(CreateActivity.this, CreateActivity.class);
		;
		if (type.equals("choice")) {
			intent = new Intent(CreateActivity.this, CreateChoiceActivity.class);
		} else if (type.equals("input")) {
			intent = new Intent(CreateActivity.this, CreateInputActivity.class);
		}

		// в ключ username пихаем текст из первого текстового поля
		intent.putExtra("position", position);
		intent.putExtra("type", answers.get(position).getType());
		startActivity(intent);

	}

	RequestTask task;
	private ProgressDialog dialog;
	
	public void createnewtest(View v) throws JSONException {
		//toJSON();
		if (toJSON())
		{
			showtoast(resultJson.toString());
			task = new RequestTask();
			task.execute("http://www.malishevkir.fvds.ru/api/scripts/gettest.php");
		}
	}
	
	JSONObject resultJson;

	public boolean toJSON() throws JSONException {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
		String login = getlogin();
		int size = answers.size();
		if (size < 1) {
			showtoast(getString(R.string.noans));
			return false;
		}
		String type;
		String quest;
		int amt;
		String[] ans = new String[8];
		JSONArray test = new JSONArray();
		resultJson = new JSONObject();
		for (int j = 0; j < size; j++) {
			JSONObject answer = new JSONObject();
			type = sharedPref.getString(login + "_type_" + j, "");
			quest = sharedPref.getString(login + "_qq_" + j, "");
			amt = sharedPref.getInt(login + "_size_" + j, 0);
			for (int g = 0; g < 8; g++) {
				ans[g] = sharedPref.getString(login + "_ans_" + j + "_" + g, "");
			}
			if (quest.equals("") || (type.equals("choice") && (ans[0].equals("") || ans[1].equals(""))
					|| (type.equals("input") && ans[0].equals("")))) {
				showtoast(getString(R.string.forgetred) + (j + 1));
				return false;
			} else {
				answer.put("type", type);
				answer.put("question", quest);
				int k = 1;
				for (int g = 0; g < 8; g++) {
					if (ans[g].equals("") == false)
						answer.put("answer" + k++, ans[g]);
				}
				test.put(answer);
			}

		}
		resultJson.put("test", test);
		Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
		return true;

	}

	public void choicetypeans() {

		context = CreateActivity.this;
		String title = "Добавить вопрос";
		String message = "Выберите тип";
		String button1String = "С выбором ответа";
		String button2String = "С вводом ответа";

		ad = new AlertDialog.Builder(context);
		ad.setTitle(title); // заголовок
		ad.setMessage(message); // сообщение
		ad.setPositiveButton(button1String, new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				setadd("Вопрос №" + (answers.size() + 1), "(с выбором ответа)", "choice");

			}
		});
		ad.setNegativeButton(button2String, new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				setadd("Вопрос №" + (answers.size() + 1), "(с вводом ответа)", "input");

			}
		});
		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				showtoast(getString(R.string.nochoose));
			}
		});
		ad.show();

	}
	
	
	class RequestTask extends AsyncTask<String, String, Boolean> {

		String authresult;
		boolean auth;

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
				HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
				 //создаем запрос на сервер
				HttpClient hc = new DefaultHttpClient(httpParameters);
				hc.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
				hc.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
				hc.getParams().setParameter("http.socket.timeout", new Integer(2000));
				httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
				ResponseHandler<String> res = new BasicResponseHandler();
				// он у нас будет посылать post запрос
				HttpPost postMethod = new HttpPost(params[0]);
				postMethod.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"Project For Step In Future By Kirill Malyshev 1.0");
				postMethod.getParams().setParameter("http.socket.timeout", new Integer(5000));
				// будем передавать два параметра
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				// передаем параметры из наших текстбоксов
				// лоигн
				nameValuePairs.add(new BasicNameValuePair("test", resultJson.toString()));
				nameValuePairs.add(new BasicNameValuePair("login", getlogin()));
				nameValuePairs.add(new BasicNameValuePair("pass", getpass()));
				// собераем их вместе и посылаем на сервер
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
				postMethod.setEntity(entity);
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
					//saveauth(email, password);
					// Отправить дальше
					Intent intent = new Intent(CreateActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();

				} else {

					showtoast(getString(R.string.errorlogin));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			dialog = new ProgressDialog(CreateActivity.this);
			dialog.setMessage("Секундочку");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}
	}
	

}