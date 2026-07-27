package com.example.allsets;

import java.io.*;
import java.util.*;
import java.net.*;

public class JasonParser
{
    InputStream is;
    String result;

    JasonParser()
    {
    }

    public String request(String url,List<NameValuePair> params)
    {
        try
        {
            URL u = new URL(url);
            HttpURLConnection hc = (HttpURLConnection) u.openConnection();
            hc.setRequestMethod("POST");
            hc.setDoOutput(true);
            hc.setDoInput(true);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) sb.append("&");
                sb.append(URLEncoder.encode(params.get(i).getName(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(params.get(i).getValue(), "UTF-8"));
            }

            OutputStream os = hc.getOutputStream();
            os.write(sb.toString().getBytes("UTF-8"));
            os.close();

            is = hc.getInputStream();
        }
        catch(IOException e)
        {
        }

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
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
