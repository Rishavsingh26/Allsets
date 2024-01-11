package com.example.allsets;


import android.app.*;
import android.os.*;
import android.widget.*;
import android.text.*;
import android.content.*;
import android.view.*;
import org.apache.http.*;
import java.util.*;
import org.apache.http.message.*;
import org.json.*;
import android.webkit.*;
import java.io.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.entity.*;
import android.net.*;
import android.graphics.*;
import android.util.*;
import java.net.*;


public class LoginActivity extends Activity 
{
	
	static final String PACKAGE = "com.example.allsets";
	static final float VERSION  = 1.2f;
	static final long UP_TIME  = (15*24*60*60*1000);
	static final String MY_URL =  "http://friended-correspond.000webhostapp.com/app/";
	
	EditText e1;
	Button b1;
	GridView g1;
	String reg,name,pass,loc="";
	SharedPreferences sp;
	JasonParser jp;
	boolean isConnected = false;
	ArrayAdapter<String> ad;
	
	final String[] his = {"History-Set-1","History-Set-2","History-Set-3","History-Set-4","History-Set-5","History-Set-6"};
	final String[] geo = {"Geography-Set-1","Geography-Set-2"};
	final String[] pol = {"Politics-Set-1","Politics-Set-2"};
	final String[] bih = {"Bio-Set-1","Bio-Set-2","Bio-Set-3"};

	final String[] gk  = {"Gk-Set-1","Gk-Set-2","Gk-Set-3"};
	final String[] ana = {"Analogy-Set-1","Analogy-Set-2"};
	final String[] odd = {"OddOneOut-Set-1","OddOneOut-Set-2"};
	final String[] one = {"OneWord-Set-1","OneWord-Set-2"};
	final String[] bt  = {"BrainTeasers-Set-1"};
	final String[] bio = {"Biology-Set-1","Biology-Set-2","Biology-Set-3"};
	final String[] che = {"Chemistry-Set-1","Chemistry-Set-2"};
	final String[] phy = {"Physics-Set-1","Physics-Set-2","Physics-Set-3"};
	final String[] eng = {" Analogy"," Biology"," Brain Teasers"," Chemistry"," GK"," Odd One Out"," One Word For All"," Physics"};
	final String[] hin = {" Biology"," Geography"," History"," Politics"};
	
	
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.allset);
      sp = getSharedPreferences("setting",0);
      g1 = (GridView) findViewById(R.id.allsetGridView);
      query();
	  
	  if(sp.getString("lang","").equals(""))
	  {
		  lang();
	  }
	  else
	  {
		  init();
	  }
      
      //final String[] sets = {"History-Set-1","Biology-Set-1","Geography-Set-1","History-Set-2","Biology-Set-2","Geography-Set-2","History-Set-3","Biology-Set-3","History-Set-4","History-Set-5","History-Set-6"};//getResources().getStringArray(R.array.ques);
	    
		g1.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					//Intent i1 = new Intent(LoginActivity.this,MainActivity.class);
					//i1.putExtra("value",sets[p3]);
					//startActivity(i1);
					select(p3);
				}
				
			
		});
    }

	private void init()
	{

		if(sp.getString("lang","").equals("eng"))
		{
			ad = new ArrayAdapter<String>(this,R.layout.activity_main,R.id.activity_mainTextView,eng);
			g1.setAdapter(ad);
		}
		
		if(sp.getString("lang","").equals("hin"))
		{
			ad = new ArrayAdapter<String>(this,R.layout.activity_main,R.id.activity_mainTextView,hin);
			g1.setAdapter(ad);
		}
		
		if(isOnline()==true) 
		{
			if(sp.getLong("last",System.currentTimeMillis()+6*24*60*60*1000) < System.currentTimeMillis())
			{

				update();

			}
		}
			
		//e1 = (EditText) findViewById(R.id.signEditText_1);
		//b1 = (Button) findViewById(R.id.signButton_1);
	
		
	}
	private void select(int which)
	{
		final String[] x = choose(which);
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
		dialog_builder.setIcon(R.mipmap.ic_launcher);
		dialog_builder.setTitle("SelectSet ");
		dialog_builder.setItems(x, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					Intent i1 = new Intent(LoginActivity.this,MainActivity.class);
					 i1.putExtra("value",x[which]);
					 i1.putExtra("mid",0);
					 startActivity(i1);
				}
			});
			dialog_builder.show();
		}
	private String[] choose(int x)
	{
		 if(sp.getString("lang","").equals("eng"))
		 {
			 switch(x)
			 {
				 case 0 : return ana;
				 case 1 : return bio;
				 case 2 : return bt;
				 case 3 : return che;
				 case 4 : return gk;
				 case 5 : return odd;
				 case 6 : return one;
				 case 7 : return phy;
			 }
		 }
		 if(sp.getString("lang","").equals("hin"))
		 {
			 switch(x)
			 {
				 case 0 : return bih;
				 case 1 : return geo;
				 case 2 : return his;
				 case 3 : return pol;
			 }
		 }
		return his;
	}
	
