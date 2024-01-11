package com.example.allsets;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import java.util.*;
import java.io.*;
import org.json.*;
import android.app.*;
import android.view.View.*;
import android.content.*;
import android.media.*;

import android.os.CountDownTimer;
import java.util.concurrent.*;
import android.preference.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;
import android.os.*;
import android.net.*;
import org.apache.http.client.entity.*;
import org.apache.http.message.*;
import android.database.sqlite.*;
import android.database.*;

public class MainActivity extends Activity {

	TextView t1,ct,sc;
	Button b1;
	RadioButton r1,r2,r3,r4;
	RadioGroup rg;
	String json,value;
	int mid = 0,type = 0,index,count=0;
	ArrayList<set> list;
	MediaPlayer mp;
	CountDownTimer timer,repeat;
	SharedPreferences sp;
	boolean sound;
	boolean isConnected = false;
	JasonParser jp;
	SQLiteDatabase mydb;
	DatabaseHelper db;
	Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		init();
   
		query();
		list = new ArrayList<set>();
		db = new DatabaseHelper(this);
		sp = getSharedPreferences("setting",0);
		sound = sp.getBoolean("sound",true);
		value = getIntent().getStringExtra("value");
		mid = getIntent().getIntExtra("mid",0);
		if(mid!=0)
		{
			type = getIntent().getIntExtra("type",0);
		}
			
		loadJSONFromAsset(value);
		beginJsonParsing();
		start();
		//Toast.makeText(getApplicationContext(),"Mid "+mid+"Type "+type,Toast.LENGTH_SHORT).show();
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(RadioGroup p1, int sid)
				{
					check();
					r1.setChecked(false);
					r2.setChecked(false);
					r3.setChecked(false);
					r4.setChecked(false);
					if(r1.getText().toString().equals(list.get(index).answ))
					{
						r1.setTextColor(Color.GREEN);
					}
					else if(r2.getText().toString().equals(list.get(index).answ))
					{
						r2.setTextColor(Color.GREEN);
					}
					else if(r3.getText().toString().equals(list.get(index).answ))
					{
						r3.setTextColor(Color.GREEN);
					}
					else
					{
						r4.setTextColor(Color.GREEN);
					}
					
				}});
				
		b1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

					r1.setClickable(true);
					r2.setClickable(true);
					r3.setClickable(true);
					r4.setClickable(true);
					r1.setTextColor(Color.BLACK);
					r2.setTextColor(Color.BLACK);
					r3.setTextColor(Color.BLACK);
					r4.setTextColor(Color.BLACK);
					
					try{
					index++;
					if(index>=(list.size()-1))
					{
						b1.setText("finish");
					}
					
					if(index>=(list.size()))
					{
						index = list.size()-1;
						timer.cancel();
						if(mid==0)
						alert();
						else
							game(2);
					}
					
					t1.setText("Q. "+(index+1)+" "+list.get(index).ques);
					r1.setText(list.get(index).opta);
					r2.setText(list.get(index).optb);
					r3.setText(list.get(index).optc);
					r4.setText(list.get(index).optd);
					if(sound)
					{
				    final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.task);
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
							{

								@Override
								public void onCompletion(MediaPlayer p1)
								{
									p1.reset();
									p1.release();
								}

							});
					 mp.start();
					}
					}catch(Exception e)
					{
						t1.setText(e.toString());
					}
				}
			});
    }
