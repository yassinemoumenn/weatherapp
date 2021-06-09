package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    Button find;
    TextView tMain,tDescription,tTemperature;

    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1)
                {
                    char current  = (char) data;
                    result += current;
                    Log.d("this",result);
                    data = reader.read();

                }

                return result;


            }
            catch (MalformedURLException e1)
            {
                e1.printStackTrace();
                return "failed";
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "Failed";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONObject jsonTemprature = jsonObject.getJSONObject("main");



                Log.i("weather",weatherInfo);
                Double tempt = jsonTemprature.getDouble("temp");
                tempt = tempt-273.15;
                Log.i("Temprature: ",String.format("%.2f",tempt));


                JSONArray weatherArray = new JSONArray(weatherInfo);

                tTemperature.setText(String.format("Temperature: %.2f", tempt)+"°C");
                for(int i = 0; i<weatherArray.length(); i++)
                {
                    JSONObject jsonPart = weatherArray.getJSONObject(i);
                   // JSONObject jsonTemp = tempratureArray.getJSONObject(i);

                    tMain.setText("Main: " +jsonPart.getString("main"));
                    tDescription.setText("Description: "+jsonPart.getString("description"));
                    Log.i("Main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }









            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.etCity);
        find = (Button) findViewById(R.id.btnFind);
        tMain = (TextView) findViewById(R.id.tvMain);
        tDescription = (TextView) findViewById(R.id.tvDescription);
        tTemperature = (TextView) findViewById(R.id.tvTemprature);


    }

    public void find(View view)
    {
        try {


            String ctName = cityName.getText().toString();
            String query = "http://api.openweathermap.org/data/2.5/weather?q="+ctName+"&appid=5d1d9c727c8e08a9a68d57c41921dd5c";
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }
}