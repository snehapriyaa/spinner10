package example.com.contactsdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    static // Variable declaration
            String TAG = "MainActivity";

    // Object for DatabaseHelperSQL Class
    static MyDatabaseOpenHelper myDatabaseOpenHelper;

    // Add More Button From the layout available.xml
    private Button add;

    // List View from the layout available.xml
    private static ListView lv;

    // Object for Cursor
    static Cursor cursor = null;

    // Object from Adapter Class for MainActivity
    static MainActivityAdapter MainActivityAdapter = null;

    static Context context;

    String buttonTitle;
    Intent intent;
    TextView title;

    // OnCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // See res/layout/activity_main.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.activity_main);

        Log.d(TAG, "-------> onCreate");

        // Get id
        title = (TextView)findViewById(R.id.textView);

        title.setText("contacts");

        // Creation of object for myDatabaseOpenHelper Class
        myDatabaseOpenHelper = new MyDatabaseOpenHelper(this);

        myDatabaseOpenHelper.createObjectForMyDatabaseOpenHelper(this);
        myDatabaseOpenHelper.getDB();

        // Store context
        context = this;

        // Get id
        add = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.listView);

        // Load data
        //myLoadData();

        // Button onclick event
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "-------> Add button pressed");

                // Get layout inflater object
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                // Get layout
                View textEntry = factory.inflate(R.layout.alertdialog, null);
                // Get id  - TextView
                final EditText et = (EditText) textEntry.findViewById(R.id.dialog);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter new");
                builder.setView(textEntry);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        //  Check the name already exit or not in table
                        boolean result = myDatabaseOpenHelper.isAvailable(et.getText().toString());

                        String input = et.getText().toString();
                        // if result true means Item already found
                        if (result == true) {
                            Toast.makeText(getApplicationContext(), "Item already Found", Toast.LENGTH_SHORT).show();
                        } else if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter valid name", Toast.LENGTH_LONG).show();
                        } else {
                            // insert item and false into table
                            myDatabaseOpenHelper.myInsert(et.getText().toString(), "false");
                            // Again load data
                            myLoadData();
                        }
                    }
                });

                builder.setNegativeButton("Cancel",	new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }


    public void onResume(){
        super.onResume();
        Log.d(TAG, "-------------------> onResume");
        // Load the date
        myLoadData();
    }

    static void myLoadData() {
        Log.d(TAG, "-------------------> myLoadData");
        // Get the records - cursor
        cursor = myDatabaseOpenHelper.myGetAvailableData();
        // AvailableActivityAdapter load data on listview
        MainActivityAdapter = new MainActivityAdapter(context, cursor, myDatabaseOpenHelper);
        // Notify listview when data changed in database
        MainActivityAdapter.notifyDataSetChanged();
        // Set adapter
        lv.setAdapter(MainActivityAdapter);
    }

    public void onDestroy(){
        System.gc();
        super.onDestroy();
    }
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