/*
	@Override
	protected void onResume()
	{
		
		super.onResume();
		
		try{
			if(!isOnline()==false)
			{

				jp = new JasonParser();
				//json = jp.request("http://friended-correspond.000webhostapp.com/app/userlist.json");	
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("tag",""));
				json = jp.request("http://localhost:8080/list1.json",params);
				try{
					beginJsonParsing("His-Set-1");

				}catch(Exception e)
				{
					t1.setText(e.toString());
				}
				start();
			}
			else
			{
				score("Error","not Connected");

			}
		}catch(Exception e)
		{
			score("Error",e.toString());
		}
	}
	*/
   private void checkDb()
	{
  try{
		mydb = openOrCreateDatabase("allsets.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
   mydb.execSQL("CREATE TABLE IF NOT EXISTS sets (name VARCHAR , score VARCHAR);");
		//mydb.execSQL("CREATE TABLE IF NOT EXISTS sets (name VARCHAR , score VARCHAR);");
   

		Cursor c = mydb.rawQuery("SELECT * FROM sets", null);
		
		while(c.moveToNext())
		{
			String s =  c.getString(c.getColumnIndex("name"));

			if(!value.equals(s))
			{
				ContentValues myInsert1 = new ContentValues();
				myInsert1.put("name",value);
				myInsert1.put("score","1");
				long result = mydb.insert("sets", null, myInsert1);
       if(result == -1)
            Toast.makeText(getApplicationContext(),value +" inserted",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"not inserted",Toast.LENGTH_SHORT).show();

			}
//Toast.makeText(getApplicationContext(),"sets"+s,Toast.LENGTH_SHORT).show();
		}
//Toast.makeText(getApplicationContext(),"table inserted - ",Toast.LENGTH_SHORT).show();
		}catch(Exception e)
		{
			Toast.makeText(getApplicationContext()," error creating db - "+e.toString(),Toast.LENGTH_SHORT).show();
		}
	}
	
	private void start()
	{
		
		index = 0;
		t1.setText(" Q. "+(index+1)+" "+list.get(index).ques);
		r1.setText(list.get(index).opta);
		r2.setText(list.get(index).optb);
		r3.setText(list.get(index).optc);
		r4.setText(list.get(index).optd);
		r1.setChecked(false);
		r2.setChecked(false);
		r3.setChecked(false);
		r4.setChecked(false);
		b1.setText("next");
		int time = list.size();
		timer(time);
			if(mid!=0)
		{
			//Toast.makeText(getApplicationContext(),"To Repeat()",Toast.LENGTH_SHORT).show();
			int time2 = list.size();
			repeat(time2);
		}
	}
	
	private void check()
	{
		
		int id = rg.getCheckedRadioButtonId();
		RadioButton rb = (RadioButton)findViewById(id);
		//String ans = rb.getText().toString();
		r1.setClickable(false);
		r2.setClickable(false);
		r3.setClickable(false);
		r4.setClickable(false);
		if(rb.getText().toString().equalsIgnoreCase(list.get(index).answ))
		{
			count++;
			if(mid==0)
			sc.setText("Score "+count);
			rb.setTextColor(Color.GREEN);
			if(sound){
			final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.success_lesson);
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{

					@Override
					public void onCompletion(MediaPlayer p1)
					{
						p1.reset();
						p1.release();
					}

				});
			mp.start();
			}
		}
		else
		{
			rb.setTextColor(Color.RED);
			if(sound){
			final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.fail);
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{

					@Override
					public void onCompletion(MediaPlayer p1)
					{
						p1.reset();
						p1.release();
					}
	
			});
			mp.start();
			}
		}
	}
	
	private void init()
	{
		t1 = (TextView)findViewById(R.id.t1);
		ct = (TextView)findViewById(R.id.ct);
		sc = (TextView)findViewById(R.id.cr);
		b1 = (Button)findViewById(R.id.b1);
		rg = (RadioGroup)findViewById(R.id.rg);
		r1 = (RadioButton)findViewById(R.id.r1);
		r2 = (RadioButton)findViewById(R.id.r2);
		r3 = (RadioButton)findViewById(R.id.r3);
		r4 = (RadioButton)findViewById(R.id.r4);
	}
	
	private void beginJsonParsing() {
		
		try {
			
			JSONObject obj = new JSONObject(json);
			JSONArray userArray = obj.getJSONArray("free");
			
			for (int i = 0; i < userArray.length(); i++) {
				try {
					
					JSONObject userDetail = userArray.getJSONObject(i);
					
					set a = new set();
					a.ques = userDetail.getString("ques");
					a.opta = userDetail.getString("opta");
					a.optb = userDetail.getString("optb");
					a.optc = userDetail.getString("optc");
					a.optd = userDetail.getString("optd");
					a.answ = userDetail.getString("answ");
					list.add(a);
					
				}catch (JSONException e){
					e.printStackTrace();
				}
			}}catch (JSONException e) {
			t1.setText(e.toString());
		}}
	public String loadJSONFromAsset(String array) {

		try {
			InputStream is = getAssets().open(array+".json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			t1.setText(ex.toString());
			return null;
		}
		return json;
	}
	
	private void alert()
	{
    
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);    
        builder.setTitle("Result : ");   
        builder.setIcon(android.R.drawable.ic_dialog_info);
		
       int old = sp.getInt(value,0);
	   try{
			if(count>sp.getInt(value,0))
			{
				SharedPreferences.Editor edit = sp.edit();
				edit.putInt(value,count);
				edit.putInt("score",sp.getInt("score",0)+(count-old+1));
				edit.putInt("play",sp.getInt("play",1)+1);
				edit.commit();
				Toast.makeText(getApplicationContext(),"New High score !!!",Toast.LENGTH_SHORT).show();
			}}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
			}


         String text = "Subject : "+value+"\n"+"Score : "+count+"\n"+"Wrong : "+(list.size()-count)+"\n"+"Progress : "+(count*100/(list.size()))+"%"+"\n"+"High Score : "+sp.getInt(value,0);
         builder.setMessage(text);
		
		 builder.setPositiveButton("Play Again !!!", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
					index = 0;
					count = 0;
					b1.setText("next");
					t1.setText("Score "+count);
					r1.setTextColor(Color.BLACK);
					r2.setTextColor(Color.BLACK);
					r3.setTextColor(Color.BLACK);
					r4.setTextColor(Color.BLACK);
					start();
				}
			});
		builder.setNegativeButton("Leave", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
        /*
					jp = new JasonParser();
					//json = jp.request("http://friended-correspond.000webhostapp.com/app/userlist.json");	
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("tag",""));
					json = jp.request("http://localhost:8080/list1.json",params);
					try{
						beginJsonParsing("His-Set-1");
						start();
					}catch(Exception e)
					{
						t1.setText(e.toString());
					}
					*/
        finish();
				}
				
			});
		AlertDialog alert = builder.create();
        alert.show();	
	}
	
	private void timer(int time)
	{
		timer = new CountDownTimer((10*time*1000L),1000L)
		{

			@Override
			public void onTick(long p1)
			{
				StringBuilder stringBuilder = new StringBuilder().append("");
				Object[] arrobject = new Object[]{TimeUnit.MILLISECONDS.toMinutes(p1), TimeUnit.MILLISECONDS.toSeconds(p1)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(p1))};
				String s = stringBuilder.append(String.format("%d m %d s", (Object[])arrobject)).toString();
				ct.setText(s);
			}

			@Override
			public void onFinish()
			{
				timer.cancel();
				if(mid==0)
				alert();
				else
					game(2);
			}


		}.start();
	}
	
	private void alert2()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);    
        builder.setTitle("Result : ");   
        builder.setIcon(android.R.drawable.ic_dialog_info);

		//int old = sp.getInt(value,0);
		if(db.check(value)==true)
		{
			int old = db.getScore(value);
			try{
				if(count>old)
				{
					db.update(value,String.valueOf(count-old+1));
					SharedPreferences.Editor edit = sp.edit();
					//edit.putInt(value,count);
					edit.putInt("score",sp.getInt("score",0)+(count-old+1));
					edit.putInt("play",sp.getInt("play",1)+1);
					edit.commit();
					Toast.makeText(getApplicationContext(),"New High score !!!",Toast.LENGTH_SHORT).show();
					
				}}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			if(db.insert(value,String.valueOf(count))==true)
			{
				Toast.makeText(getApplicationContext(),"New High score !!!",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Error Inserting Score",Toast.LENGTH_SHORT).show();
			}
		}


		String text = "Subject : "+value+"\n"+"Score : "+count+"\n"+"Wrong : "+(list.size()-count)+"\n"+"Progress : "+(count*100/(list.size()))+"%"+"\n"+"High Score : "+sp.getInt(value,0);
		builder.setMessage(text);

		builder.setPositiveButton("Play Again !!!", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{

					index = 0;
					count = 0;
					b1.setText("next");
					t1.setText("Score "+count);
					r1.setTextColor(Color.BLACK);
					r2.setTextColor(Color.BLACK);
					r3.setTextColor(Color.BLACK);
					r4.setTextColor(Color.BLACK);
					start();
				}
			});
		builder.setNegativeButton("Leave", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
					finish();
				}

			});
		AlertDialog alert = builder.create();
        alert.show();	
	}
	
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu,add items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);//Menu ResourceFile
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			//case R.id.item0:register();
			//break;
			//case R.id.item1:select();
			//break;
			case R.id.item2: sound();
			break;
			case R.id.item3: score("Total Gold : ",String.valueOf(sp.getInt("score",0)));
			break;
			case R.id.item4: score("Devloped By : ","Rishav Singh");
			break;
			case R.id.item5 : finish();
				
			break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return false;
	}*/
	
	private void select()
	{
	timer.cancel();
	final CharSequence[] dialog_countries = getResources().getStringArray(R.array.ques);
	AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
	dialog_builder.setIcon(R.mipmap.ic_launcher);
	dialog_builder.setTitle("Select Course:");
	dialog_builder.setSingleChoiceItems(dialog_countries, -1, new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which)
		{
			case 0 : 
				list.clear();
				//beginJsonParsing(dialog_countries[which].toString());
				start();
			break;
			case 1 : 
				if(sp.getInt("score",0)>=23)
				{
					list.clear();
					//beginJsonParsing(dialog_countries[which].toString());
					start();
					break;
				}
				
		}
	}
	});

	AlertDialog dialog_alert = dialog_builder.create();
	dialog_alert.show();
	
	}
	
	
	private void score(String text1,String text2)
	{
		
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
		dialog_builder.setIcon(R.mipmap.ic_launcher);
		dialog_builder.setTitle(text1);
		dialog_builder.setMessage(text2);
		dialog_builder.setPositiveButton("Offline", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					//loadJSONFromAsset();
					try{
					//	beginJsonParsing("His-Set-1");

					}catch(Exception e)
					{
						t1.setText(e.toString());
					}
					start();
				}
			});
		AlertDialog ad = dialog_builder.create();
		ad.show();
		
	}
	private void register()
	{
		
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
		final EditText e1 = new EditText(this);
		e1.setHint("Enter Your Name : ");
		final EditText e2 = new EditText(this);
		e2.setHint("Enter Your Pass : ");
		LinearLayout l1 = new LinearLayout(this);
		l1.addView(e1);
		l1.addView(e2);
		dialog_builder.setView(l1);
		dialog_builder.setIcon(R.mipmap.ic_launcher);
		dialog_builder.setTitle("Register");

		dialog_builder.setPositiveButton("Register", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					if(e1.getText().toString().equals(""))
					{
						e1.setError("empty");
						return;
						
					}
					//new fetch().execute(e1.getText().toString(),e2.getText().toString());
				}
	});
	AlertDialog ad = dialog_builder.create();
	ad.show();
	}
	
	public void query()
	{
		if(android.os.Build.VERSION.SDK_INT > 9)
		{
			StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(p);
		}
	}
	
	private boolean isOnline()
	{
		final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
			Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        return isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
		
	}

	
	private void repeat(int time)
	{
	game(1);
		repeat = new CountDownTimer(15*time*1000,10000)
		{
			@Override
			public void onTick(long p1)
			{
			//Toast.makeText(getApplicationContext(),"To Game()",Toast.LENGTH_SHORT).show();

				game(1);
			}
			
		@Override
			public void onFinish()
			{
				
			}}.start();
	}
	
	private void game(int i)
	{
	//Toast.makeText(getApplicationContext(),"In Game()",Toast.LENGTH_SHORT).show();
		Fetch f = new Fetch(this,sc,mid);
		f.execute("http://localhost:8080/game.php",String.valueOf(type),String.valueOf(mid),String.valueOf(index),String.valueOf(count),String.valueOf(i));
	}
			//	a.answ = userDetail.getString("answ");*/
				
	public class JasonParser
	{
		InputStream is;
		String result;

		JasonParser()
		{
		}

		public String request(String url,List<NameValuePair> params)
		{
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost(url);
			
			
			try
			{
				hp.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse hr = hc.execute(hp);
				HttpEntity he = hr.getEntity();
				is = he.getContent();
			}
			catch(UnsupportedOperationException e)
			{

			}
			catch(IOException e)
			{

			}

			try 
			{
				BufferedReader br = new BufferedReader(new InputStreamReader (is,"iso-8859-1"),8);
				String line = "";
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine()) != null)
				{
					sb.append(line+"\n");
				}
				is.close();
				result = sb.toString();
			}
			catch(Exception e)
			{

			}

			return result;	
		}}
		
		
		}


		
		
