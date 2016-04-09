package abcd.com.activitylifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    Button button;
    String tag ="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "..........oncreate");
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
    }
        public void onstart(){
        super.onStart();
        Log.d (tag,".........onstart");
        }
    public void onresume(){
        super.onResume();
        Log.d (tag,".........onresume");

    }
    public void onstop(){
        super.onStop();
        Log.d (tag,".........onstop");

    }
    public void ondestroy(){
        super.onDestroy();
        Log.d (tag,".........ondestroy");

    }






    public void NewActivity (View view){
        Intent intent = new Intent(MainActivity.this,ScndActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
