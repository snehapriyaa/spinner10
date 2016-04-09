

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    // Variable declaration
    static String TAG = "MyDatabaseOpenHelper";

    // Database name
    private static final String DATABASE_NAME = "AllItemsDatabase";
    // Database version
    private static final int DATABASE_VERSION = 110;

    // Tables
    private static final String TABLE_NAME1 = "Items1";

    // Table columns
    private static final String colItem = "Items";
    private static final String colFlag = "Flag";

    // MyDatabaseOpenHelper object
    private static MyDatabaseOpenHelper myDatabaseOpenHelper;
    // SQLiteDatabase object
    private static SQLiteDatabase sQLiteDatabase;

    // MyDatabaseOpenHelper contructor with parameter context
    public MyDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "----------------> MyDatabaseOpenHelper Constructor");
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "----------------> onCreate");

        // Create table query for TABLE_NAME1(Column - colItem and colFlag)
        String createTableQuery = "CREATE TABLE " + TABLE_NAME1+ " (_id integer primary key autoincrement, " + colItem + ", "+ colFlag + ");";

        // Execute create table query for TABLE_NAME1
        database.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d(TAG, "----------------> onUpgrade");
        // Drop table if already exists in data base - Drop TABLE_NAME1, TABLE_NAME2, TABLE_NAME3, TABLE_NAME4, and TABLE_NAME5
        database.execSQL("Drop table if Exists" + TABLE_NAME1);

        // Call onCreate method with parameter database to Create table again
        onCreate(database);
    }

    // Create object for MyDatabaseOpenHelper class
    void createObjectForMyDatabaseOpenHelper(Context context) {
        Log.d(TAG, "----------------> createObjectForMyDatabaseOpenHelper");
        myDatabaseOpenHelper = new MyDatabaseOpenHelper(context);
    }

    // Get writable SQLiteDatabase object from MyDatabaseOpenHelper object
    void getDB() {
        Log.d(TAG, "----------------> getDB");
        sQLiteDatabase = myDatabaseOpenHelper.getWritableDatabase();
    }

    // myInsert method - insert item and flag into table
    public void myInsert(String item, String Flag) {
        Log.d(TAG, "----------------> myInsert");

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Create object for ContentVales class
            ContentValues contentValues = new ContentValues();

            // Put values into contentValues for colItem and colFlag
            contentValues.put(colItem, item); 		// Key value pair(Key, Value)
            contentValues.put(colFlag, Flag);		// Key value pair(Key, Value)

            // Insert contentValues into table
            long response = sQLiteDatabase.insert(TABLE_NAME1, null, contentValues);

            // Display insert response
            Log.d(TAG, "----------------> myInsert, response: " + response);
        }
    }

    // myUpdate method - update flag into table if item exist in table
    public void myUpdate(String item, String flag) {
        Log.d(TAG, "----------------> myUpdate");

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Create object for ContentVales class
            ContentValues contentValues = new ContentValues();

            // Put values into contentValues for colFlag
            contentValues.put(colFlag, flag);			// Key value pair(Key, Value)

            // Update contentValues into table if item already exist in table - update colFlag with new colFlag
            long response = sQLiteDatabase.update(TABLE_NAME1, contentValues, colItem + "=?", new String[] { String.valueOf(item) });

            // Display update response
            Log.d(TAG, "----------------> myUpdate, response: " + response);
        }
    }

    // myDelete method - delete row(item and flag) from table if item exist in table
    public void myDelete(String item) {
        Log.d(TAG, "----------------> myDelete");

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Delete row(item and flag) from table if item exist in table
            long response = sQLiteDatabase.delete(TABLE_NAME1, colItem + "=?", new String[] { String.valueOf(item) });

            // Display delete response
            Log.d(TAG, "----------------> myDelete, response: " + response);

        }
    }

    // Get all the date(all the records) from table and return cursor
    public Cursor myGetAvailableData() {
        Log.d(TAG, "----------------> myGetAvailableData");
        // Create cursor object
        Cursor cursor = null;

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Query for selecting all the data from table
            String Query = "select * from " + TABLE_NAME1;

            // Execute query
            cursor = sQLiteDatabase.rawQuery(Query, null);

            // Display cursor count
            Log.d(TAG, "----------------> myGetAvailableData, cursor count: " + cursor.getCount());
        }
        // return cursor
        return cursor;
    }

    // Get all the date(all the records) from table if colFlag is true and return cursor
    public Cursor myGetSelectedData() {
        Log.d(TAG, "----------------> myGetSelectedData");
        // Create cursor object
        Cursor cursor = null;

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Get all the date(all the records) from table if colFlag is true
            cursor = sQLiteDatabase.query(TABLE_NAME1, null, colFlag + "=?", new String[] { "true"}, null, null, null);

            // Display cursor count
            Log.d(TAG, "----------------> myGetSelectedData, cursor count: " + cursor.getCount());
        }
        // return cursor
        return cursor;
    }

    // Check whether item is already available or not, return true if item available otherwise return false
    public boolean isAvailable(String item) {
        Log.d(TAG, "----------------> isAvailable");

        // Create cursor object
        Cursor cursor = null;

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Query for selecting all the data from table
            String Query = "select * from " + TABLE_NAME1;
            // Execute query
            cursor = sQLiteDatabase.rawQuery(Query, null);

            // Display cursor count
            Log.d(TAG, "----------------> isAvailable, cursor count: " + cursor.getCount());

            // if cursor count is 0 then no records found and return false
            if (cursor.getCount() == 0) {
                // Close the cursor
                cursor.close();
                // Return false, item not available
                return false;
            }

            // Move the cursor to first position
            cursor.moveToFirst();
            // do-while loop
            do {
                // Get colItem from cursor
                String itemName = cursor.getString(cursor.getColumnIndex(colItem));
                // Check the item whether equal or not
                if (itemName.equalsIgnoreCase(item)) {
                    Log.d(TAG, "----------------> isAvailable, Item available in table, itemName: " + itemName);
                    // Close cursor
                    cursor.close();
                    // Return true, item available
                    return true;
                }
                // Move the cursor to next position
            } while (cursor.moveToNext());
        }
        // Return false, item not available
        return false;
    }


    public void myRename(String oldItem, String newItem) {
        Log.d(TAG, "----------------> myRename");

        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {

            // Create object for ContentVales class
            ContentValues contentValues = new ContentValues();

            // Put values into contentValues for colItem
            contentValues.put(colItem, newItem);	// Key value pair(Key, Value)

            // Update contentValues into table - update colItem with new colItem
            long response = sQLiteDatabase.update(TABLE_NAME1, contentValues, colItem + "=?", new String[] { String.valueOf(oldItem) });

            // Display update response
            Log.d(TAG, "----------------> myRename, response: " + response);
        }
    }

    public boolean myCheckFlag(Cursor cursor) {
        Log.d(TAG, "----------------> myCheckFlag");
        // Check whether database is open or not
        if (sQLiteDatabase.isOpen()) {
            // Move the cursor to first position
            cursor.moveToFirst();
            do {
                // Get colFlag from cursor
                String flag = cursor.getString(cursor.getColumnIndex(colFlag));
                // Check whether its true or not
                if (flag.equals("true")){
                    Log.d(TAG, "----------------> myCheckFlag, colFlag is true, so return true");
                    // Close cursor
                    cursor.close();
                    return true;
                }
                // Move the cursor to next position
            } while (cursor.moveToNext());
            // Close cursor
            cursor.close();
        }
        Log.d(TAG, "----------------> myCheckFlag, colFlag is not true, so return false");
        return false;
    }
}