class Fetch extends AsyncTask<String,Void,String>
{
	Context c;
	TextView t1;
	JasonParser jp;
	String reg = "";
	int mid;
	Fetch(Context c,TextView t1,int mid)
	{
		this.c = c;
		this.t1 = t1;
		this.mid = mid;
		jp = new JasonParser();
	}

	@Override
	protected String doInBackground(String[] p1)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type",p1[1]));
		params.add(new BasicNameValuePair("mid",p1[2]));
		params.add(new BasicNameValuePair("ques1",p1[3]));
		params.add(new BasicNameValuePair("score1",p1[4]));
		params.add(new BasicNameValuePair("status",p1[5]));
		
		reg = jp.request(p1[0],params);
		
		//publishProgress("");
		return reg;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		t1.setText("Refreshing...");
	}

	@Override
	protected void onPostExecute(String result)
	{
		// TODO: Implement this method
		super.onPostExecute(result);
		try
		{
			JSONObject data = new JSONObject(result);
			String p = data.getString("player1");
			int q1 = data.getInt("ques1");
			int s1 = data.getInt("score1");
			//int mid = data.getInt("mid");
			String p2 = data.getString("player2");
			int q2 = data.getInt("ques2");
			int s2 = data.getInt("score2");
			int st = data.getInt("status");
			if(st==1)
			t1.setText(p+" | Vs | "+p2+"\n"+q1+" | Vs | "+q2+"\n"+s1+" | Vs | "+s2);
			else
			{
				if(s1>s2)
					t1.setText(p+" Wins");
				else
					t1.setText(p2+" Wins");
			}
				
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		//t1.setText(result);

	}

	@Override
	protected void onCancelled()
	{
		super.onCancelled();

	}

}

