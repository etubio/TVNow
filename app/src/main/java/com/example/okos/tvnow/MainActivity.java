package com.example.okos.tvnow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static final String SHARED_PREFERENCES_KEY = "ActivitySharedPreferences_data";
    SharedPreferences.Editor editor;

    ArrayList<String> items = new ArrayList<>();
    ListView listView1;


    public void reloadListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        listView1.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Persistencia
        SharedPreferences sPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        editor   = sPreferences.edit();

        listView1 = (ListView) findViewById(R.id.listView1);
        ImageButton button = (ImageButton) findViewById(R.id.button);

        Map<String, ?> map = sPreferences.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) items.add((String) entry.getValue());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition = position;
                String itemValue = (String) listView1.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ConcreteChannelActivity.class);
                intent.putExtra("CH_NAME", itemValue);
                startActivity(intent);

            }

        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String s = items.get(position);
                editor.remove(s);
                editor.commit();
                items.remove(position);
                Toast.makeText(getApplicationContext(),
                        "Canal eliminado : " + s, Toast.LENGTH_LONG)
                        .show();
                reloadListView();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddChannelsActivity.class);
                intent.putStringArrayListExtra("FAVORITES",items);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                ArrayList<String> selectedItems = new ArrayList<>();
                selectedItems = data.getStringArrayListExtra("SELECTED");
                items.addAll(selectedItems);
                for (String s:selectedItems) {
                    editor.putString(s, s);
                    editor.commit();
                }
                // Reload listView1
                reloadListView();
            }
        }
    }
}
