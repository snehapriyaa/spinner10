package com.example.android.demomaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity {
    ListView list;
    public static final String TAG = "Mainactivity";
    Handler handler;
    int i=0;
    ArrayList<String> arr= new ArrayList<String>();
    ArrayList<String> allNames=new ArrayList<String>();
    ArrayList<String> alllat =new ArrayList<String>();
    ArrayList<String> alllong = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler= new Handler(Looper.getMainLooper());
        String api = "https://spider.nitt.edu/lateral/appdev";
        okhttp(api,i);
    }

    private void okhttp(String api,int i) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(api)
                .build();
        Call call = client.newCall(request);
        if (i != 1) {
            call.enqueue(new Callback() {
                             @Override
                             public void onFailure(Call call, IOException e) {
                             }

                             @Override
                             public void onResponse(Call call, Response response) throws IOException {
                                 try {
                                     String Jsondata = response.body().string();
                                     if (response.isSuccessful())
                                         Log.d(TAG, "----------------" + Jsondata);
                                     getdetails(Jsondata);
                                 } catch (IOException e) {
                                     Log.e(TAG, "------exception caught");
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }
            );
        }
        else{
            call.enqueue(new Callback() {
                             @Override
                             public void onFailure(Call call, IOException e) {
                             }

                             @Override
                             public void onResponse(Call call, Response response) throws IOException {
                                 try {
                                     String Jsondata = response.body().string();
                                     if (response.isSuccessful())
                                         Log.d(TAG, "----------------" + Jsondata);
                                          getdetails2(Jsondata);
                                 } catch (IOException e) {
                                     Log.e(TAG, "------exception caught");
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }
            );

        }
    }

    private void getdetails2(String jsondata) throws JSONException{
        allNames.clear();
        alllong.clear();
        alllat.clear();;
        JSONArray arr1= new JSONArray(jsondata);
        for(int f=0;f<arr1.length();f++){
            JSONObject acto = arr1.getJSONObject(f);
            String name = acto.getString("name");
            allNames.add(name);
            String latitude = acto.getString("latitude");
            alllat.add(latitude);
            String longitude = acto.getString("longitude");
            alllong.add(longitude);
        }
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        intent.putExtra("length",arr1.length());
        intent.putStringArrayListExtra("name_list", allNames);
        intent.putStringArrayListExtra("lat_list",alllat);
        intent.putStringArrayListExtra("long_list",alllong);
        startActivity(intent);
        Log.d(TAG,"---------"+allNames);
        Log.d(TAG,"---------"+alllat);
        Log.d(TAG,"---------"+alllong);
    }



    private void getdetails(String Jsondata) throws JSONException {
        Log.d(TAG, "----------enteredgetdetails");
        JSONObject category = new JSONObject(Jsondata);
        JSONArray categories = category.getJSONArray("categories");
        try {
            arr.clear();
            for (int c = 0; c < categories.length(); c++) {
                Log.d(TAG, "----------------enteredloop");
                arr.add(categories.getString(c).toString());
                Log.d(TAG, "------" + categories.getString(c));
            }
            if (arr.size() > 0) {
                handler.post(new Runnable() {
                    public void run() {
                        list = (ListView) findViewById(R.id.list);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arr);
                        list.setAdapter(adapter);
                        Log.d(TAG, "-----" + arr);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String value= (String)parent.getItemAtPosition(position);
                                i=1;
                                okhttp("https://spider.nitt.edu/lateral/appdev/coordinates?category="+value,i);
                            }
                        });
                    }
                });

            }
        }
        catch(Exception e)
        {
            Log.d(TAG,"Foo didn't work:"+ e.getMessage());
        }

    }
}