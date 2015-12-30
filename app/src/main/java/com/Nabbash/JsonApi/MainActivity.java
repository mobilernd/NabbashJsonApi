package com.Nabbash.JsonApi;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity
{

    private ProgressDialog pDialog;
    //
////	// URL to get contacts JSON
    private static String url = "https://www.mockaroo.com/13998d00/download?count=3&key=56d09260";
//

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();




        // Calling async task to get json
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);



            if (jsonStr != null)
            {

                try
                {
                    Log.d("Mahmoud: ", "No Object ");
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    //	JSONObject jsonObj = new JSONObject(jsonStr);
                    //	JSONArray  jsonArr =
                    // Getting JSON Array node
                    //contacts = jsonArray.getJSONArray(TAG_CONTACTS);
                    Log.d("Mahmoud: ", "No jsonArray.length() "+jsonArray.length());
                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject contact_obj = jsonArray.getJSONObject(i);

                        Log.d("Mahmoud: ", " No convert happend ") ;

                        //JSONObject c = contacts.getJSONObject(i);
                        //Log.d("Mahmoud: ", c.toString()) ;
                        //String id = c.getString(TAG_ID);
                        String contactStr = contact_obj.getString("contacts") ;
                        Log.d("Mahmoud: contactStr ", contactStr.toString()) ;

                        JSONObject c = new JSONObject(contactStr);

                        String name = c.getString(TAG_NAME);
                        String email = c.getString(TAG_EMAIL);
                        String mobile = c.getString(TAG_PHONE_MOBILE);

                        //String gender = c.getString(TAG_GENDER);

                        // Phone node is JSON Object
//						JSONObject phone = c.getJSONObject(TAG_PHONE);
//						String mobile = phone.getString(TAG_PHONE_MOBILE);
//						String home = phone.getString(TAG_PHONE_HOME);
//						String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        //contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_PHONE_MOBILE, mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
                    TAG_PHONE_MOBILE }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);
        }

    }

}
