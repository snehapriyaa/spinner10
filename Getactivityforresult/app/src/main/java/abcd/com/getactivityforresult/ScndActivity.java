package abcd.com.getactivityforresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.getactivityforresult.R;

public class ScndActivity extends Activity {
    EditText et;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scnd_activity);
        et = (EditText)findViewById(R.id.editText);
        b2 = (Button)findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = et.getText().toString();
                Intent intent =new Intent();
                intent.putExtra("NAME",nam);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }
}
