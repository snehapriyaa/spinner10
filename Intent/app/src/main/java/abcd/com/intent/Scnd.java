package abcd.com.intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Scnd extends Activity {
    TextView name, number;
    String tag = "scnd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scnd);
        name =(TextView)findViewById(R.id.textView);
        number=(TextView)findViewById(R.id.textView2);
        Intent intent = getIntent();
        Log.d(tag,"...........getintent");
        if (intent != null) {
            String nam = intent.getStringExtra("NAME");
            String num = intent.getStringExtra("NUM");
            name.setText(nam);
            number.setText(num);
        } else {
            Toast.makeText(Scnd.this, "no entry is made", Toast.LENGTH_LONG).show();


        }
    }
}
