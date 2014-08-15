package net.oleart.mynewnumber;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    ListView scanList;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.main_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.black,
                android.R.color.white,
                android.R.color.darker_gray,
                android.R.color.black);

        adapter = new ContactsAdapter(this, new ArrayList<Contact>());

        scanList = (ListView) findViewById(R.id.scan_list);
        scanList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        scanList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                    (scanList == null || scanList.getChildCount() == 0)
                    ? 0
                    : scanList.getChildAt(0).getTop();
                swipeLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                Cursor cursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
//                        ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?",
//                        new String[]{"1-23%"},
                        null,
                        null,
                        null
                );
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
                    Uri contactPhoto = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                    Log.d("CONTACT", name + ": " + phoneNumber);
                    Contact c = new Contact(name, phoneNumber, "123-456-7890", contactPhoto);
                    adapter.add(c);
                    adapter.notifyDataSetChanged();
//                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//                    String selectPhone = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? AND " + ContactsContract.CommonDataKinds.Phone.MIMETYPE + "='" +
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" + " AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + "=?";
//
//                    String[] phoneArgs = new String[]{contactId, String.valueOf(phoneNumber)};
//
//                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                            .withSelection(selectPhone, phoneArgs)
//                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "066-209-3234")
//                            .build());
//
//                    try {
//                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    } catch (OperationApplicationException e) {
//                        e.printStackTrace();
//                    }
                }
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
