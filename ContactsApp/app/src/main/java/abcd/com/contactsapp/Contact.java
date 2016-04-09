package abcd.com.contactsapp;

import android.net.Uri;

/**
 * Created by snehapriyaa on 5/30/2015.
 */
public class Contact implements Comparable<Contact>{
    public String _name, _phone;
private Uri _image;
    public Contact(String name, String phone,Uri image){

        _name=name;
        _phone=phone;
        _image=image;
    }

    public String getname() {
        return _name;
    }
    public String getphone() {
        return _phone;
    }
    public Uri getimage(){
        return _image;
    }

    /*
   **  Implement the natural order for this class
   */
    public int compareTo(Contact p)
    {
        return getname().compareTo(p.getname());
    }
}
