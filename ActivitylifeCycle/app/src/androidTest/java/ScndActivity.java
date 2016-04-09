import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import abcd.com.activitylifecycle.MainActivity;

public class ScndActivity extends Activity{
    Button b2;
    String tag = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "..........oncreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "..........onstart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "..........onresume");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tag, "..........onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "..........onstop");
    }
    public void back (View view){
        Intent intent = new Intent(ScndActivity.this, MainActivity.class);
        startActivity(intent);



    }

}
