package net.oleart.mynewnumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Contact> objects;

    ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        ctx = context;
        objects = contacts;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.phone_list_item, parent, false);
        }

        Contact contact = getContact(position);

        ((TextView) view.findViewById(R.id.phones_list_name)).setText(contact.name);
        ((TextView) view.findViewById(R.id.phones_list_old)).setText(contact.oldNumber);
        ((TextView) view.findViewById(R.id.phones_list_new)).setText(contact.newNumber);
        ImageView contactPhoto = (ImageView) view.findViewById(R.id.phones_list_image);
        contactPhoto.setImageURI(contact.photo);
        if (contactPhoto.getDrawable() == null) {
            contactPhoto.setImageResource(R.drawable.person);
        }
        return view;
    }

    Contact getContact(int position) {
        return ((Contact) getItem(position));
    }

    public void add(Contact c) {
        objects.add(c);
    }

    public void clear() {
        objects.clear();
    }
}
