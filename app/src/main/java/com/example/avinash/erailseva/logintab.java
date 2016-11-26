package com.example.avinash.erailseva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.avinash.erailseva.urlconnection;

public class logintab extends Fragment {

    View mview;
    EditText txt1,txt2;
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
        mview = inflater.inflate(R.layout.fragment_logintab, container, false);
        txt1 = (EditText) mview.findViewById(R.id.editText1);
        txt2 = (EditText) mview.findViewById(R.id.editText2);
        b1 = (Button) mview.findViewById(R.id.button1);
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

        String email = txt1.getText().toString();
        String password = txt2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            txt2.setError("This field is required");
            focusView = txt2;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            txt1.setError("This is field cannot be empty");
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

        EditText t1 = (EditText) mview.findViewById(R.id.editText1);
        EditText t2 = (EditText) mview.findViewById(R.id.editText2);
        String username = t1.getText().toString(),
        password = t2.getText().toString();
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/login/",method="POST";
            try {

                jsoninput.put("cust_id" , username);
                jsoninput.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlconnection url_connect = new urlconnection();
            output = url_connect.urlreturn(url_input,jsoninput,method,"login");
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
                        Intent intent = new Intent(getActivity(), OrderForm.class);
                        intent.putExtra("username", username);
                        getActivity().startActivity(intent);
                    } else {
                        String msg = jobj.getString("msg");
                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }
}
