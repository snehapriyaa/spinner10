import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherActivity extends Activity {
    TextView sunrise1,clouds1,humidity1,temperature1;
    private static final String SUN_RISE = "sunrise";
    private static final String CLOUDS = "clouds";
    private static final String HUMIDITY = "humidity";
    private static final String TEMPERATURE = "temp";
    private static final String SYS = "sys";
    private static final String ALL = "all";
    private static final String MAIN = "main";
    JSONArray user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        sunrise1=(TextView)findViewById(R.id.textView);
        clouds1 =(TextView)findViewById(R.id.textview2) ;
        humidity1=(TextView)findViewById(R.id.textview3);
        temperature1=(TextView)findViewById(R.id.textView4);
        Intent intent = getIntent();
        String city =intent.getStringExtra("CITY");
        String url = "http://api.openweathermap.org/data/2.5/activity_weather?q=" + city + "&mode=json";
        // Creating new JSON Parser
        JSONparser jParser = new JSONparser();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        try {
            // Getting JSON Array
            user = json.getJSONArray(SYS);
            JSONObject c = user.getJSONObject(0);

            // Storing  JSON item in a Variable
            String sunrise = c.getString(SUN_RISE);
            sunrise1.setText(sunrise);

            user = json.getJSONArray(CLOUDS);
            JSONObject cl = user.getJSONObject(0);
            String clouds = cl.getString(ALL);
            clouds1.setText(clouds);

            user = json.getJSONArray(MAIN);
            JSONObject hu = user.getJSONObject(0);
            String humidity = hu.getString(HUMIDITY);
            humidity1.setText(humidity);
            String temperature = hu.getString(TEMPERATURE);
            temperature1.setText(temperature);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    }

