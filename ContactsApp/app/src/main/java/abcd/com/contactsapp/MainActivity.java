package abcd.com.contactsapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
    EditText fnametxt,phonenum,inputs;
    List<abcd.com.contactsapp.Contact> contacts = new ArrayList<abcd.com.contactsapp.Contact>();
    ImageView contactimgview;
    ListView contactlistview;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fnametxt=(EditText)findViewById(R.id.editText1);
        phonenum=(EditText)findViewById(R.id.editText3);
        contactlistview=(ListView)findViewById(R.id.listView);
        contactimgview=(ImageView)findViewById(R.id.imageView);
        inputs=(EditText)findViewById(R.id.editText);
        TabHost tabhost=(TabHost)findViewById(R.id.tabHost);

        tabhost.setup();
        TabHost.TabSpec tabspec=tabhost.newTabSpec("creator");
        tabspec.setContent(R.id.contactcreator);
        tabspec.setIndicator("creator");
        tabhost.addTab(tabspec);
        tabspec=tabhost.newTabSpec("list");
        tabspec.setContent(R.id.contactlist);
        tabspec.setIndicator("list");
        tabhost.addTab(tabspec);
        final Button addbtn=(Button)findViewById(R.id.button);

        fnametxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addbtn.setEnabled(!fnametxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void add_photo(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    public void searchButton(View view)
    {
        String findString = inputs.getText().toString();
        System.out.println("--------------------->searchButton size " + contacts.size());

        boolean findStringFlag = false;
        for(int index = 0; index < contacts.size(); index++){
            abcd.com.contactsapp.Contact currentcontact = contacts.get(index);
            System.out.println("--------------------->searchButton currentcontact.getname() " + currentcontact.getname());
            if(currentcontact.getname().equalsIgnoreCase(findString)){
                System.out.println("--------------------->searchButton FOUND " + currentcontact.getname());
                findStringFlag = true;
            }
        }
        if(findStringFlag){
            Toast.makeText(MainActivity.this, findString + " is found", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, findString + " is not found", Toast.LENGTH_LONG).show();
        }
    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable vibrate
            System.out.println("--------------------->onToggleClicked ON");
            //List<abcd.com.contactsapp.Contact> contacts = new ArrayList<abcd.com.contactsapp.Contact>();
            Collections.sort(contacts);
            populatelist();
        } else {
            // Disable vibrate
            System.out.println("--------------------->onToggleClicked OFF");
            Collections.sort(contacts, Collections.reverseOrder());
            populatelist();
        }
    }


    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        System.out.println("---------------------> onActivityResult  cursor count " + cursor.getCount());
        System.out.println("---------------------> onActivityResult  column_index " + column_index);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        //cursor.close();
        return s;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data ){
        System.out.println("---------------------> onActivityResult  requestCode " + requestCode);
        System.out.println("---------------------> onActivityResult  resultCode " +resultCode );

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                System.out.println("---------------------> onActivityResult  selectedImageUri " + selectedImageUri);
                System.out.println("---------------------> onActivityResult  selectedImagePath " + selectedImagePath);

                contactimgview.setImageURI(selectedImageUri);
            }
        }
    }

    private void populatelist(){
        ArrayAdapter<Contact> adapter=new contactlistadapter();
        contactlistview.setFastScrollEnabled(true);
        contactlistview.setAdapter(adapter);
    }

    private class contactlistadapter extends ArrayAdapter<abcd.com.contactsapp.Contact> {
        public contactlistadapter(){
            super(MainActivity.this, R.layout.listview_item, contacts);
        }

        public View getView(int position,View view,ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            abcd.com.contactsapp.Contact currentcontact = contacts.get(position);

            TextView name = (TextView)view.findViewById(R.id.cname);
            name.setText(currentcontact.getname());
            TextView phone = (TextView)view.findViewById(R.id.phonenum);
            phone.setText(currentcontact.getphone());
            ImageView conimage = (ImageView)view.findViewById(R.id.imageView2);
            conimage.setImageURI(currentcontact.getimage());
            System.out.println("--------------------->  getView .getimage() " + currentcontact.getimage());
            return view;
        }
    }

    public void add_contact(View view){
        contacts.add(new Contact(fnametxt.getText().toString(), phonenum.getText().toString(), selectedImageUri));
        populatelist();
        Toast.makeText(getApplicationContext(),fnametxt.getText().toString()+" is added to your contacts",Toast.LENGTH_SHORT).show();
        // clear the list
        fnametxt.setText("");
        phonenum.setText("");
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