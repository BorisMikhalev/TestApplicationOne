package com.test.testapplication.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection extends AsyncTask<Void,Void,String> {
    private URL url = null;
    private IConnection connection;
    private boolean haveError = false;

    public HttpConnection(String url, IConnection connection){
        super();
        this.connection = connection;
        try {
            this.url = new URL(url);
        }catch (MalformedURLException e){e.printStackTrace();}
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader br;
        StringBuilder result = new StringBuilder();
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
            } else {
                br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                haveError = true;
            }
            String line;
            while ((line = br.readLine()) != null){
                result.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if(httpURLConnection != null) httpURLConnection.disconnect();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!haveError) {
            connection.result(s);
        }else{
            connection.error(s);
        }
    }
}
