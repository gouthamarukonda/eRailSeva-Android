package com.example.avinash.erailseva;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.BufferUnderflowException;

public class Review extends AppCompatActivity {

    RatingBar ratingBar;
    String ratedValue,shop_id,station_name,shop_name,msg;
    int rating_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        shop_id = intent.getStringExtra("shop_id");
        station_name = intent.getStringExtra("station_name");
        shop_name = intent.getStringExtra("shop_name");

        TextView t1 = (TextView) findViewById(R.id.station_name);
        TextView t2 = (TextView) findViewById(R.id.shop_name);
        t1.setText("STATION NAME : "+station_name);
        t2.setText("SHOP NAME      : "+shop_name);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratedValue = String.valueOf(ratingBar.getRating());
                rating_value = (int)(ratingBar.getRating()*2);

            }
        });


        Button b1 = (Button) findViewById(R.id.logout);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new AlertDialog.Builder(Review.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure , you want to logout ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new logout().execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        Button b2 = (Button) findViewById(R.id.postreview);
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText t3 = (EditText)findViewById(R.id.review);
                msg = t3.getText().toString();
                new postreview().execute();

            }
        });
    }

    private class postreview extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url+"/customer/postreview/",method="POST";

            try {
                System.out.println("rating "+rating_value);
                System.out.println(shop_id);
                jsoninput.put("shop_id" , shop_id);
                jsoninput.put("msg" , msg);
                jsoninput.put("rating" , rating_value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlconnection myconnection = new urlconnection();
            //System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,jsoninput,method,"b");
            //System.out.println("bbbbbbb     "+output);
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    System.out.println("status reviews "+status);
                    if (status) {
                        new AlertDialog.Builder(Review.this)
                                .setMessage("Your review has been posted successfully")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getApplicationContext(),OrderForm.class);
                                        startActivity(i);
                                    }
                                })
                                .show();

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

    private class logout extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/logout/",method="GET";

            urlconnection myconnection = new urlconnection();
            System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,null,method,"logout");
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
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplication(),"Logged out Successfully",Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }
}