//	ArrayAdapter<String> ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,set);
//	g1.setAdapter(ad);

   public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu,add items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu2, menu);//Menu ResourceFile
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
     case R.id.item1: 
			if(sp.getBoolean("login",false)!=true)
			   register();
			else
				display();
			break;
			case R.id.item2: sound();
			break;
			case R.id.item3 : score();
			break;
			case R.id.item4 : playOnline();
			break;
			case R.id.item5 : update();
			break;
			case R.id.item6 : about();
			break;
			case R.id.item7 : finishAffinity();
			break;
			case R.id.item8 : lang();
			break;
			default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}


   private void sound()
	{
    String sound;
     if(sp.getBoolean("sound",true)==true)
			  sound = "Sound is ON";
			else
				sound = "Sound is OFF";
		final CharSequence[] dialog_countries = { " ON"," OFF" };
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
		dialog_builder.setIcon(R.mipmap.ic_launcher);
		dialog_builder.setTitle(sound);
		dialog_builder.setItems(dialog_countries, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(which)
					{
						case 0 : 
							SharedPreferences.Editor edit = sp.edit();
							edit.putBoolean("sound",true);
							edit.commit();
							//sound = true;
							
						break;
						case 1 : 
							SharedPreferences.Editor edit1 = sp.edit();
							edit1.putBoolean("sound",false);
							edit1.commit();
							//sound = false;
						break;
					}
				}
			});
		dialog_builder.show();
	}
	
	private void about()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout l1 = new LinearLayout(this);
        l1.setOrientation(LinearLayout.VERTICAL);
		
        final TextView tv = new TextView(this);
        final TextView tv2 = new TextView(this);
        l1.addView(tv);
		l1.addView(tv2);
	    tv.setText("\n 1. Finish the set to earn gold \n\n 2. Each time you play you earn a gold \n\n 3. Register your name to Online Ranking \n\n 4. Update your score Online in Gold Bank \n\n 5. Check your app version in update menu \n\n 6. Download latest version of app for more feature ");
        tv2.setText("\n Devloped by Rishav Singh");
        tv.setTextSize(20);
        tv2.setTextSize(22);
        tv.setTextColor(Color.BLACK);
		tv2.setTextColor(Color.RED);
	    builder.setView(l1);
        builder.setCancelable(true);
        builder.setTitle("Direction For Use : ");
        builder.show();
	}
	
   private void playOnline()
   {
      /* AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
	   dialog_builder.setIcon(R.mipmap.ic_launcher);
	   dialog_builder.setTitle("Play Online With Others");
	   dialog_builder.setMessage("Wait for New Update !");
	   
       dialog_builder.show();*/
       AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
	   final EditText e1 = new EditText(this);
	   e1.setHint("Enter Mid : ");
	   final EditText e2 = new EditText(this);
	   e2.setHint("Enter Type : ");
	   LinearLayout l1 = new LinearLayout(this);
	   l1.setOrientation(LinearLayout.VERTICAL);
	   l1.addView(e1);
	   l1.addView(e2);
	   dialog_builder.setView(l1);
	   dialog_builder.setIcon(R.mipmap.ic_launcher);
	   dialog_builder.setTitle("Join");

	   dialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
			   @Override
			   public void onClick(DialogInterface p1, int p2)
			   {
				   int mid = Integer.parseInt(e1.getText().toString());
				   int type = Integer.parseInt(e2.getText().toString());
				   
				   Intent i1 = new Intent(LoginActivity.this,MainActivity.class);
				   i1.putExtra("value","Gk-Set-1");
				   i1.putExtra("mid",mid);
				   i1.putExtra("type",type);
				   startActivity(i1);
				   //new fetch().execute("http://friended-correspond.000webhostapp.com/app/register.php",name,pass);
				   //http:friended-correspond.000webhostapp.com/app/register
				   //localhost:8080/register.php
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
	
   private void register()
	{
		
		AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
		final EditText e1 = new EditText(this);
		e1.setHint("Enter Unique Name : ");
		//final EditText e2 = new EditText(this);
		//e2.setHint("Enter Your Pass : ");
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.VERTICAL);
		l1.addView(e1);
		//l1.addView(e2);
		dialog_builder.setView(l1);
		dialog_builder.setIcon(R.mipmap.ic_launcher);
		dialog_builder.setTitle("Register");

		dialog_builder.setPositiveButton("Register", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					if(e1.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(),"name filed is empty", Toast.LENGTH_LONG).show();
						return;
						
					}
						
					if(isOnline()!=true)
					{
						Toast.makeText(getApplicationContext(),"Unable to Connect", Toast.LENGTH_LONG).show();
						return;
						
					}
        name = e1.getText().toString();
       // pass = e2.getText().toString();
		new fetch().execute("http://friended-correspond.000webhostapp.com/app/register.php",name,sp.getString("lang",""));
      //http:friended-correspond.000webhostapp.com/app/register
      //localhost:8080/register.php
				}
	});
	AlertDialog ad = dialog_builder.create();
	ad.show();
	}
  
  private void display()
  {
	  AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
	  final WebView wv = new WebView(this);
	  wv.loadUrl(MY_URL+"rank.php");
	  dialog_builder.setView(wv);
	  dialog_builder.setIcon(R.mipmap.ic_launcher);
	  dialog_builder.setTitle("Rank");
/*
	  dialog_builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener(){
			  @Override
			  public void onClick(DialogInterface p1, int p2)
			  {
				  wv.loadUrl(URL);
			  }
		  });*/
	  //AlertDialog ad = dialog_builder.create();
	  dialog_builder.show();
  }
  private void score()
  {
	  AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
	  dialog_builder.setIcon(R.mipmap.ic_launcher);
	  dialog_builder.setTitle("Gold Bank");
	  dialog_builder.setMessage("Hii "+sp.getString("name","")+" You have "+sp.getInt("score",0)+" gold And You Played "+sp.getInt("play",0)+" times.");
	  dialog_builder.setPositiveButton("Update Online Rank", new DialogInterface.OnClickListener(){
			  @Override
			  public void onClick(DialogInterface p1, int p2)
			  {
				  if(isOnline()!=true)
					{
						Toast.makeText(getApplicationContext(),"Unable to Connect", Toast.LENGTH_LONG).show();
						return;
						
					}
				  new fetch().execute("http://friended-correspond.000webhostapp.com/app/update.php",sp.getString("name",""),sp.getString("pass",""));
       //http:friended-correspond.000webhostapp.com/app/update.php
       //localhost:8080/update.php
			  }
		  });
	  AlertDialog ad = dialog_builder.create();
	  ad.show();
  }
   
   private void lang()
  {
	  final CharSequence[] items = {"English","Hindi"};
	  AlertDialog.Builder ad = new AlertDialog.Builder(this);
	  ad.setTitle("Choose Questions Language");
	  ad.setItems(items, new DialogInterface.OnClickListener() {

			  @Override
			  public void onClick(DialogInterface dialog, int which) {
				  switch(which)
				  {
				     case 0: SharedPreferences.Editor edit1 = sp.edit();
						  edit1.putString("lang","eng");
						  edit1.putInt("score",0);
						  edit1.putInt("play",sp.getInt("play",0));
						  edit1.commit(); init(); break;
					  case 1: SharedPreferences.Editor edit2 = sp.edit();
						  edit2.putString("lang","hin");
						  edit2.putInt("score",0);
						  edit2.putInt("play",0);
						  edit2.commit(); init(); break;
				 }
				  Toast.makeText(getApplicationContext(),items[which]+" Gold Bank Reseted !", Toast.LENGTH_LONG).show();
				  
				  
			  }});
	  ad.show();								
  }
  /*
    private void update()
    {
       //final String URL = "http://friended-correspond.000webhostapp.com/app/rank.php";
	     AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
       LinearLayout l1 = new LinearLayout(this);
       l1.setOrientation(LinearLayout.VERTICAL);
       final TextView tv = new TextView(this);
	   final WebView wv = new WebView(this);
       l1.addView(tv);
       l1.addView(wv);
       tv.setText("\n My App Version is "+Version);
       tv.setTextSize(18);
       tv.setTextColor(Color.BLACK);
	     wv.loadUrl("http://friended-correspond.000webhostapp.com");
	     dialog_builder.setView(l1);
	     dialog_builder.setIcon(R.mipmap.ic_launcher);
	     dialog_builder.setTitle("Update Your App");
       dialog_builder.show();
     }
*/
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
	
	
	private void update()
	{
		show("Checking For Update");
		Thread t = new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					String url2 = task(MY_URL+"updateApp.json");
					parse3(url2);
				}


			});t.start();
	}
	
	private void parse3(String a)
	{
		float ver = 1.0f;
		
		try
		{
			JSONArray j = new JSONArray(a);
			for(int i=0;i<j.length();i++)
			{
				JSONObject j2 = j.getJSONObject(i);
				String name = j2.getString("package");
				if(name.equals(PACKAGE))
				{
					ver = Float.parseFloat(j2.getString("version"));
					loc = j2.getString("src");
					break;
				}
			}
			SharedPreferences.Editor e = sp.edit();
			e.putLong("last",System.currentTimeMillis()+(UP_TIME));
			e.commit();
			if(ver > VERSION)
			{
				runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Go(loc);
						}
					});
			}
			else
			{
				runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							show("You Are Using Latest Version");
						}
					});
			}


		}
		catch(Exception e)
		{
			Log.e("UpdateErr()",e.toString());
			//cache.cacheError("UpdateErr() - "+e.toString());
		}

	}

	private void show(String p0)
	{
		Toast.makeText(getApplicationContext(),p0,Toast.LENGTH_SHORT).show();

	}
	
	private void Go(String loc)
	{
		Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(MY_URL+loc));
		startActivity(Intent.createChooser(i,"New Version is Available"));
	}
	
	private String task(String u)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new URL(u).openConnection().getInputStream()));
			String t;
			while((t=br.readLine())!=null)
			{
				sb.append(t).append("\n");
			}

		}

		catch (Exception e)
		{
			Log.e("DownErr()",e.toString());
			//cache.cacheError("DownErr()  "+e.toString());
		}

		return sb.toString().trim();
	}
	
  class fetch extends AsyncTask<String,Void,Void>
	{

   ProgressDialog pd;
		@Override
		protected Void doInBackground(String[] p1)
		{
			//try{
			//update(p1[0]);
			JasonParser jp =new JasonParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name",p1[1]));
			params.add(new BasicNameValuePair("pass",p1[2]));
			params.add(new BasicNameValuePair("score",String.valueOf(sp.getInt("score",1))));

			// reg = jp.request("http://friended-correspond.000webhostapp.com/app/register.php",params);
			reg = jp.request(p1[0],params);
			
			//}catch(Exception e)
			//{
			//t1.append("task started");
			//}
			/*List<NameValuePair> params = new ArrayList<NameValuePair>();
			 params.add(new BasicNameValuePair("name","aman"));
			 params.add(new BasicNameValuePair("pass","2929"));
			 params.add(new BasicNameValuePair("score","9"));
			 JasonParser jp =new JasonParser();
			 String reg = jp.request("http://friended-correspond.000webhostapp.com/app/register.php",params);

			 try
			 {
			 String mess = "";
			 JSONArray jArray=new JSONArray(reg);
			 for (int i=0;i < jArray.length();i++)
			 {
			 JSONObject data = jArray.getJSONObject(i);
			 int res = data.getInt("score");
			 mess = data.getString("name");

			 }

			 Toast.makeText(getApplicationContext(),mess, Toast.LENGTH_LONG).show();

			 }catch(JSONException el){
			 Toast.makeText(getApplicationContext(),el.toString(), Toast.LENGTH_LONG).show();
			 //el.printStackTrace();
			 }*/
			return null;
		}

    @Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pd = new ProgressDialog(LoginActivity.this);
			pd.setCancelable(true);
			pd.setMessage("Please Wait...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			pd.cancel();
			try
			{
				JSONObject data = new JSONObject(reg);
				int get = Integer.parseInt(data.getString("success"));
       String mess = data.getString("message");
       if(get==1)
	   {
      if(sp.getBoolean("login",false)==false)
      {
		   SharedPreferences.Editor edit = sp.edit();
		   edit.putBoolean("login",true);
		   edit.putString("name",name);
		   edit.putString("pass",pass);
      edit.commit();
      Toast.makeText(getApplicationContext(),mess, Toast.LENGTH_LONG).show();
      }
		   
	    }
       if(get==2)
       {
         Toast.makeText(getApplicationContext(),mess, Toast.LENGTH_LONG).show();
       }

       else
				Toast.makeText(getApplicationContext(),mess, Toast.LENGTH_LONG).show();

			}
			catch (JSONException e)
			{
				//t1.append("json exception - "+e.toString());
       Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
			}
			
		}
	

}}
	