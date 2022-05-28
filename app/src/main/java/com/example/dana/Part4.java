package com.example.dana;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Part4 extends AppCompatActivity {


    TextView textViewHumidity,textViewTemperature;
    EditText editText;
    Button buttonGet;
    ImageView imageViewWeather;
    ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part4);
        progressdialog = new ProgressDialog(Part4.this);
        progressdialog.setMessage("Please Wait....");
        textViewTemperature = (TextView) findViewById(R.id.txtTemprature);
        textViewHumidity = (TextView) findViewById(R.id.txtHumidity);

        imageViewWeather = (ImageView) findViewById(R.id.ivWeather);

        editText = (EditText) findViewById(R.id.etCity);

        buttonGet = (Button) findViewById(R.id.btnSearch);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressdialog.show();

                String url="http://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=1f1d6588819771e3d082ba37cbf28916&units=metric";
                getReport(url);
            }
        });

    }

    public void getReport(String url) {

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        String icon = "";

                        try {


                            //nested object
                            JSONObject jsonMain=response.getJSONObject("main");

                            double temp=jsonMain.getDouble("temp");

                            textViewTemperature.setText(temp+"");

                            JSONArray jsonArray = response.getJSONArray("weather");
                            for (int i=0; i<jsonArray.length();i++){
                                JSONObject oneObject = jsonArray.getJSONObject(i);
                                String weather = oneObject.getString("main");
                                JSONObject twoObject = jsonArray.getJSONObject(i);
                                icon=twoObject.getString("icon");

                                if(weather.equals("Clear")){
                                    Glide.with(Part4.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imageViewWeather);
                                }
                                else if(weather.equalsIgnoreCase("Rain") || weather.equalsIgnoreCase( "Thunderstorm") || weather.equalsIgnoreCase("Drizzle")){
                                    Glide.with(Part4.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imageViewWeather);
                                }
                                else if(weather.equals("Clouds")){
                                    Glide.with(Part4.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imageViewWeather);
                                }
                                else if (weather.equalsIgnoreCase("mist") || weather.equalsIgnoreCase("haze") ){
                                    Glide.with(Part4.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imageViewWeather);
                                }
                                else{
                                    Glide.with(Part4.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imageViewWeather);
                                }

                            }

                            JSONObject jsonM=response.getJSONObject("main");

                            double humid=jsonM.getDouble("humidity");

                            Log.d("Asma-humidity",String.valueOf(humid));
                            textViewHumidity.setText(humid+"%");

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Part4.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Temprature",textViewTemperature.getText().toString());
                            editor.putString("City",editText.getText().toString());
                            editor.putString("Image","http://openweathermap.org/img/w/"+icon+".png");
                            editor.apply();

                            progressdialog.dismiss();

                        }


                        catch (JSONException e){
                            e.printStackTrace();
                            Log.d("Receive Errror",e.toString());
                            progressdialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Asma","Error retrieving URL");
                Toast.makeText(Part4.this, "Please enter city name correct", Toast.LENGTH_SHORT).show();
                progressdialog.dismiss();
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);

    }
}