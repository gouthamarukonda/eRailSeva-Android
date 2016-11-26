package com.example.avinash.erailseva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class signuptab extends Fragment {

    View mview;
    EditText txt1,txt2,txt3,txt4,txt5,txt6;
    String username,password;
    Button b1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_signuptab, container, false);
        txt1 = (EditText) mview.findViewById(R.id.uemail);
        txt2 = (EditText) mview.findViewById(R.id.uname);
        txt3 = (EditText) mview.findViewById(R.id.upass);
        txt4 = (EditText) mview.findViewById(R.id.upass1);
        txt5 = (EditText) mview.findViewById(R.id.ufullname);
        txt6 = (EditText) mview.findViewById(R.id.phn_no);

        b1 = (Button) mview.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(attemptlogin()){
                    new authentication().execute();
                }
            }
        });

        return mview;
    }

    public boolean attemptlogin(){
        boolean flag=true;

        // Reset errors.
        txt1.setError(null);
        txt2.setError(null);
        txt3.setError(null);
        txt4.setError(null);
        txt5.setError(null);
        txt6.setError(null);

        String email = txt1.getText().toString();
        String username = txt2.getText().toString();
        String pass1 = txt3.getText().toString();
        String pass2 = txt4.getText().toString();
        String fullname = txt5.getText().toString();
        String phn_no = txt6.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            txt2.setError("This field is required");
            focusView = txt2;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            txt1.setError("This field is required");
            focusView = txt1;
            cancel = true;
        }
        if (TextUtils.isEmpty(pass1)) {
            txt3.setError("This field is required");
            focusView = txt3;
            cancel = true;
        }
        if (TextUtils.isEmpty(pass2)) {
            txt4.setError("This field is required");
            focusView = txt4;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullname)) {
            txt5.setError("This field is required");
            focusView = txt5;
            cancel = true;
        }
        if (TextUtils.isEmpty(phn_no)) {
            txt6.setError("This field is required");
            focusView = txt6;
            cancel = true;
        }

        if(!pass1.equals(pass2)){
            txt4.setError("Passwords do not match");
            focusView = txt4;
            cancel = true;
        }

        if(phn_no.length() != 10){
            txt6.setError("mobile number must be 10 digits");
            focusView = txt6;
            cancel = true;
        }
        if(!email.contains("@")){
            txt1.setError("invalid email id");
            focusView = txt1;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            flag=false;
            focusView.requestFocus();
        }

        return flag;
    }

    private class authentication extends AsyncTask<String, String, String> {

        EditText t1 = (EditText) mview.findViewById(R.id.uname);
        EditText t2 = (EditText) mview.findViewById(R.id.upass);
        EditText t3 = (EditText) mview.findViewById(R.id.uemail);
        EditText t4 = (EditText) mview.findViewById(R.id.ufullname);
        EditText t5 = (EditText) mview.findViewById(R.id.phn_no);
        String username = t1.getText().toString(),
                password = t2.getText().toString(),
                phn_no = t5.getText().toString(),
                fullname = t4.getText().toString(),
                email = t3.getText().toString();
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url+"/customer/register/",method="POST";
            try {

                jsoninput.put("cust_id" , username);
                jsoninput.put("password", password);
                jsoninput.put("email", email);
                jsoninput.put("mobile", phn_no);
                jsoninput.put("cust_name", fullname);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlconnection url_connect = new urlconnection();
            output = url_connect.urlreturn(url_input,jsoninput,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    pDialog.dismiss();
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        String msg = jobj.getString("msg");
                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        //intent.putExtra("username", username);
                        getActivity().startActivity(intent);
                    } else {
//                        String msg = jobj.getString("msg");
//                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "registration unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }
}
