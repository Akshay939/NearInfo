package com.example.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http_request
{
    public String make_http_reuqest(String url)
    {
        String stresult = null;
        InputStream inputStream=null;
        try
        {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            StringBuffer stringBuffer=new StringBuffer(url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(stringBuffer.toString());
            HttpResponse httpResponse = httpClient.execute(httpget);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            inputStream.close();
            stresult = sb.toString();
            return stresult;
        }
        catch (Exception e)
        {
            return null;
        }
    } // end of make_http_reuqest
    public Bitmap downloadBitmap(String url)
    {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK)
            {
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null)
            {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e)
        {
            urlConnection.disconnect();
            Log.d("ImageDownloader", "Error downloading image from " + url);
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return null;
    }
} // end of http_request class
