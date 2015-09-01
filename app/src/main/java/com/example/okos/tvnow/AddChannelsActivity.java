package com.example.okos.tvnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddChannelsActivity extends AppCompatActivity {

    ListView listView2;
    ArrayList<String> selectedItems = new ArrayList<>();
    ArrayList<String> favorite_channels;
    ArrayList<String> all_channels = new ArrayList<>();
    ArrayList<String> foundChannels = new ArrayList<String>();

    public void setChannelList(ArrayList<String> cha) {
        // Get favorite channels from main activity
        favorite_channels = getIntent().getStringArrayListExtra("FAVORITES");
        all_channels.addAll(cha);

        // Remove favorite channels from list of all channels
        for (String s:favorite_channels)
            if (all_channels.contains(s)) all_channels.remove(s);

        // Display listView2
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, all_channels);
        listView2.setAdapter(adapter);
        listView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to);

        listView2 = (ListView) findViewById(R.id.listView2);
        Button button = (Button) findViewById(R.id.button2);


        /******************************************************************************************
         ******************************************************************************************
                                                    CALL TO API
                                    This API must return the current tv channels.
         ******************************************************************************************
         ******************************************************************************************/
/*
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "http://192.168.1.10:7070/rs-tvchannels-service/channels";

        JsonObjectRequest req = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray a = response.getJSONArray("channel");
                    for (int i=0; i<a.length(); i++) {
                        JSONObject o = a.getJSONObject(i);
                        String s = o.getString("name");
                        foundChannels.add(s);
                    }
                    setChannelList(foundChannels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "No hay conexión con el servidor: " + error.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        }) {

            // Accept JSON
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("ACCEPT", "application/json");
                return headers;
            }
        };
        queue.add(req);
*/

        //mock
        foundChannels.add("la1");
        foundChannels.add("la2");
        foundChannels.add("antena3");
        foundChannels.add("cplus");
        foundChannels.add("cuatro");
        foundChannels.add("lasexta");
        foundChannels.add("telecinco");
        setChannelList(foundChannels);


        // Press OK
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get all checked items
                int cntChoice = listView2.getCount();
                SparseBooleanArray sparseBooleanArray = listView2.getCheckedItemPositions();
                for(int i = 0; i < cntChoice; i++)
                    if(sparseBooleanArray.get(i) == true)
                        selectedItems.add(listView2.getItemAtPosition(i).toString());

                // Return result to main activity
                Intent intent = new Intent();
                intent.putExtra("SELECTED", selectedItems);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }



}
