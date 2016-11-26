package com.example.avinash.erailseva;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PostOrder extends AppCompatActivity {

    Button b1,b2;
    String uname,reason;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_order);

        Intent i = getIntent();
        uname = i.getStringExtra("uname");
        reason = i.getStringExtra("reason");

        b1 = (Button) findViewById(R.id.home);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OrderForm.class);
                intent.putExtra("username",uname);
                startActivity(intent);
            }
        });

        b2 = (Button) findViewById(R.id.logout);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new logout_customer().execute();
            }
        });
    }

    private class logout_customer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/logout/",method="GET";
            urlconnection myconnection = new urlconnection();
            System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,null,method,"b");
            System.out.println("bbbbbbb     "+output);
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    } else {
//                        String msg = jobj.getString("msg");
//                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "logout unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }
}
