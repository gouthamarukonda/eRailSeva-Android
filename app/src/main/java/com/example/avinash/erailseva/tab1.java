package com.example.avinash.erailseva;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class tab1 extends Fragment {

    String uname,pnr="PNR No.";
    View myview;
    Button b1;
    EditText txt1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            pnr = bundle.getString("pnr");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_tab1, container, false);
        EditText t1 = (EditText) myview.findViewById(R.id.pnr);
        if(pnr != "PNR No."){
                t1.setText(pnr,TextView.BufferType.EDITABLE);
        }
        b1 = (Button) myview.findViewById(R.id.next1);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                txt1 = (EditText) myview.findViewById(R.id.pnr);
                pnr = txt1.getText().toString();

                if(checkpnr()){
                    new pnrstatus().execute();
                }
            }
        });
        return myview;
    }

    public boolean checkpnr(){
        boolean flag=true;

        // Reset errors.
        txt1.setError(null);
        String pnr = txt1.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(pnr)) {
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

    private class pnrstatus extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url + "/train/checkpnr/",method="POST";
            try {

                jsoninput.put("pnr" , pnr);

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

                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        Fragment fragment = new order_frag2();
                        Bundle bundle = new Bundle();
                        bundle.putString("pnr",pnr);
                        bundle.putString("uname",uname);
                        bundle.putString("tab","1");
                        fragment.setArguments(bundle);
                        LinearLayout layout = (LinearLayout) myview.findViewById(R.id.frag1);
                        layout.removeAllViews();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.frag1, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
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
