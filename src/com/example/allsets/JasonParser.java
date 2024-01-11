package com.example.allsets;

import java.io.*;
import org.apache.http.*;
import java.util.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.entity.*;

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
	}
}