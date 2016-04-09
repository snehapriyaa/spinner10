package com.example.sqlitewithcursorloaderexample;


        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.DialogInterface.OnClickListener;
        import android.database.Cursor;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnLongClickListener;
        import android.view.ViewGroup;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.CompoundButton.OnCheckedChangeListener;
        import android.widget.CursorAdapter;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import example.com.contactsdatabase.MainActivity;
        import example.com.contactsdatabase.R;

public class MainActivityAdapter extends CursorAdapter {
    // Variable declaration
    String TAG = "MainActivityAdapter";
    MyDatabaseOpenHelper myDatabaseOpenHelper;
    String buttonTitle;

    public MainActivityAdapter(Context context, Cursor cursor, MyDatabaseOpenHelper myDatabaseOpenHelper) {
        super(context, cursor);
        Log.d(TAG, "-------> MainActivityAdapter");
        this.myDatabaseOpenHelper = myDatabaseOpenHelper;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Get tag
        final ViewHolder holder = (ViewHolder) view.getTag();

        // set item in TextView
              holder.item.setText(cursor.getString(cursor.getColumnIndex("Items")));
        holder.item2.setText(cursor.getString(cursor.getColumnIndex("Items")));




        // Item click listener
        holder.item.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String optionsa[] = { "edit", "Delete" };
                AlertDialog.Builder options = new AlertDialog.Builder(context);
                options.setTitle("Select your Choice");
                options.setItems(optionsa, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "==========> Which " + which);
                        if (which == 0) {
                            Log.d(TAG,"===> edit is Selected");

                            // Get layout inflater object
                            LayoutInflater factory = LayoutInflater.from(context);
                            // Get layout
                            View renameEntry = factory.inflate(R.layout.alertdialog, null);
                            // Get id  - TextView
                            final EditText rename = (EditText) renameEntry.findViewById(R.id.editText2);
                            final EditText renum = (EditText) renameEntry.findViewById(R.id.editText3);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("edit");
                            builder.setView(renameEntry);
                            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG,"================================> which"	+ which);
                                    String input = rename.getText().toString();
                                    boolean result = myDatabaseOpenHelper.isAvailable(rename.getText().toString());
                                    if (result == true) {
                                        Toast.makeText(context, "Item already found", Toast.LENGTH_LONG).show();
                                    } else if (input.equals("")) {
                                        Toast.makeText(context,"Enter valid name", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d(TAG, "Item: "+ rename.getText().toString());
                                        myDatabaseOpenHelper.myRename(holder.item.getText().toString(), rename.getText().toString());
                                        // Load data on listview
                                        MainActivity.myLoadData();
                                    }
                                }
                            });

                            builder.setNegativeButton("Cancel",new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    Log.d(TAG,"=====> which"	+ which);
                                }
                            });
                            builder.create();
                            builder.show();
                        } else {

                            Log.d(TAG,"========================> Delete is Selected");
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Warning");
                            builder.setMessage("Delete the Selected Item?");
                            builder.setPositiveButton("Delete", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    Log.d(TAG,"-----------------------> delete  "+ holder.item.getText().toString());
                                    myDatabaseOpenHelper.myDelete(holder.item.getText().toString());
                                    // Load data on listview
                                    MainActivity.myLoadData();
                                }
                            });

                            builder.setNegativeButton("Cancel",new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                }
                            });
                            builder.create();
                            builder.show();
                        }
                    }
                });
                options.create();
                options.show();

                return true;
            }
        });

        holder.flag.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG,"------------------>setOnCheckedChangeListener flag "+ holder.flag.isChecked());
                    Log.d(TAG,"------------------>setOnCheckedChangeListener item "+ holder.item.getText().toString());
                    // passing item and true
                    myDatabaseOpenHelper.myUpdate(holder.item.getText().toString(), "true");
                } else {
                    Log.d(TAG,"------------------>setOnCheckedChangeListener else flag "+ holder.flag.isChecked());
                    Log.d(TAG,"------------------>setOnCheckedChangeListener item "+ holder.item.getText().toString());
                    // passing item and false
                    myDatabaseOpenHelper.myUpdate(holder.item.getText().toString(), "false");
                }
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Get layout inflater object
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Get layout - res/layout/singlerow
        View retView = inflater.inflate(R.layout.singlerow, parent, false);

        // Create ViewHolder object
        ViewHolder holder = new ViewHolder();
        // Get id for item - TextView, and number
        holder.item = (TextView) retView.findViewById(R.id.textView2);
        holder.item2 = (TextView) retView.findViewById(R.id.textView3);

        // Set Tag
        retView.setTag(holder);

        // Return view
        return retView;
    }

    // ViewHolder Class
    class ViewHolder {
        TextView item;
        TextView item2;
    }
}
