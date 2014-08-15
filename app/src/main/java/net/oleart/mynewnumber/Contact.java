package net.oleart.mynewnumber;

import android.net.Uri;

public class Contact {
    String name;
    String oldNumber;
    String newNumber;
    Uri photo;

    Contact(String _name, String _oldNumber, String _newNumber, Uri _photo) {
        name = _name;
        oldNumber = _oldNumber;
        newNumber = _newNumber;
        photo = _photo;
    }
}