class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DB_NAME = "AllSets.db";
	public static final String TABLE_NAME = "USER_SCORE";
	public static final String COL_1 = "ID";
	public static final String COL_2 = "SUBJECT";
	public static final String COL_3 = "SCORE";
	SharedPreferences sp;
	Context c;

	public DatabaseHelper(Context c)
	{
		super(c,DB_NAME,null,1);
		this.c=c;

	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		db.execSQL("create table if not exists "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,SUBJECT VARCHAR,SCORE VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int p2, int p3)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	
	public boolean insert(String name,String src)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(COL_2,name);
		cv.put(COL_3,src);
		long res = db.insert(TABLE_NAME,null,cv);
		if(res==-1)
			return false;
		else
			return true;
	}

	public boolean update(String name,String salary)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COL_2,name);
		cv.put(COL_3,salary);
		db.update(TABLE_NAME,cv,"SUBJECT = ?",new String[]{name});
		return true;
	}
	
	public Cursor getAllData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
		return res;

	}

	public Integer delete(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_NAME, "ID = ?",new String[] {id});


	}

	public String path()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.getPath();
	}

	public boolean check(String src)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_2+" = "+"'"+src+"'",null);
		return c.getCount()>0 ? true:false;
	}
	
	public int getScore(String src)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_2+" = "+"'"+src+"'",null);
		return Integer.parseInt(c.getString(c.getColumnIndex(COL_3)));
	}

	/*	FileChannel src =new FileInputStream(currentDB).getChannel();
	 FileChannel dst =new FileOutputStream(backupDB).getChannel();  
	 dst.transferFrom(src,0, src.size());
	 Toast.makeText(getApplicationContext(),"Backup Complete",Toast.LENGTH_SHORT).show();

	 src.close();     
	 dst.close();*/
}