package com.example.okos.tvnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class ConcreteChannelActivity extends AppCompatActivity {

    public String ch_name; // Channel's name (ID)
    public String pr_name; // Program's name (ID)

    // Method called by button_publish
    private void post(String text) {

        /******************************************************************************************
         ******************************************************************************************
                                            CALL TO FACEBOOK API
                        This call to Facebook' API write a post on Event's page wall
         ******************************************************************************************
         ******************************************************************************************/

        final String accessToken = "CAAExhTz28B0BAM0biCiwZCqZCh7IbcVSU6VugdBvCCmmA6U4oZAZAG0NlfWZAB0JB8TyDPyd0YTCZBTG0UxyfuHyQEYygMnXrlZAtVv7mHPoCNFz6EUw6TZChUVWYA9U7IdcgQifqOmBmlGUkhxoyFBRG13qDEZAN98oijabZBZCINa0Yt6IsNhAlaR3Th2KmCsLQZB0eedWXNIvOgZDZD";
        final String pageId = "1603340439899307";
        String codedText = null;
        try {
            codedText = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String URL = "https://graph.facebook.com/"+pageId+"/feed?message="+codedText+"&access_token="+accessToken;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),
                        "Publicado", Toast.LENGTH_LONG)
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "No hay conexión con Facebook: "+error.toString() + URL, Toast.LENGTH_LONG)
                        .show();
            }
        });
        queue.add(req);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_channel);

        Button button_publish = (Button) findViewById(R.id.button_publish);
        final TextView textView = (TextView) findViewById(R.id.ch_name);
        ch_name = getIntent().getStringExtra("CH_NAME");
        textView.setText(ch_name);

        // Press button_publish
        button_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textView.getText().length() > 0) {
                    String text = "Estoy viendo "+pr_name+" del canal "+ch_name;
                    post(text);
                    Toast.makeText(getApplicationContext(),
                            "Publicando...", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }


    @Override
    protected void onResume() {

        super.onResume();

        Toast.makeText(getApplicationContext(),
                "Actualizando...", Toast.LENGTH_LONG)
                .show();

        /******************************************************************************************
         ******************************************************************************************
                                                CALL TO API
         This API must return the tv program of a specific tv channel that is being broadcast now.
         ******************************************************************************************
         ******************************************************************************************/

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = null;
        try {
            URL = GuiaTiVi.getRequest("GET", "schedule/" + ch_name);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    pr_name = response.getJSONObject("data").getJSONArray("items")
                            .optJSONObject(0).getString("title");

                    TextView textView2 = (TextView) findViewById(R.id.pr_name);
                    textView2.setText(pr_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "No hay conexión con el servidor: "+error.toString(), Toast.LENGTH_LONG)
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




    }

}
